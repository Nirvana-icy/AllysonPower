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
        // 获取Location Provider
        getProvider();
        // 如果未设置位置源，打开GPS设置界面
        openGPS();
        // 获取位置
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
                //显示软键盘
                showIMM();
            }
        });
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//		Location service method
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
private final LocationListener locationListener = new LocationListener() {
	public void onLocationChanged(Location location) { // 当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
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
			// Provider被disable时触发此函数，比如GPS被关闭
		}

		public void onProviderEnabled(String provider) {
			// Provider被enable时触发此函数，比如GPS被打开
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
			// Provider的转态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
		}
	};

	// 判断是否开启GPS，若未开启，打开GPS设置界面
	private void openGPS() {
		if (locationManager
				.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)
				|| locationManager
						.isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER)) {
			return;
		}
		Toast.makeText(this, "GPS do not enable！", Toast.LENGTH_SHORT).show();
		// 转至GPS设置界面
		Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
		startActivityForResult(intent, 0);
	}

	// 获取Location Provider
	private void getProvider() {
		// 构建位置查询条件
		Criteria criteria = new Criteria();
		// 查询精度：高
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		// 是否查询海拨：否
		criteria.setAltitudeRequired(false);
		// 是否查询方位角:否
		criteria.setBearingRequired(false);
		// 是否允许付费：是
		criteria.setCostAllowed(true);
		// 电量要求：低
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		// 返回最合适的符合条件的provider，第2个参数为true说明,如果只有一个provider是有效的,则返回当前provider
		provider = locationManager.getBestProvider(criteria, true);
	}
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//	   partd btn点击 消息响应函数聚集地..       
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private void positiveBtnIsClicked()
    {
    	if(!positive)     //默认positive是true(大拇指向上的图标高亮). 当positive是false的时候->大拇指向上的图标是灰色的.此时用户点击这个btn 高亮它.
    	{
    		positive = true;
    		//Get the btn
    		ImageButton positiveBtn = (ImageButton)findViewById(R.id.ib_positive);
    		ImageButton negativeBtn = (ImageButton)findViewById(R.id.ib_negative);
    		//高亮 positiveBtn
    		positiveBtn.setImageDrawable(getResources().getDrawable(R.drawable.btn_positive_sel));
    		//灰掉 negativeBtn
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
    		//灰掉 positiveBtn
    		positiveBtn.setImageDrawable(getResources().getDrawable(R.drawable.btn_positive));
    		//高亮 negativeBtn
    		negativeBtn.setImageDrawable(getResources().getDrawable(R.drawable.btn_negative_sel));
    	}
    }
    
    private void insertLocationBtnIsClicked()
    {
    	//如果已经获得用户位置  用户再次点击此btn  ＝> 1.将btn变灰 2.mIsLocation ＝ false 3.删除获得的位置数据
        if (mIsLocation) {
            aq.id(R.id.ib_insert_location).image(R.drawable.btn_insert_location_nor);
            mIsLocation = false;
            mLatitude = DEFAULT_LATITUDE;
            mLongitude = DEFAULT_LONGTITUDE;
        } else {
        	//如果GPS不是正在获取位置的状态 用户点击此btn  ＝> 1.start GPS location 2.将btn变为正在获取位置的btn  3.设置mLocationGetting标志状态为true
        	if(!mLocationGetting) {
        		//Need to start GPS here
        		locationManager.requestLocationUpdates(provider, 2000, 100,
                        locationListener); 
        		
        		mLocationGetting = true;
        		aq.id(R.id.ib_insert_location).image(R.drawable.btn_insert_location_getting);
        	}
        	//如果GPS正在获取位置 用户点击了此btn => 1.stop获取GPS的动作 2.将GPS标志位设置为false  3.替换btn图像
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
//    	//插入 ＃＃
//    	mEdit.append("##");
//    	//调整光标位置到＃＃中间 方便用户输入
//    	Editable etext = mEdit.getText();
//    	int position = etext.length();
//    	mEdit.setSelection(position - 1);
//    }
    
    private void sendMsgBtnIsClicked() {
        mContent = mEdit.getText().toString();
        //User attached one photo to post
        if (!TextUtils.isEmpty(mPicPath)) {
            Util.showToast(this, R.string.sending);  //后面需要转换为spinner  图片暂时不支持
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
        		//Clean up 编辑框
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
 //	   为了代码整洁 每个btn 点击响应的函数在上面单独实现   
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
                displayToast("添加图片失败!");
                return;
            }

            Uri uri = data.getData();
            mPicPath = getRealPathFromURI(uri);
            if (mPicPath != null) {
                File file = new File(mPicPath);
                // load image from file, down sample to target width of 45 pixels
               // aq.id(R.id.iv_insertpic).image(file, 45).visible();
            } else {
                displayToast("添加图片失败!");
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

