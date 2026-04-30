package com.runners;

import com.utilities.CustomWebDriverListener;
import com.utilities.TestBaseClass;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.*;

import java.io.IOException;

@CucumberOptions(
    features = {"src/test/resources/features"},  // Run only UI-related features
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
    public void beforeClass() {
        base.beforeClass();
    }

   /* @BeforeMethod(alwaysRun = true)
    public void beforeMethod() {
        base.beforeMethod();
    }*/

   /* @AfterClass
    public void afterClass() {
        base.afterClass();
    }*/

    @AfterSuite
    public void afterSuite() throws IOException {
        CustomWebDriverListener.setEndTime(System.currentTimeMillis());
        CustomWebDriverListener.printAndPersistSummary("Cucumber Suite");
        base.aftersuite();
        base.afterClass();
    }
}
