package com.utilities;


import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.events.EventFiringDecorator;

import java.net.URL;
import java.time.Duration;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.openqa.selenium.remote.DesiredCapabilities;

public class DriverFactory {

    private static ThreadLocal<WebDriver> driver = new ThreadLocal<WebDriver>();

    private static ThreadLocal<AppiumDriver> mobileDriver=new ThreadLocal<>();

   // public static AppiumDriver mobileDriver;

    public static void setDriver(String env, String browser) {
        try {
            if (env.equalsIgnoreCase("Local") && ConfigReader.getValue("Environment").equalsIgnoreCase("Qa")) {
                openBrowser(browser);
           }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setMobileDriver(String platform) {
        try {
            if (platform.equalsIgnoreCase("Android")) {
                openAndroidDriver();
            } else if (platform.equalsIgnoreCase("iOS")) {
                openIOSDriver();
            } else {
                throw new Exception("Unsupported mobile platform: " + platform);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void openBrowser(String browserName) {
        try {
            if (browserName.equalsIgnoreCase("chrome")) {
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--remote-allow-origins=*");
                chromeOptions.addArguments("--no-sandbox");
                chromeOptions.addArguments("--disable-dev-shm-usage");
                chromeOptions.setPageLoadStrategy(PageLoadStrategy.EAGER);

                WebDriverManager.chromedriver().setup();

                // Step 1: Create base driver
                WebDriver baseDriver = new ChromeDriver(chromeOptions);

                // Step 2: Attach listener (VERY IMPORTANT)
                CustomWebDriverListener listener = new CustomWebDriverListener();
                WebDriver decoratedDriver = new EventFiringDecorator(listener).decorate(baseDriver);

                // Step 3: Set ThreadLocal
                driver.set(decoratedDriver);

                // Step 4: Basic setup
                getDriver().manage().window().maximize();
                //getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

                //driver.set(new ChromeDriver(chromeOptions));
            } else if (browserName.equalsIgnoreCase("FireFox")) {
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.setPageLoadStrategy(PageLoadStrategy.EAGER);
                firefoxOptions.addPreference("dom.webnotifications.enabled", false); // Disable notifications

                WebDriver baseDriver = new FirefoxDriver(firefoxOptions);
                WebDriver eventFiringDriver = new EventFiringDecorator<>(new CustomWebDriverListener()).decorate(baseDriver);
                driver.set(eventFiringDriver);

            } else if (browserName.equalsIgnoreCase("ie")) {
                WebDriverManager.iedriver().arch64().setup();
                driver.set(new InternetExplorerDriver());

            } else if (browserName.equalsIgnoreCase("edge")) {
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.addArguments("--inPrivate"); // Open in private mode
                WebDriver baseDriver = new EdgeDriver(edgeOptions);
                WebDriver eventFiringDriver = new EventFiringDecorator<>(new CustomWebDriverListener()).decorate(baseDriver);
                driver.set(eventFiringDriver);

            } else {
                throw new Exception("Unsupported browser type");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void openAndroidDriver() {
        try {
            UiAutomator2Options options = new UiAutomator2Options();
            options.setPlatformName("Android");
            options.setDeviceName(ConfigReader.getValue("MobileDeviceName"));
            options.setUdid(ConfigReader.getValue("MobileDeviceUDID"));
            options.setAutomationName(ConfigReader.getValue("MobileAutomationName"));
            options.setNoReset(Boolean.parseBoolean(ConfigReader.getValue("MobileNoReset")));
            options.setNewCommandTimeout(Duration.ofSeconds(Integer.parseInt(ConfigReader.getValue("MobileNewCommandTimeout"))));
            options.setPlatformVersion("13.0");
            // Check if we're testing a native app or mobile web
            //String appPath = ConfigReader.getValue("MobileAppPath");
            String browserName = ConfigReader.getValue("MobileBrowser");
           // options.setNoReset(false);

            String appPath = System.getProperty("user.dir")
                    + ConfigReader.getValue("MobileAppPath");

            if (appPath != null && !appPath.isEmpty()) {

                options.setApp(appPath);
            } else {
                // Mobile web testing - set browser name capability
               options.setCapability("browserName", browserName);
            }

            URL url = new URL(ConfigReader.getValue("AppiumServerUrl"));
            AndroidDriver androidDriver = new AndroidDriver(url, options);

            // Step 2: Attach listener (VERY IMPORTANT)
            CustomWebDriverListener listener = new CustomWebDriverListener();
            WebDriver decoratedDriver =  new EventFiringDecorator(listener).decorate(androidDriver);

            // Step 3: Set ThreadLocal
           // driver.set(decoratedDriver);
            //mobileDriver.set((AppiumDriver) decoratedDriver);
           // mobileDriver.set(androidDriver);        // AppiumDriver (real)
            driver.set(decoratedDriver);
            // For web testing, we can still use the CustomWebDriverListener
         /*   if (options.getCapability("browserName") != null) {
              //  WebDriver eventFiringDriver = new EventFiringDecorator<>(new CustomWebDriverListener()).decorate(androidDriver);
               // driver.set(eventFiringDriver);
            } else {
                mobileDriver.set(androidDriver);
            }*/

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void openIOSDriver() {
        try {
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability("platformName", "iOS");
            capabilities.setCapability("deviceName", ConfigReader.getValue("MobileDeviceName"));
            capabilities.setCapability("udid", ConfigReader.getValue("MobileDeviceUDID"));
            capabilities.setCapability("automationName", ConfigReader.getValue("MobileAutomationName"));
            capabilities.setCapability("noReset", Boolean.parseBoolean(ConfigReader.getValue("MobileNoReset")));
            capabilities.setCapability("newCommandTimeout", Integer.parseInt(ConfigReader.getValue("MobileNewCommandTimeout")));

            // Check if we're testing a native app or mobile web
            String appPath = ConfigReader.getValue("MobileAppPath");
            String browserName = ConfigReader.getValue("MobileBrowser");

            if (appPath != null && !appPath.isEmpty() && !appPath.equals("/path/to/your/app.app")) {
                // Native app testing
                capabilities.setCapability("app", appPath);
                capabilities.setCapability("bundleId", ConfigReader.getValue("MobileAppPackage"));
            } else {
                // Mobile web testing
                capabilities.setCapability("browserName", browserName);
            }

            URL url = new URL(ConfigReader.getValue("AppiumServerUrl"));
            IOSDriver iosDriver = new IOSDriver(url, capabilities);

            // For web testing, we can still use the CustomWebDriverListener
            if (capabilities.getCapability("browserName") != null) {
                WebDriver eventFiringDriver = new EventFiringDecorator<>(new CustomWebDriverListener()).decorate(iosDriver);
                driver.set(eventFiringDriver);
            } else {
                driver.set(iosDriver);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static WebDriver getDriver() {
        return driver.get();
    }

    public static AppiumDriver getAppiumDriver() {

        return mobileDriver.get();
    }

    public static void quitMobileDriver() {
        try {
            if (mobileDriver.get() != null) {
                String packageName = ((AndroidDriver) getAppiumDriver()).getCurrentPackage();
                System.out.println("Killing App: " + packageName);

                ((AndroidDriver) getAppiumDriver()).terminateApp(packageName);
                getAppiumDriver().quit();
            }
        }

        catch (Exception e) {
            System.out.println("Error closing mobile driver: " + e.getMessage());
            e.printStackTrace();
        }finally {
            mobileDriver.remove(); // Clear ThreadLocal
        }
    }

    public static void quitDriver() {
        try {
            if (driver.get() != null) {

                driver.get().quit();
                driver.remove(); // Clear ThreadLocal
            }
        } catch (Exception e) {
            System.out.println("Error closing driver: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
