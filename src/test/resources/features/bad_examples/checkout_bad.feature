# =============================================================================
# INTENTIONALLY NON-COMPLIANT — POC GHERKIN LINT DEMONSTRATION
#
# Tagged @wip — excluded from RunCucumberTest runner (filter: "not @wip").
#
# Gherkin Issues Demonstrated:
#   [G1]  No feature-level description
#   [G2]  Scenario names describe UI actions not business outcomes
#   [G3]  Steps expose CSS selectors, element IDs, and hardcoded test data
#   [G4]  No Background — full login+cart sequence repeated in every scenario
#   [G5]  Monster scenario — entire E2E flow in a single scenario
#   [G6]  Scenario uses Then followed by When (out of logical order)
#   [G7]  Inconsistent tags (@CHECKOUT vs @checkout vs @Checkout)
#   [G8]  Coverage gaps — only happy path, no negative or boundary scenarios
# =============================================================================

@wip @BAD_EXAMPLE
Feature: checkout
# ← [G1] No description. Reader cannot tell what business value is being validated.

  # ── [G5] Monster E2E scenario ── [G3] Selectors and data in steps ───────────

  @CHECKOUT @smoke
  Scenario: fill in checkout form and click continue and finish and see confirmation
  # ← [G2] Name describes UI clicks, not the business outcome (order placement)
    Given I navigate to url "https://www.saucedemo.com"
    And I enter text "standard_user" in field with id "user-name"
    And I enter text "secret_sauce" in field with id "password"
    And I click element with id "login-button"
    When I click xpath "//button[@data-test='add-to-cart-sauce-labs-backpack']"
    And I click css "[data-test='shopping-cart-link']"
    And I click css "[data-test='checkout']"
    And I enter text "John" in field with id "first-name"
    And I enter text "Doe" in field with id "last-name"
    And I enter text "12345" in field with id "postal-code"
    And I click css "[data-test='continue']"
    Then element with css ".summary_total_label" is visible
    When I click css "[data-test='finish']"
    Then element with css ".complete-header" is visible

  # ── [G4] Full login+cart repeated ── [G6] Then before When ──────────────────

  @checkout @regression
  Scenario: checkout with two products and confirm order complete page shown
    Given I navigate to url "https://www.saucedemo.com"
    And I enter text "standard_user" in field with id "user-name"
    And I enter text "secret_sauce" in field with id "password"
    And I click element with id "login-button"
    When I click xpath "//button[@data-test='add-to-cart-sauce-labs-backpack']"
    And I click xpath "//button[@data-test='add-to-cart-sauce-labs-bolt-t-shirt']"
    And I click css "[data-test='shopping-cart-link']"
    And I click css "[data-test='checkout']"
    Then I am on the checkout info page
    When I enter text "Jane" in field with id "first-name"
    And I enter text "Smith" in field with id "last-name"
    And I enter text "67890" in field with id "postal-code"
    And I click css "[data-test='continue']"
    Then element with css ".summary_total_label" is visible
    Then I click css "[data-test='finish']"
    And element with css ".complete-header" is visible

  # ── [G7] All three tag variants used inconsistently ─────────────────────────

  @Checkout @CHECKOUT @checkout
  Scenario: check order summary page
  # ← [G2] "check order summary page" says what, not why or what outcome passes
    Given I navigate to url "https://www.saucedemo.com"
    And I enter text "standard_user" in field with id "user-name"
    And I enter text "secret_sauce" in field with id "password"
    And I click element with id "login-button"
    When I click xpath "//button[@data-test='add-to-cart-sauce-labs-onesie']"
    And I click css "[data-test='shopping-cart-link']"
    And I click css "[data-test='checkout']"
    And I enter text "Bob" in field with id "first-name"
    And I enter text "Jones" in field with id "last-name"
    And I enter text "00000" in field with id "postal-code"
    And I click css "[data-test='continue']"
    Then element with css ".summary_subtotal_label" is visible
    And element with css ".summary_tax_label" is visible
    And element with css ".summary_total_label" is visible

  # ── [G8] COVERAGE GAPS (intentionally omitted to demo missing coverage) ─────
  #
  #   ✗ Negative: clicking Continue with all fields empty
  #   ✗ Negative: clicking Continue with only First Name missing
  #   ✗ Negative: clicking Continue with only Last Name missing
  #   ✗ Negative: clicking Continue with only Postal Code missing
  #   ✗ Negative: attempting checkout with an empty cart
  #   ✗ Boundary: postal code with special characters
  #   ✗ Boundary: extremely long first/last name values
  #   ✗ Feature: clicking Cancel on the checkout form returns to cart
  #   ✗ Feature: order total matches sum of item prices plus tax
