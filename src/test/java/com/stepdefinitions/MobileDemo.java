package com.stepdefinitions;

import com.google.common.collect.ImmutableMap;
import com.utilities.BaseStep;
import com.utilities.DriverFactory;
import com.utilities.MobileUtils;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

public class MobileDemo  {



    @When("User is on the home screen")
    public void user_is_on_the_home_screen() {
        System.out.println("User is on Home Screen");

    }
    @When("User clicks on Views")
    public void user_clicks_on_views() {
        WebElement views = DriverFactory.getDriver().findElement(AppiumBy.accessibilityId("Views"));
        views.click();
        System.out.println("Opened 'Views' section");
    }
    @When("User scrolls until Tabs is visible and clicks")
    public void user_scrolls_until_tabs_is_visible_and_clicks() {
        scrollUntilElementVisible(By.xpath("//android.widget.TextView[@text='Tabs']"));
        DriverFactory.getDriver().navigate().back();

    }


    public void scrollUntilElementVisible(By locator) {
        int maxScrolls = 10;
        int count = 0;

        while (count < maxScrolls) {
            try {
                WebElement element = DriverFactory.getDriver().findElement(locator);

                if (element.isDisplayed()) {
                    System.out.println("Element found");
                    element.click();
                    return;
                }
            } catch (Exception e) {
                // Scroll down
                ((JavascriptExecutor) DriverFactory.getDriver()).executeScript("mobile: scrollGesture", ImmutableMap.of(
                        "left", 100,
                        "top", 100,
                        "width", 200,
                        "height", 400,
                        "direction", "down",
                        "percent", 0.8
                ));
            }
            count++;
        }

        if (count == maxScrolls) {
            throw new RuntimeException("Element not found after scrolling");
        }
    }
}