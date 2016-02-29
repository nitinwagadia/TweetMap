package com.nyu.tweetmap;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.ConfirmSubscriptionRequest;
import com.amazonaws.services.sns.model.ConfirmSubscriptionResult;
import com.amazonaws.services.sns.model.CreateTopicRequest;
import com.amazonaws.services.sns.model.CreateTopicResult;
import com.amazonaws.services.sns.model.DeleteTopicRequest;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.amazonaws.services.sns.model.SubscribeRequest;

public class SNSHelper {
	
	private static AmazonSNSClient mSNSClient;
	private static String topicArn;
	private static String endpoint="http://mytweetmap.elasticbeanstalk.com/server";
	
	public static AmazonSNSClient getSNSClient()
	{
		return mSNSClient;
	}

	public static String getTopicArn()
	{
		return topicArn;
	}
	
	public static void subscribe(){
		
		SubscribeRequest subRequest = new SubscribeRequest(topicArn, "http", endpoint);
		mSNSClient.subscribe(subRequest);
		//get request id for SubscribeRequest from SNS metadata
		System.out.println("SubscribeRequest - " + mSNSClient.getCachedResponseMetadata(subRequest));
	}
	
	public static void publishMsg(String msg){
		PublishRequest publishRequest = new PublishRequest(topicArn, msg);
		PublishResult publishResult = mSNSClient.publish(publishRequest);
		//print MessageId of message published to SNS topic
		System.out.println("MessageId - " + publishResult.getMessageId());
	}
	
	public static void deleteTopic(){
		DeleteTopicRequest deleteTopicRequest = new DeleteTopicRequest(topicArn);
		mSNSClient.deleteTopic(deleteTopicRequest);
		//get request id for DeleteTopicRequest from SNS metadata
		System.out.println("DeleteTopicRequest - " + mSNSClient.getCachedResponseMetadata(deleteTopicRequest));
	}
	
	public static void confirmTopicSubmission(Message message) {
		ConfirmSubscriptionRequest confirmSubscriptionRequest = new ConfirmSubscriptionRequest()
		 							.withTopicArn(message.getTopicArn())
									.withToken(message.getToken());
		ConfirmSubscriptionResult resutlt = mSNSClient.confirmSubscription(confirmSubscriptionRequest);
		System.out.println("subscribed to " + resutlt.getSubscriptionArn());
		
	}


	public static void initializeSNS() {
		
		
		if(mSNSClient==null)
		{
			mSNSClient = new AmazonSNSClient(AWSCredentialHelper.getCredentials());
			mSNSClient.setRegion(Resources.getRegion());
			
			//create a new SNS topic
			CreateTopicRequest createTopicRequest = new CreateTopicRequest("SentimentEval");
			CreateTopicResult createTopicResult = mSNSClient.createTopic(createTopicRequest);
			
			//save TopicArn
			System.out.println(createTopicResult);
			topicArn = createTopicResult.getTopicArn();
			
			//get request id for CreateTopicRequest from SNS metadata		
			System.out.println("CreateTopicRequest - " + mSNSClient.getCachedResponseMetadata(createTopicRequest));
			System.out.println("SNS CreatedSuccess !");
			
			System.out.println("Subscribing Topic");
			subscribe();
			
			
			
		}
		
	}



	
	
	
}
