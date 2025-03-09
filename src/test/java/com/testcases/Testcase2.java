package com.testcases;

import com.pages.DbPage;
import com.utilities.ConfigReader;
import com.utilities.MySqlUtility;
import org.testng.annotations.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.utilities.ExcelUtility.getColumnValues;
import static com.utilities.ExcelUtility.getColumnValuesAsLong;


public class Testcase2 {
    static MySqlUtility dbUtil; // Replace with your class name


    @Test
    public void  test() throws SQLException {

        String filePath = "C:\\Users\\anild\\IdeaProjects\\ui_automation\\src\\test\\resources\\TestDataFile\\Europedata.xlsx"; // Update file path
        String sheetName = "Europe"; // Change this to any sheet name
        String columnName = "Population"; // Change this to any column name


        System.out.println("Reading DB Details");
        if (ConfigReader.getValue("DbEnv").equalsIgnoreCase("Qa")){
            dbUtil = new MySqlUtility(ConfigReader.getValue("DatabaseName"));
        }

        String query="SELECT country_name FROM europe \n" +
                "WHERE country_name IS NOT NULL \n" +
                "AND TRIM(country_name) <> '';";
        List<Map<String, Object>> countries= dbUtil.convertMulitpleDBResultToHashMap(dbUtil.executeQuery(dbUtil.getSQLConnection(), query));

        System.out.println(countries);
        ArrayList<String> countriesListDB= MySqlUtility.processDatabaseMap(countries,"country_name");
        System.out.println(countriesListDB);
        List<Long> columnValues = getColumnValuesAsLong(filePath, sheetName, columnName);

        System.out.println("Column '" + columnName + "' Values: " + columnValues);

        DbPage.insertValuesInDB(countriesListDB,columnValues);

        MySqlUtility.insertValuesInDB(dbUtil.getSQLConnection(),countriesListDB, columnValues,"population");

    }



}
