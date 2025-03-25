package com.pages;

import com.locators.DBLocator;
import com.utilities.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DbPage {
    static HashMap<String, String> UIdata = new HashMap<String, String>();

    public Map<String, String> getDBDetails(MySqlUtility sql, String query, String columnName) throws IOException, SQLException {
        List<Map<String, Object>> sQLDataMap = executeSQLQuery(sql, query);
        Map<String, String> testDataMap = new HashMap<>();
        for (Map<String, Object> s : sQLDataMap) {
            String country = String.valueOf(s.get("country_name"));

            testDataMap.put(country, String.valueOf(s.get(columnName)));
        }

        return testDataMap;
    }

    private List<Map<String, Object>> executeSQLQuery(MySqlUtility sql, String query) throws SQLException {
        return sql.convertMulitpleDBResultToHashMap(sql.executeQuery(sql.getSQLConnection(), query));

    }

    public ArrayList<String> combineData(ArrayList<String> keys, ArrayList<String> values) {
        ArrayList<String> combinedList = new ArrayList<>();
        for (int i = 0; i < keys.size() && i < values.size(); i++) {
            combinedList.add(keys.get(i) + "=" + values.get(i));
        }
        return combinedList;
    }

    public static void totalCasesScroll(ArrayList<String> dbList) {
        List<WebElement> countryColumn = DriverFactory.getDriver().findElements(DBLocator.countryColumn);
        List<WebElement> totalCasesColumns = DriverFactory.getDriver().findElements(DBLocator.totalCasesColumn);
        System.out.println(countryColumn.size());
        System.out.println(totalCasesColumns.size());

        for (int i = 0; i < countryColumn.size(); i++) {
            UIdata.put(countryColumn.get(i).getText().replaceAll("\\s+", " "), totalCasesColumns.get(i + 1).getText().replaceAll("\\s+", " ").replaceAll(",", ""));
        }

        System.out.println(UIdata);

        ArrayList<String> countryList = new ArrayList<>(UIdata.keySet());
        ArrayList<String> totalCases = new ArrayList<>(UIdata.values());
        System.out.println(countryList);
        System.out.println(totalCases);
        ArrayList<String> UIList=new ArrayList<>();
        for(int i=0; i< countryList.size() && i< totalCases.size();i++){
            String combined= countryList.get(i)+"="+totalCases.get(i);
            UIList.add(combined);
        }
        ReusableMethods.compareDbDataAndUiData(dbList,UIList);



    }




}
