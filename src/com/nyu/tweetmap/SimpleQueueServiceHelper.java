package com.nyu.tweetmap;

import java.io.IOException;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class SimpleQueueServiceHelper {

	private static String mQueueUrl;
	private static AmazonSQSClient mSQSClient;
	private static ThreadPoolExecutor mThreadPool;

	public static AmazonSQSClient getQueue() {
		return mSQSClient;

	}

	public static String getMessage() {
		String msg = null;
		try {
			// Receive messages
			// System.out.println("Receiving messages from MyQueue.\n");
			ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(mQueueUrl)
					.withMaxNumberOfMessages(1);
			List<Message> messages = mSQSClient.receiveMessage(receiveMessageRequest).getMessages();
			for (Message message : messages) {
				System.out.println("  Message");
				System.out.println("    MessageId:     " + message.getMessageId());
				System.out.println("    ReceiptHandle: " + message.getReceiptHandle());
				System.out.println("    MD5OfBody:     " + message.getMD5OfBody());
				System.out.println("    Body:          " + message.getBody());
				for (Entry<String, String> entry : message.getAttributes().entrySet()) {
					System.out.println("  Attribute");
					System.out.println("    Name:  " + entry.getKey());
					System.out.println("    Value: " + entry.getValue());
				}
			}
			if (!messages.isEmpty()) {
				System.out.println("Size of Queue is : " + messages.size());
				msg = messages.get(0).getBody().toString();
				System.out.println("Message is " + msg);
				System.out.println("Deleting a message.\n");
				String messageRecieptHandle = messages.get(0).getReceiptHandle();
				mSQSClient.deleteMessage(new DeleteMessageRequest(mQueueUrl, messageRecieptHandle));
				System.out.println(msg);

			}

			// Delete a message

		} catch (AmazonServiceException ase) {
			System.out.println("Caught an AmazonServiceException, which means your request made it "
					+ "to Amazon SQS, but was rejected with an error response for some reason.");
			System.out.println("Error Message:    " + ase.getMessage());
			System.out.println("HTTP Status Code: " + ase.getStatusCode());
			System.out.println("AWS Error Code:   " + ase.getErrorCode());
			System.out.println("Error Type:       " + ase.getErrorType());
			System.out.println("Request ID:       " + ase.getRequestId());
		} catch (AmazonClientException ace) {
			System.out.println("Caught an AmazonClientException, which means the client encountered "
					+ "a serious internal problem while trying to communicate with SQS, such as not "
					+ "being able to access the network.");
			System.out.println("Error Message: " + ace.getMessage());
		}

		return msg;

	}

	public static void SendMsg(Tweets tweet) throws JsonGenerationException, JsonMappingException, IOException {
		// Send a message
		//System.out.println("Sending a message to MyQueue:" + String.format("%1$020d", Id) + msg);
		try {
			//mSQSClient.sendMessage(new SendMessageRequest(mQueueUrl, String.format("%1$020d", Id) + msg));
			mSQSClient.sendMessage(new SendMessageRequest(mQueueUrl, Tweets.serialize(tweet)));
		} catch (AmazonServiceException ase) {
			System.out.println("Caught an AmazonServiceException, which means your request made it "
					+ "to Amazon SQS, but was rejected with an error response for some reason.");
			System.out.println("Error Message:    " + ase.getMessage());
			System.out.println("HTTP Status Code: " + ase.getStatusCode());
			System.out.println("AWS Error Code:   " + ase.getErrorCode());
			System.out.println("Error Type:       " + ase.getErrorType());
			System.out.println("Request ID:       " + ase.getRequestId());
		} catch (AmazonClientException ace) {
			System.out.println("Caught an AmazonClientException, which means the client encountered "
					+ "a serious internal problem while trying to communicate with SQS, such as not "
					+ "being able to access the network.");
			System.out.println("Error Message: " + ace.getMessage());
		}
	}

	public static void initializeSQS() {

		if (mSQSClient == null) {
			try {
				// Create a queue
				mSQSClient = new AmazonSQSClient(AWSCredentialHelper.getCredentials());
				mSQSClient.setRegion(Resources.getRegion());
				System.out.println("Creating a new SQS queue called MyQueue.\n");
				CreateQueueRequest createQueueRequest = new CreateQueueRequest("MyQueue");
				mQueueUrl = mSQSClient.createQueue(createQueueRequest).getQueueUrl();

			} catch (AmazonServiceException ase) {
				System.out.println("Caught an AmazonServiceException, which means your request made it "
						+ "to Amazon SQS, but was rejected with an error response for some reason.");
				System.out.println("Error Message:    " + ase.getMessage());
				System.out.println("HTTP Status Code: " + ase.getStatusCode());
				System.out.println("AWS Error Code:   " + ase.getErrorCode());
				System.out.println("Error Type:       " + ase.getErrorType());
				System.out.println("Request ID:       " + ase.getRequestId());
			}

			System.out.println("Queue Created Success!");

			mThreadPool = new ThreadPoolExecutor(2, 4, 3, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(3),
					new ThreadPoolExecutor.DiscardOldestPolicy());

			for (int i = 0; i < 4; i++) {
				String task = "@task " + i;
				System.out.println("Task is " + task);
				mThreadPool.execute(new ThreadPool(task));
			}

		}

	}

}
