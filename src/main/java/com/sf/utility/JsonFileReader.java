package com.sf.utility;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JsonFileReader {
	public String FirstName;
	public String LastName;
	public String OrderID;

	File rideOrderTextFile = new File("src/test/resources/TestData/RideOrderData.txt");
	String absoluteRideOrderTextFile = rideOrderTextFile.getAbsolutePath();

	File rideOrderJSONFile = new File("src/test/resources/TestData/RideOrderTestData.json");
	String absoluteRideOrderJSONFile = rideOrderJSONFile.getAbsolutePath();

	File sendToTopic = new File("send-to-topic");
	String absoluteSendToTopic = sendToTopic.getAbsolutePath();
	String OS = System.getProperty("os.name").toLowerCase();

	String env = System.getProperty("environment").toLowerCase();
	String gh_env;

	public void createRideOrderCommand() throws IOException, InterruptedException {
		Log.info("Absolute path for send-to-topic: " + absoluteSendToTopic);
		Log.info("Absolute path for RideOrderData.txt: " + absoluteRideOrderTextFile);

		if (env.equalsIgnoreCase("qa")) {
			gh_env = "qa";
		} else if (env.equalsIgnoreCase("stage")) {
			gh_env = "uat";
		} else {
			gh_env = "developoccm";
		}
		// Check OS and execute command
		if (OS.indexOf("win") >= 0) {
			Runtime.getRuntime()
					.exec("cmd.exe /c cd \"" + absoluteSendToTopic + "\" & start cmd.exe /k \"npm run submit-trip:"
							+ gh_env + " >" + absoluteRideOrderTextFile + "\"");
		} else {
			ProcessBuilder processBuilder = new ProcessBuilder();
			processBuilder.command("/bin/bash", "-c",
					"cd send-to-topic && npm run submit-trip:" + gh_env + " >" + absoluteRideOrderTextFile);
			Process process = processBuilder.start();
		}
		Log.info("Command Executed");
		
		if (env.equalsIgnoreCase("qa")) {
			Thread.sleep(60000);
		} 
		else {
			Thread.sleep(60000);
		}
		
	}

	public void cleanUpTextFile() throws IOException {
		RandomAccessFile file = new RandomAccessFile(absoluteRideOrderTextFile, "rw");
		String delete;
		String task = "";
		while ((delete = file.readLine()) != null) {
			if (delete.startsWith("> send-to-topic")) {
				continue;
			}
			if (delete.startsWith("> npm run submit-trip")) {
				continue;
			}
			if (delete.startsWith("> node -r dotenv/config src")) {
				continue;
			}
			if (delete.startsWith("Fetch event")) {
				continue;
			}
			if (delete.startsWith("Fetch Credentials")) {
				continue;
			}
			if (delete.startsWith("{ sharedAccessKey")) {
				continue;
			}
			if (delete.startsWith("Submit Trip")) {
				continue;
			}
			if (delete.startsWith("Event")) {
				continue;
			}
			if (delete.startsWith("Result")) {
				continue;
			}

			task += delete + "\n";
		}
		BufferedWriter writer = new BufferedWriter(new FileWriter(absoluteRideOrderTextFile));
		writer.write(task);
		file.close();
		writer.close();
	}

	public void removeAndwriteToJSONFile() throws IOException {
		List<String> lines = new ArrayList<String>();
		BufferedReader r = new BufferedReader(new FileReader(absoluteRideOrderTextFile));
		String in;
		while ((in = r.readLine()) != null)
			lines.add(in);
		r.close();
		int LineFromBottom = lines.size();
		int expected_Lines = lines.size() - 15;
		while (LineFromBottom != expected_Lines) {
			lines.remove(lines.size() - 1);
			int newLineNumber = lines.size();
			LineFromBottom = newLineNumber;
			LineFromBottom--;
		}
		PrintWriter w = new PrintWriter(new FileWriter(absoluteRideOrderJSONFile));
		for (String line : lines)
			w.println(line);
		w.close();
	}

	public String GetOrderID() throws FileNotFoundException, IOException, ParseException {
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(new FileReader(absoluteRideOrderJSONFile));
		JSONObject jo = (JSONObject) obj;
		Map node = ((Map) jo.get("body"));
		Iterator<Map.Entry> itr1 = node.entrySet().iterator();
		while (itr1.hasNext()) {
			Map.Entry pair = itr1.next();
			if (pair.getKey().equals("OrderId")) {
				OrderID = pair.getValue().toString();
			}
		}
		return OrderID;
	}

	public String GetMemberFN() throws FileNotFoundException, IOException, ParseException {
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(new FileReader(absoluteRideOrderJSONFile));
		JSONObject jo = (JSONObject) obj;
		Map node = ((Map) jo.get("body"));
		Iterator<Map.Entry> itr1 = node.entrySet().iterator();
		while (itr1.hasNext()) {
			Map.Entry pair = itr1.next();
			if (pair.getKey().equals("MemberFirstName")) {
				FirstName = pair.getValue().toString();
			}
		}
		return FirstName;
	}

	public String GetMemberLN() throws FileNotFoundException, IOException, ParseException {
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(new FileReader(absoluteRideOrderJSONFile));
		JSONObject jo = (JSONObject) obj;
		Map node = ((Map) jo.get("body"));
		Iterator<Map.Entry> itr1 = node.entrySet().iterator();
		while (itr1.hasNext()) {
			Map.Entry pair = itr1.next();

			if (pair.getKey().equals("MemberLastName")) {
				LastName = pair.getValue().toString();
			}
		}
		return LastName;
	}

	public void closeCMD() {
		try {
			Runtime.getRuntime().exec("taskkill /f /im cmd.exe");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateRideOrderCommand(String order_id, String leg_id_1, String leg_id_2, String leg_id_3)
			throws IOException, InterruptedException {
		File f = new File("send-to-topic");
		String absolute = f.getAbsolutePath();

		if (OS.indexOf("win") >= 0) {
			Runtime.getRuntime()
					.exec("cmd.exe /c cd \"" + absolute + "\" &start cmd.exe /k \"npm run submit-trip:" + gh_env
							+ "  order.id=" + order_id + " leg[1].id=" + leg_id_1 + " leg[2].id=" + leg_id_2
							+ " leg[3].id=" + leg_id_3 + "/");

		} else {
			ProcessBuilder processBuilder = new ProcessBuilder();
			processBuilder.command("/bin/bash", "-c",
					"cd send-to-topic && npm run submit-trip:" + gh_env + "  order.id=" + order_id + " leg[1].id="
							+ leg_id_1 + " leg[2].id=" + leg_id_2 + " leg[3].id=" + leg_id_3);
			Process process = processBuilder.start();
		}

		Log.info("Updation Command Executed");
		if (env.equalsIgnoreCase("qa")) {
			Thread.sleep(60000);
		} 
		else {
			Thread.sleep(60000);
		}
	}

	public void canceledRideOrderCommand(String orderID) throws IOException, InterruptedException {
		Log.info("Absolute path for send-to-topic: " + absoluteSendToTopic);
		Log.info("Absolute path for RideOrderData.txt: " + absoluteRideOrderTextFile);

		if (env.equalsIgnoreCase("qa")) {
			gh_env = "qa";
		} else if (env.equalsIgnoreCase("stage")) {
			gh_env = "uat";
		} else {
			gh_env = "developoccm";
		}
		// Check OS and execute command
		if (OS.indexOf("win") >= 0) {
			Runtime.getRuntime().exec("cmd.exe /c cd \"" + absoluteSendToTopic
					+ "\" & start cmd.exe /k \"npm run submit-trip:" + gh_env + "  order.id=" + orderID);
		} else {
			ProcessBuilder processBuilder = new ProcessBuilder();
			processBuilder.command("/bin/bash", "-c",
					"cd send-to-topic && npm run submit-trip:" + gh_env + "   order.id=" + orderID);
			Process process = processBuilder.start();
		}
		Log.info("Command Executed");
		Thread.sleep(60000);

	}
	
	public int getNumberOfLines() throws IOException {
		List<String> lines = new ArrayList<String>();
		BufferedReader r = new BufferedReader(new FileReader(absoluteRideOrderTextFile));
		String in;
		while ((in = r.readLine()) != null)
			lines.add(in);
		r.close();
		int LineFromBottom = lines.size();
		return LineFromBottom;
	}

	
}
