package com.allysonpower.parse;

import com.parse.ParseGeoPoint;

public class NewsInfo {
	// ндубid
	private String id;

	public String getId() {
		return id;
	}

	// ндубдзхщ
	private String eventText;

	public String getEventText() {
		return eventText;
	}

	public void setEventText(String text) {
		this.eventText = text;
	}
	
	private boolean bPositive;

	public boolean getPositive() {
		return bPositive;
	}

	public void setPositive(boolean positive) {
		this.bPositive = positive;
	}
	
	private ParseGeoPoint parseGeoLocation;

	public ParseGeoPoint getLocation() {
		return parseGeoLocation;
	}

	public void setLocation(ParseGeoPoint positive) {
		this.parseGeoLocation = positive;
	}
}
