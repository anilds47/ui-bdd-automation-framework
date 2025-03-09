package com.utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.nio.file.Files;

import java.nio.file.Paths;

import java.text.SimpleDateFormat;
import java.util.*;

import javax.mail.*;

import javax.mail.internet.*;

public class EmailConfiguration {
    private static final Logger logger = LogManager.getLogger(EmailConfiguration.class);
    public static void emailSetup() {

        if(ConfigReader.getValue("EmailSetUp").equalsIgnoreCase("Yes")) {
            final String senderEmail = ConfigReader.getValue("SenderEmail");
            final String senderPassword = ConfigReader.getValue("SenderPassword");
            final String recipientEmails = ConfigReader.getValue("recipientEmail");

            List<String> recipientList = Arrays.asList(recipientEmails.split(","));

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Format: 2025-03-01
            String currentDate = dateFormat.format(new Date());

            String reportFolderPath = DirectoryPaths.extentReportPath;

            int totalTests = CustomWebDriverListener.getTotalTests();
            int passedTests = CustomWebDriverListener.getPassedTests();
            int failedTests = CustomWebDriverListener.getFailedTests();
            int skippedTests = CustomWebDriverListener.getSkippedTests();
            String executionTimeHours = CustomWebDriverListener.getExecutionTimeInSeconds();
            List<String> failedTestCases = CustomWebDriverListener.getFailedTestCaseNames();
            Map<String, String> failedErrors = CustomWebDriverListener.getFailedTestCaseErrors();

            String startTime=CustomWebDriverListener.getStartTime();
            String endTime=CustomWebDriverListener.getEndTime();

            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.ssl.protocols", "TLSv1.2");
            props.put("mail.debug", "true");

            // Create session
            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(senderEmail, senderPassword);
                }
            });

            try {
                // Create ZIP file with reports
                String zipFilePath = ZipUtil.zipReportTest(reportFolderPath);
                String htmlSummaryTable = "<h3>Test Execution Summary</h3>" +
                        "<table border='1' cellpadding='5' cellspacing='0' style='border-collapse: collapse; width: 50%;'>" +
                        "<tr style='background-color: #4CAF50; color: white;'><th>Metric</th><th>Value</th></tr>" +
                        "<tr><td>Start Time</td><td>" + startTime + "</td></tr>" +
                        "<tr><td>End Time</td><td>" + endTime + "</td></tr>" +
                        "<tr><td>Total Test Cases</td><td>" + totalTests + "</td></tr>" +
                        "<tr><td>Passed</td><td style='color:green;'><b>" + passedTests + "</b></td></tr>" +
                        "<tr><td>Failed</td><td style='color:red;'><b>" + failedTests + "</b></td></tr>" +
                        "<tr><td>Skipped</td><td style='color:orange;'><b>" + skippedTests + "</b></td></tr>" +
                        "<tr><td>Total Execution Time</td><td>" + executionTimeHours + " hours</td></tr>" +
                        "</table>";

                StringBuilder failedTestsHtml = new StringBuilder("<h3>Failed Test Cases</h3><ul>");
                for (String testCase : failedTestCases) {
                    String errorMessage = failedErrors.get(testCase);
                    failedTestsHtml.append("<li><b>").append(testCase).append(":</b> ").append(errorMessage).append("</li>");
                }
                failedTestsHtml.append("</ul>");


                String subject = "Automation Test Report - Summary [" + startTime + "]";
                for (String recipient : recipientList) {
                    Message message = new MimeMessage(session);
                    message.setFrom(new InternetAddress(senderEmail));
                    message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient.trim()));

                    message.setSubject(subject);

                    // Create Multipart email (HTML + Zip attachment)
                    Multipart multipart = new MimeMultipart();

                    // Add HTML Summary Table in the email body
                    MimeBodyPart htmlPart = new MimeBodyPart();
                    htmlPart.setContent("<html><body>" + htmlSummaryTable + failedTestsHtml + "</body></html>", "text/html");
                    multipart.addBodyPart(htmlPart);

                    // Attach the Zipped Report
                    MimeBodyPart attachmentPart = new MimeBodyPart();
                    attachmentPart.attachFile(new File(zipFilePath));
                    attachmentPart.setFileName("TestReport.zip");
                    multipart.addBodyPart(attachmentPart);
                    // Set email content
                    message.setContent(multipart);
                    // Send email
                    Transport.send(message);

                    logger.info("======================================");
                    logger.info("📢 EMAIL SENT SUCCESSFULLY 📢");
                    logger.info("Recipient: {}", recipient);
                    logger.info("======================================");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
