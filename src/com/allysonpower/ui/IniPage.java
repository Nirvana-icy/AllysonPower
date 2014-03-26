package com.allysonpower.ui;

import java.util.Timer;
import java.util.TimerTask;

import com.allysonpower.ui.R;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;



public class IniPage extends Activity {
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wel_page);	
		Timer timer = new Timer();
        TimerTask task = new TimerTask() {
	         @Override
	         public void run() {
	        	 Intent intent = new Intent(IniPage.this, FourIcon_page.class); 
	        	 startActivity(intent);
	        	 IniPage.this.finish();
	         }
        };
        timer.schedule(task, 1000);
	}
}

