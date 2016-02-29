package com.nyu.tweetmap;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import twitter4j.Status;

public class Tweets implements Serializable
{

	private String latitude;
	private String longitude;
	private String text;
	private String time;
	private String tweetId;
	private String sentiment;


	public Tweets()
	{
		
	}
	public Tweets(Status status) {

		latitude = Double.toString(status.getGeoLocation().getLongitude());
		longitude = Double.toString(status.getGeoLocation().getLatitude());
		text = status.getText();
		time = Long.toString(System.currentTimeMillis());
		tweetId = Long.toString(status.getId());

	}

	
	public static <T> T  deserialize(String content, Class<T> valueType) throws IOException {
	    return new ObjectMapper().readValue(content, valueType);
	}
	
	public static String serialize(Object object) throws IOException {

	    ObjectMapper objectMapper = new ObjectMapper();
	    StringWriter stringEmp = new StringWriter();
	    objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
	    objectMapper.writeValue(stringEmp, object);
	    return stringEmp.toString();
	}
	
	public void setSentiment(String sentimentString) {
		this.sentiment=sentimentString;
	}
	
	public String getLatitude() {
		return latitude;
	}


	public String getLongitude() {
		return longitude;
	}


	public String getText() {
		return text;
	}


	public String getTime() {
		return time;
	}


	public String getTweetId() {
		return tweetId;
	}


	public String getSentiment() {
		return sentiment;
	}


	
	
	
	
}
