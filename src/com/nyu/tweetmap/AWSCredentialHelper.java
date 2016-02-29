package com.nyu.tweetmap;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;

public class AWSCredentialHelper {

	private static AWSCredentials mCredentials;

	public static void initializeCredentials() {

		if (mCredentials == null) {
			try {

				mCredentials = new BasicAWSCredentials("your access key","your secret key");

			} catch (Exception e) {
				throw new AmazonClientException("Cannot load the credentials from the credential profiles file. "
						+ "Please make sure that your credentials file is at the correct ", e);
			}

			System.out.println("Credentials Success!");

		}
	}

	public static AWSCredentials getCredentials() {
		return mCredentials;
	}

}
