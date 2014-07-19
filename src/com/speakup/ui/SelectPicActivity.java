package com.speakup.ui;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.speakup.util.SDCardFileUtils;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

//选择文件操作类
public class SelectPicActivity extends Activity implements OnClickListener{
	private static final String IMAGE_PATH = "path";
	private static String PHOTO_NAME;

	//使用照相机拍照获取图片
	public static final int SELECT_PIC_BY_TACK_PHOTO = 1;
	//使用相册中的图片
	public static final int SELECT_PIC_BY_PICK_PHOTO = 2;
	//从Intent获取图片路径的KEY
	public static final String KEY_PHOTO_PATH = "photo_path";	
	private static final String TAG = "SelectPicActivity";	
	private LinearLayout dialogLayout;
	private Button takePhotoBtn,pickPhotoBtn,cancelBtn;
	 public static final int MEDIA_TYPE_IMAGE = 1;
	    public static final int MEDIA_TYPE_VIDEO = 2;
	    private String mImagePath;
	    private static String TAKE_PHOTO_PATH_CACHE = Environment.getExternalStorageDirectory().getPath() + "/baidu_startface/" + "takephoto_cache/";
	/**获取到的图片路径*/
	private String picPath;
	private Intent lastIntent ;	
	private Uri photoUri;
	private Uri fileUri;
    private Uri mUri;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_pic_layout);
		
		dialogLayout = (LinearLayout) findViewById(R.id.dialog_layout);
		dialogLayout.setOnClickListener(this);
		takePhotoBtn = (Button) findViewById(R.id.btn_take_photo);
		takePhotoBtn.setOnClickListener(this);
		pickPhotoBtn = (Button) findViewById(R.id.btn_pick_photo);
		pickPhotoBtn.setOnClickListener(this);
		cancelBtn = (Button) findViewById(R.id.btn_cancel);
		cancelBtn.setOnClickListener(this);	
		lastIntent = getIntent();
	}

	 
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dialog_layout:
			finish();
			break;
		case R.id.btn_take_photo:
			takePhoto();
			break;
		case R.id.btn_pick_photo:
			pickPhoto();
			break;
		default:
			finish();
			break;
		}
	}

	/**
	 * 拍照获取图片
	 */
	private void takePhoto() {
		 Intent localIntent = new Intent("android.media.action.IMAGE_CAPTURE");
		 //String timeStamp = new SimpleDateFormat(
				//	"yyyy_MM_dd_HH_mm_ss").format(new Date());
		// ContentValues values = new ContentValues();
		// values.put(MediaStore.Images.Media.TITLE, "IMG_" + timeStamp + ".jpg");
		// fileUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values); // store content values
		// localIntent.putExtra( MediaStore.EXTRA_OUTPUT,  fileUri);
		    localIntent.putExtra("return-data", true);
		    startActivityForResult(localIntent, SELECT_PIC_BY_TACK_PHOTO);
		    
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	  {
		Log.e(ACTIVITY_SERVICE, "SelectPicActivity121 onActivityResult "+requestCode+" "+resultCode);
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == Activity.RESULT_OK)
		{  // Log.e(ACTIVITY_SERVICE, "SelectPicActivity121 "+(resultCode == Activity.RESULT_OK));
		   // Log.e(ACTIVITY_SERVICE, "SelectPicActivity122 "+resultCode);
			doPhoto(requestCode,data);
			//Log.e(ACTIVITY_SERVICE, "SelectPicActivity121 doPhoto success");
		}else
			Log.e(ACTIVITY_SERVICE, "SelectPicActivity121 doPhoto fail");

	  }
	
	//-------take photo before 6-30-----//
	/*String currentPhotoPath = "";
	private void takePhoto() {
		//执行拍照前，应该先判断SD卡是否存在
		String SDState = Environment.getExternalStorageState();
		if(SDState.equals(Environment.MEDIA_MOUNTED))
		{  /////--------new try-----------//
			 String timeStamp = new SimpleDateFormat(
						"yyyy_MM_dd_HH_mm_ss").format(new Date());
			 ContentValues values = new ContentValues();
			 values.put(MediaStore.Images.Media.TITLE, "IMG_" + timeStamp + ".jpg");
			 Intent intent=new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		       // Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		     fileUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values); // store content values
		        intent.putExtra( MediaStore.EXTRA_OUTPUT,  fileUri);
		     startActivityForResult(intent, SELECT_PIC_BY_TACK_PHOTO);
			////--------new end-----//
			//Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				//File photo = null;
	            
	          //  SimpleDateFormat timeStampFormat = new SimpleDateFormat(
						//"yyyy_MM_dd_HH_mm_ss");
				//String filename = timeStampFormat.format(new Date());
				//ContentValues values = new ContentValues();
				//values.put(Media.TITLE, filename);
				//photoUri = getContentResolver().insert(
						//MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
				//photo = setupPhotoFile();
				//takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT,
					//	photoUri);
				//currentPhotoPath = photoUri.toString();
				//setResult(Activity.RESULT_OK, takePhotoIntent);
				//takePhotoIntent.putExtra("return-data", true);	
				
				//startActivityForResult(takePhotoIntent, SELECT_PIC_BY_TACK_PHOTO);
				
		}else{
			Toast.makeText(this,"内存卡不存在", Toast.LENGTH_LONG).show();
		}
		
	}*/
	//-------take photo before 6-30 end-----//

				
	

	
	/***
	 * 从相册中取图片
	 */
	private void pickPhoto() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		Log.e(ACTIVITY_SERVICE, "SelectPicActivity132 "+(intent));
		startActivityForResult(intent, SELECT_PIC_BY_PICK_PHOTO);
	}
	
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		return super.onTouchEvent(event);
	}
	
	//-----onActivityResult before 6-30--------//
	/*@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.e(ACTIVITY_SERVICE, "SelectPicActivity145 onActivityResult"+data);
		super.onActivityResult(requestCode, resultCode, data);
	//	Uri uri = null;
		//if (data != null && data.getData() != null) {
		//	 uri = data.getData();
		//	}
		//if (uri == null) {
		//	if (photoUri != null) {
		//	uri = photoUri;
		//	}
		//	}
		Log.e(ACTIVITY_SERVICE, "SelectPicActivity125 "+(data==null)+requestCode);
		Log.e(ACTIVITY_SERVICE, "SelectPicActivity118 "+(resultCode == Activity.RESULT_OK));
		if(resultCode == Activity.RESULT_OK)
		{   Log.e(ACTIVITY_SERVICE, "SelectPicActivity121 "+(resultCode == Activity.RESULT_OK));
		    Log.e(ACTIVITY_SERVICE, "SelectPicActivity122 "+resultCode);
			doPhoto(requestCode,data);
			Log.e(ACTIVITY_SERVICE, "SelectPicActivity121 doPhoto success");
		}else
			Log.e(ACTIVITY_SERVICE, "SelectPicActivity121 doPhoto fail");
		

		
		
		
		
	}*/
	//-----onActivityResult before 6-30 end--------//
	/**
	 * 选择图片后，获取图片的路径
	 * @param requestCode
	 * @param data
	 */
	private void doPhoto(int requestCode,Intent paramIntent){
		if(requestCode == SELECT_PIC_BY_PICK_PHOTO )  //从相册取图片，有些手机有异常情况，请注意
		{
			if(paramIntent == null){
				Toast.makeText(this, "选择图片文件出错", Toast.LENGTH_LONG).show();
				return;
			}
			this.mImagePath = "";
			mUri = paramIntent.getData();
			Log.e(ACTIVITY_SERVICE, "SelectPicActivity233 "+(mUri!=null)); 
			mImagePath = getPath(mUri);
			//Intent localIntent = new Intent();
		    //localIntent.putExtra("photo_path", this.mImagePath);
		   // localIntent.putExtra("can_upload", true);
			Log.e(ACTIVITY_SERVICE, "SelectPicActivity237 doPhoto SELECT_PIC_BY_PICK_PHOTO"+mImagePath); 
		    lastIntent.putExtra(KEY_PHOTO_PATH, mImagePath);
			setResult(Activity.RESULT_OK, lastIntent);
			finish(); 	
		    
			//------select image new try 17:53----//
			/*photoUri = paramIntent.getData();
			  Log.e(ACTIVITY_SERVICE, "SelectPicActivity229 doPhoto SELECT_PIC_BY_PICK_PHOTO"+photoUri);  
			if(photoUri == null ){
				Toast.makeText(this, "选择图片文件出错", Toast.LENGTH_LONG).show();
				return;
			}
			String[] pojo = {MediaStore.Images.Media.DATA};
			Log.e(ACTIVITY_SERVICE, "SelectPicActivity145 "+photoUri);
			Cursor cursor = managedQuery(photoUri, pojo, null, null,null);
			if(cursor != null ){
				Log.e(ACTIVITY_SERVICE, "SelectPicActivity147 cursor != null ");
				int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);
				cursor.moveToFirst();
				picPath = cursor.getString(columnIndex);
				cursor.close();
			}else
				Log.e(ACTIVITY_SERVICE, "SelectPicActivity152 cursor == null ");
			    
			lastIntent.putExtra(KEY_PHOTO_PATH, picPath);
			Log.e(ACTIVITY_SERVICE, "SelectPicActivity247 "+picPath);
			setResult(Activity.RESULT_OK, lastIntent);
			finish(); */
			//------select image new try 17:53 end----//
		}
	else if(requestCode == SELECT_PIC_BY_TACK_PHOTO )
		{ 
			//select photo by tack photo
			if(paramIntent == null){
				 Log.e(ACTIVITY_SERVICE, "SelectPicActivity163 data == null");  
				Toast.makeText(this, "拍照出错", Toast.LENGTH_LONG).show();
				return;
			} 
			 this.mImagePath = "";
		        Bitmap localBitmap = (Bitmap)paramIntent.getExtras().get("data");
		        if (localBitmap != null)
		        {
		          this.mImagePath = writeToFile(localBitmap, TAKE_PHOTO_PATH_CACHE, PHOTO_NAME);
		          if (TextUtils.isEmpty(this.mImagePath))
		            this.mImagePath = writeToFile(localBitmap, "/udisk/baidu_startface/takephoto_cache/", PHOTO_NAME);
		          Log.e(ACTIVITY_SERVICE, "SelectPicActivity240 localBitmap"+mImagePath);  
		        }
		       // localBitmap.recycle();
		        lastIntent.putExtra(KEY_PHOTO_PATH, mImagePath);
				setResult(Activity.RESULT_OK, lastIntent);
				finish();
				}
				else{
					Toast.makeText(this, "拍照文件不正确!", Toast.LENGTH_LONG).show();
					
				}  
		        //----------add old code-----//
	  
			  //----------add old code  end-----//   
		
		 // if (TextUtils.isEmpty(this.mImagePath))
		        //Toast.makeText(this, getResources().getString(2131099651), 0).show();
		  Log.e(ACTIVITY_SERVICE, "SelectPicActivity254 localBitmap doPhoto almost done");   
		     // Intent localIntent = new Intent();
		     // localIntent.setClass(this, partd.class);
		    //  localIntent.putExtra("photo_path", this.mImagePath);
		     // localIntent.putExtra("can_upload", true);
		     // startActivity(localIntent);
	}
	
	//------doPhoto before 6-30---------//
	/*@SuppressWarnings("unused")
	private void doPhoto(int requestCode,Intent data){
		if(requestCode == SELECT_PIC_BY_PICK_PHOTO )  //从相册取图片，有些手机有异常情况，请注意
		{
			if(data == null){
				Toast.makeText(this, "选择图片文件出错", Toast.LENGTH_LONG).show();
				return;
			}
			photoUri = data.getData();
			if(photoUri == null ){
				Toast.makeText(this, "选择图片文件出错", Toast.LENGTH_LONG).show();
				return;
			}
		}
		else if(requestCode == SELECT_PIC_BY_TACK_PHOTO )
		{  
			 Bitmap photo = (Bitmap) data.getExtras().get("data");
			  if(fileUri != null) {
                  Log.d(TAG, "Image saved to:\n" + fileUri);
                  Log.d(TAG, "Image path:\n" + fileUri.getPath());
                  Log.d(TAG, "Image name:\n" + getName(fileUri)); // use uri.getLastPathSegment() if store in folder
              }
			if(data == null){
				 Log.e(ACTIVITY_SERVICE, "SelectPicActivity163 data == null");  
				Toast.makeText(this, "拍照出错", Toast.LENGTH_LONG).show();
				return;
			} 
			photoUri = data.getData();

			//filePath = StringUtils.isNotBlank(filePath)?filePath:getRealFilePath();
			if(photoUri == null ){
				  Bundle bundle = data.getExtras();    
	               if (bundle != null) {                 
	               Bitmap  photo1 = (Bitmap) bundle.get("data"); //get bitmap 
	               if(photo1 !=null)
	               {
	            	   Log.e(ACTIVITY_SERVICE, "SelectPicActivity173 get the photo");  
	               }
	               
	               //spath :生成图片取个名字和路径包含类型     
	               String spath =(String) "tttt";
	               saveImage( photo,spath);
	               }
	               else{
	            	   Toast.makeText(this, "拍照出错", Toast.LENGTH_LONG).show();
	            	   return;}
	               }
			}
		
		//--------------------//
		
		if (requestCode == REQUEST_CODE_PICK_IMAGE) {             
            Uri uri = data.getData();  
            //to do find the path of pic  
        
    } else if (requestCode == REQUEST_CODE_CAPTURE_CAMEIA ) {             
    Uri uri = data.getData();  
             //to do find the path of pic 
   
   //-------------------------//
		String[] pojo = {MediaStore.Images.Media.DATA};
		Log.e(ACTIVITY_SERVICE, "SelectPicActivity145 "+photoUri);
		Cursor cursor = managedQuery(photoUri, pojo, null, null,null);
		if(cursor != null ){
			Log.e(ACTIVITY_SERVICE, "SelectPicActivity147 cursor != null ");
			int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);
			cursor.moveToFirst();
			picPath = cursor.getString(columnIndex);
			cursor.close();
		}else
			Log.e(ACTIVITY_SERVICE, "SelectPicActivity152 cursor == null ");
		Log.i(TAG, "imagePath = "+picPath);
		//if(picPath != null && ( picPath.endsWith(".png") || picPath.endsWith(".PNG") ||picPath.endsWith(".jpg") ||picPath.endsWith(".JPG")  ))
		if(picPath !=null){
	    	Log.e(ACTIVITY_SERVICE, "SelectPicActivity156  picPath !=null");
			lastIntent.putExtra(KEY_PHOTO_PATH, picPath);
			setResult(Activity.RESULT_OK, lastIntent);
			finish();
		}else{
			Toast.makeText(this, "选择文件不正确!", Toast.LENGTH_LONG).show();
			
		}
	}*/
	//------doPhoto before 6-30 end---------//
	public static void saveImage(Bitmap photo, String spath) {
        try {
            BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream(spath, false));
            photo.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        return;
    }
	/** Create a file Uri for saving an image or video to specific folder
     * https://developer.android.com/guide/topics/media/camera.html#saving-media
     * */
    private static Uri getOutputMediaFileUri(int type)
    {
          return Uri.fromFile(getOutputMediaFile(type));
    }
 
    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(int type)
    {
        // To be safe, you should check that the SDCard is mounted
         
        if(Environment.getExternalStorageState() != null) {
            // this works for Android 2.2 and above
            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "AndroidCameraTestsFolder");
             
            // This location works best if you want the created images to be shared
            // between applications and persist after your app has been uninstalled.
 
            // Create the storage directory if it does not exist
            if (! mediaStorageDir.exists()) {
                if (! mediaStorageDir.mkdirs()) {
                    Log.d(TAG, "failed to create directory");
                    return null;
                }
            }
 
            // Create a media file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File mediaFile;
            if (type == MEDIA_TYPE_IMAGE){
                mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");
            } else if(type == MEDIA_TYPE_VIDEO) {
                mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                "VID_"+ timeStamp + ".mp4");
            } else {
                return null;
            }
 
            return mediaFile;
        }
         
        return null;
    }
	// grab the name of the media from the Uri
    protected String getPath(Uri contentUri) 
    {  
    	   try
           {
               String[] proj = {MediaStore.Images.Media.DATA};
               Cursor cursor = managedQuery(contentUri, proj, null, null, null);
               int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
               cursor.moveToFirst();
               return cursor.getString(column_index);
           }
           catch (Exception e)
           {
               return contentUri.getPath();
           }
    }
    
    public  String writeToFile(Bitmap paramBitmap, String paramString1, String paramString2)
    {
      String str = null;
      if (paramBitmap == null);
      while (true)
      {
        
        try
        {
          File localFile1 = new File(paramString1);
          if (!localFile1.exists())
            localFile1.mkdirs();
          File localFile2 = new File(paramString1 + paramString2);
          if (localFile2.exists())
            localFile2.delete();
          if (localFile2.createNewFile())
          {
            FileOutputStream localFileOutputStream = new FileOutputStream(localFile2);
            paramBitmap.compress(Bitmap.CompressFormat.JPEG, 70, localFileOutputStream);
            localFileOutputStream.flush();
            localFileOutputStream.close();
          }
          str = paramString1 + paramString2;
        }
        catch (FileNotFoundException localFileNotFoundException)
        {
          localFileNotFoundException.printStackTrace();
        }
        catch (IOException localIOException)
        {
          localIOException.printStackTrace();
        }
        
        return str;
      }
    }

}
