package com.nyu.tweetmap;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.DescribeTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.amazonaws.services.dynamodbv2.model.TableDescription;
import com.amazonaws.services.dynamodbv2.util.Tables;

import twitter4j.Status;

@SuppressWarnings("deprecation")
public class DynamoHelper {

	private static AmazonDynamoDBClient mDynamoClient;
	private static DynamoDB mDynamoDB;
	final static String tableName = "tweetslocation";
	public static final String ITEM_TWEET_ID = "tweetID";
	public static final String ITEM_TWEET_LATITUDE = "lat";
	public static final String ITEM_TWEET_LONGITUDE = "lon";
	public static final String ITEM_TWEET_SENTIMENT = "sentiment";
	public static final String ITEM_TWEET_TIME = "tweettime";
	public static final String ITEM_TWEET_TEXT = "text";

	public static void initializeDynamo() {
		if (mDynamoClient == null) {

			mDynamoClient = new AmazonDynamoDBClient(AWSCredentialHelper.getCredentials());
			mDynamoClient.setRegion(Resources.getRegion());
			mDynamoDB = new DynamoDB(mDynamoClient);
			System.out.println("Dynamo Client Ready!");

		}

		initializeTable();
	}

	public static AmazonDynamoDBClient getDynamoClient() {
		return mDynamoClient;
	}

	public static DynamoDB getDynamoDB() {
		return mDynamoDB;
	}

	private static void initializeTable() {

		System.out.println("Creating Table");

		try {

			// Create table if it does not exist yet

			if (Tables.doesTableExist(mDynamoClient, tableName)) {
				System.out.println("Table " + tableName + " is already ACTIVE");
			} else {

				// Create a table with a primary hash key named 'name', which
				// holds a string

				CreateTableRequest createTableRequest = new CreateTableRequest().withTableName(tableName)
						.withKeySchema(
								new KeySchemaElement().withAttributeName(ITEM_TWEET_ID).withKeyType(KeyType.HASH))
						.withAttributeDefinitions(new AttributeDefinition().withAttributeName(ITEM_TWEET_ID)
								.withAttributeType(ScalarAttributeType.S))
						.withProvisionedThroughput(
								new ProvisionedThroughput().withReadCapacityUnits(1L).withWriteCapacityUnits(1L));

				TableDescription createdTableDescription = mDynamoClient.createTable(createTableRequest)
						.getTableDescription();
				System.out.println("Created Table: " + createdTableDescription);

				// Wait for it to become active

				System.out.println("Waiting for " + tableName + " to become ACTIVE...");
				Tables.awaitTableToBecomeActive(mDynamoClient, tableName);
			}

			// Describe our new table

			DescribeTableRequest describeTableRequest = new DescribeTableRequest().withTableName(tableName);
			TableDescription tableDescription = mDynamoClient.describeTable(describeTableRequest).getTable();
			System.out.println("Table Description: " + tableDescription);
		} catch (AmazonServiceException ase) {

			System.out.println("Caught an AmazonServiceException, which means your request made it "
					+ "to AWS, but was rejected with an error response for some reason.");
			System.out.println("Error Message:    " + ase.getMessage());
			System.out.println("HTTP Status Code: " + ase.getStatusCode());
			System.out.println("AWS Error Code:   " + ase.getErrorCode());
			System.out.println("Error Type:       " + ase.getErrorType());
			System.out.println("Request ID:       " + ase.getRequestId());
		} catch (AmazonClientException ace) {

			System.out.println("Caught an AmazonClientException, which means the client encountered "
					+ "a serious internal problem while trying to communicate with AWS, "
					+ "such as not being able to access the network.");
			System.out.println("Error Message: " + ace.getMessage());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void insertIntoTable(String message) throws IOException {

		System.out.println("Dynamo Message is : " + message);
		PutItemRequest putItemRequest;
		PutItemResult putItemResult;

		Map<String, AttributeValue> item = newItem(Tweets.deserialize(message, Tweets.class));
		putItemRequest = new PutItemRequest(tableName, item);
		putItemResult = mDynamoClient.putItem(putItemRequest);
		System.out.println("Result : " + putItemResult);

	}

	private static Map<String, AttributeValue> newItem(Tweets tweet) {
		Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
		item.put(ITEM_TWEET_ID, new AttributeValue(tweet.getTweetId()));
		item.put(ITEM_TWEET_TIME, new AttributeValue(Long.toString(System.currentTimeMillis())));
		item.put(ITEM_TWEET_LATITUDE, new AttributeValue(tweet.getLatitude()));
		item.put(ITEM_TWEET_LONGITUDE, new AttributeValue(tweet.getLongitude()));
		item.put(ITEM_TWEET_SENTIMENT, new AttributeValue(tweet.getSentiment()));
		item.put(ITEM_TWEET_TEXT, new AttributeValue(tweet.getText()));

		return item;
	}

}
