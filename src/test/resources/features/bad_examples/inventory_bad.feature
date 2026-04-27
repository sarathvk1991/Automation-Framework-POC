# =============================================================================
# INTENTIONALLY NON-COMPLIANT — POC GHERKIN LINT DEMONSTRATION
#
# Tagged @wip — excluded from RunCucumberTest runner (filter: "not @wip").
#
# Gherkin Issues Demonstrated:
#   [G1]  No feature-level description
#   [G2]  Scenario names describe implementation not business behaviour
#   [G3]  Steps contain XPath expressions and CSS selectors
#   [G4]  Duplicate steps within the same scenario
#   [G5]  Duplicate scenarios (sort low-to-high tested twice with different names)
#   [G6]  Long scenario mixing sort, cart, and navigation in one test
#   [G7]  Inconsistent tags (@Inventory vs @inventory vs @BAD_INVENTORY)
#   [G8]  Coverage gaps — only one sort direction, no name-sort, no boundary tests
# =============================================================================

@wip @BAD_EXAMPLE
Feature: inventory page stuff
# ← [G1] Feature name is vague; no description block

  # ── [G2][G3] Implementation-coupled step text ─────────────────────────────

  @Inventory @smoke
  Scenario: check if inventory div exists on page
    Given I navigate to url "https://www.saucedemo.com"
    And I enter text "standard_user" in field with id "user-name"
    And I enter text "secret_sauce" in field with id "password"
    And I click element with id "login-button"
    Then css ".inventory_list" is present in DOM

  # ── [G4] Duplicate steps — login repeated, not in Background ────────────────

  @inventory @regression
  Scenario: click price low to high option in sort dropdown and see products
    Given I navigate to url "https://www.saucedemo.com"
    And I enter text "standard_user" in field with id "user-name"
    And I enter text "secret_sauce" in field with id "password"
    And I click element with id "login-button"
    When I enter text "standard_user" in field with id "user-name"
    And I enter text "secret_sauce" in field with id "password"
    When I select option "Price (low to high)" from css "[data-test='product-sort-container']"
    Then prices are in ascending order

  # ── [G5] Duplicate of above scenario with slightly different name ─────────

  @BAD_INVENTORY @regression
  Scenario: prices sorted from lowest to highest when low to high option selected
    Given I navigate to url "https://www.saucedemo.com"
    And I enter text "standard_user" in field with id "user-name"
    And I enter text "secret_sauce" in field with id "password"
    And I click element with id "login-button"
    When I select option "Price (low to high)" from css "[data-test='product-sort-container']"
    Then prices are in ascending order

  # ── [G6] Monster scenario — sort + cart + checkout in one test ───────────

  @inventory @regression @BAD_INVENTORY
  Scenario: sort products by price low to high and add first product and go to cart and checkout and complete order and verify confirmation
    Given I navigate to url "https://www.saucedemo.com"
    And I enter text "standard_user" in field with id "user-name"
    And I enter text "secret_sauce" in field with id "password"
    And I click element with id "login-button"
    When I select option "Price (low to high)" from css "[data-test='product-sort-container']"
    Then prices are in ascending order
    When I click xpath "//button[@data-test='add-to-cart-sauce-labs-onesie']"
    And I click css "[data-test='shopping-cart-link']"
    Then element with css ".cart_item" is visible
    When I click css "[data-test='checkout']"
    And I enter text "Test" in field with id "first-name"
    And I enter text "User" in field with id "last-name"
    And I enter text "99999" in field with id "postal-code"
    And I click css "[data-test='continue']"
    And I click css "[data-test='finish']"
    Then element with css ".complete-header" is visible

  # ── [G8] COVERAGE GAPS (intentionally omitted to demo missing coverage) ─────
  #
  #   ✗ Sort by Name A to Z
  #   ✗ Sort by Name Z to A
  #   ✗ Sort by Price High to Low (negative assertion for ascending order)
  #   ✗ Verify product count matches expected number of catalogue items
  #   ✗ Verify product names are displayed
  #   ✗ Verify product prices are displayed
  #   ✗ Verify product images load
  #   ✗ Boundary: sorting with single product in inventory
  #   ✗ Negative: behaviour when inventory is empty
