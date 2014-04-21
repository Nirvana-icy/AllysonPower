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
    	//partc �������ȴ�parse ��ȡ����
        //regist the subclass of ParseObject first and then call Parse.initialize
        ParseObject.registerSubclass(AllysonNewsInfo.class);
        Parse.initialize(this, "bDExeWi2vct7yqm52r5WPnEiuNyorLu9B2tSFREW", "MvzGGKqOt5Q56inQCPNpbYLAaiJmtykCjgh93C1K");
        //Get query 
		query = ParseQuery.getQuery(AllysonNewsInfo.class);
		//���ս���˳�� ץȡ100��news����Ϣ 
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
					for(int i = newsList.size() - 1; i >= 0; i--)  //��Ϊ�ǰ���ʱ�併���ѯ����newslist �������ﷴ����������һλ��ʼ ��ȡpositive״̬
					{
						positveArray[newsList.size() - 1 - i] = newsList.get(i).getPositive(); 
						if(positveArray[newsList.size() - 1 - i]) 
							positiveCount++; //ͳ��positive news����Ŀ
					}
				}
				else {
					Log.e("Allysonpower.ui.partc.", "Get query result from parse error!");
				}
			}
		});	
		
		//����processing���Ʋ���
		background(193,205,205);
		frameRate(1); //����processing frameRate = 1,ÿ��һ�����һ��draw����..timeDelta����һ
		textSize(32);
		textAlign(CENTER);
		noStroke();
    }   
		
    public void draw(){ 
    	if(newsRetrieved > 0){  //if newsRetrieved > 0 => we get the data from parse
    		timeDelta++;
    		int i = timeDelta % newsRetrieved; //i = ʱ�� mod newsRetrieved (��i��0��newsRetrieved֮��ѭ���ı仯) 
    		//Calculate the drawing parameters
    		int lengthOfWide = width < height ? width : height; //������ʱ�� processing width()�������ص��� ��Ļ�ĳ�...�����������width > height ��ʾ�豸���ں���״̬...�������ԲȦ��r Ӧ��ȡheight�������ص���ֵ
    		int lengthOfHeight = width < height ? height : width;
    		int stepR = (int)((float)(lengthOfWide*0.5)/(float)newsRetrieved);  //����positve news��negativenews��������Ļ��� ���� ÿ��һ�� ����Բ��İ뾶Ӧ����������
    		int centerPositiveCircleX = stepR*positiveCount;
    		int centerNegativeCircleX = lengthOfWide - stepR*(newsRetrieved - positiveCount);
    		int centerCircleY = (int)(lengthOfWide*0.5);
    		
			Log.e("AllysonPower.partc", "stepR:" + stepR);
			Log.d("AllysonPower.partc", "positiveCount:" + positiveCount);
			//���� �������Ӱ 
			fill(193,205,205);
			rect(0,0,width,height);
			//Draw out the positive/negative news trend step by step  (positive/negative news������ʱ�����������)
			if (positveArray[i]) {
				positiveStepCount++;
				fill(255,66,66);  //����positive circle ����ɫ
				ellipse(centerPositiveCircleX, centerCircleY, positiveStepCount*stepR, positiveStepCount*stepR);
				text("Positive news:" + positiveStepCount, centerPositiveCircleX, 40);  //��positive circleͬ����ɫ �����������
				fill(10,10,236);  //����ÿ��ˢ�²������ϴλ��Ƶ����� �����������»���һ��negative new��ԲȦ ������� �� �ٷֱ�
				ellipse(centerNegativeCircleX, centerCircleY,negativeStepCount * stepR,negativeStepCount * stepR);
				text("Negative news:" + negativeStepCount, centerNegativeCircleX, 40);
				fill(255,255,255);  //��ɫ���� �ٷֱ�
				if(positiveStepCount != positiveStepCount + negativeStepCount || negativeStepCount != positiveStepCount + negativeStepCount) //���һ����100% ��һ����0%  ������� ���ǵ�����  �����Ƴ��ٷֱ�
				{
					text((int)(100*(float)positiveStepCount/(float)(positiveStepCount+negativeStepCount)) + "%", centerPositiveCircleX, centerCircleY + 10); //positive �ٷֱ�
					text((int)(100*(1-(float)positiveStepCount/(float)(positiveStepCount+negativeStepCount))) + "%", centerNegativeCircleX, centerCircleY + 10); //negative �ٷֱ�
				}
				if(positiveStepCount == positiveCount)	//����positive news�����仯���� ������� ��Ҫ��������
				{
					positiveStepCount = 0; //���� ���»���
					negativeStepCount = 0; //���� ���»���
				}
			} else {
				negativeStepCount++;
				fill(10,10,236);  //����negative circle ����ɫ
				ellipse(centerNegativeCircleX, centerCircleY,negativeStepCount * stepR,negativeStepCount * stepR);
				text("Negative news:" + negativeStepCount, centerNegativeCircleX, 40);
				fill(255,66,66);   //����ÿ��ˢ�²������ϴλ��Ƶ����� �����������»���һ��positive new�������
				text("Positive news:" + positiveStepCount, centerPositiveCircleX, 40);  //��positive circleͬ����ɫ �����������
				ellipse(centerPositiveCircleX, centerCircleY, positiveStepCount*stepR, positiveStepCount*stepR);
				fill(255,255,255);  //��ɫ���� �ٷֱ�
				if(positiveStepCount != positiveStepCount + negativeStepCount || negativeStepCount != positiveStepCount + negativeStepCount) //���һ����100% ��һ����0%  ������� ���ǵ�����  �����Ƴ��ٷֱ�
				{
					text((int)(100*(float)positiveStepCount/(float)(positiveStepCount+negativeStepCount)) + "%", centerPositiveCircleX, centerCircleY + 10); //positive �ٷֱ�
					text((int)(100*(1-(float)positiveStepCount/(float)(positiveStepCount+negativeStepCount))) + "%", centerNegativeCircleX, centerCircleY + 10); //negative �ٷֱ�
				}
			}
		}
    }
}
