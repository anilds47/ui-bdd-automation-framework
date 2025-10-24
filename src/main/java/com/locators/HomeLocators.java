package com.locators;

import org.openqa.selenium.By;

import java.util.Arrays;
import java.util.List;

public class HomeLocators {
    /*public static String addToCart="(//button[contains(text(),'ADD TO CART')])[1]";
    public static String searchValue="//input[@type='search']";
    public static String searchButton="//button[@class='search-button']";*/

    public static By primarySearch=By.xpath("//input[@type='search1']");//twotabsearchtextbox2
    public static List<By> secondrySearch= Arrays.asList(
            By.xpath("//input[@type='search']"),
            By.xpath("//input[@placeholder='Search for Vegetables and Fruits']"),
            By.xpath("//input[@class='search-keyword']") );

    public static By primarySearchButton=By.xpath("//button[@type='submit']");
    public static List<By> secondrySearchButton= Arrays.asList(
            By.xpath("//button[@class='search-button']"),
            By.cssSelector("button[type='submit']"));

    public static By primaryDeliveryDayGetItToday=By.xpath("//a[@aria-label='Apply the filter Get It by Tomorrow to narrow results']");
    public static List<By> secondryDeliveryDayGetItToday= Arrays.asList(
            By.xpath("//input[@value='Go']"),
            By.xpath("//a[@aria-label='Apply the filter Get It Today to narrow results']//input[@type='checkbox']"));



}
