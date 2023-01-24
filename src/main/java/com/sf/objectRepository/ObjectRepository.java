package com.sf.objectRepository;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.openqa.selenium.By;

public class ObjectRepository {

	public Properties config;
	public FileInputStream propertiesFile;

	public ObjectRepository(String filePath) throws IOException {
		config = new Properties();
		propertiesFile = new FileInputStream("src/test/resources/Properties/" + filePath + "");
		config.load(propertiesFile);
	}

	public ObjectRepository() {

	}

	/*
	 * Get Web element
	 */
	public By getElement(String elementName) {
		String[] values = config.getProperty(elementName).split(",", 2);
		String by = values[0];
		String locator = values[1].trim();
		return getLocator(by, locator);
	}

	/*
	 * Get Web element with dynamic locator
	 */
	public By getElement(String elementName, Object... values) {
		String[] value = config.getProperty(elementName).split(",", 2);
		String by = value[0];
		String locator = value[1].trim();
		locator = value[1].trim();
		if (values.length > 0) {
			for (int i = 0; i < values.length; i++) {
				locator = locator.replaceAll("d" + (i + 1), values[i].toString());
			}
		}
		return getLocator(by, locator);
	}

	// Get locator value
	public By getLocator(String by, String locator) {
		By element = null;
		switch (by) {
		case "xpath":
			element = By.xpath(locator);
			break;
		case "name":
			element = By.name(locator);
			break;
		case "id":
			element = By.id(locator);
			break;
		case "class":
			element = By.className(locator);
			break;
		case "css":
			element = By.cssSelector(locator);
			break;
		case "linkText":
			element = By.linkText(locator);
			break;
		case "partialLinkText":
			element = By.partialLinkText(locator);
			break;
		case "tagName":
			element = By.tagName(locator);
			break;
		default:
			System.out.println("locator  is not correct");
		}
		return element;
	}
}