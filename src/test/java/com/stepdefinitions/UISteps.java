package com.stepdefinitions;

import com.locators.HomeLocators;
import com.locators.DBLocator;
import com.pages.HomePage;
import com.pages.DbPage;
import com.utilities.*;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

public class UISteps extends TestBaseClass {  // Extend for driver setup
    private Map<String, String> excelDataMap = new HashMap<>();
    private HomePage homePage = new HomePage();
    private DbPage dbPage = new DbPage();
    private MySqlUtility dbUtil;
    private ArrayList<String> DBList;

    @Given("I navigate to the URL from test data")
    public void navigateToUrl() throws Exception {
        if (DriverFactory.getDriver() == null) {
            DriverFactory.setDriver(ConfigReader.getValue("Env"), ConfigReader.getValue("Browser"));
        }
       // ExtentUtility.startTestInit("BDD UI Test");
        excelDataMap = ExcelUtility.getSheetDataAsMap("TestingData", "Test", DirectoryPaths.testDataExcelPath);
        ReusableMethods.naviagteToUrl(excelDataMap.get("URL"));
    }

    @When("I click on the search button")
    public void clickSearchButton() {
        ReusableMethods.click(HomeLocators.primarySearch, HomeLocators.secondrySearch, "Click on Search button");
    }

    @When("I enter the item name in the search box")
    public void enterItemInSearch() {
        ReusableMethods.sendKeys(HomeLocators.primarySearch, HomeLocators.secondrySearch, excelDataMap.get("Items"), "Entering the item in the search box");
    }

    @When("I click the search button again")
    public void clickSearchButtonAgain() {
        ReusableMethods.click(HomeLocators.primarySearchButton, HomeLocators.secondrySearchButton, "Click on Search button");
    }

    @Then("I verify the text matches expected values")
    public void verifyText() {
        ReusableMethods.verifyText("100", "200");
    }

    // For DB test
    @Given("I connect to the database")
    public void connectToDB() {
       // ExtentUtility.startTestInit("BDD DB Test");
        dbUtil = new MySqlUtility(ConfigReader.getValue("DatabaseName"));
    }

    @When("I query total cases from the Europe table")
    public void queryTotalCases() throws Exception {
        String query = "SELECT country_name, total_cases FROM europe WHERE country_name IS NOT NULL AND TRIM(country_name) <> ''";
        Map<String, String> totalCasesDataMap = dbPage.getDBDetails(dbUtil, query, "total_cases");
        ArrayList<String> countryList = new ArrayList<>(totalCasesDataMap.keySet());
        ArrayList<String> totalCasessList = new ArrayList<>(totalCasesDataMap.values());
        DBList = dbPage.combineData(countryList, totalCasessList);
    }

    @When("I navigate to the Europe page on the UI")
    public void navigateToEuropePage() throws Exception {
        if (DriverFactory.getDriver() == null) {
            DriverFactory.setDriver(ConfigReader.getValue("Env"), ConfigReader.getValue("Browser"));
        }
        excelDataMap = ExcelUtility.getSheetDataAsMap("TestingData", "Test3", DirectoryPaths.testDataExcelPath);
        ReusableMethods.naviagteToUrl(excelDataMap.get("URL"));
        ReusableMethods.click(DBLocator.europe, "Click on Europe");
    }

    @Then("I scroll and compare total cases data")
    public void scrollAndCompare() {
        DbPage.totalCasesScroll(DBList);
    }
}
