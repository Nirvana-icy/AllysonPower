package com.allysonpower.parse;

import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

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
	
	public ParseGeoPoint getLocation() {
		return getParseGeoPoint("Location");
	}
	
	public void setLocation(double latitude,double longitude) {
		ParseGeoPoint location = new ParseGeoPoint(latitude, longitude);
		put("Location", location);
	}
	
//	//convenient getQuery method
//	public static ParseQuery<AllysonNewsInfo> getQuery() {
//		return ParseQuery.getQuery(AllysonNewsInfo.class);
//	}
}
