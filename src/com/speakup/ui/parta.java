package com.speakup.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import com.markupartist.android.widget.PullToRefreshListView;
import com.markupartist.android.widget.PullToRefreshListView.OnRefreshListener;
import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.speakup.parse.PartANewsInfo;
import com.speakup.ui.R;

public class parta extends ListActivity { 

	
    private ParseQuery<PartANewsInfo> query;
  
    private List<Map<String, PartANewsInfo>> newsData = new ArrayList<Map<String,PartANewsInfo>>();  
    
    
    private int pullDownTimes = 0;  
    private int howManyNews = 0; 
    private int numOfItmesInOnePage = 10;  
    public boolean checkRefreshComplete=false;
    private Token token = null;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.parta_home);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_bar);
        token = new Token();  
        loadList();
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
		            parta.this.startActivity(intent);
				}
	        	
	        });
	        
    }

    private void loadList() {
    	WorkThread workthread = new WorkThread();  
        workthread.start(); 
        ((PullToRefreshListView) getListView()).setOnRefreshListener(new OnRefreshListener() {
        	
        	
            @Override
            public void onRefresh() {
            	synchronized (token) {  
            		new GetDataTask().execute();
                    token.setFlag("wangmz");  
                    token.notifyAll();  
                    Log.v("Main","parta108tokenflag");  
                }
            	/*if(!checkRefreshComplete){
                // Do work to refresh the list here.
            		new GetDataTask().execute();
                }
            	else
            		{
            		Log.d(ACTIVITY_SERVICE, "parta.onrefresh107"); 
            	    new GetDataTaskAagain().execute();}*/
            	}
            
            
            });
    	//Register the subclass for ParseObject first and then call Parse.initialize
        ParseObject.registerSubclass(PartANewsInfo.class);
        Parse.initialize(this, "bDExeWi2vct7yqm52r5WPnEiuNyorLu9B2tSFREW", "MvzGGKqOt5Q56inQCPNpbYLAaiJmtykCjgh93C1K");
        
        query = ParseQuery.getQuery(PartANewsInfo.class);
		query.orderByDescending("createAt");
		query.setLimit(numOfItmesInOnePage);
	
		query.findInBackground(new FindCallback<PartANewsInfo>() {

			@Override
			public void done(List<PartANewsInfo> newsList, ParseException e) {
				if (e == null) {
					Log.e(ACTIVITY_SERVICE, "parta.loadlist().e==null");
					//Log.i("com.allysonpower.ui.GetNewsFromParse", "Retrieved " + newsList.size() + " news from Parse.");
					for(int i = 0; i < newsList.size(); i++)
					{  
						Map<String, PartANewsInfo> map = new HashMap<String, PartANewsInfo>();
						map.put("News_Text", newsList.get(i));
						newsData.add(map);
						Log.e("News_Property", "~~~~~~~" + newsList.get(i));
					}
			    setListAdapter(new NewsAdapter(getApplicationContext(),newsData,R.layout.news_content)
					);
	
					//Get how many news after the first qurey
					query.countInBackground(new CountCallback() {
						  public void done(int count, ParseException e) {
						    if (e == null) {
						    	howManyNews = count;
						    	if(count < numOfItmesInOnePage) ((PullToRefreshListView) getListView()).hiddenPullToRefresh();
						    }
						    else howManyNews = numOfItmesInOnePage;
						  }
						});
				} else {
					Log.e(ACTIVITY_SERVICE, "parta.loadlist().else.e!=null");
				}
			}
			
		});

	}

	private class GetDataTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
        	   // Simulates a background job.  
           /* try {  
                Thread.sleep(1000);  
            } catch (InterruptedException e) {  
            }  */
            String[] str={"Added after refresh...I add","ssss"};  
            return str;  
        }

        @Override
        protected void onPostExecute(String[] result) {	
        	pullDownTimes++;
        	if(howManyNews - pullDownTimes*numOfItmesInOnePage < numOfItmesInOnePage)
        	{
            	((PullToRefreshListView) getListView()).hiddenPullToRefresh();
        	}
        	if(pullDownTimes*numOfItmesInOnePage < howManyNews) 
        	{
        		query.setSkip(pullDownTimes*numOfItmesInOnePage);   //You can skip the first results with setSkip. This can be useful for pagination:
        		query.findInBackground(new FindCallback<PartANewsInfo>() {
        			@Override
        			public void done(List<PartANewsInfo> newsList, ParseException e) {
        				if (e == null) {
        					newsData.clear();
        					for(int i = 0; i < newsList.size(); i++)
        					{
        						Map<String,PartANewsInfo> map = new HashMap<String, PartANewsInfo>();
        				        map.put("News_Text", newsList.get(i)); 
        				        
        						//Log.i("com.speakup.ui.paartb.GetNewsFromParse", "GetNewsEventText:" + newsList.get(i).getEventText());
        						if(newsList.get(i).getPositive())
        							map.put("Positive"+i, newsList.get(i));
        						else
        							map.put("Negative"+i, newsList.get(i));
        						newsData.add(map);
        						//Log.e("News_Property", newsData.toString());
        						Log.e(ACTIVITY_SERVICE, "parta.onPostExecute()186"+newsData.toString());
        					}
        					setListAdapter(new NewsAdapter(getApplicationContext(),newsData,R.layout.news_content)
        					);
        				} 
        				else {
        					//Log.d("com.speakup.ui.partb.GetNewsFromParse", "Error:" + e.getMessage());
        				}
        			}
        		});
        		// Call onRefreshComplete when the list has been refreshed.
        		((PullToRefreshListView) getListView()).onRefreshComplete();
        		 checkRefreshComplete= true;
        		super.onPostExecute(result);
        	}
        }
   }
	private class GetDataTaskAagain extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
        	   // Simulates a background job.  
           /* try {  
                Thread.sleep(1000);  
            } catch (InterruptedException e) {  
            }  */
            String[] str={"Added after refresh...I add","ssss"};  
            return str;  
        }

        @Override
        protected void onPostExecute(String[] result) {	
        	pullDownTimes--;
        	if(howManyNews - pullDownTimes*numOfItmesInOnePage < numOfItmesInOnePage)
        	{
            	((PullToRefreshListView) getListView()).hiddenPullToRefresh();
        	}
        	if(pullDownTimes*numOfItmesInOnePage < howManyNews) 
        	{
        		query.setSkip(pullDownTimes*numOfItmesInOnePage);   //You can skip the first results with setSkip. This can be useful for pagination:
        		query.findInBackground(new FindCallback<PartANewsInfo>() {
        			@Override
        			public void done(List<PartANewsInfo> newsList, ParseException e) {
        				if (e == null) {
        					newsData.clear();
        					for(int i = 0; i < newsList.size(); i++)
        					{
        						Map<String,PartANewsInfo> map = new HashMap<String, PartANewsInfo>();
        				        map.put("News_Text", newsList.get(i)); 
        				        
        						//Log.i("com.speakup.ui.paartb.GetNewsFromParse", "GetNewsEventText:" + newsList.get(i).getEventText());
        						if(newsList.get(i).getPositive())
        							map.put("Positive"+i, newsList.get(i));
        						else
        							map.put("Negative"+i, newsList.get(i));
        						newsData.add(map);
        						//Log.e("News_Property", newsData.toString());
        						Log.e(ACTIVITY_SERVICE, "parta.onPostExecute()186"+newsData.toString());
        					}
        					setListAdapter(new NewsAdapter(getApplicationContext(),newsData,R.layout.news_content)
        					);
        				} 
        				else {
        					//Log.d("com.speakup.ui.partb.GetNewsFromParse", "Error:" + e.getMessage());
        				}
        			}
        		});
        		// Call onRefreshComplete when the list has been refreshed.
        		((PullToRefreshListView) getListView()).onRefreshComplete();
        		 checkRefreshComplete= true;
        		super.onPostExecute(result);
        	}
        }
   }
	private class Token{
		 private String flag;  
	        public Token() {  
	            setFlag(null);  
	        }  
	        public void setFlag(String flag) {  
	            this.flag = flag;  
	            }  
	        public String getFlag() {  
	            return flag;}
	       }
	private class WorkThread extends Thread {  
        @Override  
        public void run() {  
            Random r=new Random();  
            while (true) {                
//              try {  
//                  Thread.sleep((r.nextInt()+1)*1000);//可能在sleep的时候其他线程执行notify()。但此时对这个线程不起作用。所以结果不会按顺序出现  
//              } catch (InterruptedException e1) {  
//                  e1.printStackTrace();  
//              }  
                synchronized (token) {  
                        try {  
                            token.wait();  
                            Log.v("Main", "the value is " + token.getFlag());  
                        } catch (InterruptedException e) {  
                            e.printStackTrace();  
                        }  
                }  
                Log.v("work","while!!");  
            }  
        }  
    } 
   // private String[] mStrings = {"AllysonPower", "Pull to refresh."};
        
}
