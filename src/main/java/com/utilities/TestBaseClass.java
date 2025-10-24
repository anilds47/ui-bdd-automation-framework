package com.utilities;

import com.aventstack.extentreports.Status;
import org.testng.annotations.*;

import java.io.IOException;
import java.util.HashMap;

import static com.utilities.ReusableMethods.jiraOperation;

public class TestBaseClass {

    @BeforeSuite
    public void beforeSuite(){
        FolderUtility.folderCreation(DirectoryPaths.executionReportFolder);
        ExtentUtility.setupReport(DirectoryPaths.executionReportFolder);
        FolderUtility.folderCreation(DirectoryPaths.screenShotFolderNameFailedFolder);


    }

    @BeforeClass(alwaysRun = true)
    public void beforeClass()
    {
        DriverFactory.setDriver(ConfigReader.getValue("Env"),ConfigReader.getValue("Browser"));

    }

    @AfterClass
    public void afterClass(){

        DriverFactory.getDriver().quit();
    }

    @AfterSuite
    public void aftersuite() throws IOException {

        FolderUtility.moveExtentReportForBackup(DirectoryPaths.generalPath,DirectoryPaths.reportFolderName,
                DirectoryPaths.backUpFolder,DirectoryPaths.backUpReportName);
        ExtentUtility.extent.flush();
        EmailConfiguration. emailSetup();
    }



}
