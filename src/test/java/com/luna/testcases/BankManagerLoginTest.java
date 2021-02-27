package com.luna.testcases;

import org.testng.Assert;
import org.testng.annotations.Test;
import java.io.IOException;
import org.openqa.selenium.By;
import com.luna.base.BaseTest;

public class BankManagerLoginTest extends BaseTest {

	@Test
	public void bankManagerLoginTest() throws InterruptedException, IOException {

		log.debug("Running Login Test");
		
		// can use softassert
		// SoftAssert softassert = new SoftAssert();
		// softassert.assertEquals("abc", "xyz","Not equal");

		verifyEquals("Luna", "Halo");
		click("bml_CSS");
		// driver.findElement(By.cssSelector(OR.getProperty("bml"))).click();
		Thread.sleep(5000);
		Assert.assertTrue(isElementPresent(By.cssSelector(OR.getProperty("acb_CSS"))), "LOGIN NOT SUCCESSFUL!!!");
		log.debug("Login successfully executed! ");
		// Assert.fail("assertion forced fail testing1!");

	}

}
