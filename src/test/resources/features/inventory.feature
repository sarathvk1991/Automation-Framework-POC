@inventory
Feature: Product Inventory — Browse and Sort
  As a logged-in user
  I want to browse the product inventory
  So that I can find and select products to purchase

  Background:
    Given the user is on the login page
    When the user logs in with username "standard_user" and password "secret_sauce"

  # ── Happy path ───────────────────────────────────────────────────────────────

  @smoke
  Scenario: Product inventory is displayed after successful login
    Then the user should land on the inventory page
    And the product list should be displayed

  # ── Sorting ──────────────────────────────────────────────────────────────────

  @regression
  Scenario: Products can be sorted by price from low to high
    Given the user is on the inventory page
    When the user sorts products by "Price (low to high)"
    Then the products should be sorted by price from low to high

  # ── Negative ─────────────────────────────────────────────────────────────────

  @negative @regression
  Scenario: Products sorted by price high to low are not in ascending price order
    Given the user is on the inventory page
    When the user sorts products by "Price (high to low)"
    Then the products should not be sorted by price from low to high
