package com.locators;

import org.openqa.selenium.By;

import java.util.Arrays;
import java.util.List;

public class HomeLocators {
    /*public static String addToCart="(//button[contains(text(),'ADD TO CART')])[1]";
    public static String searchValue="//input[@type='search']";
    public static String searchButton="//button[@class='search-button']";*/

    public static By primarySearch=By.xpath("//input[@name='field-keywords1']");//twotabsearchtextbox2
    public static List<By> secondrySearch= Arrays.asList(By.xpath("//input[@name='field-keywords']"),
            By.xpath("//input[@placeholder='Search Amazon.in']"),
           By.xpath("//input[contains(@class, 'nav-input')]") );
    public static By primarySearchButton=By.xpath("//input[@id='nav-search-submit-button']");
    public static List<By> secondrySearchButton= Arrays.asList(By.xpath("//input[@value='Go']"),
            By.xpath("//input[contains(@class, 'nav-input')]"),
            By.xpath("//input[@type='submit' and @value='Go']"),
            By.cssSelector("#nav-search-submit-button"),
            By.cssSelector("input[value='Go']"));
    public static By primaryDeliveryDayGetItToday=By.xpath("//a[@aria-label='Apply the filter Get It by Tomorrow to narrow results']");
    public static List<By> secondryDeliveryDayGetItToday= Arrays.asList(By.xpath("//input[@value='Go']"),
            By.xpath("//a[@aria-label='Apply the filter Get It Today to narrow results']//input[@type='checkbox']"));



}
