package SalesForce;


import com.aventstack.chaintest.plugins.ChainTestListener;
import Utils.BaseTest;
import Utils.ConfigReader;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;

import java.sql.Driver;
import java.time.Duration;

public class SFLogin extends BaseTest {

    @Test
    public void LoginSF(){

        ChainTestListener.log("Navigating to Salesforce");
        driver.get("https://www.salesforce.com");
        clickLoginInShadowDom();
        embedScreenshot();

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
        // Explicit wait to load the page
        new WebDriverWait(driver, Duration.ofSeconds(8)).until((ExpectedCondition<Boolean>) wd ->
                ((JavascriptExecutor) wd).executeScript("return document.readyState").equals("complete"));
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            WebElement appLauncher = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//div[@class='slds-icon-waffle']")));

            Assert.assertTrue(appLauncher.isDisplayed(), "❌ FAIL: App Launcher is not visible after login.");
            System.out.println("✅ PASS: Logged in Successfully");
            ChainTestListener.log("✅ PASS: Logged in Successfully");
            embedScreenshot();
        } catch (TimeoutException e) {
            System.out.println("❌ FAIL: App Launcher not found within the wait time.");
            ChainTestListener.log("❌ FAIL: App Launcher not found within the wait time.");
            embedScreenshot();
            throw new AssertionError("App Launcher was not found after login", e);
        }
    }

    @Test
    public void SalesConsole() {
        ChainTestListener.log("🔗 Navigating to Sales Console");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        // Click App Launcher
        WebElement appLauncher = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@title='App Launcher']")));
        appLauncher.click();

        // Wait for View All Applications to appear and be clickable
        WebElement viewAll = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@aria-label='View All Applications']")));
        viewAll.click();

        // Wait for Sales Console tile to be visible
        WebElement salesConsole = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//lightning-formatted-rich-text//span/p[text()='Sales Console']")));
        salesConsole.click();


        // Verify Sales Console loaded
        boolean actualTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[@data-aura-class='navexWorkspaceManager']"))).isDisplayed();
        AssertJUnit.assertTrue(actualTitle);

        new WebDriverWait(driver, Duration.ofSeconds(15)).until(
                webDriver -> ((JavascriptExecutor) webDriver)
                        .executeScript("return document.readyState").equals("complete"));
        embedScreenshot();
        ChainTestListener.log("✅ Navigated to Sales Console");

        // Interact with navigation dropdown
        WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@title='Show Navigation Menu']")));
        dropdown.click();
    }

    private void clickLoginInShadowDom() {
        SearchContext shadow1 = driver.findElement(By.cssSelector("hgf-c360nav[locale='in']")).getShadowRoot();
        SearchContext shadow2 = shadow1.findElement(By.cssSelector("hgf-c360login[aria-haspopup='true']")).getShadowRoot();
        shadow2.findElement(By.cssSelector(" hgf-popover:nth-child(1) > hgf-button:nth-child(1) > span:nth-child(2)")).click();
        shadow2.findElement(By.cssSelector("a[href='https://login.salesforce.com/?locale=in']")).click();
    }
}
