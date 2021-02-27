package com.luna.testcases;

import java.util.Hashtable;

import org.openqa.selenium.Alert;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;
import com.luna.base.BaseTest;
import com.luna.listeners.CustomListeners;
import com.luna.utilities.TestUtil;

public class OpenAccountTest extends BaseTest {

	@Test(dataProviderClass = TestUtil.class,dataProvider = "dp")
	public void openAccountTest(Hashtable<String, String> data)
			throws InterruptedException {
		
		if (!data.get("runMode").equalsIgnoreCase("Y")) {

			CustomListeners.testReport.get().log(Status.SKIP,
					"Skipping test data:" + data.get("firstName") + ":" + data.get("lastName") + ":" + data.get("postCode") + " as Run Mode set as No");

			throw new SkipException("Skipping test data as Run Mode set as No");

		}
		
		click("openaccount_CSS");
		select("customer_ID", data.get("customer"));
		select("currency_ID", data.get("currency"));
		click("processBtn_CSS");
		
		Alert alert = wait.until(ExpectedConditions.alertIsPresent());
		alert.accept();
		
	}

	

}
