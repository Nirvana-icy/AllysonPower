package com.allysonpower;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class FourIcon_page extends Activity {
	
	private ImageButton button_parta,button_partb,button_partc,button_partd,button_help;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub		
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ini_page);
		
		//Get all the button of the initial page
		button_parta = (ImageButton) findViewById(R.id.button_parta);
		button_partb = (ImageButton) findViewById(R.id.button_partb);
		button_partc = (ImageButton) findViewById(R.id.button_partc);
		button_partd = (ImageButton) findViewById(R.id.button_partd);
		button_help = (ImageButton) findViewById(R.id.button_help);
		
		//Create all the listener events to control actions jump to next activity
		//Jump to part a
		button_parta.setOnClickListener(
		    		new View.OnClickListener() 
		    		{@Override
				     public void onClick(View view) {
					// TODO Auto-generated method stub
					Intent intent= new Intent(view.getContext(),parta.class);
				    startActivityForResult(intent,0);
				    }});
		//Jump to part b
		button_partb.setOnClickListener(
	    		new View.OnClickListener() 
	    		{@Override
			     public void onClick(View view) {
				// TODO Auto-generated method stub
				Intent intent= new Intent(view.getContext(),partb.class);
			    startActivityForResult(intent,0);
			    }});
		//Jump to part c
		button_partc.setOnClickListener(
	    		new View.OnClickListener() 
	    		{@Override
			     public void onClick(View view) {
				// TODO Auto-generated method stub
				Intent intent= new Intent(view.getContext(),partc.class);
			    startActivityForResult(intent,0);
			    }});
		//Jump to part d
		button_partd.setOnClickListener(
	    		new View.OnClickListener() 
	    		{@Override
			     public void onClick(View view) {
				// TODO Auto-generated method stub
				Intent intent= new Intent(view.getContext(),partd.class);
			    startActivityForResult(intent,0);
			    }});
		//Open help alert dialog
		button_help.setOnClickListener(
	    		new View.OnClickListener() 
	    		{@Override
			     public void onClick(View view) {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(FourIcon_page.this)
				.setTitle("对话框标题")
				.setMessage("对话框内容")
				.setPositiveButton("确定",null).show();
			    }});	
	}
}

