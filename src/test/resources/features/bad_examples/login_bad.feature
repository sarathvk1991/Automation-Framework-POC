@wip @BAD_EXAMPLE
Feature: User Authentication via Login Page

  Background:
    Given the application is running and the login page is loaded

  @smoke
  Scenario: Successful login with standard user credentials
    Given I am on the login page
    When I enter valid username and password
    Then I should be redirected to the inventory page

  @regression
  Scenario: Login redirects authenticated user to inventory page
    Given I am on the login page
    When I log in with a valid standard user account
    Then I should land on the products page

  @regression
  Scenario: Locked out user sees error on login
    Given I am on the login page
    When I attempt to log in with a locked out user account
    Then I should see an error banner displayed

  @regression @smoke
  Scenario: Standard user is redirected to products page after login
    Given I navigate to the SauceDemo login URL
    When I enter "standard_user" and "secret_sauce"
    And I click the Login button
    Then the page heading should read "Products"

  @negative
  Scenario Outline: Login with various credential types
    Given I am on the login page
    When I enter "<username>" and "<password>"
    Then I should see the expected outcome for that user type

    Examples:
      | username        | password     |
      | standard_user   | secret_sauce |
      | locked_out_user | secret_sauce |
