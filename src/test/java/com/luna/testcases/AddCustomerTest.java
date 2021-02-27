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

public class AddCustomerTest extends BaseTest {

	@Test(dataProviderClass = TestUtil.class, dataProvider = "dp")
	public void addCustomerTest(Hashtable<String, String> data)
			throws InterruptedException {

		if (!data.get("runMode").equalsIgnoreCase("Y")) {

			CustomListeners.testReport.get().log(Status.SKIP,
					"Skipping test data:" + data.get("firstName") + ":" + data.get("lastName") + ":" + data.get("postCode") + " as Run Mode set as No");

			throw new SkipException("Skipping test data as Run Mode set as No");

		}

		log.info("clicking on add customer button");
		click("acb_CSS");
		type("fname_CSS", data.get("firstName"));
		type("lname_CSS", data.get("lastName"));
		type("pcode_CSS", data.get("postCode"));
		click("addCustBtn_CSS");

		Alert alert = wait.until(ExpectedConditions.alertIsPresent());
		Assert.assertTrue(alert.getText().contains(data.get("alertText")));
		alert.accept();
	}

}
