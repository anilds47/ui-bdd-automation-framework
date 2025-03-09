package com.utilities;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FolderUtility {

    public static void folderCreation(String folderPath ){
        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }

    }

    public static void moveExtentReportForBackup(String executionReportFolder, String reportFolderName, String backUpFolder, String backUpReportName) throws IOException {
        try{
            String filePathNewReport = executionReportFolder + File.separator + "Reports";
         String filePathBackupReport = executionReportFolder + File.separator + backUpFolder;

        File newReportFolder = new File(filePathNewReport);
        folderCreation(filePathBackupReport);
        File[] listOfFiles = newReportFolder.listFiles();
        if (listOfFiles == null || listOfFiles.length == 0) {
            System.out.println("No reports found in " + executionReportFolder);
            return;
        }
        for (File file : listOfFiles) {
            if (file.isFile()) {
                Path sourcePath = file.toPath();
                Path destinationPath = Paths.get(filePathBackupReport, backUpReportName);

                Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Report copied to: " + destinationPath);
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}


    /*public static void moveExtentReportForBackup(String generalPath,  String backUpFolder, String backUpReportName) {

        try {
            String filePathNewReport = generalPath + File.separator + "Reports";
            String filePathBackupReport = generalPath + File.separator + backUpFolder;

            System.out.println(filePathBackupReport);


            File newReportFolder = new File(filePathNewReport);
            File backupFolder = new File(filePathBackupReport);

            // Ensure the backup folder exists
            if (!backupFolder.exists()) {
                backupFolder.mkdirs();
            }

            File[] listOfFiles = newReportFolder.listFiles();

            if (listOfFiles == null || listOfFiles.length == 0) {
                System.out.println("No reports found in " + filePathNewReport);
                return;
            }

            for (File file : listOfFiles) {
                if (file.isFile()) {
                    Path sourcePath = file.toPath();
                    Path destinationPath = Paths.get(filePathBackupReport, backUpReportName);

                    Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("Report copied to: " + destinationPath);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }*/
}
