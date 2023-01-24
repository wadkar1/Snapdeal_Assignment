package com.flipcart.pages;

import java.io.IOException;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.sf.objectRepository.ObjectRepository;
import com.sf.utility.Functions;
import com.sf.webActions.ElementActions;
import com.twilio.Twilio;
import com.twilio.base.ResourceSet;
import com.twilio.rest.api.v2010.account.Message;

public class HomePage extends ObjectRepository {

	Logger log;
	WebDriver driver;
	ElementActions element_Actions;
	Functions functions;
	ObjectRepository obj;

	public HomePage(WebDriver driver, Logger log) throws IOException {
		super("Flipkart.properties");
		obj = new ObjectRepository("Flipkart.properties");
		this.log = log;
		this.driver = driver;
		functions = new Functions();
		element_Actions = new ElementActions(driver);
	}
	public void clickOnMenFashion() {
		log.info("Clicking on Men's fashion");
		Actions act= new Actions(driver);
		WebElement mensfashion=element_Actions.findElement(obj.getElement("menas_fashion"));
		//WebElement sportShoes=element_Actions.findElement(obj.getElement("sport_shoes"));
		act.moveToElement(mensfashion).perform();
		element_Actions.waitUntilVisibilityLocated(obj.getElement("sport_shoes"));
		element_Actions.findElement(obj.getElement("sport_shoes"));
		element_Actions.click(obj.getElement("sport_shoes"));
		log.info("Men's fashion is clicked successfully");
	}
	public void clickOnProduct() {
		log.info("Clicking on product");
		element_Actions.waitUntilVisibilityLocated(obj.getElement("product"));
		WebElement product=element_Actions.findElement(obj.getElement("product"));
		element_Actions.scrollElementIntoView(product);
		element_Actions.click(obj.getElement("product"));
		log.info("product is clicked successfully");
	}
	public void clickOnAddToCart() throws InterruptedException {
		log.info("Clicking Add to cart Button");
		element_Actions.switchToDesiredWindow(1);
		Thread.sleep(2000);
		element_Actions.waitUntilVisibilityLocated(obj.getElement("add_to_cart"));
		element_Actions.findElement(obj.getElement("add_to_cart"));
		element_Actions.click(obj.getElement("add_to_cart"));
		log.info("Add to cart Button is clicked successfully");
	}
	public void clickViewToCart() throws InterruptedException {
		log.info("Clicking View to cart Button");
		element_Actions.waitUntilVisibilityLocated(obj.getElement("view_cart"));
		element_Actions.findElement(obj.getElement("view_cart"));
		element_Actions.click(obj.getElement("view_cart"));
		log.info("View to cart Button is clicked successfully");
	}
	public boolean checkProductIsAddedSuccefullyToCart(){  
		log.info("user checking product is successfully added to cart");
		boolean cart = element_Actions.checkElementPresence(obj.getElement("subtotal"));
		log.info("user checked product is successfully added in cart");
		return cart;
	}
	public int checkPrice()throws InterruptedException {
		log.info("checking price when select one product");
		//element_Actions.waitUntilStatusChanged(obj.getElement("subtotal"));
		element_Actions.waitUntilVisibilityLocated(obj.getElement("subtotal"));
		WebElement subtotal=element_Actions.findElement(obj.getElement("subtotal"));
		String price1 = subtotal.getText();
		int price=new Functions().convertStringToInteger(price1);
		log.info("View to cart Button is clicked successfully");
		return price;
	}
	public void increaseQTY() throws InterruptedException {
		log.info("User is increasing QTy to cart Button");
		element_Actions.waitUntilVisibilityLocated(obj.getElement("select1"));
	    element_Actions.findElement(obj.getElement("select1")).click();
	    element_Actions.waitUntilVisibilityLocated(obj.getElement("qty"));
	    element_Actions.findElement(obj.getElement("qty")).click();
		log.info("QTY is increased successfully");
	}
}