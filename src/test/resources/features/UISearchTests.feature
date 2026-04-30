Feature: UI Search Functionality
  As a user, I want to search for items on the website.

  Scenario: Verify filter functionality
    Given I navigate to the URL from test data
    When I click on the search button
    And I enter the item name in the search box
    And I click the search button again
    Then I verify the text matches expected values
