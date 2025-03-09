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

public class DriverFactory {

    private static ThreadLocal<WebDriver> driver = new ThreadLocal<WebDriver>();

    public static void setDriver(String env, String browser) {
        try {
            if (env.equalsIgnoreCase("Local") && ConfigReader.getValue("Environment").equalsIgnoreCase("Qa")) {
                openBrowser(browser);
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
                chromeOptions.setPageLoadStrategy(PageLoadStrategy.EAGER);
               WebDriverManager.chromedriver().setup();

                WebDriver baseDriver = new ChromeDriver(chromeOptions);

                // Decorate WebDriver with the listener
                WebDriver eventFiringDriver = new EventFiringDecorator<>(new CustomWebDriverListener()).decorate(baseDriver);

                driver.set(eventFiringDriver);

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

    public static WebDriver getDriver() {
        return driver.get();
    }

}
