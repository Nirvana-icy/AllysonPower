package com.speakup.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;

import com.actionbarsherlock.view.Window;

import com.markmao.pulltorefresh.widget.XListView;
import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.speakup.parse.PartANewsInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * XListView demo
 *
 * @author markmjw
 * @date 2013-10-08
 */
public class parta extends Activity implements XListView.IXListViewListener {
    private XListView mListView;

    private ParseQuery<PartANewsInfo> query;
    
    private List<Map<String, PartANewsInfo>> newsData = new ArrayList<Map<String,PartANewsInfo>>();  
    
    private int loadMoreTimes = 0;  
    private int howManyNews = 0; 
    private int numOfItmesInOnePage = 20;
    
    private boolean bIsQueryParse = false;
    
    private Handler mHandler;

    public static void launch(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, parta.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        createPartAUI(); //Step A after onCreate
        initListView(); //Step B init xListView
        loadList(); //Step C load data from parse
    }
    
    private void initListView() {
        mHandler = new Handler();
        mListView = (XListView) findViewById(R.id.list_view);
        mListView.setPullRefreshEnable(true);
        mListView.setPullLoadEnable(true);
        mListView.setAutoLoadEnable(true);
        mListView.setXListViewListener(this);
        mListView.setRefreshTime(getTime());
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (hasFocus) {
            mListView.autoRefresh();
        }
    }

    @Override
    public void onRefresh() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                mIndex = ++mRefreshIndex;
//                items.clear();
//                geneItems();
//                mAdapter = new ArrayAdapter<String>(parta.this, R.layout.vw_list_item,
//                        items);
//                mListView.setAdapter(mAdapter);
            	if(loadMoreTimes > 0 && !bIsQueryParse)
    			{
    				loadMoreTimes--;
    				queryDataFromParse();
    			}
                onLoad();
            }
        }, 2000);
    }

    @Override
    public void onLoadMore() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
            	if (!bIsQueryParse) {
            		loadMoreTimes++;
            		queryDataFromParse();
 //               mAdapter.notifyDataSetChanged();
                	onLoad();
            	}
            }
        }, 2000);
    }

    private void onLoad() {
        mListView.stopRefresh();
        mListView.stopLoadMore();
        mListView.setRefreshTime(getTime());
    }

    private String getTime() {
        return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA).format(new Date());
    }
    
    public void queryDataFromParse()
	{
		if(loadMoreTimes*numOfItmesInOnePage < howManyNews) 
    	{
			bIsQueryParse = true;
    		query.setSkip(loadMoreTimes*numOfItmesInOnePage);   //You can skip the first results with setSkip. This can be useful for pagination:
    		query.findInBackground(new FindCallback<PartANewsInfo>() {
    			@Override
    			public void done(List<PartANewsInfo> newsList, ParseException e) {
    				bIsQueryParse = false;
    				if (e == null) {
    					newsData.clear();//try to remove this ,then the latest news can be shown continuouly but move to the first line
    					for(int i = 0; i < newsList.size(); i++)
    					{
    						Map<String,PartANewsInfo> map = new HashMap<String, PartANewsInfo>();
    				        map.put("News_Text", newsList.get(i)); 
    						newsData.add(map);
    					}
    					mListView.setAdapter(new NewsAdapter(getApplicationContext(),newsData,R.layout.news_content, parta.this)

    					);
    					//newsAdapter.notifyDataSetChanged();
    				} 
    				else {
    					//Log.d("com.speakup.ui.partb.GetNewsFromParse", "Error:" + e.getMessage());
    				}
    			}
 
    		});
    		}else{
    			//鍦ㄨ繖涓皢鏄剧ず鏇村鐨勬爣蹇楁樉绀轰负娌℃湁鏇村淇℃伅
    			
    		}
		
	}
    
    private void loadList() {
//    	WorkThread workthread = new WorkThread();  
//        workthread.start(); 
    	//Register the subclass for ParseObject first and then call Parse.initialize
        ParseObject.registerSubclass(PartANewsInfo.class);
        Parse.initialize(this, "bDExeWi2vct7yqm52r5WPnEiuNyorLu9B2tSFREW", "MvzGGKqOt5Q56inQCPNpbYLAaiJmtykCjgh93C1K");
        
        query = ParseQuery.getQuery(PartANewsInfo.class);
		query.orderByDescending("createdAt");
		query.setLimit(numOfItmesInOnePage);
	
		query.findInBackground(new FindCallback<PartANewsInfo>() {
 
			@Override
			public void done(List<PartANewsInfo> newsList, ParseException e) {
				// TODO Auto-generated method stub
				if (e == null) {
					//Log.i("com.allysonpower.ui.GetNewsFromParse", "Retrieved " + newsList.size() + " news from Parse.");
					for(int i = 0; i < newsList.size(); i++)
					{  	
						Map<String, PartANewsInfo> map = new HashMap<String, PartANewsInfo>();
						map.put("News_Text", newsList.get(i));
						newsData.add(map);
						Log.e("News_Text", "~~~~~~~" + newsList.get(i)+" "+newsList.get(i).getEventText());
						Log.e("News_Text", "~~~~~~~" + newsList.get(i)+" "+newsList.get(i).getHaveImage());
					}
					mListView.setAdapter(new NewsAdapter(getApplicationContext(),newsData,R.layout.news_content, parta.this)
);
	
					//Get how many news after the first qurey
					query.countInBackground(new CountCallback() {
						  public void done(int count, ParseException e) {
						    if (e == null) {
						    	howManyNews = count;
//						    	if(count < numOfItmesInOnePage) ((PullToRefreshListView) getListView()).hiddenPullToRefresh();
						    }
						    else howManyNews = numOfItmesInOnePage;
						  }
						});
				} else {
					Log.e(ACTIVITY_SERVICE, "parta.loadlist().else.e!=null!!!@@@@");
				}
			}

			
		});
 
	}
    
    private void createPartAUI(){
    	requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        
        setContentView(R.layout.parta_home);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_bar);
          
        ImageButton btn_movetopartb,btn_movetopartc,btn_movetopartd,btn_back; 
		btn_movetopartb = (ImageButton)findViewById(R.id.btn_movetopartb);  
		btn_movetopartc = (ImageButton)findViewById(R.id.btn_movetopartc);  
		btn_movetopartd = (ImageButton)findViewById(R.id.btn_movetopartd);  
		btn_back = (ImageButton)findViewById(R.id.btn_back);
		
		
		
		
		btn_movetopartb.setOnClickListener(new OnClickListener(){
 
			    @Override
				public void onClick(View arg0) {
					Intent intent = new Intent(); 
		            intent.setClass(parta.this, partb.class);
		            parta.this.startActivity(intent);
				}
	        	
	        });
		btn_movetopartc.setOnClickListener(new OnClickListener(){
 
				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(); 
		            intent.setClass(parta.this, partc.class);
		            parta.this.startActivity(intent);
				}
	        	
	        });
		btn_movetopartd.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(); 
		            intent.setClass(parta.this, partd.class);
		            parta.this.startActivity(intent);
				}
	        });
		btn_back.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(); 
		            intent.setClass(parta.this, FourIcon_page.class);
		           // parta.this.startActivity(intent);
				}
	        }); 
    }

}
