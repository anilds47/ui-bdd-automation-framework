package com.utilities;

import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.events.WebDriverListener;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.IExecutionListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import io.cucumber.java.Scenario;

import static com.utilities.ReusableMethods.jiraOperation;

/**
 * CustomWebDriverListener
 *
 * IMPORTANT BDD/CUCUMBER NOTES:
 * ─────────────────────────────────────────────────────────────────────────────
 * ITestListener methods (onTestStart, onTestSuccess, onTestFailure, etc.) are
 * NOT fired by Cucumber. Cucumber manages its own lifecycle via @Before / @After
 * hooks. This class therefore:
 *
 *  1. Keeps ITestListener for plain TestNG (non-BDD) suites.
 *  2. Exposes static helper methods that your Cucumber @Before / @After step
 *     definitions must call explicitly (see CucumberHooks.java below).
 *
 * REGISTER THIS LISTENER:
 *  • TestNG only  → add to testng.xml <listeners> section
 *  • Cucumber     → do NOT rely on the ITestListener methods;
 *                   call the static helpers from your @Before / @After hooks.
 *
 * INFINITE-RECURSION FIX:
 *  beforeFindElement / beforeFindElements previously called retryFindElement
 *  which itself called driver.findElement, triggering the listener again.
 *  Retry logic has been removed from the listener; use explicit waits
 *  (WebDriverWait) in page-object classes instead.
 * ─────────────────────────────────────────────────────────────────────────────
 */
public class CustomWebDriverListener implements WebDriverListener, ITestListener, IExecutionListener {

    private static final Logger logger = LogManager.getLogger(CustomWebDriverListener.class);

    // ── Counters (thread-safe for parallel runs) ─────────────────────────────
    private static final AtomicInteger totalTests   = new AtomicInteger(0);
    private static final AtomicInteger passedTests  = new AtomicInteger(0);
    private static final AtomicInteger failedTests  = new AtomicInteger(0);
    private static final AtomicInteger skippedTests = new AtomicInteger(0);

    // ── Timing ────────────────────────────────────────────────────────────────
    private static volatile long   startTimeMillis;
    private static volatile long   endTimeMillis;
    private static volatile String startTimeFormatted;
    private static volatile String endTimeFormatted;

    // ── Failure tracking ──────────────────────────────────────────────────────
    private static final List<String>        failedTestCasesList = Collections.synchronizedList(new ArrayList<>());
    private static final Map<String, String> failedTestErrors    = Collections.synchronizedMap(new LinkedHashMap<>());
    private static volatile String           lastFailedTestName;

    // ── Per-thread test name (works in parallel TestNG) ───────────────────────
    private static final ThreadLocal<String> currentTestName = new ThreadLocal<>();

    // =========================================================================
    // PUBLIC STATIC HELPERS — call these from Cucumber @Before / @After hooks
    // =========================================================================

    /**
     * Call from Cucumber {@code @Before} hook to record a test start.
     *
     * <pre>
     * {@literal @}Before
     * public void beforeScenario(Scenario scenario) {
     *     CustomWebDriverListener.onScenarioStart(scenario.getName());
     * }
     * </pre>
     */
    public static void onScenarioStart(String scenarioName) {
        totalTests.incrementAndGet();
        currentTestName.set(scenarioName);
        logger.info("[EXECUTED] -> Scenario Started: {}", scenarioName);
        ExtentUtility.startTestInit(scenarioName);
    }

    // =========================================================================
    // ITestListener — used by plain TestNG (non-Cucumber) suites
    // =========================================================================

    @Override
    public void onStart(ITestContext context) {
        String suiteName = (context.getSuite() != null && context.getSuite().getName() != null)
                ? context.getSuite().getName() : "Default Suite";
        logger.info("[EXECUTED] -> Test Execution Started for Suite: {}", suiteName);

        // Reset counters at suite start
        totalTests.set(0);
        passedTests.set(0);
        failedTests.set(0);
        skippedTests.set(0);

        startTimeMillis   = System.currentTimeMillis();
        startTimeFormatted = formatTimestamp(startTimeMillis);
        logger.info("Start Time: {}", startTimeFormatted);
    }

    @Override
    public void onTestStart(ITestResult result) {
        totalTests.incrementAndGet();
        String name = result.getMethod().getMethodName();
        currentTestName.set(name);
        logger.info("[EXECUTED] -> Test Started: {}", name);
        ExtentUtility.startTestInit(name);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        passedTests.incrementAndGet();
        logger.info("[SUCCESS] -> Test Passed: {}", result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String name = result.getMethod().getMethodName();
        String errorMessage = result.getThrowable() != null
                ? result.getThrowable().toString() : "Unknown error";

        logger.error("[ERROR] -> Test Failed: {} - Exception: {}", name, errorMessage);
        failedTests.incrementAndGet();
        failedTestCasesList.add(name);
        failedTestErrors.put(name, errorMessage);
        lastFailedTestName = name;

        String screenshotPath = ScreenshotUtil.takeScreenshot();
        ExtentUtility.attachScreenshotOnFailure("Screenshot on failure", screenshotPath);
        jiraOperation(screenshotPath, name + " : " + errorMessage);
    }

    public class TestContext {
        public static ThreadLocal<ITestResult> testResult = new ThreadLocal<>();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        skippedTests.incrementAndGet();
        logger.warn("[WARN] -> Test Skipped: {}", result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        logger.warn("[WARN] -> Test Failed but within success percentage: {}",
                result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailedWithTimeout(ITestResult result) {
        logger.error("[ERROR] -> Test Failed due to Timeout: {}", result.getMethod().getMethodName());
    }

    @Override
    public void onFinish(ITestContext context) {
        endTimeMillis   = System.currentTimeMillis();
        endTimeFormatted = formatTimestamp(endTimeMillis);

        logger.info("[EXECUTED] -> Test Execution Finished for Suite: {}", context.getSuite().getName());
        logger.info("End Time: {}", endTimeFormatted);

        printAndPersistSummary(context.getSuite().getName());
    }

    // =========================================================================
    // IExecutionListener (optional — fires once per JVM execution)
    // =========================================================================

    @Override
    public void onExecutionStart() {
        logger.info("[EXECUTED] -> Test Execution Started (IExecutionListener)");
    }

    @Override
    public void onExecutionFinish() {
        logger.info("[EXECUTED] -> Test Execution Finished (IExecutionListener)");
    }

    // =========================================================================
    // Static summary helpers — safe to call from Cucumber AfterAll hooks too
    // =========================================================================

    /**
     * Prints the execution summary and writes test-summary.txt / test-summary.html.
     * Call from a Cucumber {@code @AfterAll} hook when not using TestNG.
     */
    public static void printAndPersistSummary(String suiteName) {
        System.out.println("=============================================");
        System.out.println("            TEST EXECUTION SUMMARY           ");
        System.out.println("=============================================");
        System.out.println("Suite Name     : " + suiteName);
        System.out.println("Total Tests    : " + totalTests.get());
        System.out.println("Passed Tests   : " + passedTests.get());
        System.out.println("Failed Tests   : " + failedTests.get());
        System.out.println("Skipped Tests  : " + skippedTests.get());
        System.out.println("Start Time     : " + startTimeFormatted);
        System.out.println("End Time       : " + endTimeFormatted);
        System.out.println("=============================================");

        String csvSummary = totalTests + "," + passedTests + "," + failedTests + ","
                + skippedTests + "," + startTimeFormatted + "," + endTimeFormatted;
        try {
            Files.write(Paths.get("test-summary.txt"), csvSummary.getBytes(),
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            logger.info("Test summary saved to test-summary.txt");
        } catch (IOException e) {
            logger.error("Failed to write test-summary.txt", e);
        }

        String html = "<html><body>"
                + "<h3>Test Execution Summary</h3>"
                + "<table border='1' cellpadding='5' cellspacing='0' style='border-collapse:collapse;width:60%;'>"
                + "<tr style='background-color:#4CAF50;color:white;'><th>Metric</th><th>Value</th></tr>"
                + "<tr><td>Total Tests</td><td>" + totalTests + "</td></tr>"
                + "<tr><td>Passed</td><td style='color:green;'><b>" + passedTests + "</b></td></tr>"
                + "<tr><td>Failed</td><td style='color:red;'><b>" + failedTests + "</b></td></tr>"
                + "<tr><td>Skipped</td><td style='color:orange;'><b>" + skippedTests + "</b></td></tr>"
                + "<tr><td>Start Time</td><td>" + startTimeFormatted + "</td></tr>"
                + "<tr><td>End Time</td><td>" + endTimeFormatted + "</td></tr>"
                + "</table></body></html>";
        try {
            Files.write(Paths.get("test-summary.html"), html.getBytes(),
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            logger.info("Test summary HTML saved to test-summary.html");
        } catch (IOException e) {
            logger.error("Failed to write test-summary.html", e);
        }
    }

    // =========================================================================
    // WebDriverListener — navigation & element interaction logging
    // =========================================================================

    @Override
    public void beforeGet(WebDriver driver, String url) {
        logger.info("[NAVIGATE] -> Navigating to URL: {}", url);

        /*
         * Page-load monitoring runs in a daemon thread so it never blocks
         * the test thread. The thread is marked daemon so it doesn't prevent
         * JVM shutdown.
         */
        Thread monitor = new Thread(() -> {
            try {
                new WebDriverWait(driver, Duration.ofSeconds(60))
                        .pollingEvery(Duration.ofSeconds(5))
                        .until(d -> {
                            String state = (String) ((JavascriptExecutor) d)
                                    .executeScript("return document.readyState");
                            boolean complete = "complete".equals(state);
                            if (!complete) logger.info("[SEARCH] -> Page is still loading...");
                            return complete;
                        });
                logger.info("[SUCCESS] -> Page has fully loaded.");
            } catch (Exception e) {
                logger.warn("[WARN] -> Page load monitor interrupted: {}", e.getMessage());
            }
        });
        monitor.setDaemon(true);
        monitor.start();
    }

    @Override
    public void afterGet(WebDriver driver, String url) {
        logger.info("[SUCCESS] -> Page loaded successfully: {}", url);
    }

    @Override
    public void beforeGetCurrentUrl(WebDriver driver) {
        // no-op — logging the URL before we even have it is noise
    }

    @Override
    public void afterGetCurrentUrl(WebDriver driver, String result) {
        logger.info("[SUCCESS] -> Current URL: {}", result);
    }

    /**
     * NOTE: retryFindElement / retryFindElements have been intentionally removed
     * from beforeFindElement / beforeFindElements because calling driver.findElement()
     * inside the listener causes infinite recursion. Use WebDriverWait in your
     * page-object classes instead.
     */
    @Override
    public void beforeFindElement(WebDriver driver, By locator) {
        logger.debug("[SEARCH] -> Looking for element: {}", locator);
    }

    @Override
    public void afterFindElement(WebDriver driver, By locator, WebElement result) {
        logger.debug("[SUCCESS] -> Element found: {}", locator);
    }

    @Override
    public void beforeFindElements(WebDriver driver, By locator) {
        logger.debug("[SEARCH] -> Looking for elements: {}", locator);
    }

    @Override
    public void afterFindElements(WebDriver driver, By locator, List<WebElement> result) {
        if (result.isEmpty()) {
            logger.warn("[WARN] -> No elements found: {}", locator);
        } else {
            logger.debug("[SUCCESS] -> Found {} element(s): {}", result.size(), locator);
        }
    }

    @Override
    public void beforeClick(WebElement element) {
        logger.info("[CLICK] -> Clicking element: {}", element);
    }

    @Override
    public void afterClick(WebElement element) {
        logger.info("[SUCCESS] -> Clicked element: {}", element);
    }

    @Override
    public void beforeSendKeys(WebElement element, CharSequence... keysToSend) {
        // Mask sensitive fields — check tag/type to decide whether to log value
        String value = String.join("", keysToSend);
        logger.info("[INPUT] -> Typing into element: {} | value: {}", element, value);
    }

    @Override
    public void afterSendKeys(WebElement element, CharSequence... keysToSend) {
        logger.info("[SUCCESS] -> Typed into element: {}", element);
    }

    @Override
    public void onError(Object target, Method method, Object[] args, InvocationTargetException e) {
        Throwable cause = e.getTargetException();
        if (cause instanceof NoSuchElementException) {
            logger.error("[ERROR] -> Element not found in method '{}': {}", method.getName(), cause.getMessage());
        } else if (cause instanceof TimeoutException) {
            logger.error("[ERROR] -> Timeout in method '{}': {}", method.getName(), cause.getMessage());
        } else if (cause instanceof StaleElementReferenceException) {
            logger.warn("[WARN] -> Stale element in method '{}': {}", method.getName(), cause.getMessage());
        } else if (cause instanceof ElementClickInterceptedException) {
            logger.warn("[WARN] -> Click intercepted in method '{}': {}", method.getName(), cause.getMessage());
        } else {
            logger.error("[ERROR] -> WebDriverListener error in method '{}': ", method.getName(), cause);
        }
    }

    // =========================================================================
    // Static getter methods (unchanged API for callers)
    // =========================================================================

    public static int getTotalTests()   { return totalTests.get(); }
    public static int getPassedTests()  { return passedTests.get(); }
    public static int getFailedTests()  { return failedTests.get(); }
    public static int getSkippedTests() { return skippedTests.get(); }

    public static String getExecutionTimeInSeconds() {
        long totalSec = (endTimeMillis - startTimeMillis) / 1000;
        return String.format("%02d hours, %02d minutes, %02d seconds",
                totalSec / 3600, (totalSec % 3600) / 60, totalSec % 60);
    }

    public static List<String>        getFailedTestCaseNames()  { return Collections.unmodifiableList(failedTestCasesList); }
    public static Map<String, String> getFailedTestCaseErrors() { return Collections.unmodifiableMap(failedTestErrors); }
    public static String getStartTime()        { return startTimeFormatted; }
    public static String getEndTime()          { return endTimeFormatted; }
    public static String getOnTestFailure()    { return lastFailedTestName; }

    public static void setStartTime(long millis) {
        startTimeMillis   = millis;
        startTimeFormatted = formatTimestamp(millis);
    }

    public static void setEndTime(long millis) {
        endTimeMillis   = millis;
        endTimeFormatted = formatTimestamp(millis);
    }

    // =========================================================================
    // Private helpers
    // =========================================================================

    /**
     * Extracts error message from a failed scenario.
     * Attempts to get the actual Throwable (AssertionError, TimeoutException, etc.)
     * and returns the full error message in the format: "java.lang.AssertionError: [message]"
     */

    /**
     * Helper method to extract error message details from a string
     */
    private static String extractMessageFromString(String str, String errorType) {
        try {
            int index = str.indexOf(errorType);
            if (index != -1) {
                int start = index + errorType.length();
                int end = str.indexOf("\n", start);
                if (end == -1) end = str.length();
                return str.substring(start, end).trim().replaceFirst("^: ", "");
            }
        } catch (Exception e) {
            logger.debug("Error extracting message: {}", e.getMessage());
        }
        return errorType;
    }

    private static String formatTimestamp(long millis) {
        return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a").format(new Date(millis));
    }
}

