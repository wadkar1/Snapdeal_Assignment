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
import org.openqa.selenium.support.ui.WebDriverWait;

import com.sf.objectRepository.ObjectRepository;
import com.sf.utility.Functions;
import com.sf.webActions.ElementActions;
import com.twilio.Twilio;
import com.twilio.base.ResourceSet;
import com.twilio.rest.api.v2010.account.Message;

public class LoginPage extends ObjectRepository {

	Logger log;
	WebDriver driver;
	ElementActions element_Actions;
	Functions functions;
	ObjectRepository obj;


	public LoginPage(WebDriver driver, Logger log) throws IOException {
		super("Flipkart.properties");
		obj = new ObjectRepository("Flipkart.properties");
		this.log = log;
		this.driver = driver;
		functions = new Functions();
		element_Actions = new ElementActions(driver);
	}
	public void clickOnLogin() {
		log.info("Clicking on Login Button");
		Actions act= new Actions(driver);
		WebElement signup=element_Actions.findElement(obj.getElement("sign_in"));
		act.moveToElement(signup).perform();
		element_Actions.waitUntilVisibilityLocated(obj.getElement("login_btn"));
		element_Actions.findElement(obj.getElement("login_btn"));
		element_Actions.click(obj.getElement("login_btn"));
		log.info("Login button is clicked successfully");
	}
	public void enterUsername(String username) throws InterruptedException {
		log.info("Entering the username");
		driver.switchTo().frame("loginIframe");
		element_Actions.findElement(obj.getElement("username")).click();
		element_Actions.sendKeys(username);	
		log.info("Username entered successfully : "+ username);
	}
	public void clickContinue() {
		log.info("Clicking on Continue Button");
		element_Actions.waitUntilVisibilityLocated(obj.getElement("continue_btn"));
		element_Actions.findElement(obj.getElement("continue_btn"));
		element_Actions.click(obj.getElement("continue_btn"));
		log.info("Continue Button is clicked successfully");
		driver.switchTo().defaultContent();
	}
	public String getOTPFromMail(String mailName,String mailUrl) throws InterruptedException {
		((JavascriptExecutor)driver).executeScript("window.open()");
		element_Actions.switchToDesiredWindow(1);
		driver.navigate().to(mailUrl);
		driver.navigate().refresh();
		Thread.sleep(2000);
		element_Actions.waitUntilVisibilityLocated(obj.getElement("mail_name_field"));
		element_Actions.findElement(obj.getElement("mail_name_field"));
		log.info("User entering the name for creating the mail");
		element_Actions.sendKeys(mailName);
		element_Actions.click(obj.getElement("mail_arrow_btn"));
		log.info("User Enter the name successfully");
		driver.switchTo().frame("ifmail");
		log.info("OTP Field display on mail");
		WebElement mailfield=element_Actions.findElement(obj.getElement("mail_Otp_field"));
		element_Actions.scrollElementIntoView(mailfield);
		log.info("Capturing the OTP on Mail");
		String OTP = mailfield.getText();
		log.info("OTP is captured successfully on Mail : "+ OTP);
		driver.switchTo().defaultContent();
		element_Actions.switchToDesiredWindow(0);
		return OTP;
	}
	public void enterOTP(String otp) {
		log.info("User entering the OTP");
		driver.switchTo().frame("loginIframe");
		element_Actions.waitUntilVisibilityLocated(obj.getElement("otp_text"));
		element_Actions.findElement(obj.getElement("otp_text")).click();
		element_Actions.sendKeys(otp);
		log.info("User enters the OTP successfully");
	}
	public void clickSubmit() {
		log.info("Clicking on Submit Button");
		element_Actions.waitUntilVisibilityLocated(obj.getElement("continue_btn1"));
		element_Actions.findElement(obj.getElement("continue_btn1"));
		element_Actions.click(obj.getElement("continue_btn1"));
		log.info("Submit Button is clicked successfully");
		driver.switchTo().defaultContent();
	}

}