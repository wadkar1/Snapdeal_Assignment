
package com.sf.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sf.utility.ConfigReader;
import com.sf.utility.ExcelReader;

public class TestBase {
	public static ThreadLocal<WebDriver> threadLocalDriver = new ThreadLocal<WebDriver>();
	public WebDriver driver;
	public Properties config;
	public FileInputStream propertiesFile;

	public String username;
	public String password;
	public String URL;

	public static ExtentSparkReporter htmlReporter;
	public static ExtentReports extent;
	public ExtentTest test;
	private long minWaitTimeToFindElement;

	String report_Location = "Reports/LatestReport";
	String automation_Report = report_Location + "/AutomationReport.html";
	String emailable_Content = "target/surefire-reports/EmailableData.html";
	private String archiveDir = "Reports/Archive/";
	String email_Content = "target/surefire-reports/EmailableReport.html";


	@BeforeSuite(alwaysRun = true)
	public void iniExtentReport() throws IOException {
		File file = new File(report_Location);
		File Screenshots = new File(report_Location + "/Screenshots");
		if (file.exists()) {
			FileUtils.cleanDirectory(file);
			file.mkdir();
			if (!Screenshots.exists()) {
				Screenshots.mkdir();
			}
		}
		File report = new File(automation_Report);
		System.out.println("Automation report is generated at location: " + file.getAbsolutePath());

		htmlReporter = new ExtentSparkReporter(report);
		extent = new ExtentReports();
		extent.attachReporter(htmlReporter);

		extent.setSystemInfo("Host Name", "SourceFuse-Sample");
		extent.setSystemInfo("Broswer Name", "Chrome");
		extent.setSystemInfo("Environment", System.getProperty("environment").toUpperCase());
		//extent.setSystemInfo("User Name", username);
		//extent.setSystemInfo("Application URL", URL);

		//htmlReporter.config().setChartVisibilityOnOpen(true);
		htmlReporter.config().setDocumentTitle("Automation Test Report ");
		htmlReporter.config().setReportName("Automation Test Report");
		//htmlReporter.config().setTestViewChartLocation(ChartLocation.TOP);
		htmlReporter.config().setTheme(Theme.DARK);

	}

	@AfterSuite(alwaysRun = true)
	public void closeSuite() throws Exception {
		extent.flush();
		Path path = Paths.get(archiveDir);
		if (!Files.exists(path)) {
			Files.createDirectories(path);
		}
		File dest = new File(new File(archiveDir).getAbsolutePath() + "/" + "TestReport_"
				+ new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()) + ".zip");
		zipFolder(new File(report_Location), dest);
	}

	public void createdEmailableReport() {
		try {
			File emailReport = new File("target/surefire-reports/emailable-report2.html");
			System.out.println(emailReport.getAbsolutePath());
			String file_content = FileUtils.readFileToString(emailReport, StandardCharsets.UTF_8);
			String summaryContent = file_content.split("<h2>")[0];
		    //.replace("#3F3", "#cee8ce").replace("#0A0", "#cee8ce").replace("#F33","#ffadad").replace("#D00", "#ffadad");
			File f = new File(email_Content);
			if (f.exists()) {
				f.delete();
			}
			FileWriter fw = new FileWriter(email_Content);

			for (int i = 0; i < summaryContent.length(); i++)
				fw.write(summaryContent.charAt(i));
			    fw.close();
		} catch (Exception e) {
			System.out.println("Emailable report is not present");
		}
	}

	private String getLatestFilefromDir() {
		File dir = new File("Reports/Archive");
		File[] files = dir.listFiles();
		if (files == null || files.length == 0) {
			return null;
		}

		File lastModifiedFile = files[0];
		for (int i = 1; i < files.length; i++) {
			if (lastModifiedFile.lastModified() < files[i].lastModified()) {
				lastModifiedFile = files[i];
			}
		}
		return lastModifiedFile.getName();
	}

	@AfterMethod
	public void tearDown(ITestResult result) throws Exception {

		if (result.getStatus() == ITestResult.FAILURE) {
			test.log(Status.FAIL, MarkupHelper.createLabel(result.getName() + " Test case FAILED due to below issues:",
					ExtentColor.RED));
			test.fail(result.getThrowable().getMessage());
			try {
				String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
				TakesScreenshot ts = (TakesScreenshot) driver;
				File source = ts.getScreenshotAs(OutputType.FILE);
				String image_name = result.getName() + "_" + timeStamp + "_fail" + ".png";
				String filename = "Screenshots/" + image_name;
				File destinationScreenShot = new File(report_Location + "/" + filename);
				FileUtils.copyFile(source, destinationScreenShot);
				System.out.println("Screenshot taken");
				test.addScreenCaptureFromPath(filename);
			} catch (Exception e) {
				System.out.println("Exception while taking screenshot " + e.getMessage());
			}
		} else if (result.getStatus() == ITestResult.SKIP) {
			test.log(Status.SKIP, MarkupHelper.createLabel(result.getName() + "Test case SKIPED due to below issue:",
					ExtentColor.YELLOW));
			test.skip(result.getThrowable().getMessage());
			try {
				String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
				TakesScreenshot ts = (TakesScreenshot) driver;
				File source = ts.getScreenshotAs(OutputType.FILE);
				String image_name = result.getName() + "_" + timeStamp + "_skip" + ".png";
				String filename = "Screenshots/" + image_name;
				File destinationScreenShot = new File(report_Location + "/" + filename);
				FileUtils.copyFile(source, destinationScreenShot);
				System.out.println("Screenshot taken");
				test.addScreenCaptureFromPath(filename);
			} catch (Exception e) {
				System.out.println("Exception while taking screenshot " + e.getMessage());
			}
		}
		logOut();
		result.getTestContext().getSkippedTests().removeResult(result.getMethod());

	}

	@BeforeClass
	public void setUp() throws Exception {
		config = new Properties();
		propertiesFile = new FileInputStream("config/envconfig.properties");
		config.load(propertiesFile);
		executionBrowser();
		executionEnviornmentDetails();
		loader();

	}

	public void executionBrowser() throws Exception {
		minWaitTimeToFindElement = Long.valueOf(
				config.getProperty("minWaitTimeToFindElement") != null ? config.getProperty("minWaitTimeToFindElement")
						: config.getProperty("minWaitTimeToFindElement"));

		if (config.getProperty("browser.name").equalsIgnoreCase("Chrome")) {
			ChromeOptions options = new ChromeOptions();
			options.addArguments("enable-automation");
			if (ConfigReader.getLocalValue("headlessExecution").equalsIgnoreCase("yes")) {
				options.addArguments("--headless");
			}
			options.addArguments("--no-sandbox");
			options.addArguments("--disable-dev-shm-usage");

			options.addArguments("--window-size=1920,1080");
			options.addArguments("--disable-extensions");
			options.addArguments("--dns-prefetch-disable");
			options.addArguments("--disable-gpu");
			options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
			options.addArguments("--disable-features=VizDisplayCompositor");

			Map<String, Object> prefs = new HashMap<String, Object>();

			prefs.put("download.default_directory", System.getProperty("user.dir") + File.separator + "externalFiles"
					+ File.separator + "downloadFiles");
			System.out.println("Download Directory: " + System.getProperty("user.dir"));
			options.setExperimentalOption("prefs", prefs);
			String OS = System.getProperty("os.name").toLowerCase();
			// Check OS and set the driver properties
			if (OS.indexOf("win") >= 0) {
				System.setProperty("webdriver.chrome.driver", "src/test/resources/Executable/chromedriver.exe");
			} else {
				System.setProperty("webdriver.chrome.driver", "src/test/resources/Executable/chromedriver");

			}
			ChromeDriverService driverService = ChromeDriverService.createDefaultService();
			threadLocalDriver.set(new ChromeDriver(driverService, options));
			driver = threadLocalDriver.get();
			if (ConfigReader.getLocalValue("headlessExecution").equalsIgnoreCase("yes")) {
				headlessDownloadPermission(driverService);
			}
			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(minWaitTimeToFindElement, TimeUnit.SECONDS);
			driver.manage().deleteAllCookies();
		}

		else if (config.getProperty("browser.name").equalsIgnoreCase("FireFox")) {
			threadLocalDriver.set(new FirefoxDriver());
			driver = threadLocalDriver.get();
			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(minWaitTimeToFindElement, TimeUnit.SECONDS);
		}

		else if (config.getProperty("browser.name").equalsIgnoreCase("IE")) {
			System.setProperty("webdriver.ie.driver", "src\\test\\resources\\Executable\\IEDriverServer.exe");
			threadLocalDriver.set(new InternetExplorerDriver());
			driver = threadLocalDriver.get();
			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(minWaitTimeToFindElement, TimeUnit.SECONDS);
		}
	}

	private void headlessDownloadPermission(ChromeDriverService driverService) throws Exception {
		Map<String, Object> commandParams = new HashMap<String, Object>();
		commandParams.put("cmd", "Page.setDownloadBehavior");

		Map<String, String> params = new HashMap<String, String>();
		params.put("behavior", "allow");
		params.put("downloadPath",
				System.getProperty("user.dir") + File.separator + "externalFiles" + File.separator + "downloadFiles");
		commandParams.put("params", params);

		ObjectMapper objectMapper = new ObjectMapper();
		HttpClient httpClient = HttpClientBuilder.create().build();

		String command = objectMapper.writeValueAsString(commandParams);

		String u = driverService.getUrl().toString() + "/session/"
				+ ((RemoteWebDriver) threadLocalDriver.get()).getSessionId() + "/chromium/send_command";

		HttpPost request = new HttpPost(u);
		request.addHeader("content-type", "application/json");
		request.setEntity(new StringEntity(command));
		httpClient.execute(request);
	}

	public void executionEnviornmentDetails() throws IOException {
		String workbookLocation = config.getProperty("relay.workbook");
		String sheetName = config.getProperty("relay.sheetName");
		String serverInfoRequired = System.getProperty("environment");
		ExcelReader reader = new ExcelReader();
		LinkedHashMap<String, String> envDetails = reader.getTestEnvironmentFromExcel(workbookLocation, sheetName,
				serverInfoRequired);

		URL = envDetails.get("URL");
		username = envDetails.get("Username");
		password = envDetails.get("Password");
	}

	public void loader() throws IOException {

	}

	@AfterClass
	public void tearDown() throws InterruptedException {
		closeSession();
	}

	public void logOut() throws InterruptedException {

	}

	public void closeSession() throws InterruptedException {
//		driver.close();
	}

	public void zipFolder(File srcFolder, File destZipFile) throws Exception {
		try (FileOutputStream fileWriter = new FileOutputStream(destZipFile);
				ZipOutputStream zip = new ZipOutputStream(fileWriter)) {

			addFolderToZip(srcFolder, srcFolder, zip);
		}
	}

	private void addFileToZip(File rootPath, File srcFile, ZipOutputStream zip) throws Exception {

		if (srcFile.isDirectory()) {
			addFolderToZip(rootPath, srcFile, zip);
		} else {
			byte[] buf = new byte[1024];
			int len;
			try (FileInputStream in = new FileInputStream(srcFile)) {
				String name = srcFile.getPath();
				name = name.replace(rootPath.getPath(), "");
				name = name.substring(1);
				zip.putNextEntry(new ZipEntry(name));
				while ((len = in.read(buf)) > 0) {
					zip.write(buf, 0, len);
				}
			}
		}
	}

	private void addFolderToZip(File rootPath, File srcFolder, ZipOutputStream zip) throws Exception {
		for (File fileName : srcFolder.listFiles()) {
			addFileToZip(rootPath, fileName, zip);
		}
	}

}