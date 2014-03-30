package com.allysonpower.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.NavUtils;
import android.support.v4.content.CursorLoader;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.allysonpower.ui.R;
import com.allysonpower.util.Log;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.LocationAjaxCallback;
import com.allysonpower.util.*;

import java.io.File;

import com.allysonpower.parse.AllysonNewsInfo;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;

public class partd extends BaseActivity implements OnClickListener
         {
    private EditText mEdit;
    private InputMethodManager imm;
    private String mPicPath = "";
    private String mContent = "";
    
    private LocationManager locationManager = null;
    private String provider = null;
    private static final double DEFAULT_LATITUDE = 22.282663;
    private static final double DEFAULT_LONGTITUDE = 114.138752;
    private double mLatitude = DEFAULT_LATITUDE;
    private double mLongitude = DEFAULT_LONGTITUDE; 

    private AQuery aq;
    private boolean mIsLocation = false;
    private boolean mLocationGetting = false;
    private boolean positive = true;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy); 

        imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        //Regist the subclass of ParseObject first and then call Parse.initialize
        ParseObject.registerSubclass(AllysonNewsInfo.class);
        Parse.initialize(this, "bDExeWi2vct7yqm52r5WPnEiuNyorLu9B2tSFREW", "MvzGGKqOt5Q56inQCPNpbYLAaiJmtykCjgh93C1K");
        //Get LocationManager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // ��ȡLocation Provider
        getProvider();
        // ���δ����λ��Դ����GPS���ý���
        openGPS();
        // ��ȡλ��
        //Init the view
        initView();
    }

    public void initView() {
        this.setContentView(R.layout.post_new_msg);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        aq = new AQuery(this);

        aq.id(R.id.ib_positive).clicked(this);
        aq.id(R.id.ib_negative).clicked(this);
        aq.id(R.id.ib_insert_pic).clicked(this);
        aq.id(R.id.ib_insert_location).clicked(this);
        aq.id(R.id.ib_send_msg).clicked(this);

        mEdit = (EditText) this.findViewById(R.id.et_mblog);

        mEdit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //��ʾ�����
                showIMM();
            }
        });
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//		Location service method
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
private final LocationListener locationListener = new LocationListener() {
	public void onLocationChanged(Location location) { // ������ı�ʱ�����˺��������Provider������ͬ�����꣬���Ͳ��ᱻ����
	// log it when the location changes
	if (location != null) {
		mLatitude = location.getLatitude();
		mLongitude = location.getLongitude();
		mIsLocation = true;
		mLocationGetting = false;
		aq.id(R.id.ib_insert_location).image(R.drawable.btn_insert_location_res);
		
		android.util.Log.i("Allysonpower partD Geted GPS info~",				
				"Location changed : Lat: " + location.getLatitude()
				 + " Lng: " + location.getLongitude());
			}
		}

		public void onProviderDisabled(String provider) {
			// Provider��disableʱ�����˺���������GPS���ر�
		}

		public void onProviderEnabled(String provider) {
			// Provider��enableʱ�����˺���������GPS����
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
			// Provider��ת̬�ڿ��á���ʱ�����ú��޷�������״ֱ̬���л�ʱ�����˺���
		}
	};

	// �ж��Ƿ���GPS����δ��������GPS���ý���
	private void openGPS() {
		if (locationManager
				.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)
				|| locationManager
						.isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER)) {
			return;
		}
		Toast.makeText(this, "GPS do not enable��", Toast.LENGTH_SHORT).show();
		// ת��GPS���ý���
		Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
		startActivityForResult(intent, 0);
	}

	// ��ȡLocation Provider
	private void getProvider() {
		// ����λ�ò�ѯ����
		Criteria criteria = new Criteria();
		// ��ѯ���ȣ���
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		// �Ƿ��ѯ��������
		criteria.setAltitudeRequired(false);
		// �Ƿ��ѯ��λ��:��
		criteria.setBearingRequired(false);
		// �Ƿ������ѣ���
		criteria.setCostAllowed(true);
		// ����Ҫ�󣺵�
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		// ��������ʵķ���������provider����2������Ϊtrue˵��,���ֻ��һ��provider����Ч��,�򷵻ص�ǰprovider
		provider = locationManager.getBestProvider(criteria, true);
	}
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//	   partd btn��� ��Ϣ��Ӧ�����ۼ���..       
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private void positiveBtnIsClicked()
    {
    	if(!positive)     //Ĭ��positive��true(��Ĵָ���ϵ�ͼ�����). ��positive��false��ʱ��->��Ĵָ���ϵ�ͼ���ǻ�ɫ��.��ʱ�û�������btn ������.
    	{
    		positive = true;
    		//Get the btn
    		ImageButton positiveBtn = (ImageButton)findViewById(R.id.ib_positive);
    		ImageButton negativeBtn = (ImageButton)findViewById(R.id.ib_negative);
    		//���� positiveBtn
    		positiveBtn.setImageDrawable(getResources().getDrawable(R.drawable.btn_positive_sel));
    		//�ҵ� negativeBtn
    		negativeBtn.setImageDrawable(getResources().getDrawable(R.drawable.btn_negative));
    	}
    }
    
    private void negativeBtnIsClicked()
    {
    	if(positive)
    	{
    		positive = false;
    		//Get the btn
    		ImageButton positiveBtn = (ImageButton)findViewById(R.id.ib_positive);
    		ImageButton negativeBtn = (ImageButton)findViewById(R.id.ib_negative);
    		//�ҵ� positiveBtn
    		positiveBtn.setImageDrawable(getResources().getDrawable(R.drawable.btn_positive));
    		//���� negativeBtn
    		negativeBtn.setImageDrawable(getResources().getDrawable(R.drawable.btn_negative_sel));
    	}
    }
    
    private void insertLocationBtnIsClicked()
    {
    	//����Ѿ�����û�λ��  �û��ٴε����btn  ��> 1.��btn��� 2.mIsLocation �� false 3.ɾ����õ�λ������
        if (mIsLocation) {
            aq.id(R.id.ib_insert_location).image(R.drawable.btn_insert_location_nor);
            mIsLocation = false;
            mLatitude = DEFAULT_LATITUDE;
            mLongitude = DEFAULT_LONGTITUDE;
        } else {
        	//���GPS�������ڻ�ȡλ�õ�״̬ �û������btn  ��> 1.start GPS location 2.��btn��Ϊ���ڻ�ȡλ�õ�btn  3.����mLocationGetting��־״̬Ϊtrue
        	if(!mLocationGetting) {
        		//Need to start GPS here
        		locationManager.requestLocationUpdates(provider, 2000, 100,
                        locationListener); 
        		
        		mLocationGetting = true;
        		aq.id(R.id.ib_insert_location).image(R.drawable.btn_insert_location_getting);
        	}
        	//���GPS���ڻ�ȡλ�� �û�����˴�btn => 1.stop��ȡGPS�Ķ��� 2.��GPS��־λ����Ϊfalse  3.�滻btnͼ��
        	else {
        		//Need to stop GPS here
        		locationManager.removeUpdates(locationListener);
        		mLocationGetting = false;
        		aq.id(R.id.ib_insert_location).image(R.drawable.btn_insert_location_nor);
        	}
        }
    }
    
    private void insertPicBtnIsClicked()
    {
//        PopupMenu popup = new PopupMenu(this, view);
//      popup.getMenuInflater().inflate(R.menu.pic, popup.getMenu());
//
//      popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//          public boolean onMenuItemClick(android.view.MenuItem item) {
//
//              Intent galleryIntent = new Intent();
//              galleryIntent.setType("image/*");
//              galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
//              galleryIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
//              startActivityForResult(galleryIntent, 2);
//
//              return true;
//          }
//      });
//
//      popup.show();
    }

//    private void insertTopic()
//    {
//    	//���� ����
//    	mEdit.append("##");
//    	//�������λ�õ������м� �����û�����
//    	Editable etext = mEdit.getText();
//    	int position = etext.length();
//    	mEdit.setSelection(position - 1);
//    }
    
    private void sendMsgBtnIsClicked() {
        mContent = mEdit.getText().toString();
        //User attached one photo to post
        if (!TextUtils.isEmpty(mPicPath)) {
            Util.showToast(this, R.string.sending);  //������Ҫת��Ϊspinner  ͼƬ��ʱ��֧��
//          parseObject.upload(this.mContent, this.mPicPath, mLatitude, mLongitude, this);
        } else {
        	//User do not attache one photo but input text to post
        	if (!TextUtils.isEmpty(mContent)) {
                // Create a post.
                AllysonNewsInfo post = new AllysonNewsInfo();
                //set user input values to parse
                post.setEventText(mEdit.getText().toString());
                post.setPositive(positive);
                post.setLocation(mLatitude, mLongitude);
                // Save the post
                post.saveInBackground();
                //This Toast should be shown after post success
        		displayToast("Post one text news to Parse.");
        		//Clean up �༭��
        		mEdit.setText("");
        	}
        	//User do not attache one photo and not input sth..
        	else {
        		displayToast(R.string.plz_input_sth);
        	}
        }
    }
    
 //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 //	   The message dispatch center of btn clicked    
 //	   Ϊ�˴������� ÿ��btn �����Ӧ�ĺ��������浥��ʵ��   
 //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Override
    public void onClick(View view) {
        int viewId = view.getId();

        switch(viewId) {
        case android.R.id.home:
            NavUtils.navigateUpFromSameTask(this);
            break;
        case R.id.ib_positive:
        	positiveBtnIsClicked();
        	break;
        case R.id.ib_negative:
        	negativeBtnIsClicked();
        	break;
        case R.id.ib_insert_location:
            insertLocationBtnIsClicked();
            break;
        case R.id.ib_insert_pic:
        	insertPicBtnIsClicked();
        	break;
        case R.id.ib_send_msg:
        	sendMsgBtnIsClicked();
        	break;
        default:
        	android.util.Log.v("alllysonpower", "-----partd switch handle empty!");
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) { 
            if (null == data) {
                displayToast("���ͼƬʧ��!");
                return;
            }

            Uri uri = data.getData();
            mPicPath = getRealPathFromURI(uri);
            if (mPicPath != null) {
                File file = new File(mPicPath);
                // load image from file, down sample to target width of 45 pixels
               // aq.id(R.id.iv_insertpic).image(file, 45).visible();
            } else {
                displayToast("���ͼƬʧ��!");
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    // And to convert the image URI to the direct file system path of the image file
    // TODO: bugs on Android 4.4, it return null when Choose Open from Rencent/Images/Downlaods.
    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(this, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        aq.dismiss();
    }

    private void showIMM() {
        
    	imm.showSoftInput(mEdit, 0);
    }
}

