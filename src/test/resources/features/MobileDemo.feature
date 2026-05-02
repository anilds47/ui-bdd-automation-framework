Feature: Scroll and open Tabs from Views

  Scenario: User navigates to Tabs section
    #Given I launch the mobile app on "Android" device with UDID "emulator-5554"
    When User is on the home screen
    When User clicks on Views
    And User scrolls until Tabs is visible and clicks
