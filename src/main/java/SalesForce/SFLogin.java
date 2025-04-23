package SalesForce;


import com.aventstack.chaintest.plugins.ChainTestListener;
import Utils.BaseTest;
import Utils.ConfigReader;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.time.Duration;

public class SFLogin extends BaseTest {

    @Test
    public void LoginSF(){

        ChainTestListener.log("Navigating to Salesforce");
        driver.get("https://www.salesforce.com");

        //This Element is inside 2 nested shadow DOM.
        SearchContext shadow1 = driver.findElement(By.cssSelector("hgf-c360nav[locale='in']")).getShadowRoot();
        SearchContext shadow2 = shadow1.findElement(By.cssSelector("hgf-c360login[aria-haspopup='true']")).getShadowRoot();
        shadow2.findElement(By.cssSelector(" hgf-popover:nth-child(1) > hgf-button:nth-child(1) > span:nth-child(2)")).click();

        //This Element is inside 2 nested shadow DOM.
        SearchContext shadow3 = driver.findElement(By.cssSelector("hgf-c360nav[locale='in']")).getShadowRoot();
        SearchContext shadow4 = shadow3.findElement(By.cssSelector("hgf-c360login[aria-haspopup='true']")).getShadowRoot();
        shadow4.findElement(By.cssSelector("a[href='https://login.salesforce.com/?locale=in']")).click();
        embedScreenshot();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        ChainTestListener.log("🔗 Entering Username");
        WebElement UserName = driver.findElement(By.xpath("//div[@id='username_container']/input[@id='username']"));
        UserName.click();
        UserName.sendKeys(ConfigReader.getProperty("username"));
        System.out.println("Username entered securely!");
        ChainTestListener.log("✅ Entered Username");

        ChainTestListener.log("🔗 Entering SF Password");
        WebElement Password = driver.findElement(By.xpath("//input[@id='password']"));
        Password.sendKeys(ConfigReader.getProperty("password"));
        System.out.println("Password entered securely!");
        ChainTestListener.log("✅ Entered SF password");
        Password.click();
        embedScreenshot();

        WebElement LoginButton = driver.findElement(By.xpath("//input[@id='Login']"));
        LoginButton.click();
        ChainTestListener.log("✅ Logged in Successfully");
        new WebDriverWait(driver, Duration.ofSeconds(8)).until((ExpectedCondition<Boolean>) wd ->
                ((JavascriptExecutor) wd).executeScript("return document.readyState").equals("complete"));
        embedScreenshot();
    }

    public void embedScreenshot() {
        byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        ChainTestListener.embed(screenshot, "image/png");
    }
}
