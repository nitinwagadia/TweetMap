package com.nyu.tweetmap;

import java.io.IOException;
import java.io.Serializable;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.alchemyapi.api.AlchemyAPI;

public class ThreadPool implements Runnable, Serializable {

	private static final long serialVersionUID = 1L;
	private Object mTask;
	private Document doc;
	private static AlchemyAPI alchemyAPI = AlchemyAPI.GetInstanceFromString("Your Alchemy Key");

	public ThreadPool(Object task) {
		this.mTask = task;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {

		System.out.println("In threadPool -->" + mTask);

		while (true) {
			String msg = SimpleQueueServiceHelper.getMessage();
			try {
				if (msg != null) {
					Tweets tweet = Tweets.deserialize(msg, Tweets.class);
					doc = alchemyAPI.TextGetTextSentiment(tweet.getText());
					tweet.setSentiment(getStringFromDocument(doc));
					System.out.println("Sentiment  : " + getStringFromDocument(doc));
					System.out.println("Publishing Message");
					SNSHelper.publishMsg(Tweets.serialize(tweet));
				} else {
					System.out.println("Sleeping" + mTask.toString());
					Thread.sleep(2000);

				}
			} catch (IOException | XPathExpressionException | SAXException | ParserConfigurationException
					| InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private static String getStringFromDocument(Document doc) {
		return doc.getElementsByTagName("type").item(0).getTextContent();
		/*
		 * + " " + doc.getElementsByTagName("score").item(0).getTextContent();
		 */
	}

}
