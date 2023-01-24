package com.sf.utility;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;

import org.apache.commons.lang3.time.DateUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;

import com.google.common.collect.Ordering;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class Functions {

	String env = System.getProperty("environment").toLowerCase();

	public String captializeFirstLetter(String str) {
		String words[] = str.toLowerCase().split("\\s");
		String capitalizeWord = "";
		for (String w : words) {
			String first = w.substring(0, 1);
			String afterfirst = w.substring(1);
			capitalizeWord += first.toUpperCase() + afterfirst + " ";
		}

		return capitalizeWord.trim();
	}

	public String removeSpecifiedSpecialCharacters(String chr, String str) {
		return str.replaceAll(chr, "");
	}

	public String removeSpecialCharacters(String str) {
		return str.replaceAll("[-+.^:,() ]", "").substring(1);
	}

	public String toUpperCase(String str) {
		return str.toUpperCase();
	}

	public String toLowerCase(String str) {
		return str.toLowerCase();
	}

	public boolean containsString(String str1, String str2) {
		return str1.toLowerCase().contains(str2.toLowerCase());
	}

	public String returnFutureDate(int days) {
		DateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, days);
		return sdf.format(cal.getTime());
	}

	public String returnPastDate(int days, String format) {
		DateFormat sdf = new SimpleDateFormat(format);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, days);
		return sdf.format(cal.getTime());
	}

	public int convertStringToInteger(String str) {
		return Integer.parseInt(str);
	}

	public String convertIntegerToString(int number) {
		return Integer.toString(number);
	}

	public String randomTinGenerator() {
		Random random1 = new Random();
		long n1 = (long) (1000L + random1.nextFloat() * 9000L);
		String number1 = String.valueOf(n1);
		return number1;
	}

	public String randomNumberGenerator(int length) {
		char[] chars = "123456789".toCharArray();
		StringBuilder sb = new StringBuilder(length);
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			char c = chars[random.nextInt(chars.length)];
			sb.append(c);
		}
		String number = sb.toString();
		return number;
	}

	public String randomStringGenerator(int length) {
		char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			char c = chars[random.nextInt(chars.length)];
			sb.append(c);
		}
		String output = sb.toString().replace("'", "");
		return output.toUpperCase();
	}

	public boolean checkArrayListToBeInAscendingOrder(ArrayList<Float> array) {
		boolean issorted = Ordering.natural().isOrdered(array);
		return issorted;
	}

	public float convertStringToFloat(String str) {
		return Float.parseFloat(str);
	}

	public float convertIntegerToFloat(int nbr) {
		return (float) nbr;
	}

	public boolean checkStringIsNumeric(String number) {
		return number.matches("^[0-9]*\\.?[0-9]+$");
	}

	public int deleteOrgAPI(String Org_Name) throws JSONException {
		RequestSpecification delete_Request;
		Response delete_Response;
		Log.info("Deleting Organisation :" + Org_Name);
		if (env.equalsIgnoreCase("qa")) {
			RestAssured.baseURI = "https://wuuw4208n0.execute-api.us-east-1.amazonaws.com/qa/organisation";
		} else if (env.equalsIgnoreCase("stage")) {
			RestAssured.baseURI = "https://tq55szov80.execute-api.us-west-2.amazonaws.com/stage/organisation";
		}

		delete_Request = RestAssured.given().pathParam("orgName", Org_Name);
		delete_Response = delete_Request.delete("/{orgName}");
		Log.info(delete_Response.asString());
		int statusCode = delete_Response.getStatusCode();
		Log.info("Status Code of the Response is : " + statusCode);
		return statusCode;
	}

	public int deleteMSAPI(String MS_Name) throws JSONException {
		RequestSpecification delete_Request;
		Response delete_Response;
		Log.info("Deleting Market Segment :" + MS_Name);
		if (env.equalsIgnoreCase("qa")) {
			RestAssured.baseURI = "https://wuuw4208n0.execute-api.us-east-1.amazonaws.com/qa/marketsegment";
		} else if (env.equalsIgnoreCase("stage")) {
			RestAssured.baseURI = "https://tq55szov80.execute-api.us-west-2.amazonaws.com/stage/marketsegment";
		}

		delete_Request = RestAssured.given().pathParam("marketSegmentName", MS_Name);
		delete_Response = delete_Request.delete("/{marketSegmentName}");
		Log.info(delete_Response.asString());
		int statusCode = delete_Response.getStatusCode();
		Log.info("Status Code of the Response is : " + statusCode);
		return statusCode;
	}

	public String returnESTDateInGhealthFormat(int addHours, int addMinutes) throws ParseException {
		Date date = new Date();
		DateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
		timeFormat.setTimeZone(TimeZone.getTimeZone("America/New_York"));
		String estTime = timeFormat.format(date);
		date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", Locale.ENGLISH).parse(estTime);
		Date offSetTime = DateUtils.addHours(date, addHours);
		Date targetTime = DateUtils.addMinutes(offSetTime, addMinutes);
		String[] Date = timeFormat.format(targetTime).split("\\s");
		return (Date[0] + "T" + Date[1]);
	}

	public String returnFutureDateMAS(int days) {
		DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, days);
		return sdf.format(cal.getTime());
	}

	public String returnFutureTimeMAS(int addHours, int addMinutes) throws ParseException {
		Date date = new Date();
		DateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HHmm z");
		timeFormat.setTimeZone(TimeZone.getTimeZone("America/New_York"));
		String estTime = timeFormat.format(date);
		date = new SimpleDateFormat("yyyy-MM-dd HHmm z", Locale.ENGLISH).parse(estTime);
		Date offSetTime = DateUtils.addHours(date, addHours);
		Date targetTime = DateUtils.addMinutes(offSetTime, addMinutes);
		String[] Date = timeFormat.format(targetTime).split("\\s");
		System.out.println(Date[1]);
		return Date[1];
	}

	public void copyToClipboard(String file) throws UnsupportedFlavorException, IOException {

		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Clipboard clipboard = toolkit.getSystemClipboard();
		StringSelection strSel = new StringSelection(file);
		clipboard.setContents(strSel, null);
		System.out.print(clipboard.getData(DataFlavor.stringFlavor).toString());

	}

	public int createRideMAS(String xml_File) throws JSONException {
		String id;
		String display_Name;

		if (env.equalsIgnoreCase("qa")) {
			id = "WMb0wsAAA";
			display_Name = "QA";
		} else if (env.equalsIgnoreCase("stage")) {
			id = "Qhj93MAAA";
			display_Name = "Stage";
		} else {
			id = "UCyRfMAAA";
			display_Name = "Dev";
		}

		String payload = "{\"delay_enabled\":false,\"delay_in_sec\":1,\"description\":\"\",\"display_name\":\"Pull Roster "
				+ display_Name + "\",\"http_response\":{\"body\":\"" + xml_File
				+ "\",\"body_interpreter\":\"jinja2\",\"content_encoding\":\"UTF-8\",\"content_type\":\"application/xml\",\"http_status\":\"200\"},\"id\":\""
				+ id + "\",\"is_deployed\":true,\"log_enabled\":false,\"name\":\"Pull Roster " + display_Name
				+ "\",\"path\":\"pull-roster-" + env + "\",\"url\":\"http://sourcefuse.mockable.io/pull-roster-" + env
				+ "\",\"url_secured\":\"https://sourcefuse.mockable.io/pull-roster-" + env + "\",\"verb\":\"POST\"}";
		RequestSpecification mas_Ride_Request = null;
		Response mas_Ride_Response;
		Map<String, Object> ride_Details_Headers = new HashMap<String, Object>();
		RestAssured.baseURI = "https://v8-firewall-test-dot-tomate-premium.appspot.com/_ah/api/tomate/v2/space/sourcefuse/rest/"
				+ id;
		ride_Details_Headers.put("Accept", "application/json, text/plain, /");
		ride_Details_Headers.put("Referer", "https://www.mockable.io/a/");
		ride_Details_Headers.put("Origin", "https://www.mockable.io/");
		ride_Details_Headers.put("Authorization",
				"LrFf3NXI4VhnI1rkbZi0eBSlNtgWaA8kN3cg87WumAqCtcKYwIZF8FdGB2HpdILDLXtOdw1o6xhvbDdQP_oFrUWaHmu5mqQ7VqGL4rNR_nA");
		ride_Details_Headers.put("Content-Type", "application/json;charset=UTF-8");
		mas_Ride_Request = RestAssured.given().headers(ride_Details_Headers).body(payload);
		mas_Ride_Response = mas_Ride_Request.put();
		int statusCode = mas_Ride_Response.getStatusCode();
		Log.info("login_Response" + mas_Ride_Response.asString());
		return statusCode;
	}

	public String returnESTFutureTime(int addHours, int addMinutes) throws ParseException {
			Date date = new Date();
			DateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mmaa z");
			timeFormat.setTimeZone(TimeZone.getTimeZone("America/New_York"));
			String estTime = timeFormat.format(date);
			date = new SimpleDateFormat("yyyy-MM-dd HH:mmaa z", Locale.ENGLISH).parse(estTime);
			Date offSetTime = DateUtils.addHours(date, addHours);
			Date targetTime = DateUtils.addMinutes(offSetTime, addMinutes);
			String[] Date = timeFormat.format(targetTime).split("\\s");
			return (Date[1]);
	}

	public String returnESTFutureDate(int addHours, int addMinutes) throws ParseException {
		Date date = new Date();
		DateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd z");
		timeFormat.setTimeZone(TimeZone.getTimeZone("America/New_York"));
		String estTime = timeFormat.format(date);
		date = new SimpleDateFormat("yyyy-MM-dd z", Locale.ENGLISH).parse(estTime);
		Date offSetTime = DateUtils.addHours(date, addHours);
		Date targetTime = DateUtils.addMinutes(offSetTime, addMinutes);
		String[] date1 = timeFormat.format(targetTime).split("\\s");
		System.out.println(date1[0]);
		return date1[0];
	}

	public int generateBWBReport(String username, String password) throws JSONException {
		JSONObject loginCredentials;
		JsonPath jsonPathEvaluator;
		RequestSpecification login_Request;
		RequestSpecification bwbCron_Get;
		RequestSpecification logout_Request;
		Response login_Response;
		Response logout_Response;
		Response bwbCron_Response;
		String token;
		Map<String, Object> logout_Request_Headers = new HashMap<String, Object>();
		Map<String, Object> ride_Details_Headers = new HashMap<String, Object>();
		// =========================API Login==============================
		RestAssured.baseURI = "https://qa.relayhealthcare.co/user/login";
		loginCredentials = new JSONObject();
		loginCredentials.put("username", username);
		loginCredentials.put("password", password);
		login_Request = RestAssured.given().body(loginCredentials.toString()).contentType(ContentType.JSON);
		login_Response = login_Request.post();
		// System.out.println(login_Response.asString());
		jsonPathEvaluator = login_Response.jsonPath();
		token = jsonPathEvaluator.getString("token");
		// =========================API HIT BWB CRON============================
		RestAssured.baseURI = "https://qa.relayhealthcare.co";
		ride_Details_Headers.put("usertoken", token);
		ride_Details_Headers.put("username", username);
		bwbCron_Get = RestAssured.given().headers(ride_Details_Headers);
		bwbCron_Response = bwbCron_Get.get("/bwbCron");
		int statusCode = bwbCron_Response.getStatusCode();
		Log.info("Checking that response came as 200");
		Assert.assertEquals(statusCode, 200, "200 Status code didn't come");
		Log.info("Response came as 200. BWB Cron ran succesfully");
		String message = bwbCron_Response.asString();
		Log.info(message);
		// =========================API Logout==============================
		logout_Request_Headers.put("usertoken", token);
		logout_Request_Headers.put("username", username);
		logout_Request_Headers.put("Content-Type", "application/json");
		RestAssured.baseURI = "https://qa.relayhealthcare.co/user/logout";
		logout_Request = RestAssured.given().headers(logout_Request_Headers);
		logout_Response = logout_Request.put();
		System.out.println(logout_Response.asString());
		return statusCode;
	}

	public int downloadBWBReport(String username, String password) throws JSONException, IOException {
		JSONObject loginCredentials;
		JsonPath jsonPathEvaluator;
		RequestSpecification login_Request;
		RequestSpecification bwbCron_Get;
		RequestSpecification logout_Request;
		Response login_Response;
		Response logout_Response;
		Response bwbCron_Response;
		String token;
		Map<String, Object> logout_Request_Headers = new HashMap<String, Object>();
		Map<String, Object> ride_Details_Headers = new HashMap<String, Object>();
		/* =========================API Login============================== */
		RestAssured.baseURI = "https://qa.relayhealthcare.co/user/login";
		loginCredentials = new JSONObject();
		loginCredentials.put("username", username);
		loginCredentials.put("password", password);
		login_Request = RestAssured.given().body(loginCredentials.toString()).contentType(ContentType.JSON);
		login_Response = login_Request.post();
		// System.out.println(login_Response.asString());
		jsonPathEvaluator = login_Response.jsonPath();
		token = jsonPathEvaluator.getString("token");
		/*
		 * =========================API HIT BWB DOWNLOAD
		 * CRON============================
		 */
		RestAssured.baseURI = "https://qa.relayhealthcare.co";
		ride_Details_Headers.put("usertoken", token);
		ride_Details_Headers.put("username", username);
		bwbCron_Get = RestAssured.given().headers(ride_Details_Headers);
		bwbCron_Response = bwbCron_Get.get("/fuse/bwb-report");
		int statusCode = bwbCron_Response.getStatusCode();
		Log.info("Checking that response came as 200");
		Assert.assertEquals(statusCode, 200, "200 Status code didn't come");
		Log.info("Response came as 200. BWB Report Generation Cron ran succesfully");
		byte[] arraybyteResponse = bwbCron_Response.getBody().asByteArray();
		ByteBuffer buffer = ByteBuffer.wrap(arraybyteResponse);
		OutputStream os = new FileOutputStream("simplifiedSample.json");
		WritableByteChannel channel = Channels.newChannel(os);
		channel.write(buffer);
		System.out.println("BWB report downloaded and written successfully to file.");
		/* =========================API Logout============================== */
		logout_Request_Headers.put("usertoken", token);
		logout_Request_Headers.put("username", username);
		logout_Request_Headers.put("Content-Type", "application/json");
		RestAssured.baseURI = "https://qa.relayhealthcare.co/user/logout";
		logout_Request = RestAssured.given().headers(logout_Request_Headers);
		logout_Response = logout_Request.put();
		System.out.println(logout_Response.asString());
		return statusCode;
	}

	public static String extractInt(String str) {
		str = str.replaceAll("[^\\d]", " ");
		str = str.trim();
		str = str.replaceAll(" +", " ");

		if (str.equals(""))
			return "0";

		return str;
	}
	
	public String returnESTDateInFormatFixed(int addHours, String time) throws ParseException {
		Date date = new Date();
		DateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
		timeFormat.setTimeZone(TimeZone.getTimeZone("America/New_York"));
		String estTime = timeFormat.format(date);
		date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", Locale.ENGLISH).parse(estTime);
		Date offSetTime = DateUtils.addHours(date, addHours);
		String[] Date = timeFormat.format(offSetTime).split("\\s");
		return (Date[0] + "T"+time);
	}
	
	public String returnESTFutureTime12HourFormat(int addHours, int addMinutes) throws ParseException {
		Date date = new Date();
		DateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd hh:mmaa z");
		timeFormat.setTimeZone(TimeZone.getTimeZone("America/New_York"));
		String estTime = timeFormat.format(date);
		date = new SimpleDateFormat("yyyy-MM-dd hh:mmaa z", Locale.ENGLISH).parse(estTime);
		Date offSetTime = DateUtils.addHours(date, addHours);
		Date targetTime = DateUtils.addMinutes(offSetTime, addMinutes);
		String[] Date = timeFormat.format(targetTime).split("\\s");
		return (Date[1]);
}
	public String returnESTDate(int addHours,int minutes) throws ParseException {
		Date date = new Date();
		DateFormat timeFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss z");
		timeFormat.setTimeZone(TimeZone.getTimeZone("America/New_York"));
		String estTime = timeFormat.format(date);
		date = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss z", Locale.ENGLISH).parse(estTime);
		Date offSetTime = DateUtils.addHours(date, addHours);
		String[] Date = timeFormat.format(offSetTime).split("\\s");
		return (Date[0]);
	}
}
