package com.speakup.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;


public class MyImageBtn extends ImageButton {

	public MyImageBtn(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public String imageUrl;
	
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	public String getImageUrl() {
		return this.imageUrl;
	}
}