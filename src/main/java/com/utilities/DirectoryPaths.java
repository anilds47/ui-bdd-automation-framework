package com.utilities;

import java.io.File;
import java.util.Calendar;

public class DirectoryPaths {
    public static final String resourcepath = "src/main/resources";
    public static final String testResourceFolder = "src"+File.separator+"test"+File.separator+"resources";
    public static final String configPath = resourcepath + File.separator + "ConfigFiles/config.properties";
    public static final String testDataExcelPath = System.getProperty("user.dir") + File.separator + testResourceFolder + File.separator + "TestDataFile"+File.separator+"TestData.xlsx";

    public static final String reportFolderName = "HtmlReports";
    public static final String generalPath = System.getProperty("user.dir") + File.separator + reportFolderName ;
    public static final String executionReportFolder = generalPath + File.separator + "Reports";
    public static final String screenShotFolderNameFailedFolder =System.getProperty("user.dir") + File.separator + reportFolderName + File.separator + "FailedScreenshots";
    public static final String screenShotFolderNamePassedFolder = System.getProperty("user.dir") + File.separator + reportFolderName + File.separator + "PassedScreenshots";
    public static final String backUpFolder = "ReportsBackup";
    public static String backUpReportName = "ExtentReport_" + Calendar.getInstance().getTime().toString().replace(" ", "_").replace(":", "-").trim() + ".html";
    public static String extentReportPath=executionReportFolder +File.separator + "report.html";

}
