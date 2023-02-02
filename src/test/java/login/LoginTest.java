package login;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;

import com.aventstack.extentreports.Status;
import com.flipcart.pages.HomePage;
import com.flipcart.pages.LoginPage;
import com.sf.base.TestBase;
import com.sf.utility.Functions;
import com.sf.utility.Log;
import com.sf.utility.XmlReader;

public class LoginTest extends TestBase {

	LoginPage fp;
	HomePage hp;
	Functions functions;
	public Logger log;
	
	public void loader() throws IOException {
		log = LogManager.getLogger(LoginTest.class.getName());
		functions = new Functions();
		fp = new LoginPage(driver, log);
		hp = new HomePage(driver, log);
	}

	@Test(dataProvider = "snapdeal")
	public void snapdealLogin(String username, String mailname,String mailurl)throws IOException, InterruptedException {
		test = extent.createTest("Snapdeal Login Functionality");
		log.info("Started==Snapdeal Login Functionality");
		driver.get(URL);
		log.info("Logging into the application using Email");
		test.log(Status.INFO, "Logging into the application using Email");
		
		test.log(Status.INFO, "Clicking on Login Button");
		fp.clickOnLogin();
		test.log(Status.PASS, "Login button is clicked successfully from login page");	
		
		test.log(Status.INFO, "Entering the username in username field");
		fp.enterUsername(username);
		test.log(Status.PASS, "Username is entered successfully : "+ username);

		test.log(Status.INFO, "Clicking on Continue button");
		fp.clickContinue();
		test.log(Status.PASS, "Continue button is clicked successfully");

		test.log(Status.INFO, "Capturing the OTP from mail");
		String otp=fp.getOTPFromMail(mailname,mailurl);
		test.log(Status.PASS, "OTP is captured successfully from Mail : " +otp);

		test.log(Status.INFO, "Entering the OTP");
		fp.enterOTP(otp);
		test.log(Status.PASS, "User enters the OTP successfully");
		
		test.log(Status.INFO, "Clicking on Submit button");
		fp.clickSubmit();
		test.log(Status.PASS, "Submit button is clicked successfully");
		
		test.log(Status.INFO, "Clicking on mens fashion");
		hp.clickOnMenFashion();
		test.log(Status.PASS, "Men's fashion is clicked successfully");
		
		test.log(Status.INFO, "Clicking on frist product");
		hp.clickOnProduct();
		test.log(Status.PASS, "Product is clicked successfully");
		
		test.log(Status.INFO, "Clicking on Add to cart");
		hp.clickOnAddToCart();
		test.log(Status.PASS, "Add to cart is clicked successfully");
		
		test.log(Status.INFO, "Clicking on view to cart");
		hp.clickViewToCart();
		test.log(Status.PASS, "View to cart is clicked successfully");
		
		Assert.assertEquals(hp.checkProductIsAddedSuccefullyToCart(), true, "Product is not displyed to user");
		test.log(Status.PASS, "Product is successfully added to cart & displyed to user");
		
		test.log(Status.INFO, "Checking price for one product");
		int priceWith1QTY=hp.checkPrice();
		test.log(Status.PASS, "Successfully checked price for one product");
		
		test.log(Status.INFO, "Increasing the QTY in cart");
		hp.increaseQTY();
		test.log(Status.PASS, "Qty is increased successfully");
		
		test.log(Status.INFO, "Checking price for two product");
		Thread.sleep(3000);
		int priceAfter2QTY=hp.checkPrice();
		test.log(Status.PASS, "Successfully checked price for two product");
		
		test.log(Status.INFO, "Verifying price is increased successfully when QTY increased");
		Assert.assertEquals(priceWith1QTY*2, priceAfter2QTY);
		test.log(Status.PASS, "Price of the product is increased successfully");
	}
	@DataProvider(name = "snapdeal")
	public Object[][] snapdeal() throws IOException, ParserConfigurationException, SAXException {
		XmlReader reader = new XmlReader();
		return reader.testData("snapdeal");
	}
	public void closeSession() throws InterruptedException {
		driver.quit();
	}
}
