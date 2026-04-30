package com.utilities;

import com.aventstack.extentreports.Status;
import org.testng.annotations.*;

import java.io.IOException;
import java.util.HashMap;

import static com.utilities.ReusableMethods.jiraOperation;

public class TestBaseClass {

    public void beforeSuite(){
        FolderUtility.folderCreation(DirectoryPaths.executionReportFolder);
        ExtentUtility.setupReport(DirectoryPaths.executionReportFolder);
        FolderUtility.folderCreation(DirectoryPaths.screenShotFolderNameFailedFolder);
       // com.utilities.CustomWebDriverListener.setStartTime(System.currentTimeMillis());
    }


    public void beforeClass()
    {
        DriverFactory.setDriver(ConfigReader.getValue("Env"),ConfigReader.getValue("Browser"));

    }


    public void beforeMethod() {
        ExtentUtility.startTestInit("BDD Scenario");
    }


    public void afterClass(){

        DriverFactory.getDriver().quit();
    }


    public void aftersuite() throws IOException {
        FolderUtility.moveExtentReportForBackup(DirectoryPaths.generalPath,DirectoryPaths.reportFolderName,
                DirectoryPaths.backUpFolder,DirectoryPaths.backUpReportName);
        ExtentUtility.extent.flush();
        com.utilities.CustomWebDriverListener.setEndTime(System.currentTimeMillis());
        EmailConfiguration. emailSetup();
    }



}
