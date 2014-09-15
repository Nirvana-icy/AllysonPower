package com.speakup.ui;

import java.util.List;
import java.util.Map;
import com.parse.ParseQuery;
import com.speakup.parse.PartANewsInfo;
import com.speakup.util.Log;
import cn.trinea.android.common.entity.FailedReason;
import cn.trinea.android.common.service.impl.ImageCache;
import cn.trinea.android.common.service.impl.ImageMemoryCache.OnImageCallbackListener;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.method.ScrollingMovementMethod;
//import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

public class NewsAdapter extends BaseAdapter {
    private LayoutInflater newsInflater;
    public List<Map<String, PartANewsInfo>> newsData1;
    public Map<String, PartANewsInfo> tempmap;
	public NewsAdapter(Context context, List<Map<String,PartANewsInfo>> newsData, int newsContent) {
		ParseQuery.getQuery(PartANewsInfo.class);
		newsData1=newsData;
		this.newsInflater = LayoutInflater.from(context);
	}
	
	public void clear(){
		if(newsData1!=null){
			newsData1.clear();
		}
	}

	public int getCount() {
		
		return newsData1.size();
	}

	@Override
	public Object getItem(int position) {
		return newsData1.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
   
	@SuppressWarnings("deprecation")
	public View getView(int position, View convertView, ViewGroup parent) {
		NewsHolder holder;
		if (convertView == null) {
			convertView = newsInflater.inflate(R.layout.news_content, null);
			holder = new NewsHolder();
			holder.attIcon = (ImageView) convertView.findViewById(R.id.atticon);
			holder.subject = (TextView) convertView.findViewById(R.id.subject);
			holder.postername = (TextView) convertView.findViewById(R.id.postername);
			holder.posttime = (TextView) convertView.findViewById(R.id.posttime);
			holder.happentime = (TextView) convertView.findViewById(R.id.happentime);
			holder.happenarea = (TextView) convertView.findViewById(R.id.happenarea);
			holder.newscontent = (TextView) convertView.findViewById(R.id.newscontent);
			holder.newscontent.setMovementMethod(ScrollingMovementMethod.getInstance());
			holder.newsImage = (ImageView) convertView.findViewById(R.id.newsimage);
		    convertView.setTag(holder);
		      } 
		else {
			holder = (NewsHolder) convertView.getTag();
		      }
		tempmap = newsData1.get(position);
		if(tempmap.get("News_Text").getPositive()) 
		{
			holder.attIcon.setImageResource(R.drawable.po_news);}
		else
			holder.attIcon.setImageResource(R.drawable.ne_news);
		    	
		    	holder.subject.setText(tempmap.get("News_Text").getSubject());
		    	holder.postername.setText(tempmap.get("News_Text").getPosterName());
		    	holder.posttime.setText(tempmap.get("News_Text").getPostTime().toLocaleString());
		    	//holder.posttime.setText(tempmap.get("News_Text").getPostTime().getDate());
		    	holder.happentime.setText(tempmap.get("News_Text").getHappenTime());
		    	holder.happenarea.setText(tempmap.get("News_Text").getHappenArea());
		    	holder.newscontent.setText(tempmap.get("News_Text").getEventText());
		    	if(tempmap.get("News_Text").getHaveImage()) 
		    	{
		    		Log.d("find image!find image!find image!find image!find image!find image!");
		    		String imageUrl;
		    		imageUrl=tempmap.get("News_Text").getNewsImageURL();
		    		IMAGE_CACHE.get(imageUrl, holder.newsImage);
		    	}
				else
				{
					Log.d("no image!no image!no image!no image!no image!no image!no image!no image!");
				}
		    	return convertView;
		}
	
	public void remove(int position){
		newsData1.remove(position);
		this.notifyDataSetChanged();
	}
	
	
	public class NewsHolder {
	
	    private ImageView attIcon;
	    public ImageView getAttIcon(){
	        return attIcon;
	    }
	    public void setAttIcon(ImageView attIcon){
	        this.attIcon=attIcon;
	    }
	    //subject
	    private TextView subject;
	    public TextView getSubject(){
	        return subject;
	    }
	    public void setSubject(TextView subject){
	        this.subject=subject;
	    }
	    //poster name
	    private TextView postername;
	    public TextView getPosterName(){
	        return postername;
	    }
	    public void setPosterName(TextView postername){
	        this.postername=postername;
	    }
	   
	    //post time
	    private TextView posttime;
	    public TextView getPostTime(){
	        return posttime;
	    }
	    public void setPostTime(TextView posttime)
	    {
	        this.posttime=posttime;
	    }
	    //happen time
	    private TextView happentime;
	    public TextView getHappenTime(){
	        return happentime;
	    }
	    public void setHappenTime(TextView happentime)
	    {
	        this.happentime=happentime;
	    }
	  //happen area
	    private TextView happenarea;
	    public TextView getHappenArea(){
	        return happenarea;
	    }
	    public void setHappenArea(TextView happenarea)
	    {
	        this.happenarea=happenarea;
	    }
	    //news content
	    private TextView newscontent;
	    public TextView getNewsContent(){
	        return newscontent;
	    }
	    public void setNewsContent(TextView newscontent){
	        this.newscontent=newscontent;
	    }
	    //是否有图片
	    private Boolean haveNewsImage=false;
	    public Boolean getHaveImage(){
	        return haveNewsImage;
	    }
	    public void setHaveImage(Boolean newsImage){
	        this.haveNewsImage=newsImage;
	    }
	    
	    private ImageView newsImage;
	    public ImageView getLocation(){
	        return newsImage;
	    }
	    public void setLocation(ImageView newsImage){
	        this.newsImage=newsImage;
	    }

	}

   //ImageCacha

	public static final ImageCache IMAGE_CACHE = new ImageCache();

	static {
		OnImageCallbackListener imageCallBack = new OnImageCallbackListener() {

			
			//private static final String TAG_CACHE="123";

			@Override
			public void onPreGet(String imageUrl, View view) {
			}

			// callback function after get image successfully, run on ui thread
			@Override
			public void onGetSuccess(String imageUrl, Bitmap loadedImage, View view, boolean isInCache) {
				// can be another view child, like textView and so on
				if (view != null && loadedImage != null && view instanceof ImageView) {
					ImageView imageView = (ImageView)view;
					imageView.setImageBitmap(loadedImage);
				}
			}

			@Override
			public void onGetFailed(String imageUrl, Bitmap loadedImage, View view, FailedReason failedReason) {
				//Log.e(TAG_CACHE);
			}

			@Override
			public void onGetNotInCache(String imageUrl, View view) {

			}
		};
		IMAGE_CACHE.setOnImageCallbackListener(imageCallBack);
	}
}




