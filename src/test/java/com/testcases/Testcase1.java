package com.testcases;

import com.locators.HomeLocators;
import com.pages.HomePage;
import com.utilities.DirectoryPaths;
import com.utilities.ExcelUtility;
import com.utilities.ReusableMethods;
import com.utilities.TestBaseClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class Testcase1 extends TestBaseClass {
    Map<String, String> excelDataMap= new HashMap<String, String>();
    HomePage homePage=new HomePage();
  /* @Test(priority = 1)
    public void verify_search_functionality() throws Exception {
        excelDataMap=  ExcelUtility.getSheetDataAsMap("TestingData","Test", DirectoryPaths.testDataExcelPath);
        System.out.println(excelDataMap);
        ReusableMethods.naviagteToUrl(excelDataMap.get("URL"));
        homePage.searchFunctionality(excelDataMap);


    }*/

   @Test(priority = 1)
    public void verify_filter_functionalityTest() throws Exception {
        excelDataMap=  ExcelUtility.getSheetDataAsMap("TestingData","Test",DirectoryPaths.testDataExcelPath);
        System.out.println(excelDataMap);
        ReusableMethods.naviagteToUrl(excelDataMap.get("URL"));

        ReusableMethods.click(HomeLocators.primarySearch, HomeLocators.secondrySearch, "Click on Search button");
        ReusableMethods.sendKeys(HomeLocators.primarySearch, HomeLocators.secondrySearch,excelDataMap.get("Items"), "Entering the item in the search box");
        ReusableMethods.click(HomeLocators.primarySearchButton, HomeLocators.secondrySearchButton, "Click on Search button");
       // ReusableMethods.click(HomeLocators.primaryDeliveryDayGetItToday, HomeLocators.secondryDeliveryDayGetItToday, "Click on Search button");
        ReusableMethods.verifyText("100","200");
    }

}

