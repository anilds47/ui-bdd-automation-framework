package com.utilities;

import java.io.*;
import java.nio.file.*;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtil{
public static String zipReport(String reportPath) throws IOException {
    String zipFilePath = reportPath.replace(".html", ".zip");
    try (FileOutputStream fos = new FileOutputStream(zipFilePath);
         ZipOutputStream zipOut = new ZipOutputStream(fos);
         FileInputStream fis = new FileInputStream(reportPath)) {

        ZipEntry zipEntry = new ZipEntry(new File(reportPath).getName());
        zipOut.putNextEntry(zipEntry);

        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zipOut.write(bytes, 0, length);
        }
    }
    return zipFilePath;
}

    static void zipReports(String sourceDirPath, String zipFilePath) throws IOException {
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFilePath))) {
            Path sourceDir = Paths.get(sourceDirPath);
            Files.walk(sourceDir).filter(path -> !Files.isDirectory(path)).forEach(path -> {
                try {
                    String fileName = sourceDir.relativize(path).toString();
                    ZipEntry zipEntry = new ZipEntry(fileName);
                    zipOutputStream.putNextEntry(zipEntry);
                    Files.copy(path, zipOutputStream);
                    zipOutputStream.closeEntry();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public static String zipReportTest(String reportPath) throws IOException {
        File reportFile = new File(reportPath);


         String zipFolderPath = DirectoryPaths.generalPath + File.separator + "ZipReport" ; // Change this as needed
         FolderUtility.folderCreation(zipFolderPath);
         String zipFilePath = zipFolderPath + File.separator + "TestReport.zip"; // Fixed missing separator

        try (FileOutputStream fos = new FileOutputStream(zipFilePath);
             ZipOutputStream zos = new ZipOutputStream(fos);
             FileInputStream fis = new FileInputStream(reportFile )) {

            ZipEntry zipEntry = new ZipEntry(reportFile.getName());
            zos.putNextEntry(zipEntry);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                zos.write(buffer, 0, length);
            }

            zos.closeEntry();
        }

        return zipFilePath;
    }
}