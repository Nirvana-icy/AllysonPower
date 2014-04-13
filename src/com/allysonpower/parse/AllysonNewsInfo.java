package com.allysonpower.parse;

import java.util.Date;

import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.anywall.AnywallPost;

@ParseClassName("News")
public class AllysonNewsInfo extends ParseObject {
	//convenient getter/setter method to get/setter the data which is store in parse-News table
	
	public String getEventText() {
		return getString("Event");
	}
	
	public void setEventText(String eventText) {
		put("Event", eventText);
	}
	
	public boolean getPositive() {
		return getBoolean("Positive");
	}
	
	public void setPositive(boolean boolPositive) {
		put("Positive", boolPositive);
	}
	
	public Date getPostTime() {
		return getUpdatedAt();
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
		return getParseGeoPoint("location");
	}
	
	public void setLocation(double latitude,double longitude) {
		ParseGeoPoint location = new ParseGeoPoint(latitude, longitude);
		put("Location", location);
	}
	public static ParseQuery<AllysonNewsInfo> getQuery() {
		return ParseQuery.getQuery(AllysonNewsInfo.class);
	}
}
