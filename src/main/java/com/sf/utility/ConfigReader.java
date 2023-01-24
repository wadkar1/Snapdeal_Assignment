package com.sf.utility;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

	public static String getLocalValue(String variable) {

		Properties prop = new Properties();
		String propFileName = "config/envconfig.properties";

		try {
			prop.load(new FileReader(propFileName));

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return prop.getProperty(variable);
	}

}
