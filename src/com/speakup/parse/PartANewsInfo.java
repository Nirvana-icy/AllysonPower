package com.speakup.parse;

import java.util.Date;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.TextView;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

@ParseClassName("News")
public class PartANewsInfo extends ParseObject {
	//convenient getter/setter method to get/setter the data which is store in parse-News table

	public String getEventText() {
		return getString("Event");
	}
	
	public void setEventText(String eventText) {
		put("Event", eventText);
	}
//~~~~~~~~~New Added~~~~~~~~~~~
	public String getSubject() {
		return getString("Subject");
	}
	
	public void setSubject(String subjectStr) {
		put("Subject", subjectStr);
	}
	
	public String getPosterName() {
		return getString("PosterName");
	}
	
	public void setPosterName(String posterName) {
		put("PosterName", posterName);
	}

	public String getHappenTime() {
		return getString("HappenTime");
	}
	
	public void setHappenTime(String happenTime) {
		put("HappenTime", happenTime);
	}
	
	public String getHappenArea() {
		return getString("HappenArea");
	}
	
	public void setHappenArea(String happenArea) {
		put("HappenArea", happenArea);
	}
	
	public boolean getHaveImage() {
		return getBoolean("bPhotoExist");
	}
	
	public void setHaveImage(boolean bPhotoExist) {
		put("bPhotoExist", bPhotoExist);
	}
	
	public Bitmap getNewsImage() {
		ParseFile photo = getParseFile("photoImage");
        byte[] phtoData = null;
		try {
			phtoData = photo.getData();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return BitmapFactory.decodeByteArray(phtoData,0,phtoData.length);
	}
	
	public void setNewsImage(ParseFile photoImage) {
		put("photoImage", photoImage);
	}
    
	public String getNewsImageURL() {
		ParseFile photo = getParseFile("photoImage");
		Log.d("$$$$$$$$$$$$$$$$" , photo.getUrl());
		return photo.getUrl();
	}
	
//~~~~~~~~~~~~End of New Added~~~~~~~~~~~~~~
	public boolean getPositive() {
		return getBoolean("Positive");
	}
	
	public void setPositive(boolean boolPositive) {
		put("Positive", boolPositive);
	}
	
	public Date getPostTime() {
		return getCreatedAt();
	}
	
	public boolean getDGB_Boolean() {
		return getBoolean("Post_To_DGB");
	}
	
	public void setDGB_Boolean(boolean boolPositive) {
		put("Post_To_DGB", boolPositive);
	}
	
	public boolean getTYB_Boolean() {
		return getBoolean("Post_To_TYB");
	}
	
	public void setTYB_Boolean(boolean boolPositive) {
		put("Post_To_TYB", boolPositive);
	}
	
	public boolean getNHRB_Boolean() {
		return getBoolean("Post_To_NHRB");
	}
	
	public void setNHRB_Boolean(boolean boolPositive) {
		put("Post_To_NHRB", boolPositive);
	}
	
	public boolean getXDRB_Boolean() {
		return getBoolean("Post_To_XDRB");
	}
	
	public void setXDRB_Boolean(boolean boolPositive) {
		put("Post_To_XDRB", boolPositive);
	}
	
	public ParseGeoPoint getLocation() {
		return getParseGeoPoint("Location");
	}
	
	public void setLocation(double latitude,double longitude) {
		ParseGeoPoint location = new ParseGeoPoint(latitude, longitude);
		put("Location", location);
	}
	public static ParseQuery<PartANewsInfo> getQuery() {
		return ParseQuery.getQuery(PartANewsInfo.class);
	}




}
