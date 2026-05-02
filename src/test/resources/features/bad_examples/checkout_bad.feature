@wip @BAD_EXAMPLE
Feature:
  # Covers end-to-end checkout flows including form validation and order confirmation

  @e2e
  Scenario:
    Given I have items in my cart
    When I navigate to the checkout page
    Then I should see the customer information form


  @e2e
  Scenario: Submit checkout with valid customer details
    Given I am logged in and have a product in my cart
  When I fill in the checkout form and click Continue
    Then I should reach the checkout overview page

  @e2e
  @critical # @smoke
  Scenario: Guest cannot checkout with an empty cart
    Given I have an empty cart
    When I navigate directly to the checkout URL
    Then I should be redirected back to the cart page

  @e2e
  Scenario Outline: Checkout fails when required fields are left blank
    Given I am on the checkout information page
    When I leave "<field>" blank and attempt to continue
    Then I should see a validation error for that field
