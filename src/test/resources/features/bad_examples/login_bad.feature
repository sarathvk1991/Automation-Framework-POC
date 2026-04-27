# =============================================================================
# INTENTIONALLY NON-COMPLIANT — POC GHERKIN LINT DEMONSTRATION
#
# This file is tagged @wip at the Feature level so it is completely excluded
# from the main RunCucumberTest runner (filter: "not @wip"). It exists solely
# to demonstrate Gherkin quality violations that a linter would flag.
#
# Gherkin Issues Demonstrated:
#   [G1]  No feature-level description (missing As a / I want / So that)
#   [G2]  Vague, non-descriptive scenario names ("test login", "login works")
#   [G3]  Implementation details in step text (CSS selectors, element IDs, URLs)
#   [G4]  No Background — login steps copy-pasted into every scenario
#   [G5]  Duplicate scenarios (identical steps, only the name differs)
#   [G6]  Scenario starts with When — no Given context established
#   [G7]  One step combines multiple actions (login + navigate + assert)
#   [G8]  Monster scenario mixing login, cart, and checkout concerns
#   [G9]  Inconsistent tag casing (@bad_login vs @BadLogin vs @BAD_LOGIN vs @SMOKE)
#   [G10] Coverage gaps — only happy path, all negative/boundary cases missing
# =============================================================================

@wip @BAD_EXAMPLE
Feature: login
# ← [G1] No description. Good files have "As a... / I want... / So that..."

  # ── [G2] Vague name ── [G3] Steps expose element IDs ── [G4] No Background ──

  @bad_login @smoke
  Scenario: test login
    Given I navigate to url "https://www.saucedemo.com"
    When I enter text "standard_user" in field with id "user-name"
    And I enter text "secret_sauce" in field with id "password"
    And I click element with id "login-button"
    Then element with css ".inventory_list" is visible

  # ── [G5] Duplicate — identical steps to "test login", only name changed ──────

  @BadLogin
  Scenario: login works
    Given I navigate to url "https://www.saucedemo.com"
    When I enter text "standard_user" in field with id "user-name"
    And I enter text "secret_sauce" in field with id "password"
    And I click element with id "login-button"
    Then element with css ".inventory_list" is visible

  # ── [G6] No Given ── [G7] One step does everything ──────────────────────────

  @bad_login
  Scenario: check thing after doing login
    When I login as "standard_user" with "secret_sauce" and verify dashboard loads and check title
    Then page title is "Swag Labs"

  # ── [G8] Monster scenario ── [G9] Mixed tag casing ──────────────────────────

  @BAD_EXAMPLE @regression @bad_login @SMOKE
  Scenario: user logs in and goes to inventory and adds item to cart and navigates to cart and proceeds to checkout all in one test
    Given I navigate to url "https://www.saucedemo.com"
    When I enter text "standard_user" in field with id "user-name"
    And I enter text "secret_sauce" in field with id "password"
    And I click element with id "login-button"
    Then element with css ".inventory_list" is visible
    When I click xpath "//button[@data-test='add-to-cart-sauce-labs-backpack']"
    And I click css "[data-test='shopping-cart-link']"
    Then element with css ".shopping_cart_badge" has text "1"
    When I click css "[data-test='checkout']"
    And I enter text "John" in field with id "first-name"
    And I enter text "Doe" in field with id "last-name"
    And I enter text "12345" in field with id "postal-code"
    And I click element with id "continue"
    Then element with css ".summary_total_label" is visible

  # ── [G10] COVERAGE GAPS (intentionally omitted to demo missing coverage) ─────
  #
  #   ✗ Negative: login with wrong password
  #   ✗ Negative: login with empty username
  #   ✗ Negative: login with empty password
  #   ✗ Negative: login with both fields empty
  #   ✗ Negative: locked_out_user cannot access the system
  #   ✗ Boundary: maximum-length username / password
  #   ✗ Security: SQL injection characters in input fields
  #   ✗ UX: error message clears after correcting credentials
