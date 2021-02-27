package com.luna.utilities;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Hashtable;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.annotations.DataProvider;

import com.luna.base.BaseTest;

public class TestUtil extends BaseTest {

	public static String srcPath;
	public static String screenshotName;

	public static void captureScreenshot() throws IOException {
		Date d = new Date();

		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		screenshotName = "Screenshot_"+d.toString().replace(" ", "_").replace(":", "_") + "error.jpg";
		FileUtils.copyFile(scrFile,
				new File(System.getProperty("user.dir") + "/target/surefire-reports/html/" + screenshotName));
	}
	
	@DataProvider(name="dp")
	public Object[][] getData(Method m) {

		String sheetname = m.getName();
		int rows = excel.getRowCount(sheetname);
		int cols = excel.getColumnCount(sheetname);
		Object[][] data = new Object[rows - 1][1];
		Hashtable<String, String> table = null;
		
		for (int i = 2; i <= rows; i++) {
			table = new Hashtable<String, String>();
			for (int j = 0; j < cols; j++) {
				table.put(excel.getCellData(sheetname, j, 1), excel.getCellData(sheetname, j, i));
				data[i - 2][0] = table;
			}
		}
		return data;

	}
	
	public static boolean isTestRunnable(String testName, ExcelReader excel) {
		
		String sheetName = "test_suite";
		int rows = excel.getRowCount(sheetName);
		
		for(int rNum=2;rNum<=rows;rNum++) {
			
			String testcase = excel.getCellData(sheetName, "TCID", rNum);
			if(testcase.equalsIgnoreCase(testName)) {
				
				String runmode = excel.getCellData(sheetName, "Runmode", rNum);
				if(runmode.equalsIgnoreCase("Y"))
					return true;
				else
					return false;

			}
		}
		
		return false;
	}
}
