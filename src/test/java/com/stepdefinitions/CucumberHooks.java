/*
package com.stepdefinitions;

import com.utilities.CustomWebDriverListener;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.Scenario;

import java.time.Instant;

*/
/**
 * CucumberHooks
 *
 * Bridges Cucumber's lifecycle into CustomWebDriverListener so that
 * pass/fail/skip tracking and Extent Reports work correctly in BDD mode.
 *
 * Register this class in your CucumberOptions runner:
 *
 *   {@literal @}CucumberOptions(
 *       glue = { "com.stepdefinitions" },   // must include this package
 *       ...
 *   )
 *//*

public class CucumberHooks {

    */
/**
     * Called once before the entire Cucumber suite starts.
     * Captures the suite start time.
     *//*

    @BeforeAll
    public static void beforeAll() {
        CustomWebDriverListener.setStartTime(Instant.now().toEpochMilli());
    }

    */
/**
     * Called before every scenario.
     * Initialises the Extent Report test node for this scenario.
     *
     * @param scenario the current Cucumber scenario (injected by Cucumber)
     *//*

    @Before
    public void beforeScenario(Scenario scenario) {
        // Use the scenario name as the "test case name" for reporting
        CustomWebDriverListener.onScenarioStart(scenario.getName());
    }

    */
/**
     * Called after every scenario (pass, fail, or skip).
     * Records the outcome and captures a screenshot on failure.
     *
     * @param scenario the current Cucumber scenario
     *//*

    @After
    public void afterScenario(Scenario scenario) {
        CustomWebDriverListener.onScenarioFinish(scenario);

    }

    */
/**
     * Called once after the entire Cucumber suite finishes.
     * Writes test-summary.txt and test-summary.html.
     *//*

    @AfterAll
    public static void afterAll() {
        CustomWebDriverListener.setEndTime(Instant.now().toEpochMilli());
        CustomWebDriverListener.printAndPersistSummary("Cucumber Suite");
    }
}*/
