package com.allysonpower.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.util.Log;

import com.allysonpower.parse.AllysonNewsInfo;
import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import processing.core.*;  

/*
public class partc extends PApplet{  
	private ParseQuery<AllysonNewsInfo> queryA;
	private ParseQuery<AllysonNewsInfo> queryB;
	private float positiveCount = 0.0f;
	private float negativeCount = 0.0f;
	private int positivePercent = 0;
    public void setup(){  
    	//partc 启动后先从parse 获取数据
    		
        //regist the subclass of ParseObject first and then call Parse.initialize
        ParseObject.registerSubclass(AllysonNewsInfo.class);
        Parse.initialize(this, "bDExeWi2vct7yqm52r5WPnEiuNyorLu9B2tSFREW", "MvzGGKqOt5Q56inQCPNpbYLAaiJmtykCjgh93C1K");
        //Get query 
		queryA = ParseQuery.getQuery(AllysonNewsInfo.class);
		//Query the positive count.
		queryA.whereEqualTo("Positive", true);
		queryA.countInBackground(new CountCallback() {
		  public void done(int count, ParseException e) {
		    if (e == null) {
		    	positiveCount = count;
		      // The count request succeeded. Log the count
		      Log.d("Positive", "News Count:" + count + ".");
		    } else {
		      // The request failed
		    }
		  }
		});
		//Query the negative count
		queryB = ParseQuery.getQuery(AllysonNewsInfo.class);
		queryB.whereEqualTo("Positive", false);
		queryB.countInBackground(new CountCallback() {
		  public void done(int count, ParseException e) {
		    if (e == null) {
		    	negativeCount = count;
		      // The count request succeeded. Log the count
		      Log.d("Negative", "News Count:" + count + ".");
		    } else {
		      // The request failed
		    }
		  }
		});	
		
		//设置processing绘制参数
		background(0);
    }   
		
    public void draw(){ 
    	if(0 != positiveCount && 0 != negativeCount)
    	{
    		//红色矩形绘制出positive news的占比
    		fill(255,0,0);
    		rect((float)0, (float)(height*0.5*0.5), (float)(positiveCount/(positiveCount+negativeCount))*width,(float)(height*0.2));
    		//黑色矩形绘制出negative news的占比
    		fill(255);
    		rect((float)(positiveCount/(positiveCount+negativeCount))*width, (float)(height*0.5*0.5), width,(float)(height*0.2));
    		//文本输出占比信息
    		textSize(32); 
    		fill(0, 102, 153);
    		positivePercent = (int)(positiveCount/(positiveCount+negativeCount)*100);
    		text("Positive news:" + positivePercent +"%.", (float)(0.2*width), (float)(height*0.75));
    	}
    }  
}
*/

public class partc extends PApplet{  
	private ParseQuery<AllysonNewsInfo> query;
	private int NUM_OF_NEWS_TO_SHOW = 100;
	private boolean positveArray[];
	private int newsRetrieved = 0;
	private int r = (int)((width/NUM_OF_NEWS_TO_SHOW)*0.5);
	
	private int textAreaBaseY = height - width;
	private int circleDrawTimeInterval = 0;

    public void setup(){  
    	positveArray = new boolean[NUM_OF_NEWS_TO_SHOW];	
    	newsRetrieved = 0;
    	//partc 启动后先从parse 获取数据
        //regist the subclass of ParseObject first and then call Parse.initialize
        ParseObject.registerSubclass(AllysonNewsInfo.class);
        Parse.initialize(this, "bDExeWi2vct7yqm52r5WPnEiuNyorLu9B2tSFREW", "MvzGGKqOt5Q56inQCPNpbYLAaiJmtykCjgh93C1K");
        //Get query 
		query = ParseQuery.getQuery(AllysonNewsInfo.class);
		//按照降序顺序 抓取100条news的信息 
		query.orderByDescending("createAt");
		query.setLimit(NUM_OF_NEWS_TO_SHOW); 
		//Query the data from parse
		query.findInBackground(new FindCallback<AllysonNewsInfo>() {
			@SuppressWarnings("deprecation")
			@Override
			public void done(List<AllysonNewsInfo> newsList, ParseException e) {
				// TODO Auto-generated method stub
				if (e == null) {
					Log.i("Allysonpower.ui.partc", "partc retrieved " + newsList.size() + "positive/negative statues.");
					newsRetrieved = newsList.size();
					for(int i = newsList.size() - 1; i >= 0; i--)  //因为是按照时间降序查询到的newslist 我们这里反向从数组最后一位开始 获取positive状态
					{
						positveArray[newsList.size() - 1 - i] = newsList.get(i).getPositive(); 
					}
				}
				else {
					Log.e("Allysonpower.ui.partc.", "Get query result from parse error!");
				}
			}
		});	
		
		//设置processing绘制参数
		background(51);
		frameRate(1);
    }   
		
    public void draw(){ 
    	if(newsRetrieved > 0){  //if newsRetrieved > 0 => we get the data from parse
    		int positiveCount = 0;
    		for(int i = 0; i < newsRetrieved; i++)
    		{
    			if(positveArray[i])
    			{
    				positiveCount++;
    				fill(255,0,0);
    				ellipse(width/2,height/2, positiveCount*10,positiveCount*10);
    				textSize(32);
    				text("Get the data from parse.", 10, 30);
    			}
    		}
    	}
    }
}
