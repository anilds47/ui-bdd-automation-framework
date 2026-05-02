@Mobile
Feature: Mobile Application Testing

  Background:
    Given I launch mobile browser on "Android" device with UDID "emulator-5554"

  @mobile @smoke
  Scenario: Mobile browser navigation test
    When I navigate to mobile URL "https://www.google.com"
    Then I should see mobile element with "id" "hplogo"
    When I tap on mobile element with "name" "q"
    And I enter "Appium testing" in mobile field with "name" "q"
    Then I should see mobile element with "name" "btnK"

  @mobile @regression
  Scenario: Mobile scrolling and swiping test
    When I navigate to mobile URL "https://www.google.com"
    And I scroll down on mobile screen
    And I swipe left on mobile screen
    And I swipe right on mobile screen
    Then I should see mobile element with "id" "hplogo"

  @mobile @native
  Scenario: Native mobile app test (requires app installation)
    Given I launch the mobile app on "Android" device with UDID "emulator-5554"
    When I tap on mobile element with "accessibilityId" "login_button"
    And I enter "testuser" in mobile field with "id" "username_field"
    And I enter "password123" in mobile field with "id" "password_field"
    When I tap on mobile element with "id" "submit_button"
    Then I should see mobile element with "id" "welcome_message"
    And mobile element with "id" "welcome_message" should contain text "Welcome"

  @mobile @gestures
  Scenario: Mobile gestures test
    Given I launch the mobile app on "Android" device with UDID "emulator-5554"
    When I long press on mobile element with "id" "menu_item"
    Then I should see mobile element with "id" "context_menu"
    When I scroll down on mobile screen
    And I swipe left on mobile screen
    Then I should see mobile element with "id" "next_page"
