package com.allysonpower.ui;

import android.app.Activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.app.ListActivity;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.SimpleAdapter;
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

    private List<Map<String, String>> newsData = new ArrayList<Map<String,String>>();  
    
    private int pullDownTimes = 0;  //����ܹ���50��news �û��ڵ�����pull down��ʱ�� ���ǾͲ�ȥquery�������� ��Ϊ�ٴ�query �����fc
    private int howManyNews = 0;  //Parse�� �ܹ��洢�˶���news
    private int numOfItmesInOnePage = 10;  //�Զ���� ÿһҳ��ʾ������news


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
		
		query.orderByDescending("createAt");
		query.setLimit(numOfItmesInOnePage); 
		//launch partb => load the latestest 10 news automatically
		query.findInBackground(new FindCallback<AllysonNewsInfo>() {
			@SuppressWarnings("deprecation")
			@Override
			public void done(List<AllysonNewsInfo> newsList, ParseException e) {
				// TODO Auto-generated method stub
				if (e == null) {
					Log.i("com.allysonpower.ui.GetNewsFromParse", "Retrieved " + newsList.size() + " news from Parse.");
					
					//���list��ÿһ�е�string����  lsit��ÿһ���а�����С�� ��һ�д�������ʾnews��Ϣ��android.R.id.text1��  �ڶ���С������ʾ �ϴ�ʱ�䣨android.R.id.text2�� 
					for(int i = 0; i < newsList.size(); i++)
					{
						Map<String, String> map = new HashMap<String, String>();
				        map.put("News_Text", newsList.get(i).getEventText()); 
						Log.i("com.allysonpower.ui.paartb.GetNewsFromParse", "GetNewsEventText:" + newsList.get(i).getEventText());
						if(newsList.get(i).getPositive())
							map.put("News_Property", "Positive!   Post@" + newsList.get(i).getPostTime().toLocaleString());
						else
							map.put("News_Property", "Negative!   Post@" + newsList.get(i).getPostTime().toLocaleString());
						
						newsData.add(map);
						Log.e("News_Property", newsData.toString());
					}
					//����parse��õ����� �󶨵�list��
					setListAdapter(new SimpleAdapter(getApplicationContext(),newsData,R.layout.parta_listview_two_text_item,  
			                new String[]{"News_Text","News_Property"},             
			                new int[]{android.R.id.text1,android.R.id.text2})
					);
	
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
        	//������и���news �����query from parse 
        	if(pullDownTimes*numOfItmesInOnePage < howManyNews) 
        	{
        		query.setSkip(pullDownTimes*numOfItmesInOnePage);   //You can skip the first results with setSkip. This can be useful for pagination:
        		query.findInBackground(new FindCallback<AllysonNewsInfo>() {
        			@Override
        			public void done(List<AllysonNewsInfo> newsList, ParseException e) {
        				// TODO Auto-generated method stub
        				if (e == null) {
        					//���֮ǰ��һҳ������
        					newsData.clear();
        					//���list��ÿһ�е�string����  lsit��ÿһ���а�����С�� ��һ�д�������ʾnews��Ϣ��android.R.id.text1��  �ڶ���С������ʾ �ϴ�ʱ�䣨android.R.id.text2�� 
        					for(int i = 0; i < newsList.size(); i++)
        					{
        						Map<String, String> map = new HashMap<String, String>();
        				        map.put("News_Text", newsList.get(i).getEventText()); 
        						Log.i("com.allysonpower.ui.paartb.GetNewsFromParse", "GetNewsEventText:" + newsList.get(i).getEventText());
        						if(newsList.get(i).getPositive())
        							map.put("News_Property", "Positive!   Post@" + newsList.get(i).getPostTime().toLocaleString());
        						else
        							map.put("News_Property", "Negative!   Post@" + newsList.get(i).getPostTime().toLocaleString());
        						
        						newsData.add(map);
        						Log.e("News_Property", newsData.toString());
        					}
        					//����parse��õ����� �󶨵�list��
        					setListAdapter(new SimpleAdapter(getApplicationContext(),newsData,R.layout.parta_listview_two_text_item,  
        			                new String[]{"News_Text","News_Property"},             
        			                new int[]{android.R.id.text1,android.R.id.text2})
        					);
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
        

