package com.sf.utility;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.spi.json.JacksonJsonNodeJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;

public class JsonWriter {
	String userDir = System.getProperty("user.dir");

	public String generateStringFromResource(String fileName) throws IOException {
		return new String(Files.readAllBytes(completePath(fileName)));
	}

	public Path completePath(String fileName) {
		return Paths.get(path(fileName));
	}

	public String path(String fileName) {
		// System.out.println("********" + userDir);
		return userDir + "/" + fileName + ".json";
	}

	public void replaceValueInJson(String filename, String value, String fieldJsonPath) throws Exception {

		Configuration configuration = Configuration.builder().jsonProvider(new JacksonJsonNodeJsonProvider())
				.mappingProvider(new JacksonMappingProvider()).build();

		String updatedJson = JsonPath.using(configuration).parse(generateStringFromResource(filename))
				.set(fieldJsonPath, value).json().toString();

		FileWriter file = new FileWriter(path(filename));
		file.write(updatedJson);
		file.flush();
		file.close();
	}

	public String getValueInJson(String filename, String fieldJsonPath) throws Exception {
		Configuration configuration = Configuration.builder().jsonProvider(new JacksonJsonNodeJsonProvider())
				.mappingProvider(new JacksonMappingProvider()).build();

		String field_Value = JsonPath.using(configuration).parse(generateStringFromResource(filename))
				.read(fieldJsonPath).toString();
		return field_Value;

	}

	public Object updateLegValuesInJsonObject(String filename, int leg_Id, String pick_Up_Time,
			String appointment_Time) throws Exception {
		Configuration configuration = Configuration.builder().jsonProvider(new JacksonJsonNodeJsonProvider())
				.mappingProvider(new JacksonMappingProvider()).build();
		Object old_JsonObject = JsonPath.using(configuration).parse(generateStringFromResource(filename))
				.read("$.body.Legs[0]");
		Object updated_JsonObject = JsonPath.using(configuration).parse(old_JsonObject).set("$.LegID", leg_Id)
				.set("$.PickupDateTime", pick_Up_Time).set("$.AppointmentDate", appointment_Time).json();
		return updated_JsonObject;
	}

	public void addLegValueInJsonArray(String filename, String fieldJsonPath, Object updated_JsonObject)
			throws Exception {
		Configuration configuration = Configuration.builder().jsonProvider(new JacksonJsonNodeJsonProvider())
				.mappingProvider(new JacksonMappingProvider()).build();
		String added_Json = JsonPath.using(configuration).parse(generateStringFromResource(filename))
				.add(fieldJsonPath, updated_JsonObject).json().toString();
		FileWriter file = new FileWriter(path(filename));
		file.write(added_Json);
		file.flush();
		file.close();
	}

	// TEST DATA
	// wo.deleteValueInJson("Test", "$.body.Legs[2]");
	// wo.addLegValueInJsonArray("Test",
	// "$.body.Legs",wo.updateLegIDValueInJsonObject("Test","99"));

	/**
	 * Delete value in json.
	 *
	 * @param filename      the filename
	 * @param fieldJsonPath the field json path
	 * @throws Exception the exception
	 */
	public void deleteValueInJson(String filename, String fieldJsonPath) throws Exception {
		Configuration configuration = Configuration.builder().jsonProvider(new JacksonJsonNodeJsonProvider())
				.mappingProvider(new JacksonMappingProvider()).build();
		String updatedJson = JsonPath.using(configuration).parse(generateStringFromResource(filename))
				.delete(fieldJsonPath).json().toString();
		FileWriter file = new FileWriter(path(filename));
		file.write(updatedJson);
		file.flush();
		file.close();
	}

	public void replaceArrayValueInJson(String filename, String value, String fieldJsonPath) throws Exception {
		Configuration configuration = Configuration.builder().jsonProvider(new JacksonJsonNodeJsonProvider())
				.mappingProvider(new JacksonMappingProvider()).build();
		String updatedJson = JsonPath.using(configuration).parse(generateStringFromResource(filename))
				.add(fieldJsonPath, value).json().toString();
		FileWriter file = new FileWriter(path(filename));
		file.write(updatedJson);
		file.flush();
		file.close();
	}

	public void replaceValueInJson(String filename, String[] value, String fieldJsonPath) throws Exception {

		Configuration configuration = Configuration.builder().jsonProvider(new JacksonJsonNodeJsonProvider())
				.mappingProvider(new JacksonMappingProvider()).build();

		String updatedJson = JsonPath.using(configuration).parse(generateStringFromResource(filename))
				.set(fieldJsonPath, value).json().toString();

		FileWriter file = new FileWriter(path(filename));
		file.write(updatedJson);
		file.flush();
		file.close();
	}

	
	public void replaceNullObjectValueInJson(String filename, String fieldJsonPath) throws Exception {

		Configuration configuration = Configuration.builder().jsonProvider(new JacksonJsonNodeJsonProvider())
				.mappingProvider(new JacksonMappingProvider()).build();

		String updatedJson = JsonPath.using(configuration).parse(generateStringFromResource(filename))
				.set(fieldJsonPath, null).json().toString();

		FileWriter file = new FileWriter(path(filename));
		file.write(updatedJson);
		file.flush();
		file.close();
	}
}
