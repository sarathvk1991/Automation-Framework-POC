# =============================================================================
# INTENTIONALLY NON-COMPLIANT — POC GHERKIN LINT DEMONSTRATION
#
# Tagged @wip — excluded from RunCucumberTest runner (filter: "not @wip").
#
# Gherkin Issues Demonstrated:
#   [G1]  No feature-level description
#   [G2]  Vague scenario names ("add stuff", "cart test 1")
#   [G3]  Steps contain XPath selectors and data-test attribute references
#   [G4]  No Background — full login sequence copy-pasted into every scenario
#   [G5]  Duplicate scenarios (add single item — two scenarios do the same thing)
#   [G6]  And used as first step keyword (no preceding Given/When to chain from)
#   [G7]  Inconsistent tags (@CART vs @cart vs @Cart)
#   [G8]  Coverage gaps — no empty cart, no remove item, no quantity boundary
# =============================================================================

@wip @BAD_EXAMPLE
Feature: cart
# ← [G1] Feature name "cart" has no description, no business context

  # ── [G2][G3][G4] Vague name, selectors in steps, no Background ──────────────

  @CART @smoke
  Scenario: add stuff
    Given I navigate to url "https://www.saucedemo.com"
    And I enter text "standard_user" in field with id "user-name"
    And I enter text "secret_sauce" in field with id "password"
    And I click element with id "login-button"
    When I click xpath "//button[@data-test='add-to-cart-sauce-labs-backpack']"
    Then element with css ".shopping_cart_badge" has text "1"

  # ── [G5] Duplicate of "add stuff" — same test, renamed ────────────────────

  @cart @smoke
  Scenario: cart test 1
    Given I navigate to url "https://www.saucedemo.com"
    And I enter text "standard_user" in field with id "user-name"
    And I enter text "secret_sauce" in field with id "password"
    And I click element with id "login-button"
    When I click xpath "//button[@data-test='add-to-cart-sauce-labs-backpack']"
    Then element with css ".shopping_cart_badge" has text "1"

  # ── [G6] Starts with And (no Given preceding it) ─────────────────────────

  @Cart @regression
  Scenario: add two items to cart
    And I navigate to url "https://www.saucedemo.com"
    And I enter text "standard_user" in field with id "user-name"
    And I enter text "secret_sauce" in field with id "password"
    And I click element with id "login-button"
    When I click xpath "//button[@data-test='add-to-cart-sauce-labs-backpack']"
    And I click xpath "//button[@data-test='add-to-cart-sauce-labs-bike-light']"
    Then element with css ".shopping_cart_badge" has text "2"
    When I click css "[data-test='shopping-cart-link']"
    Then element with css ".cart_item" is visible

  # ── [G7] Mixed tag casing ── [G3] XPath in step ────────────────────────────

  @CART @Cart @cart @regression
  Scenario: go to cart and see product name in cart item div
    Given I navigate to url "https://www.saucedemo.com"
    And I enter text "standard_user" in field with id "user-name"
    And I enter text "secret_sauce" in field with id "password"
    And I click element with id "login-button"
    When I click xpath "//button[@data-test='add-to-cart-sauce-labs-backpack']"
    And I click css "[data-test='shopping-cart-link']"
    Then xpath "//div[@class='inventory_item_name' and text()='Sauce Labs Backpack']" is visible

  # ── [G8] COVERAGE GAPS (intentionally omitted to demo missing coverage) ─────
  #
  #   ✗ Negative: navigating to cart when no items have been added (empty cart)
  #   ✗ Negative: cart badge not shown when cart is empty
  #   ✗ Boundary: adding the same product twice
  #   ✗ Boundary: adding all available products (maximum cart size)
  #   ✗ Feature: removing an item from the cart
  #   ✗ Feature: cart persists after page refresh
  #   ✗ Feature: cart count shown correctly after removing one of multiple items
