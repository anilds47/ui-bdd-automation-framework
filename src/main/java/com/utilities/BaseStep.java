package com.utilities;

import io.appium.java_client.AppiumDriver;

public class BaseStep {
    public static AppiumDriver getMobileDriver() {
        return DriverFactory.getAppiumDriver();
    }
}
