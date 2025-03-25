package com.utilities;

import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.locators.DBLocator;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.events.WebDriverListener;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;



public class ReusableMethods {
    private static final Logger logger = LogManager.getLogger(ReusableMethods.class);
    private static SoftAssert softAssert = new SoftAssert();

    public static void naviagteToUrl(String url) {
        try {
            if (DriverFactory.getDriver() == null) {
                throw new Exception("Driver is not initialized");
            }
            DriverFactory.getDriver().manage().window().maximize();
            DriverFactory.getDriver().manage().deleteAllCookies();
            DriverFactory.getDriver().get(url);

        } catch (Throwable ex) {
            Assert.fail("Navigation to URL failed: " + url, ex);
        }
    }


    public static WebElement findElement(By primaryLocator) {
        try {
            WebDriverWait wait = new WebDriverWait(DriverFactory.getDriver(), Duration.ofSeconds(5));
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(primaryLocator));
            logger.info("✅ Found element using primary locator: {}", primaryLocator);
            return element;
        } catch (Exception e) {
            logger.error("❌ Element not found using primary locator within timeout: {}", primaryLocator);
            return null;
        }
    }

    public static WebElement findElement(By primaryLocator, List<By> secondaryLocators) {
        WebElement element = null;
        try {
            WebDriverWait wait = new WebDriverWait(DriverFactory.getDriver(), Duration.ofSeconds(5));
            element = wait.until(ExpectedConditions.visibilityOfElementLocated(primaryLocator));
            logger.info("✅ Found element using primary locator: {}", primaryLocator);
            return element;
        } catch (Exception e) {
            logger.warn("⚠️ Element not found using primary locator within timeout: {}", primaryLocator);
            element = null;
        }

        // Try to find the element using secondary locators
        for (int i = 0; i < secondaryLocators.size(); i++) {
            try {
                element = DriverFactory.getDriver().findElement(secondaryLocators.get(i));
                if (element.isDisplayed()) {
                    logger.info("🔍 Found element using secondary locator: {}", secondaryLocators.get(i));
                    return element;
                }
            } catch (NoSuchElementException | org.openqa.selenium.StaleElementReferenceException ignored) {
            }
        }

        logger.error("❌ Element not found using primary or secondary locators.");
        return null;
    }

    public static void click(By primarySearch, List<By> secondrySearch, String message) {
        try {
            WebElement element = findElement(primarySearch, secondrySearch);
            if (element == null) {  // Explicitly handle null case
                throw new NoSuchElementException("Element not found: "+" Primary Locator " + primarySearch.toString() +"Secondary locator" +secondrySearch.toString() );
            }

            ExtentUtility.attatchPassMessageToReport(message);
            element.click();
        }catch (Exception e){

            ExtentUtility.logger.get().log(Status.FAIL,
                    "AssertionError: " + e.getMessage());
            //ExtentUtility.attachScreenshotOnFailure("Click not working");
            logger.error("❌ Click action failed: Unable to click on the element.");
            throw e;
        }
    }

    public static void sendKeys(By primarySearch, List<By> secondrySearch, String item, String message) {
        try {
            WebElement element = findElement(primarySearch, secondrySearch);
            if (element == null) {  // Explicitly handle null case
                throw new NoSuchElementException("Element not found: "+" Primary Locator " + primarySearch.toString() +"Secondary locator" +secondrySearch.toString() );
            }

            ExtentUtility.attatchPassMessageToReport(message);
            element.sendKeys(item);
        }catch (Exception e){

            ExtentUtility.logger.get().log(Status.FAIL,
                    "SendKeys not working: " + e.getMessage());
           // ExtentUtility.attachScreenshotOnFailure("SendKeys not working");
            logger.error("❌ sendKeys action failed: Unable to input text.");

        }
    }

    public static void verifyText(String actual, String expected) {
        if (actual.contains(expected)) {
            ExtentUtility.attatchPassMessageToReport("Both UI and Expected values are matched - " +
                    "UI Value: " + actual + " | Expected Value: " + expected);
        } else {
            try {
                Assert.assertEquals(actual, expected, "Expected value and UI value are not matching");
            } catch (AssertionError e) {
                logger.error("❌ Mismatch detected: UI value differs from the expected value.");
                ExtentUtility.logger.get().log(Status.FAIL,
                        "AssertionError: " + e.getMessage() +
                                " | UI Value: " + actual + ", Expected Value: " + expected);
                throw e;

            }
        }
    }

    public static void assertAll() {
        softAssert.assertAll(); // Fails test at the end
    }

     /*   String screenshotPath = ScreenshotUtil.takeScreenshot();
                ExtentUtility.attachScreenshotOnFailure("Both UI and Expected values are not matched for - " +
                        "UI Value: " + actual + "______________" + "Expected Value: " + expected, screenshotPath);
                ExtentUtility.logger.get().log(Status.FAIL,
                        "AssertionError: " + e.getMessage() +
                                " | UI Value: " + actual + ", Expected Value: " + expected);
                System.out.println(e.getClass().getSimpleName());
                String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
                System.out.println("Exception in Method: " + methodName);
                jiraOperation(screenshotPath,e.getClass().getSimpleName() +": Both UI and Expected values are not matched");*/

    public static void jiraOperation(String screenshotPath, String message) {
        String issueSummary = "Test Case Failed: " + message;

        if (message.contains("NoSuchElementException") || message.contains("StaleElementReferenceException") ) {
            logger.info("⚠️ Possible Locator Issue");
        }
        else if (message.contains("500") || message.contains("404") || message.contains("TimeoutException") || message.contains("AssertionError")) {
            String issueId = JiraUtility.createJiraIssue(issueSummary, "Bug");
            JiraUtility.attachFileToJiraIssue(issueId, screenshotPath);

            logger.error("❌ Jira Bug Logged: " + issueId);
        }


    }

    public static void click(By primaryLocator, String message) {
        try {
            WebElement element = findElement(primaryLocator);
            if (element == null) {  // Explicitly handle null case
                throw new NoSuchElementException("Element not found: " + primaryLocator.toString());
            }
            ExtentUtility.attatchPassMessageToReport(message);

            element.click();

        } catch (Exception e) {
            String screenshotPath = ScreenshotUtil.takeScreenshot();
            ExtentUtility.logger.get().log(Status.FAIL, "Click failed: " + e.getMessage());
           // ExtentUtility.attachScreenshotOnFailure("Click not working",screenshotPath);
            //jiraOperation(screenshotPath,e.getMessage());
            logger.error("❌ Click action failed: Unable to click on the element.");
            throw e;
        }
    }

    public static void sendKeys(By primarySearch, String item, String message) {
        try {
            WebElement element = findElement(primarySearch);
            if (element == null) {  // Explicitly handle null case
                throw new NoSuchElementException("Element not found: " + primarySearch.toString());
            }
            ExtentUtility.attatchPassMessageToReport(message);
            element.sendKeys(item);
        }catch (Exception e){

            ExtentUtility.logger.get().log(Status.FAIL,
                    "SendKeys not working: " + e.getMessage());
            //ExtentUtility.attachScreenshotOnFailure("SendKeys not working");
            logger.error("❌ sendKeys action failed: Unable to input text.");

        }
    }

    public static void scrollDown(int scroll){

        JavascriptExecutor js = (JavascriptExecutor) DriverFactory.getDriver();
        js.executeScript("window.scrollBy(0,"+scroll+")");
    }

    public static void scrollDownByElement(){
        WebElement element = DriverFactory.getDriver().findElement(DBLocator.europe);
        JavascriptExecutor js = (JavascriptExecutor) DriverFactory.getDriver();
        js.executeScript("arguments[0].scrollIntoView(true);", element);
    }

    public static void compareDbDataAndUiData(ArrayList<String> DBData, List<String> UIList) {
        DBData.forEach(dbValue -> {
            String uiValue = UIList.stream()
                    .filter(dbValue::equals)
                    .findFirst()
                    .orElse(null);

            if (uiValue != null) {
                ExtentUtility.attatchPassMessageToReport("Both UI and DB values are matched - " +
                        "Ui Value: " + uiValue + " DB value: " + dbValue);
            } else {
               /* ExtentUtility.attatchFailMessageToReport("Both UI and DB values are not matched for - " +
                        "Ui Value: " + uiValue + " DB value: " + dbValue);
                Assert.fail(errorMessage);*/

                String errorMessage = "Both UI and DB values are not matched for - " +
                        "DB Value: " + dbValue + ", UI Value: Not Found";
                ExtentUtility.attatchFailMessageToReport(errorMessage);
                Assert.fail(errorMessage); // Throws AssertionError

            }
        });
    }

    public static void compareDbDataAndUiDataTest(ArrayList<String> DBData, List<String> UIList) {
        StringBuilder failureMessages = new StringBuilder();

        DBData.forEach(dbValue -> {
            String uiValue = UIList.stream()
                    .filter(dbValue::equals)
                    .findFirst()
                    .orElse(null);

            if (uiValue != null) {
                ExtentUtility.attatchPassMessageToReport("Both UI and DB values are matched - " +
                        "UI Value: " + uiValue + " DB Value: " + dbValue);
            } else {
                String errorMessage = "Mismatch found - DB Value: " + dbValue + ", UI Value: Not Found";
                ExtentUtility.attatchFailMessageToReport(errorMessage);
                failureMessages.append(errorMessage).append("\n"); // Collect failure messages
            }
        });

        // Fail the test only once at the end
        if (failureMessages.length() > 0) {
            Assert.fail("Data mismatch found:\n" + failureMessages);
        }
    }

}
