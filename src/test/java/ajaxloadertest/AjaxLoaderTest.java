package ajaxloadertest;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.AssertJUnit;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.Reporter;

public class AjaxLoaderTest {
	WebDriver driver;

	@BeforeMethod
	public void setUp() {
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get("http://webdriveruniversity.com/index.html");
	}

	// Verify & Click Ajax Loader Link
	@Test(invocationCount = 5)
	public void verifyAjaxLoader() throws InterruptedException {
		Reporter.log("VERIFY AJAX LOADER TEST STARTS");
		WebElement eleAjaxLoader = driver.findElement(By.xpath("//h1[text()='AJAX LOADER']"));

		// Scroll to Element
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", eleAjaxLoader);

		// Verify
		boolean ajaxLoaderPresent = false;
		ajaxLoaderPresent = eleAjaxLoader.isDisplayed();
		AssertJUnit.assertTrue(ajaxLoaderPresent);

		// Click Ajax Loader
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
		driver.findElement(By.xpath("//a[@id='ajax-loader']")).click();

		// Switch to Other tab
		ArrayList<String> newTb = new ArrayList<String>(driver.getWindowHandles());
		driver.switchTo().window(newTb.get(1));

		// explicit wait - to wait for CLICK ME button
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
		WebElement btnClickMe = wait
				.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@id='button1']")));
		
		// Click CLICK ME
		btnClickMe.click();

		// Verify Pop-up Title
		Thread.sleep(2000);
		WebElement elePopupTitle = driver.findElement(By.xpath("//h4[text()='Well Done For Waiting....!!!']"));
		// Verify
		boolean blnPopUpTitleDisplay = false;
		blnPopUpTitleDisplay = elePopupTitle.isDisplayed();
		AssertJUnit.assertTrue(blnPopUpTitleDisplay);
		Thread.sleep(2000);

	}

	@AfterMethod
	public void ScreenshotCapture(ITestResult result) throws IOException {
		Reporter.setCurrentTestResult(result);
		String currentWorkingDir = System.getProperty("user.dir");
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		File img = new File(currentWorkingDir + "//test-output//screenshots//" + timeStamp + ".png");
		if (result.getStatus() == 1 || result.getStatus() == 2) {
			FileOutputStream screenshotStream = new FileOutputStream(img);
			screenshotStream.write(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES));
			screenshotStream.close();
			Reporter.log("<a href='" + img.getAbsolutePath() + "'><img src='" + img.getAbsolutePath()
					+ "' height='200' width='200'/> </a>");
		}
	}

	@AfterMethod
	public void tearDown() {
		driver.close();
		driver.quit();
	}
}
