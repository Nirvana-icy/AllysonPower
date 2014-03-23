package com.allysonpower;

import com.parse.Parse;
import com.parse.ParseObject;

import android.app.Activity;
import android.os.Bundle;

public class parta extends Activity{
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		Parse.initialize(this, "bDExeWi2vct7yqm52r5WPnEiuNyorLu9B2tSFREW", "MvzGGKqOt5Q56inQCPNpbYLAaiJmtykCjgh93C1K");
		ParseObject testObject = new ParseObject("TestObject");
		testObject.put("foo", "bar");
		testObject.saveInBackground();
	}
}

