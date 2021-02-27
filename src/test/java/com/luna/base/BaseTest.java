package com.luna.base;

import org.testng.Assert;
import org.testng.Reporter;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.luna.listeners.CustomListeners;
import com.luna.utilities.ExcelReader;
import com.luna.utilities.TestUtil;
import io.github.bonigarcia.wdm.WebDriverManager;

public class BaseTest {

	public static WebDriver driver;
	public static Properties config = new Properties();
	public static Properties OR = new Properties();
	public static FileInputStream fis;
	public static Logger log = Logger.getLogger(BaseTest.class.getName());
	public static ExcelReader excel = new ExcelReader(
			System.getProperty("user.dir") + "/src/test/resources/excel/testData.xlsx");
	public static WebDriverWait wait;
	static WebElement dropdown;
	public static String browser;

	@BeforeSuite
	public void setUp() throws IOException {

		PropertyConfigurator.configure("./src/test/resources/properties/log4j.properties");

		if (driver == null) {
			fis = new FileInputStream(
					System.getProperty("user.dir") + "/src/test/resources/properties/Config.properties");
			config.load(fis);
			log.info("Config file loaded");
			fis = new FileInputStream(
					System.getProperty("user.dir") + "/src/test/resources/properties/ObjectRepo.properties");
			OR.load(fis);
			log.info("Object repository file loaded");

			//setting browser parameterized 
			if (System.getenv("browser") != null && !System.getenv("browser").isEmpty()) {

				browser = System.getenv("browser");
			} else {

				browser = config.getProperty("browser");
			}
			config.setProperty("browser", browser);

			if (config.getProperty("browser").equals("firefox")) {
				WebDriverManager.firefoxdriver().setup();
				driver = new FirefoxDriver();
				log.debug("FF launched");
			} else if (config.getProperty("browser").equals("chrome")) {
				WebDriverManager.chromedriver().setup();
				driver = new ChromeDriver();
				log.debug("Chrome launched");
			} else if (config.getProperty("browser").equals("safari")) {
				driver = new SafariDriver();
				log.debug("Safari launched");
			}
			driver.get(config.getProperty("testsiteurl"));
			log.debug("Navigated to URL: " + config.getProperty("testsiteurl"));
			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(Integer.parseInt(config.getProperty("implicitwait")),
					TimeUnit.SECONDS);
			wait = new WebDriverWait(driver, 5);
		}

	}

	public void click(String locator) {

		if (locator.endsWith("_CSS")) {

			driver.findElement(By.cssSelector(OR.getProperty(locator))).click();

		} else if (locator.endsWith("_XPATH")) {

			driver.findElement(By.xpath(OR.getProperty(locator))).click();

		} else if (locator.endsWith("_ID")) {

			driver.findElement(By.id(OR.getProperty(locator))).click();

		}

		CustomListeners.testReport.get().log(Status.INFO, "Clicked on Element " + locator);

	}

	public void type(String locator, String value) {

		if (locator.endsWith("_CSS")) {

			driver.findElement(By.cssSelector(OR.getProperty(locator))).sendKeys(value);

		} else if (locator.endsWith("_XPATH")) {

			driver.findElement(By.xpath(OR.getProperty(locator))).sendKeys(value);

		} else if (locator.endsWith("_ID")) {

			driver.findElement(By.id(OR.getProperty(locator))).sendKeys(value);

		}

		CustomListeners.testReport.get().log(Status.INFO, "Typing " + value + " in Element " + locator);

	}

	public void select(String locator, String value) {

		if (locator.endsWith("_CSS")) {

			dropdown = driver.findElement(By.cssSelector(OR.getProperty(locator)));

		} else if (locator.endsWith("_XPATH")) {

			dropdown = driver.findElement(By.xpath(OR.getProperty(locator)));

		} else if (locator.endsWith("_ID")) {

			dropdown = driver.findElement(By.id(OR.getProperty(locator)));

		}

		Select selectVal = new Select(dropdown);
		selectVal.selectByVisibleText(value);
		CustomListeners.testReport.get().log(Status.INFO, "Selecting from dropdown: " + locator + "value as: " + value);

	}

	public boolean isElementPresent(By by) {
		try {

			driver.findElement(by);
			return true;

		} catch (NoSuchElementException e) {

			return false;

		}
	}

	public void verifyEquals(String actual, String expected) throws IOException {

		try {

			Assert.assertEquals(actual, expected);

		} catch (Throwable t) {
			TestUtil.captureScreenshot();

			// reportng
			Reporter.log("<br>" + "Verification failure : " + t.getMessage() + "<br>");
			Reporter.log("<a target=\"_blank\" href=\"" + TestUtil.screenshotName + "\"><img src=\""
					+ TestUtil.screenshotName + "\" height=200 width=200></img></a>");
			Reporter.log("<br>");

			// extentreport
			CustomListeners.testReport.get()
					.fail("<details>" + "<summary>" + "<b>" + "<font color=" + "red>"
							+ "Verification failed with exception : Click to see" + "</font>" + "</b >" + "</summary>"
							+ t.getMessage().replaceAll(",", "<br>") + "</details>" + " \n");
			CustomListeners.testReport.get().fail(
					"<b>" + "<font color=" + "red>" + "Screenshot of failure" + "</font>" + "</b>",
					MediaEntityBuilder.createScreenCaptureFromPath(TestUtil.screenshotName).build());
			Markup m = MarkupHelper.createLabel("Verification failure", ExtentColor.RED);
			CustomListeners.testReport.get().log(Status.FAIL, m);
		}

	}

	@AfterSuite
	public void tearDown() {

		if (driver != null) {
			driver.quit();
		}

	}

}
