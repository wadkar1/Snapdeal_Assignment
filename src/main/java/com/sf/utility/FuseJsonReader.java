package com.sf.utility;

import java.io.FileReader;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class FuseJsonReader {

	JSONArray invoice_List;
	int number_Of_Invoices;

	Object field_Value;
	Object leg_Field_Value;
	Object fare_Breakdown_Value;

	public FuseJsonReader() throws Exception {
		Object obj = new JSONParser().parse(new FileReader("src/test/resources/TestData/simplifiedSample.json"));
		invoice_List = (JSONArray) obj;
		number_Of_Invoices = invoice_List.size();
	}

	public String getFieldValue(String fileno, String field) {
		for (int i = 0; i < number_Of_Invoices; i++) {
			JSONObject invoiceObject = (JSONObject) invoice_List.get(i);

			if (invoiceObject.get("file_no").equals(fileno)) {
				field_Value = invoiceObject.get(field);
				break;
			}
		}
		return field_Value.toString();
	}

	public String getLegFieldValue(String fileno, String leg_field, int leg_Number) {
		for (int i = 0; i < number_Of_Invoices; i++) {
			JSONObject invoiceObject = (JSONObject) invoice_List.get(i);

			if (invoiceObject.get("file_no").equals(fileno)) {
				JSONArray legs_List = (JSONArray) invoiceObject.get("legs");
				JSONObject leg_Object = (JSONObject) legs_List.get(leg_Number);
				leg_Field_Value = leg_Object.get(leg_field);
				break;
			}
		}
		return leg_Field_Value.toString();
	}

	public String getfareBreakdownValue(String fileno, String fare_Breakdown_field, int leg_Number) {
		for (int i = 0; i < number_Of_Invoices; i++) {
			JSONObject invoiceObject = (JSONObject) invoice_List.get(i);

			if (invoiceObject.get("file_no").equals(fileno)) {
				JSONArray legs_List = (JSONArray) invoiceObject.get("legs");
				JSONObject leg_Object = (JSONObject) legs_List.get(leg_Number);
				JSONObject fare_Breakdown_Object = (JSONObject) leg_Object.get("fare_breakdown");
				fare_Breakdown_Value = fare_Breakdown_Object.get(fare_Breakdown_field);
				break;
			}
		}
		return fare_Breakdown_Value.toString();
	}
}
