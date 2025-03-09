package com.pages;

import com.locators.HomeLocators;
import com.utilities.ReusableMethods;

import java.util.Map;

public class HomePage {

    public void searchFunctionality(Map<String, String> excelDataMap) {
        ReusableMethods.click(HomeLocators.primarySearch, HomeLocators.secondrySearch,"Click on Search button");
        ReusableMethods.sendKeys(HomeLocators.primarySearch, HomeLocators.secondrySearch,excelDataMap.get("Items"),"Entering the item in the search box");
        ReusableMethods.click(HomeLocators.primarySearchButton, HomeLocators.secondrySearchButton, "Click on Search button");
    }


}
