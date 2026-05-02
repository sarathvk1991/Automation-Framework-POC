Feature: Application Health Check

  Scenario: Application loads successfully
    Given the application URL is accessible
    When I open the home page
    Then the page title should be visible
