package com.utilities;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenshotUtil {

    // Screenshot Utility
    /*public static String takeScreenshot() {
        try {
            // Create screenshot directory if not exists
            File screenshotDir = new File(DirectoryPaths.screenShotFolderNameFailedFolder);
            if (!screenshotDir.exists()) {
                screenshotDir.mkdirs();
            }


            // Generate timestamp for filename
            String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
            String fileName = "Screenshot_" + timestamp + ".png";
            String filePath = DirectoryPaths.screenShotFolderNameFailedFolder + File.separator + fileName;





            String jenkinsHome = System.getenv("JENKINS_HOME");
            System.out.println(jenkinsHome);


            // Take screenshot
            File srcFile = ((TakesScreenshot) DriverFactory.getDriver()).getScreenshotAs(OutputType.FILE);
            //FileUtils.copyFile(srcFile, new File(filePath));
            Files.copy(srcFile.toPath(), Paths.get(filePath));
            return filePath;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }*/

    public static String takeScreenshot() {
        try {
            // Detect environment: Jenkins or local
            String baseDir;
            String workspace = System.getenv("WORKSPACE");
            String jenkinsHome = System.getenv("JENKINS_HOME");

            if (jenkinsHome != null && workspace != null) {
                baseDir = workspace + File.separator + "HtmlReports" + File.separator + "FailedScreenshots";
            } else {

                baseDir = DirectoryPaths.screenShotFolderNameFailedFolder + File.separator + "FailedScreenshots";
            }

            File screenshotDir = new File(baseDir);
            if (!screenshotDir.exists()) {
                screenshotDir.mkdirs();
            }

            // Generate timestamp for filename
            String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
            String fileName = "Screenshot_" + timestamp + ".png";
            String filePath = baseDir + File.separator + fileName;

            // Take screenshot
            File srcFile = ((TakesScreenshot) DriverFactory.getDriver()).getScreenshotAs(OutputType.FILE);
            Files.copy(srcFile.toPath(), Paths.get(filePath));

            // Return full path for reporting
            return filePath;



        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


}
