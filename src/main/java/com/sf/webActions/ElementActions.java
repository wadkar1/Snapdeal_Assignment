package com.sf.webActions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.sf.utility.ConfigReader;

public class ElementActions {
	WebDriver driver;
	JavascriptExecutor executor;
	static WebDriverWait wait;
	WebElement element;
	By by;
	List<WebElement> elements;
	Long maxWaitTimeToFindElement = Long.valueOf(ConfigReader.getLocalValue("maxWaitTimeToFindElement"));
	Long maxWaitTimeToPOLLElement = Long.valueOf(ConfigReader.getLocalValue("maxWaitTimeToPOLLElement"));

	public ElementActions(WebDriver driver) {
		this.driver = driver;
		wait = new WebDriverWait(driver, maxWaitTimeToFindElement);
	}

	/**
	 * Finds the element with the given "By" condition and waits for the specified
	 * amount of time
	 * 
	 * @return the (same) WebElement once it is found in DOM else throws Timeout
	 *         exception
	 */
	public WebElement waitAndFindElement(By by) {
		String message = "Searching element " + by.toString().substring(by.toString().indexOf(":"))
				+ " for presence, will wait by maximum " + maxWaitTimeToFindElement + " seconds";
		try {
			element = new WebDriverWait(driver, maxWaitTimeToFindElement)
					.until(ExpectedConditions.visibilityOfElementLocated(by));
			element.toString();
		} catch (Error e) {
			e.printStackTrace();
		}
		return element;
	}

	public WebElement waitAndFindElement(By by, int maxWaitTime) {
		String message = "Searching element " + by.toString().substring(by.toString().indexOf(":"))
				+ " for presence, will wait by maximum " + maxWaitTime + " seconds";
		try {
			element = new WebDriverWait(driver, maxWaitTime).until(ExpectedConditions.visibilityOfElementLocated(by));
			element.toString();
		} catch (Error e) {
			e.printStackTrace();
		}
		return element;
	}

	public List<WebElement> waitAndFindAllElements() {

		return new WebDriverWait(driver, maxWaitTimeToFindElement).until(new ExpectedCondition<List<WebElement>>() {
			@Override
			public List<WebElement> apply(WebDriver driver) {
				List<WebElement> elements = driver.findElements(by);
				return elements.size() > 0 ? elements : null;
			}

			@Override
			public String toString() {
				return " - searched for element located by " + by;
			}
		});
	}

	public List<WebElement> waitAndFindAllElements(Long maxWaitTimeToFindElement) {

		return new WebDriverWait(driver, maxWaitTimeToFindElement).until(new ExpectedCondition<List<WebElement>>() {
			@Override
			public List<WebElement> apply(WebDriver driver) {
				List<WebElement> elements = driver.findElements(by);
				return elements.size() > 0 ? elements : null;
			}

			@Override
			public String toString() {
				return " - searched for element located by " + by;
			}
		});
	}

	public void waitUntilExport(By by) {
		new WebDriverWait(driver, 300).until(
				ExpectedConditions.elementToBeClickable(wait.until(ExpectedConditions.visibilityOfElementLocated(by))));
	}

	public WebElement findElement(By by) {
		element = driver.findElement(by);
		return element;
	}

	public List<WebElement> findElements(By by) {
		// return waitAndFindAllElements();
		elements = driver.findElements(by);
		return elements;

	}

	public void waitUntilVisibilityLocated(By by) {
		wait.until(
				ExpectedConditions.elementToBeClickable(wait.until(ExpectedConditions.visibilityOfElementLocated(by))));
	}

	public void waitUntilInVisibilityLocated(By by) {
		wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
		/*
		 * driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
		 * wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
		 * driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		 */ }

	public boolean waitUntilStatusChanged(By by) /* function to wait until ride status changes */
	{
		boolean visibility = true;
		try {
			new WebDriverWait(driver, 180).until(ExpectedConditions
					.elementToBeClickable(wait.until(ExpectedConditions.visibilityOfElementLocated(by))));
		} catch (Exception e) {
			visibility = false;
		}
		return visibility;
	}

	public boolean checkElementPresence(By by) {
		try {
			waitAndFindElement(by, 40);
			return true;
		} catch (Exception e) {
			return false;
		}
		/**
		 * driver.manage().timeouts().implicitlyWait(40, TimeUnit.SECONDS); if
		 * (driver.findElements(by).size() > 0) {
		 * driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS); return true;
		 * }
		 * 
		 * else { driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		 * return false; }
		 */
	}

	public boolean checkElementPresenceForVersion(By by) {
		try {
			waitECTimeoutWrapped(driver, by, 5);
			return true;
		} catch (Exception e) {
			return false;
		}
		/*
		 * driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS); if
		 * (driver.findElements(by).size() > 0) {
		 * driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS); return true;
		 * }
		 * 
		 * else { driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS); return
		 * false; }
		 */

	}

	public boolean checkElementVisibility(By by) {
		boolean visibility = true;

		try {
			waitAndFindElement(by);
		}

		catch (Exception e) {
			visibility = false;
		}

		return visibility;
	}

	public int checkElementCount(By by) {
		return driver.findElements(by).size();
	}

	public void navigateBack() {
		driver.navigate().back();
	}

	public void click() {
		element.click();
	}

	public void click(By by) {
		try {
			WebElement element = waitAndFindElement(by);
			element.click();
		} catch (Exception e) {
			WebElement element = waitAndFindElement(by);
			executor = (JavascriptExecutor) driver;
			executor.executeScript("arguments[0].click();", element);
		}
	}

	public void click_JS() {
		executor = (JavascriptExecutor) driver;
		executor.executeScript("arguments[0].click();", element);
	}

	public void actionsClick() {
		Actions action = new Actions(driver);
		action.moveToElement(element).click().build().perform();
	}

	public void clearField() {
		element.clear();
	}

	public void sendKeys(String text) {
		element.sendKeys(text);
	}

	public void sendKeysJS(String text) {
		executor = (JavascriptExecutor) driver;
		executor.executeScript("document.getElementsByName('mainContactEmail')[0].value='" + text + "';");
	}

	public String getElementText() {
		return element.getText();
	}

	public String getAttributeValue() {
		return element.getAttribute("value");
	}

	public String getTextOfSelectedOptionInDropdown(By by) {
		Select select = new Select(driver.findElement(by));
		WebElement option = select.getFirstSelectedOption();
		String selected_Item = option.getText();
		return selected_Item;
	}

	public double removeSpecialCharactersFromString(String costFromUiText) {
		return Double.valueOf(costFromUiText.replaceAll("[\\$|,|;|']", ""));
	}

	public String compareDouble(Double firstVal, double secondVal) {
		int compareCost = Double.compare(firstVal, secondVal);

		if (compareCost == 0) {
			return "Values Match";
		}

		else {
			return "Values Dont Match";
		}
	}

	public void selectValueFromDropdown(WebElement element,String dropdown_Value) {
		Select dropdown = new Select(element);
		dropdown.selectByVisibleText(dropdown_Value);
	}

	public void selectDropdownByIndex(int dropdown_Value) {
		Select dropdown = new Select(element);
		dropdown.selectByIndex(dropdown_Value);
	}

	public void waitUntilVisibilityOfAllElementsLocated(By by) {
		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(by));
	}

	public void scrollElementIntoView(WebElement element) {
		executor = (JavascriptExecutor) driver;
		executor.executeScript("arguments[0].scrollIntoView(true);", element);
	}

	public String getAttribute(String attribute) {
		return element.getAttribute(attribute);
	}

	public void acceptWindowAlert() throws IOException {
		Alert alert = driver.switchTo().alert();
		alert.accept();

	}

	public void switchToFrame(String name) throws IOException {
		driver.switchTo().frame(name);
	}

	public void switchToDefault() throws IOException {
		driver.switchTo().defaultContent();
	}

	public boolean isAlertPresent() {
		try {
			driver.switchTo().alert();
			return true;
		} catch (NoAlertPresentException e) {
			return false;
		}
	}

	public void removeDisabledAttribute(By by) {
		executor = (JavascriptExecutor) driver;
		element = driver.findElement(by);
		executor.executeScript("arguments[0].removeAttribute('disabled','disabled')", element);
	}

	public ArrayList<Float> returnListOfElements(By by) {
		ArrayList<Float> webelementList = new ArrayList<Float>();
		for (WebElement element : findElements(by)) {

			webelementList.add(Float.parseFloat(element.getText()));
		}

		return webelementList;
	}

	public void switchToDesiredWindow(int i) {
		ArrayList<String> windowList = new ArrayList<String>(driver.getWindowHandles());
		driver.switchTo().window(windowList.get(i));
	}

	public boolean checkElementPresenceForDisable(By by) {
		try {
			waitECTimeoutWrapped(driver, by, 5);
			return true;
		} catch (Exception e) {
			return false;
		}
		/*
		 * driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS); if
		 * (driver.findElements(by).size() > 0) {
		 * driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS); return true;
		 * }
		 * 
		 * else { driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS); return
		 * false; }
		 */
	}

	protected void waitECTimeoutWrapped(WebDriver driver, By by, int timeout) {
		driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
		waitAndFindElement(by, timeout);
		driver.manage().timeouts().implicitlyWait(40, TimeUnit.SECONDS);
	}
}
