package com.luna.listeners;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.SkipException;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.luna.base.BaseTest;
import com.luna.utilities.ExtentManager;
import com.luna.utilities.MonitoringMail;
import com.luna.utilities.TestConfig;
import com.luna.utilities.TestUtil;

public class CustomListeners extends BaseTest implements ITestListener, ISuiteListener {
	
	//public static Date d = new Date();
	//public static String fileName = "Extent_" + d.toString().replace(":", "_").replace(" ", "_") + ".html";
	
	public String messageBody;
	public static String fileName = "Extent.html";
	public static ExtentReports extent = ExtentManager
			.createInstance(System.getProperty("user.dir") + "/target/surefire-reports/html/" + fileName);
	public static ThreadLocal<ExtentTest> testReport = new ThreadLocal<ExtentTest>();

	public void onTestStart(ITestResult result) {

		ExtentTest test = extent.createTest(
				result.getTestClass().getName() + "       @TestCase : " + result.getMethod().getMethodName());
		testReport.set(test);

		if (!TestUtil.isTestRunnable(result.getMethod().getMethodName(), excel)) {
			
			testReport.get().log(Status.SKIP, "Skipping the test case " + result.getMethod().getMethodName().toUpperCase() + " as the Run Mode is null!" );
			throw new SkipException(
					"Skipping the test case " + result.getMethod().getMethodName().toUpperCase() + "as the Run Mode is null! ");
			
		}
	}

	public void onTestSuccess(ITestResult result) {

		String methodName = result.getMethod().getMethodName();
		String logText = "<b>" + "TEST CASE:- " + methodName.toUpperCase() + " PASSED" + "</b>";
		Markup m = MarkupHelper.createLabel(logText, ExtentColor.GREEN);
		testReport.get().pass(m);
	}

	public void onTestFailure(ITestResult result) {

		// extend reports
		String failureLogg = "TEST CASE FAILED";
		String excepionMessage = Arrays.toString(result.getThrowable().getStackTrace());
		testReport.get()
				.fail("<details>" + "<summary>" + "<b>" + "<font color=" + "red>" + "Exception Occured:Click to see"
						+ "</font>" + "</b >" + "</summary>" + excepionMessage.replaceAll(",", "<br>") + "</details>"
						+ " \n");

		// reportng report
		System.setProperty("org.uncommons.reportng.escape-output", "false");
		Reporter.log("Click to view screenshot ");
		Reporter.log("<br>");
		try {
			TestUtil.captureScreenshot();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Reporter.log("<a target=\"_blank\" href=\"" + TestUtil.screenshotName + "\">Screenshot</a>");
		Reporter.log("<br>");
		Reporter.log("<br>");
		Reporter.log("<a target=\"_blank\" href=\"" + TestUtil.screenshotName + "\"><img src=\""
				+ TestUtil.screenshotName + "\" height=200 width=200></img></a>");

		// extent reports
		testReport.get().fail("<b>" + "<font color=" + "red>" + "Screenshot of failure" + "</font>" + "</b>",
				MediaEntityBuilder.createScreenCaptureFromPath(TestUtil.screenshotName).build());
		Markup m = MarkupHelper.createLabel(failureLogg, ExtentColor.RED);
		testReport.get().log(Status.FAIL, m);

	}

	public void onTestSkipped(ITestResult result) {
		String methodName = result.getMethod().getMethodName();
		String logText = "<b>" + "Test Case:- " + methodName + " Skipped" + "</b>";
		Markup m = MarkupHelper.createLabel(logText, ExtentColor.AMBER);
		testReport.get().skip(m);
	}

	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		// TODO Auto-generated method stub

	}

	public void onStart(ITestContext context) {
		// TODO Auto-generated method stub

	}

	public void onFinish(ITestContext context) {
		if (extent != null) {

			extent.flush();
		}
	}

	public void onStart(ISuite suite) {
		// TODO Auto-generated method stub
		
	}

	public void onFinish(ISuite suite) {

		MonitoringMail mail = new MonitoringMail();
		
		try {
			messageBody = "http://"+InetAddress.getLocalHost().getHostAddress()+":8383/job/DataDrivenLiveProject/Extent_20Reports/";
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			mail.sendMail(TestConfig.server, TestConfig.from, TestConfig.to, TestConfig.subject, messageBody);
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
