package com.runners;

import com.utilities.CustomWebDriverListener;
import com.utilities.TestBaseClass;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.*;

import java.io.IOException;

@CucumberOptions(
    features = {"src/test/resources/features/MobileDemo.feature"},  // Run only UI-related features
    glue = {"com.stepdefinitions"},  // Package for step definitions
    plugin = {"pretty", "html:target/cucumber-reports.html"},  // Reporting
    monochrome = true
)
public class CucumberTestRunner extends AbstractTestNGCucumberTests {
    private TestBaseClass base = new TestBaseClass();

    @BeforeSuite
    public void beforeSuite() {
        CustomWebDriverListener.setStartTime(System.currentTimeMillis());
        base.beforeSuite();
    }

    @BeforeClass(alwaysRun = true)
    @Parameters({"platform", "browser"})
    public void beforeClass(String platform,  @Optional("chrome") String browser) {

        base.beforeClass(platform, browser);
    }

   /* @BeforeMethod(alwaysRun = true)
    public void beforeMethod() {
        base.beforeMethod();
    }*/

    @AfterClass
    @Parameters({"platform", "browser"})
    public void afterClass(String platform,  @Optional("chrome") String browser) {
        base.afterClass(platform,browser);
    }

    @AfterSuite
    @Parameters({"platform", "browser"})
    public void afterSuite(String platform,  @Optional("chrome") String browser) throws IOException {
        CustomWebDriverListener.setEndTime(System.currentTimeMillis());
        CustomWebDriverListener.printAndPersistSummary("Cucumber Suite");
        base.aftersuite();
       // base.afterClass(platform,browser);
    }
}
