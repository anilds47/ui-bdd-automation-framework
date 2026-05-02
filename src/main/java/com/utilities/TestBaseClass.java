package com.utilities;

import io.appium.java_client.AppiumDriver;

import java.io.IOException;

public class TestBaseClass {

    public void beforeSuite(){
        FolderUtility.folderCreation(DirectoryPaths.executionReportFolder);
        ExtentUtility.setupReport(DirectoryPaths.executionReportFolder);
        FolderUtility.folderCreation(DirectoryPaths.screenShotFolderNameFailedFolder);
       // com.utilities.CustomWebDriverListener.setStartTime(System.currentTimeMillis());
    }


    public void beforeClass(String platform, String browser)
    {
        if(platform.equalsIgnoreCase("Web") && browser.equalsIgnoreCase(ConfigReader.getValue("Browser"))) {
            DriverFactory.setDriver(ConfigReader.getValue("Env"), ConfigReader.getValue("Browser"));
        } else if(platform.equals("android") || platform.equals("iOS")) {
            DriverFactory.setMobileDriver(platform);
        } else {
            throw new IllegalArgumentException("Unsupported platform or browser: " + platform + ", " + browser);
        }

    }


    public void beforeMethod() {
        ExtentUtility.startTestInit("BDD Scenario");
    }


    public void afterClass(String platform, String browser){
        try {
            if(platform.equalsIgnoreCase("Web") && browser.equalsIgnoreCase(ConfigReader.getValue("Browser"))) {
                DriverFactory.quitDriver();
            } else if(platform.equalsIgnoreCase("android") || platform.equalsIgnoreCase("iOS")) {
                DriverFactory.quitMobileDriver();
            } else {
                throw new IllegalArgumentException("Unsupported platform or browser: " + platform + ", " + browser);
            }
        } catch (Exception e) {
            System.out.println("Error in afterClass: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public void aftersuite() throws IOException {
        FolderUtility.moveExtentReportForBackup(DirectoryPaths.generalPath,DirectoryPaths.reportFolderName,
                DirectoryPaths.backUpFolder,DirectoryPaths.backUpReportName);
        ExtentUtility.extent.flush();
        com.utilities.CustomWebDriverListener.setEndTime(System.currentTimeMillis());
        EmailConfiguration. emailSetup();
    }



}
