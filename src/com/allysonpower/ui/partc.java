package com.allysonpower.ui;

import java.util.List;

import android.util.Log;

import com.allysonpower.parse.AllysonNewsInfo;
import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import processing.core.*;  
public class partc extends PApplet{  
	private ParseQuery<AllysonNewsInfo> queryA;
	private ParseQuery<AllysonNewsInfo> queryB;
	private float positiveCount = 0.0f;
	private float negativeCount = 0.0f;
	private float positivePercent = 0.0f;
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
    		positivePercent = positiveCount/(positiveCount+negativeCount)*100;
    		text("Positive news:" + positivePercent +"%.", (float)(0.2*width), (float)(height*0.75));
    	}
    }  
}
