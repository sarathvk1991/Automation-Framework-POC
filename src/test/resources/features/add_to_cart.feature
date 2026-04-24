@cart
Feature: Add Products to Cart
  As a logged-in user
  I want to add products to my shopping cart
  So that I can proceed to purchase them

  Background:
    Given the user is on the login page
    When the user logs in with username "standard_user" and password "secret_sauce"
    And the user is on the inventory page

  # ── Happy path ───────────────────────────────────────────────────────────────

  @smoke
  Scenario: User adds a single product to the cart
    When the user adds "Sauce Labs Backpack" to the cart
    Then the cart count should be "1"
    When the user navigates to the cart
    Then the cart should contain "Sauce Labs Backpack"

  # ── Multi-item ───────────────────────────────────────────────────────────────

  @regression
  Scenario: User adds multiple products to the cart
    When the user adds "Sauce Labs Backpack" to the cart
    And the user adds "Sauce Labs Bike Light" to the cart
    Then the cart count should be "2"
    When the user navigates to the cart
    Then the cart should contain "Sauce Labs Backpack"
    And the cart should contain "Sauce Labs Bike Light"

  # ── Negative ─────────────────────────────────────────────────────────────────

  @negative @regression
  Scenario: Cart is empty when no products have been added
    When the user navigates to the cart
    Then the cart should be empty
