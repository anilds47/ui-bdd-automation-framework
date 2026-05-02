package com.stepdefinitions;

import com.utilities.DriverFactory;
import com.utilities.ConfigReader;
import com.utilities.MobileUtils;
import io.appium.java_client.AppiumDriver;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;



public class MobileSteps {

    private AppiumDriver mobileDriver;
    private MobileUtils mobileUtils;

    @Given("I launch the mobile app on {string} device with UDID {string}")
    public void launchMobileApp(String platform, String udid) {
       // mobileUtils = MobileUtils.launchMobileAppFromConfig(platform, udid);
        mobileDriver = DriverFactory.getAppiumDriver();
    }

    @Given("I launch mobile browser on {string} device with UDID {string}")
    public void launchMobileBrowser(String platform, String udid) {
        mobileUtils = MobileUtils.launchMobileBrowserFromConfig(platform, udid);
        mobileDriver = mobileUtils.getDriver();
    }

    @When("I tap on mobile element with {string} {string}")
    public void tapOnMobileElement(String locatorType, String locatorValue) {
        mobileUtils.tapElement(locatorType, locatorValue);
    }

    @When("I enter {string} in mobile field with {string} {string}")
    public void enterTextInMobileField(String text, String locatorType, String locatorValue) {
        mobileUtils.enterText(text, locatorType, locatorValue);
    }

    @When("I scroll down on mobile screen")
    public void scrollDownOnMobileScreen() {
        mobileUtils.scrollDown();
    }

    @When("I scroll up on mobile screen")
    public void scrollUpOnMobileScreen() {
        mobileUtils.scrollUp();
    }

    @When("I swipe left on mobile screen")
    public void swipeLeftOnMobileScreen() {
        mobileUtils.swipeLeft();
    }

    @When("I swipe right on mobile screen")
    public void swipeRightOnMobileScreen() {
        mobileUtils.swipeRight();
    }

    @When("I long press on mobile element with {string} {string}")
    public void longPressOnMobileElement(String locatorType, String locatorValue) {
        mobileUtils.longPressElement(locatorType, locatorValue, 2000);
    }

    @When("I long press on mobile element with {string} {string} for {int} seconds")
    public void longPressOnMobileElementWithDuration(String locatorType, String locatorValue, int seconds) {
        mobileUtils.longPressElement(locatorType, locatorValue, seconds * 1000);
    }

   /* @Then("I should see mobile element with {string} {string}")
    public void verifyMobileElementVisible(String locatorType, String locatorValue) {
        assertTrue("Element should be visible", mobileUtils.isElementVisible(locatorType, locatorValue));
    }

    @Then("mobile element with {string} {string} should contain text {string}")
    public void verifyMobileElementText(String locatorType, String locatorValue, String expectedText) {
        String actualText = mobileUtils.getElementText(locatorType, locatorValue);
        assertTrue("Element text should contain: " + expectedText, actualText.contains(expectedText));
    }

    @Then("mobile element with {string} {string} should have exact text {string}")
    public void verifyMobileElementExactText(String locatorType, String locatorValue, String expectedText) {
        String actualText = mobileUtils.getElementText(locatorType, locatorValue);
        assertTrue("Element text should be: " + expectedText + " but was: " + actualText, actualText.equals(expectedText));
    }
*/
    @When("I navigate to mobile URL {string}")
    public void navigateToMobileUrl(String url) {
        mobileUtils.navigateToUrl(url);
    }

    @When("I go back on mobile browser")
    public void goBackOnMobileBrowser() {
        mobileUtils.goBack();
    }

    @When("I refresh mobile page")
    public void refreshMobilePage() {
        mobileUtils.refreshPage();
    }

    @When("I hide mobile keyboard")
    public void hideMobileKeyboard() {
        mobileUtils.hideKeyboard();
    }

   /* @Then("mobile keyboard should be visible")
    public void verifyKeyboardVisible() {
        assertTrue("Keyboard should be visible", mobileUtils.isKeyboardShown());
    }

    @Then("mobile keyboard should not be visible")
    public void verifyKeyboardNotVisible() {
        assertTrue("Keyboard should not be visible", !mobileUtils.isKeyboardShown());
    }*/

    @When("I wait for mobile element with {string} {string} to be visible")
    public void waitForMobileElement(String locatorType, String locatorValue) {
        mobileUtils.waitForElement(locatorType, locatorValue, 30);
    }

    @When("I wait {int} seconds for mobile element with {string} {string} to be visible")
    public void waitForMobileElementWithTimeout(int timeoutSeconds, String locatorType, String locatorValue) {
        mobileUtils.waitForElement(locatorType, locatorValue, timeoutSeconds);
    }
}
