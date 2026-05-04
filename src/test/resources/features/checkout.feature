@checkout
Feature: Checkout Process
  As a logged-in user
  I want to complete the checkout process
  So that I can place an order for products in my cart

  Background:
    Given the user is on the login page
    When the user logs in with username "standard_user" and password "secret_sauce"
    And the user is on the inventory page

  # ── Happy path ───────────────────────────────────────────────────────────────

  @smoke
  Scenario: User successfully completes checkout with a single item
    When the user adds "Sauce Labs Backpack" to the cart
    And the user navigates to the cart
    And the user proceeds to place an order
    And the user enters order details with first name "John", last name "Doe", and postal code "12345"
    And the user continues to order overview
    Then the order total should be displayed
    When the user finishes the order
    Then the order should be placed successfully

  # ── Multi-item ───────────────────────────────────────────────────────────────

  @regression
  Scenario: User completes checkout with multiple items
    When the user adds "Sauce Labs Backpack" to the cart
    And the user adds "Sauce Labs Bolt T-Shirt" to the cart
    And the user navigates to the cart
    And the user proceeds to place an order
    And the user enters order details with first name "Jane", last name "Smith", and postal code "67890"
    And the user continues to order overview
    Then the order total should be displayed
    When the user finishes the order
    Then the order should be placed successfully

  # ── Negative ─────────────────────────────────────────────────────────────────

  @negative @regression
  Scenario: Checkout fails when required fields are missing
    When the user adds "Sauce Labs Backpack" to the cart
    And the user navigates to the cart
    And the user proceeds to place an order
    And the user continues without entering order details
    Then the order error "Error: First Name is required" should be displayed
