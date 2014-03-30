package com.allysonpower.ui;

import android.app.Activity;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import android.app.ListActivity;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.allysonpower.parse.AllysonNewsInfo;
import com.markupartist.android.widget.PullToRefreshListView;
import com.markupartist.android.widget.PullToRefreshListView.OnRefreshListener;
import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

public class parta extends ListActivity {    
    private ParseQuery<AllysonNewsInfo> query;

    private LinkedList<String> mListItems_News;
    private LinkedList<String> mListItems_UploadTime;
    
    private int pullDownTimes = 0;  //如果总共有50条news  用户连续pull down十次..此时在第六次pull down的时候 我们就不去query服务器了 因为再次query 程序会fc
    private int howManyNews = 0;  //Parse中 总共存储了多少news
    private int numOfItmesInOnePage = 10;  //自定义的 每一页显示多少条news


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parta_home);
        
        // Set a listener to be invoked when the list should be refreshed.
        ((PullToRefreshListView) getListView()).setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Do work to refresh the list here.
                new GetDataTask().execute();
            }
        });
        
        //regist the subclass of ParseObject first and then call Parse.initialize
        ParseObject.registerSubclass(AllysonNewsInfo.class);
        Parse.initialize(this, "bDExeWi2vct7yqm52r5WPnEiuNyorLu9B2tSFREW", "MvzGGKqOt5Q56inQCPNpbYLAaiJmtykCjgh93C1K");
        
		query = ParseQuery.getQuery(AllysonNewsInfo.class);
		
		query.orderByDescending("updatedAt");
		query.setLimit(numOfItmesInOnePage); 
		//launch partb => load the latestest 10 news automatically
		query.findInBackground(new FindCallback<AllysonNewsInfo>() {
			@Override
			public void done(List<AllysonNewsInfo> newsList, ParseException e) {
				// TODO Auto-generated method stub
				if (e == null) {
					Log.i("com.allysonpower.ui.GetNewsFromParse", "Retrieved " + newsList.size() + " news from Parse.");
					mListItems_News = new LinkedList<String>();
					mListItems_UploadTime  = new LinkedList<String>();;
					for(int i = 0; i < newsList.size(); i++)
					{
						//依次将每一条从Parse retrieved的news 添加到list中
						mListItems_News.add(newsList.get(i).getEventText());
						Log.i("com.allysonpower.ui.paartb.GetNewsFromParse", "GetNewsEventText:" + newsList.get(i).getEventText());
						if(newsList.get(i).getPositive())
							mListItems_UploadTime.add("Positive!   Post@" + newsList.get(i).getPostTime().toLocaleString());
						else
							mListItems_UploadTime.add("Negative..  Post@" + newsList.get(i).getPostTime().toLocaleString());
					}
					//使用系统自带的list 每个ListItem有两行  第一行大字体显示news信息（android.R.id.text1）  第二行小字体显示 上传时间（android.R.id.text2）
					//根据从parse获取到的数据 设置第一行文字  android.R.id.text1
					ArrayAdapter<String> adapterNews = new ArrayAdapter<String>(getApplicationContext(),
			                android.R.layout.simple_list_item_2, android.R.id.text1, mListItems_News);
										
			        setListAdapter(adapterNews);
			        //根据从parse获取道德数据 设置第二行文字 android.R.id.text1
			        ArrayAdapter<String> adapterUploadTime = new ArrayAdapter<String>(getApplicationContext(),
			                android.R.layout.simple_list_item_2, android.R.id.text2, mListItems_UploadTime);
					
			        setListAdapter(adapterUploadTime);
					//Get how many news after the first qurey
					query.countInBackground(new CountCallback() {
						  public void done(int count, ParseException e) {
						    if (e == null) howManyNews = count;
						    else howManyNews = numOfItmesInOnePage;
						  }
						});
				} else {
					Log.d("com.allysonpower.ui.partb.GetNewsFromParse", "Error:" + e.getMessage());
				}
			}
		});
    }

    private class GetDataTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
        	
            return mStrings;
        }

        @Override
        protected void onPostExecute(String[] result) {
        	pullDownTimes++;
        	//如果还有更多news 则继续query from parse 
        	if(pullDownTimes*numOfItmesInOnePage < howManyNews) 
        	{
        		query.setSkip(pullDownTimes*numOfItmesInOnePage);   //You can skip the first results with setSkip. This can be useful for pagination:
        		query.findInBackground(new FindCallback<AllysonNewsInfo>() {
        			@Override
        			public void done(List<AllysonNewsInfo> newsList, ParseException e) {
        				// TODO Auto-generated method stub
        				if (e == null) {
        					Log.i("com.allysonpower.ui.GetNewsFromParse", "Retrieved " + newsList.size() + " news from Parse.");
        					mListItems_News = new LinkedList<String>();
        					mListItems_UploadTime  = new LinkedList<String>();;
        					for(int i = 0; i < newsList.size(); i++)
        					{
        						//依次将每一条从Parse retrieved的news 添加到list中
        						mListItems_News.add(newsList.get(i).getEventText());
        						if(newsList.get(i).getPositive())
        							mListItems_UploadTime.add("Positive!   Post@" + newsList.get(i).getPostTime().toLocaleString());
        						else
        							mListItems_UploadTime.add("Negative..  Post@" + newsList.get(i).getPostTime().toLocaleString());
        						Log.i("com.allysonpower.ui.paartb.GetNewsFromParse", "GetNewsEventText:" + newsList.get(i).getEventText());
        					}
        					//使用系统自带的list 每个ListItem有两行  第一行大字体显示news信息（android.R.id.text1）  第二行小字体显示 上传时间（android.R.id.text2）
        					//根据从parse获取到的数据 设置第一行文字  android.R.id.text1
        					ArrayAdapter<String> adapterNews = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_2, android.R.id.text1, mListItems_News);
    										
        					setListAdapter(adapterNews);
        					//根据从parse获取道德数据 设置第二行文字 android.R.id.text1
        					ArrayAdapter<String> adapterUploadTime = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_2, android.R.id.text2, mListItems_UploadTime);
    					
        					setListAdapter(adapterUploadTime);
        				} 
        				else {
        					Log.d("com.allysonpower.ui.partb.GetNewsFromParse", "Error:" + e.getMessage());
        				}
        			}
        		});
        		// Call onRefreshComplete when the list has been refreshed.
        		((PullToRefreshListView) getListView()).onRefreshComplete();
        		super.onPostExecute(result);
        	}
        	else
        		Toast.makeText(getApplicationContext(), R.string.no_more_news, Toast.LENGTH_SHORT).show();
        }
   }

    private String[] mStrings = {"AllysonPower", "Pull to refresh."};
}
        

