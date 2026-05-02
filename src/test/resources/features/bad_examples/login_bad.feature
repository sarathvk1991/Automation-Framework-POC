@wip @BAD_EXAMPLE
Feature: SauceDemo Regression Tests — E2E Coverage for All User Flows Including Login Authentication and Cart Management
  # [LINT] name-length: Feature name exceeds 70-char maximum

  Background:
    Given the application is running and the login page is loaded

  @smoke
  Scenario: Successful login with standard user credentials
    Given I am on the login page
    When I enter valid username and password   
    Then I should be redirected to the inventory page

  @regression
  Scenario: Verify login redirect
    Given I am on the login page
    When I log in with a valid standard user account
    Then I should land on the products page

  @regression
  Scenario: Verify login redirect
    Given I am on the login page
    When I attempt to log in with a locked out user account
    Then I should see an error banner displayed

  @regression @smoke @smoke
  Scenario: user enters standard credentials on the login page and clicks login and verifies they are redirected to the inventory products page
    Given I navigate to the SauceDemo login URL
    When I enter "standard_user" and "secret_sauce"
    And I click the Login button
    Then the page heading should read "Products"

  @negative
  Scenario Outline: Login with various credential types
    Given I am on the login page
    When I enter "<username>" and "<password>"
    Then I should see the expected outcome for that user type