Feature: UI and DB Data Comparison
  As a tester, I want to compare UI data with database values.

  Scenario: Verify UI and DB comparison for Europe data
    Given I connect to the database
    When I query total cases from the Europe table
    And I navigate to the Europe page on the UI
    Then I scroll and compare total cases data
