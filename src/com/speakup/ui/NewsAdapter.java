package com.speakup.ui;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import android.app.Activity;
import android.app.Dialog;
import java.util.Map;

//import com.image.dialog.R;
import com.parse.ParseQuery;
import com.speakup.parse.PartANewsInfo;
import com.speakup.util.Log;
import cn.trinea.android.common.entity.FailedReason;
import cn.trinea.android.common.service.impl.ImageCache;
import cn.trinea.android.common.service.impl.ImageMemoryCache.OnImageCallbackListener;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.method.ScrollingMovementMethod;
//import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.*;
import com.speakup.ui.MyImageBtn;


public class NewsAdapter extends BaseAdapter {

	private Dialog dialog;
	private LayoutInflater newsInflater;
	public List<Map<String, PartANewsInfo>> newsdata;
	public Map<String, PartANewsInfo> tempmap;
	private Activity partaActivity;
	private String imageUrl;

	public NewsAdapter(Context context,
			List<Map<String, PartANewsInfo>> newsData, int newsContent,
			Activity partaActivity) {
		LayoutInflater.from(context);
		this.partaActivity = partaActivity;
		ParseQuery.getQuery(PartANewsInfo.class);
		newsdata = newsData;
		this.newsInflater = LayoutInflater.from(context);

	}

	public void clear() {
		if (newsdata != null) {
			newsdata.clear();
		}
	}

	public int getCount() {
		return newsdata.size();
	}

	@Override
	public Object getItem(int position) {
		return newsdata.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressWarnings("deprecation")
	public View getView(int position, View convertView, ViewGroup parent) {
		final NewsHolder holder;
		if (convertView == null) {
			Log.d("73 convertView is  null");
			convertView = newsInflater.inflate(R.layout.news_content, null);
			holder = new NewsHolder();
			holder.attIcon = (ImageView) convertView.findViewById(R.id.atticon);
			holder.subject = (TextView) convertView.findViewById(R.id.subject);
			holder.postername = (TextView) convertView
					.findViewById(R.id.postername);
			holder.posttime = (TextView) convertView
					.findViewById(R.id.posttime);
			holder.happentime = (TextView) convertView
					.findViewById(R.id.happentime);
			holder.happenarea = (TextView) convertView
					.findViewById(R.id.happenarea);
			holder.newscontent = (TextView) convertView
					.findViewById(R.id.newscontent);
			holder.newscontent.setMovementMethod(ScrollingMovementMethod
					.getInstance());
			holder.btn_newsImage = (MyImageBtn) convertView
					.findViewById(R.id.seephoto);
			convertView.setTag(holder);
		} else {
			Log.d("88 convertView is  not null");
			holder = (NewsHolder) convertView.getTag();
		}

		tempmap = newsdata.get(position);
		if (tempmap.get("News_Text").getPositive())
			holder.attIcon.setImageResource(R.drawable.po_news);
		else
			holder.attIcon.setImageResource(R.drawable.ne_news);

		holder.subject.setText(tempmap.get("News_Text").getSubject());
		holder.postername.setText(tempmap.get("News_Text").getPosterName());
		holder.posttime.setText(tempmap.get("News_Text").getPostTime()
				.toLocaleString());
		// holder.posttime.setText(tempmap.get("News_Text").getPostTime().getDate());
		holder.happentime.setText(tempmap.get("News_Text").getHappenTime());
		holder.happenarea.setText(tempmap.get("News_Text").getHappenArea());

		if (tempmap.get("News_Text").getHaveImage()) { 
			// have image
			holder.newscontent.setText(tempmap.get("News_Text").getNewsImageURL());

			holder.btn_newsImage.setImageResource(R.drawable.ver);
			
			
			holder.btn_newsImage.setImageUrl(tempmap.get("News_Text").getNewsImageURL());
			
			holder.btn_newsImage.setOnClickListener(new Button.OnClickListener() {
						@Override
						public void onClick(View arg0) {
							Log.d("ttclick imagebutton click imagebutton");
							dialog = new Dialog(partaActivity);
							dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
							dialog.getWindow().setFlags(
									WindowManager.LayoutParams.FLAG_FULLSCREEN,
									WindowManager.LayoutParams.FLAG_FULLSCREEN);
							dialog.setContentView(R.layout.image_dialog);
							show();
					
							String imageUrl = holder.btn_newsImage.getImageUrl();
							
							//String imageUrl = "http://source.qunar.com/mkt_download/guide/rio_de_janeiro/imgs/small_cover_eab6f216.jpg";

							// imageUrl=tempmap.get("News_Text").getNewsImageURL();
							// imageUrl="http://files.parsetfss.com/1c7638ad-a242-4244-91f2-624ddea87453/tfss-260c534b-75a0-454c-b683-3a9c942357bb-IMG_2044.jpg";
							Log.d("after click url" + imageUrl);
							Log.d("id:" + tempmap.get("News_Text").getEventText());
							ImageView image = (ImageView) dialog.findViewById(R.id.image);
							new DownloadImageTask((ImageView) dialog.findViewById(R.id.image)).execute(imageUrl);

							image.setOnClickListener(new ImageView.OnClickListener() {

								@Override
								public void onClick(View arg0) {
									if (dialog != null) {
										dialog.dismiss();
									}
								}
							});
						}
					});
		} else {
			//holder.btn_newsImage.setVisibility(View.INVISIBLE);
			// holder.btn_newsImage.setImageResource(R.drawable.location);
			// Log.d("no image!no image! "+tempmap.get("News_Text").getEventText());
		}
		return convertView;
	}

	protected void show() {
		dialog.show();

	}

	public void remove(int position) {
		newsdata.remove(position);
		this.notifyDataSetChanged();
	}

	public class NewsHolder {

		private ImageView attIcon;

		public ImageView getAttIcon() {
			return attIcon;
		}

		// subject
		private TextView subject;

		public TextView getSubject() {
			return subject;
		}

		public void setSubject(TextView subject) {
			this.subject = subject;
		}

		public void setAttIcon(ImageView attIcon) {
			this.attIcon = attIcon;
		}

		// poster name
		private TextView postername;

		public TextView getPosterName() {
			return postername;
		}

		public void setPosterName(TextView postername) {
			this.postername = postername;
		}

		// post time
		private TextView posttime;

		public TextView getPostTime() {
			return posttime;
		}

		public void setPostTime(TextView posttime) {
			this.posttime = posttime;
		}

		// happen time
		private TextView happentime;

		public TextView getHappenTime() {
			return happentime;
		}

		public void setHappenTime(TextView happentime) {
			this.happentime = happentime;
		}

		// happen area
		private TextView happenarea;

		public TextView getHappenArea() {
			return happenarea;
		}

		public void setHappenArea(TextView happenarea) {
			this.happenarea = happenarea;
		}

		// news content
		private TextView newscontent;

		public TextView getNewsContent() {
			return newscontent;
		}

		public void setNewsContent(TextView newscontent) {
			this.newscontent = newscontent;
		}

		// have image or not
		private Boolean haveNewsImage = false;

		public Boolean getHaveImage() {
			return haveNewsImage;
		}

		public void setHaveImage(Boolean newsImage) {
			this.haveNewsImage = newsImage;
		}

		private MyImageBtn btn_newsImage;

		public ImageView getLocation() {
			return btn_newsImage;
		}

		public void setLocation(MyImageBtn newsImage) {
			this.btn_newsImage = newsImage;
		}

	}

	
	// ImageCacha
	/*
	 * public static final ImageCache IMAGE_CACHE = new ImageCache();
	 * 
	 * static { OnImageCallbackListener imageCallBack = new
	 * OnImageCallbackListener() {
	 * 
	 * 
	 * //private static final String TAG_CACHE="123";
	 * 
	 * @Override public void onPreGet(String imageUrl, View view) { }
	 * 
	 * // callback function after get image successfully, run on ui thread
	 * 
	 * @Override public void onGetSuccess(String imageUrl, Bitmap loadedImage,
	 * View view, boolean isInCache) { // can be another view child, like
	 * textView and so on if (view != null && loadedImage != null && view
	 * instanceof ImageView) { ImageView imageView = (ImageView)view;
	 * imageView.setImageBitmap(loadedImage); } }
	 * 
	 * @Override public void onGetFailed(String imageUrl, Bitmap loadedImage,
	 * View view, FailedReason failedReason) { //Log.e(TAG_CACHE); }
	 * 
	 * @Override public void onGetNotInCache(String imageUrl, View view) {
	 * 
	 * } }; IMAGE_CACHE.setOnImageCallbackListener(imageCallBack); }
	 */
	/*
	 * public static Drawable LoadImageFromWebOperations(String urlstring) {
	 * InputStream is=null;
	 * 
	 * HttpURLConnection con = null; try { Log.d("TRYTRYTRY"+urlstring); URL
	 * url1 = new URL(urlstring);
	 * 
	 * 
	 * if(url1!=null) { Log.d("URLc not null"+url1); } con = (HttpURLConnection)
	 * url1.openConnection(); if(con==null) Log.d("con==null"); else
	 * Log.d("con!=null"+con); Log.d("TTTTTT"); //Object content =
	 * url1.getContent(); is=url1.openStream(); //is = con.getInputStream(); //
	 * InputStream is = (InputStream) new URL(urlstring).getContent();
	 * Log.d("TTTTTTend"); Drawable d = Drawable.createFromStream(is,
	 * "src name"); return d; } catch (Exception e) { Log.d("TTTTTTNULL");
	 * return null; } }
	 */
	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		ImageView bmImage;

		public DownloadImageTask(ImageView bmImage) {

			this.bmImage = bmImage;
		}

		protected Bitmap doInBackground(String... urls) {
			String urldisplay = urls[0];
			Bitmap mIcon11 = null;
			try {
				InputStream in = new java.net.URL(urldisplay).openStream();
				mIcon11 = BitmapFactory.decodeStream(in);
			} catch (Exception e) {
				Log.e("bitmap error " + e.getMessage());
				e.printStackTrace();
			}
			return mIcon11;
		}

		protected void onPostExecute(Bitmap result) {
			bmImage.setImageBitmap(result);
		}
	}

}
