package com.nyu.tweetmap;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;

import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

public class Resources implements Runnable {

	private static ConfigurationBuilder mConfigurationBuilder;
	private final static Region mRegion = Region.getRegion(Regions.US_WEST_2);
	final static String tableName = "tweetslocation";
	private TwitterStream twitterStream;

	public Resources() {

		System.out.println("Calling Credentials");
		AWSCredentialHelper.initializeCredentials();

		System.out.println("Calling Dynamo");
		DynamoHelper.initializeDynamo();

		System.out.println("Calling SNS");
		SNSHelper.initializeSNS();
		
		System.out.println("Calling SQS");
		SimpleQueueServiceHelper.initializeSQS();

		System.out.println("Calling Twitter");
		initializeTwitter();

	}


	public static Region getRegion()
	{
		return mRegion;
	}


	private void initializeTwitter() {

		if (mConfigurationBuilder == null) {
			mConfigurationBuilder = new ConfigurationBuilder();
			mConfigurationBuilder.setDebugEnabled(true).setOAuthConsumerKey("Your Consumer Key")
					.setOAuthConsumerSecret("Your Consumer Secret Key")
					.setOAuthAccessToken("Your Access Token")
					.setOAuthAccessTokenSecret("Your Access Token Secret");
			
			System.out.print("Twitter Success");
		}
	}

	public static ConfigurationBuilder getConfigurationBuilder() {
		return mConfigurationBuilder;

	}


	@Override
	public void run() {
		
		twitterStream = new TwitterStreamFactory(Resources.getConfigurationBuilder().build()).getInstance();
			
		StatusListener statusListener = new StatusListener() {
			
			@Override
			public void onException(Exception arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onTrackLimitationNotice(int arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStatus(Status status) {
				
				try {
					
					if(status.getGeoLocation()!=null && status.getLang().equalsIgnoreCase("en"))
					{
						System.out.println("Tweet is : "+status.getText());
						SimpleQueueServiceHelper.SendMsg(new Tweets(status));
						System.out.println("Message sent to Queue");
						
						
					}
				}
						
				 catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
			@Override
			public void onStallWarning(StallWarning arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onScrubGeo(long arg0, long arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onDeletionNotice(StatusDeletionNotice arg0) {
				// TODO Auto-generated method stub
				
			}
		};
		
		twitterStream.addListener(statusListener);
		twitterStream.sample();
	}

}
