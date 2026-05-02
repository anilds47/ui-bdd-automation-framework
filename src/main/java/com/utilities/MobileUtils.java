package com.utilities;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;

import java.time.Duration;
import java.util.Arrays;

/**
 * MobileUtils - Reusable utility methods for mobile automation
 * Provides common mobile operations like tapping, scrolling, swiping, etc.
 */
public class MobileUtils {

    private AppiumDriver mobileDriver;
    private WebDriverWait wait;

    public MobileUtils(AppiumDriver driver) {
        this.mobileDriver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    public MobileUtils(AppiumDriver driver, int timeoutSeconds) {
        this.mobileDriver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
    }

    /**
     * Tap on a mobile element
     * @param locatorType Type of locator (id, xpath, css, classname, name, accessibilityid)
     * @param locatorValue Value of the locator
     */
    public void tapElement(String locatorType, String locatorValue) {
        By locator = getLocator(locatorType, locatorValue);
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
        element.click();
    }

    /**
     * Enter text in a mobile input field
     * @param text Text to enter
     * @param locatorType Type of locator
     * @param locatorValue Value of the locator
     */
    public void enterText(String text, String locatorType, String locatorValue) {
        By locator = getLocator(locatorType, locatorValue);
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        element.clear();
        element.sendKeys(text);
    }

    /**
     * Scroll down on mobile screen
     */
    public void scrollDown() {
        Dimension size = mobileDriver.manage().window().getSize();
        int startY = (int) (size.height * 0.8);
        int endY = (int) (size.height * 0.2);
        int centerX = size.width / 2;

        performTouchAction(centerX, startY, centerX, endY, 1000);
    }

    /**
     * Scroll up on mobile screen
     */
    public void scrollUp() {
        Dimension size = mobileDriver.manage().window().getSize();
        int startY = (int) (size.height * 0.2);
        int endY = (int) (size.height * 0.8);
        int centerX = size.width / 2;

        performTouchAction(centerX, startY, centerX, endY, 1000);
    }

    /**
     * Swipe left on mobile screen
     */
    public void swipeLeft() {
        Dimension size = mobileDriver.manage().window().getSize();
        int startX = (int) (size.width * 0.8);
        int endX = (int) (size.width * 0.2);
        int centerY = size.height / 2;

        performTouchAction(startX, centerY, endX, centerY, 1000);
    }

    /**
     * Swipe right on mobile screen
     */
    public void swipeRight() {
        Dimension size = mobileDriver.manage().window().getSize();
        int startX = (int) (size.width * 0.2);
        int endX = (int) (size.width * 0.8);
        int centerY = size.height / 2;

        performTouchAction(startX, centerY, endX, centerY, 1000);
    }

    /**
     * Long press on a mobile element
     * @param locatorType Type of locator
     * @param locatorValue Value of the locator
     * @param durationMillis Duration of long press in milliseconds
     */
    public void longPressElement(String locatorType, String locatorValue, int durationMillis) {
        By locator = getLocator(locatorType, locatorValue);
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));

        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence longPress = new Sequence(finger, 1);
        longPress.addAction(finger.createPointerMove(Duration.ofMillis(0), PointerInput.Origin.pointer(),
                element.getLocation().x, element.getLocation().y));
        longPress.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        longPress.addAction(finger.createPointerMove(Duration.ofMillis(durationMillis), PointerInput.Origin.pointer(),
                element.getLocation().x, element.getLocation().y));
        longPress.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        mobileDriver.perform(Arrays.asList(longPress));
    }

    /**
     * Check if mobile element is visible
     * @param locatorType Type of locator
     * @param locatorValue Value of the locator
     * @return true if element is visible
     */
    public boolean isElementVisible(String locatorType, String locatorValue) {
        try {
            By locator = getLocator(locatorType, locatorValue);
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get text from mobile element
     * @param locatorType Type of locator
     * @param locatorValue Value of the locator
     * @return Text content of the element
     */
    public String getElementText(String locatorType, String locatorValue) {
        By locator = getLocator(locatorType, locatorValue);
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        return element.getText();
    }

    /**
     * Navigate to URL in mobile browser
     * @param url URL to navigate to
     */
    public void navigateToUrl(String url) {
        mobileDriver.get(url);
    }

    /**
     * Go back in mobile browser
     */
    public void goBack() {
        mobileDriver.navigate().back();
    }

    /**
     * Refresh mobile page
     */
    public void refreshPage() {
        mobileDriver.navigate().refresh();
    }

    /**
     * Wait for element to be present
     * @param locatorType Type of locator
     * @param locatorValue Value of the locator
     * @param timeoutSeconds Timeout in seconds
     * @return WebElement if found
     */
    public WebElement waitForElement(String locatorType, String locatorValue, int timeoutSeconds) {
        WebDriverWait customWait = new WebDriverWait(mobileDriver, Duration.ofSeconds(timeoutSeconds));
        By locator = getLocator(locatorType, locatorValue);
        return customWait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Perform custom touch action
     * @param startX Start X coordinate
     * @param startY Start Y coordinate
     * @param endX End X coordinate
     * @param endY End Y coordinate
     * @param durationMillis Duration in milliseconds
     */
    private void performTouchAction(int startX, int startY, int endX, int endY, int durationMillis) {
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence action = new Sequence(finger, 1);
        action.addAction(finger.createPointerMove(Duration.ofMillis(0), PointerInput.Origin.viewport(), startX, startY));
        action.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        action.addAction(finger.createPointerMove(Duration.ofMillis(durationMillis), PointerInput.Origin.viewport(), endX, endY));
        action.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        mobileDriver.perform(Arrays.asList(action));
    }

    /**
     * Get locator based on type and value
     * @param locatorType Type of locator
     * @param locatorValue Value of the locator
     * @return By locator object
     */
    private By getLocator(String locatorType, String locatorValue) {
        switch (locatorType.toLowerCase()) {
            case "id":
                return By.id(locatorValue);
            case "xpath":
                return By.xpath(locatorValue);
            case "css":
                return By.cssSelector(locatorValue);
            case "classname":
                return By.className(locatorValue);
            case "name":
                return By.name(locatorValue);
            case "accessibilityid":
                return AppiumBy.accessibilityId(locatorValue);
            case "uiautomator":
                return AppiumBy.androidUIAutomator(locatorValue);
            case "iosnspredicate":
                return AppiumBy.iOSNsPredicateString(locatorValue);
            default:
                throw new IllegalArgumentException("Unsupported locator type: " + locatorType);
        }
    }

    /**
     * Get the underlying AppiumDriver instance
     * @return AppiumDriver instance
     */
    public AppiumDriver getDriver() {
        return mobileDriver;
    }

    /**
     * Set implicit wait timeout
     * @param seconds Timeout in seconds
     */
    public void setImplicitWait(int seconds) {
        mobileDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(seconds));
    }

    /**
     * Hide keyboard if visible
     */
    public void hideKeyboard() {
        try {
            if (mobileDriver instanceof io.appium.java_client.android.AndroidDriver) {
                ((io.appium.java_client.android.AndroidDriver) mobileDriver).hideKeyboard();
            } else if (mobileDriver instanceof io.appium.java_client.ios.IOSDriver) {
                ((io.appium.java_client.ios.IOSDriver) mobileDriver).hideKeyboard();
            }
        } catch (Exception e) {
            // Keyboard might not be visible or method not supported, ignore
        }
    }

    /**
     * Check if keyboard is shown
     * @return true if keyboard is shown
     */
    public boolean isKeyboardShown() {
        try {
            if (mobileDriver instanceof io.appium.java_client.android.AndroidDriver) {
                return ((io.appium.java_client.android.AndroidDriver) mobileDriver).isKeyboardShown();
            } else if (mobileDriver instanceof io.appium.java_client.ios.IOSDriver) {
                return ((io.appium.java_client.ios.IOSDriver) mobileDriver).isKeyboardShown();
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Launch mobile app with specified platform and device UDID
     * @param platform Android or iOS
     * @param deviceName Device name
     * @param udid Device UDID
     * @param appiumServerUrl Appium server URL
     * @return MobileUtils instance with initialized driver
     */
    public static MobileUtils launchMobileApp(String platform, String deviceName, String udid, String appiumServerUrl) {
        DriverFactory.setMobileDriver(platform);
        AppiumDriver driver = DriverFactory.getAppiumDriver();
        return new MobileUtils(driver);
    }

    /**
     * Launch mobile browser with specified platform and device UDID
     * @param platform Android or iOS
     * @param deviceName Device name
     * @param udid Device UDID
     * @param appiumServerUrl Appium server URL
     * @return MobileUtils instance with initialized driver
     */
    public static MobileUtils launchMobileBrowser(String platform, String deviceName, String udid, String appiumServerUrl) {
        DriverFactory.setMobileDriver(platform);
        AppiumDriver driver = DriverFactory.getAppiumDriver();
        return new MobileUtils(driver);
    }

    /**
     * Launch mobile app using configuration from config.properties
     * @param platform Android or iOS
     * @param udid Device UDID
     * @return MobileUtils instance with initialized driver
     */
    public static MobileUtils launchMobileAppFromConfig(String platform, String udid) {
        String appiumServerUrl = ConfigReader.getValue("AppiumServerUrl");
        String deviceName = ConfigReader.getValue("MobileDeviceName");
        return launchMobileApp(platform, deviceName, udid, appiumServerUrl);
    }

    /**
     * Launch mobile browser using configuration from config.properties
     * @param platform Android or iOS
     * @param udid Device UDID
     * @return MobileUtils instance with initialized driver
     */
    public static MobileUtils launchMobileBrowserFromConfig(String platform, String udid) {
        String appiumServerUrl = ConfigReader.getValue("AppiumServerUrl");
        String deviceName = ConfigReader.getValue("MobileDeviceName");
        return launchMobileBrowser(platform, deviceName, udid, appiumServerUrl);
    }
}
