@wip @BAD_EXAMPLE @e2e
Feature: Checkout Flow and Form Validation

  Scenario: Checkout page shows customer information form
    Given I have items in my cart
    When I navigate to the checkout page
    Then I should see the customer information form

  Scenario: Submit checkout with valid customer details
    Given I am logged in and have a product in my cart
    When I fill in the checkout form and click Continue
    Then I should reach the checkout overview page

  @critical
  Scenario: Guest cannot checkout with an empty cart
    Given I have an empty cart
    When I navigate directly to the checkout URL
    Then I should be redirected back to the cart page

  Scenario Outline: Checkout fails when required fields are left blank
    Given I am on the checkout information page
    When I leave "<field>" blank and attempt to continue
    Then I should see a validation error for that field

    Examples:
      | field      |
      | first_name |
      | last_name  |
      | zip_code   |