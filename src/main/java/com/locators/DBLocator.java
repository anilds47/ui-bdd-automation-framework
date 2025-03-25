package com.locators;

import org.openqa.selenium.By;

public class DBLocator {
    public static By europe=By.xpath("(//a[@href='#c-europe\"'][normalize-space()='Europe'])[2]");
    public static By countryColumn=By.xpath("//table[@id='main_table_countries_today']/child::*/tr/td/a[@class='mt_a']");
    public static By totalCasesColumn=By.xpath("//table[@id='main_table_countries_today']/child::*/tr/td[@class='sorting_1']");



}
