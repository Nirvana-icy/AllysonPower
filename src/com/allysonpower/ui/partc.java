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

public class partc extends PApplet{  
	private ParseQuery<AllysonNewsInfo> query;
	private int NUM_OF_NEWS_TO_SHOW = 100;
	private boolean positveArray[];
	private int newsRetrieved = 0;
	
	private int textAreaBaseY = height - width;
	private int circleDrawTimeInterval = 0;
	
	private int timeDelta = 0;
	private int positiveCount = 0;
	
	private int positiveStepCount = 0;
	private int negativeStepCount = 0;

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
						if(positveArray[newsList.size() - 1 - i]) 
							positiveCount++; //统计positive news的数目
					}
				}
				else {
					Log.e("Allysonpower.ui.partc.", "Get query result from parse error!");
				}
			}
		});	
		
		//设置processing绘制参数
		background(193,205,205);
		frameRate(1); //设置processing frameRate = 1,每过一秒调用一次draw函数..timeDelta增加一
		textSize(32);
		noStroke();
    }   
		
    public void draw(){ 
    	if(newsRetrieved > 0){  //if newsRetrieved > 0 => we get the data from parse
    		timeDelta++;
    		int i = timeDelta % newsRetrieved; //i = 时间 mod newsRetrieved (让i在0到newsRetrieved之间循环的变化) 
    		//Calculate the drawing parameters
    		int stepR = (int)((float)(width*0.5)/(float)newsRetrieved);  //根据positve news和negativenews比例和屏幕宽度 计算 每隔一秒 绘制圆球的半径应该增长多少
    		int centerPositiveCircleX = stepR*positiveCount;
    		int cneterNegativeCircleX = width - stepR*(newsRetrieved - positiveCount);
    		int cneterCircleY = (int)(width*0.5);
    		
			Log.e("AllysonPower.partc", "stepR:" + stepR);
			Log.d("AllysonPower.partc", "positiveCount:" + positiveCount);
			//Draw out the positive/negative news trend step by step  (positive/negative news数量随时间的增长趋势)
			if (positveArray[i]) {
				positiveStepCount++;
				fill(255,66,66);  //绘制positive circle 的颜色
				ellipse(centerPositiveCircleX, cneterCircleY, positiveStepCount*stepR, positiveStepCount*stepR);
				if(positiveStepCount == positiveCount)	//本轮positive news增长变化趋势 绘制完毕 需要清零数据和画布 重新绘制
				{
					fill(193,205,205);
					rect(0,0,width,height); //清屏
					positiveStepCount = 0; //清零 重新绘制
				}
			} else {
				negativeStepCount++;
				fill(10,10,236);  //绘制negative circle 的颜色
				ellipse(cneterNegativeCircleX, cneterCircleY,negativeStepCount * stepR,negativeStepCount * stepR);
				if(negativeStepCount == (newsRetrieved - positiveCount))
				{
					negativeStepCount = 0; //清零 重新绘制
				}
			}

		}
    }
}
