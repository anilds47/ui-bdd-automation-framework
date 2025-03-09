package com.utilities;

import com.aventstack.extentreports.ExtentReports;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

public class ExtentUtility {

    public static ExtentReports extent;
    public static ExtentSparkReporter spark;
    public static ExtentTest loggerTest;
    public static ThreadLocal<ExtentTest> logger = new ThreadLocal<ExtentTest>();

    public static void setupReport(String reportName) {
        extent = new ExtentReports();
        String filePath = reportName + File.separator + "report.html";
        spark = new ExtentSparkReporter(filePath);
        spark.config().thumbnailForBase64(true);
        spark.config().setTheme(Theme.DARK);
        spark.config().setReportName(ConfigReader.getValue("ProjectName"));
        spark.config().setDocumentTitle("Execution Report - " + ConfigReader.getValue("ProjectName"));
        extent.attachReporter(spark);
        extent.setSystemInfo("Executed by", System.getProperty("user.name"));

    }



    public static void startTestInit(String testCaseName) {
        loggerTest = extent.createTest(testCaseName);
        logger.set(loggerTest);
    }

    public static void attatchPassMessageToReport(String message) {
        try {
            logger.get().pass(MarkupHelper.createLabel("Passed: "+message , ExtentColor.GREEN));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void attatchFailMessageToReport(String message) {
        try {
            logger.get().fail(MarkupHelper.createLabel("Failed: "+message , ExtentColor.RED));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Attach a screenshot to Extent Report
    public static void attachScreenshotOnFailure(String message, String screenshotPath) {
        try {
            if (screenshotPath != null) {
                ExtentUtility.logger.get().fail(MarkupHelper.createLabel("Failed: " + message, ExtentColor.RED));
                ExtentUtility.logger.get().fail("Screenshot:",
                        MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
            } else {
                ExtentUtility.logger.get().fail("Screenshot capture failed!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
