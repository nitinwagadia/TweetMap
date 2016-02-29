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
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;

/**
 * Servlet implementation class AjaxServlet
 */
@WebServlet("/AjaxServlet")
public class AjaxServlet extends HttpServlet {

	private static int i = 0;
	private static final long serialVersionUID = 1L;
	

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AjaxServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("Inside Ajax Servlet doGet()");

		if (DynamoHelper.getDynamoClient() != null) {
			String time = String.valueOf(System.currentTimeMillis() - 5000);
			Map<String, AttributeValue> expressionAttributeValues = new HashMap<String, AttributeValue>();
			expressionAttributeValues.put(":val", new AttributeValue().withS(time));

			ScanRequest scanRequest = new ScanRequest().withTableName(DynamoHelper.tableName).withFilterExpression("tweettime > :val")
					.withExpressionAttributeValues(expressionAttributeValues);
			PrintWriter writer = response.getWriter();

			ScanResult result = DynamoHelper.getDynamoClient().scan(scanRequest);
			StringBuffer sb = new StringBuffer();
			for (Map<String, AttributeValue> item : result.getItems()) {
				String lat = item.get("lat").getS();
				String lng = item.get("lon").getS();
				sb.append(lat + "," + lng + ";");
			}
			System.out.println("LatLong: " + sb + " Time: " + System.currentTimeMillis());
			if (sb.length() != 0) {
				//sb.substring(0, sb.length() - 1);
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
