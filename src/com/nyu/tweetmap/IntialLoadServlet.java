package com.nyu.tweetmap;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;

@WebServlet("/InitialLoadServlet")
public class IntialLoadServlet extends HttpServlet {

	public IntialLoadServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("Inside initialLoad Servlet doGet()");

		if (DynamoHelper.getDynamoClient() != null) {

			PrintWriter writer = response.getWriter();
			Map<String, AttributeValue> expressionAttributeValues = new HashMap<String, AttributeValue>();
			ScanRequest scanRequest = new ScanRequest().withTableName(DynamoHelper.tableName);// .withExpressionAttributeValues(expressionAttributeValues)

			ScanResult result = DynamoHelper.getDynamoClient().scan(scanRequest);
			StringBuffer sb = new StringBuffer();
			for (Map<String, AttributeValue> item : result.getItems()) {
				String lat = item.get("lat").getS();
				String lng = item.get("lon").getS();
				sb.append(lat + "," + lng + ";");
			}
			System.out.println("LatLong: " + sb + " Time: " + System.currentTimeMillis());
			if (sb.length() != 0) {
				writer.write(sb.toString());
			}

		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
