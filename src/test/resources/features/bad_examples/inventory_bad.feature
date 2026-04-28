# Intentional bad Gherkin examples for lint failure demo
@wip @BAD_EXAMPLE
Feature: Inventory Bad Feature — Demonstrates Intentional Gherkin Lint Anti-Patterns For Lint Demo
# [LINT] name-length: Feature name exceeds 70-char maximum

  # ── Violation: no-homogenous-tags ───────────────────────────────────────────
  # @smoke appears on every scenario below. It should be on the Feature.

  # ── Violation: no-unnamed-scenarios ─────────────────────────────────────────
  @smoke
  Scenario:
    Given I am logged in as a standard user
    When I am on the inventory page
    Then products should be displayed

  # ── Violation: no-dupe-scenario-names (first occurrence) ────────────────────
  @smoke
  Scenario: Check inventory
    Given I am logged in as a standard user
    When I am on the inventory page
    Then products should be displayed

  # ── Violation: no-dupe-scenario-names (duplicate) ───────────────────────────
  @smoke
  Scenario: Check inventory
    Given I am logged in as a standard user
    When I sort products by price
    Then products should be in order

  # ── Violation: no-duplicate-tags (@e2e twice) ────────────────────────────────
  # ── Violation: name-length (Scenario name exceeds 90-char maximum) ───────────
  @smoke @e2e @e2e
  Scenario: user logs in and sorts inventory by price and adds cheapest product to cart and navigates to the cart page
    Given I am logged in as a standard user
    When I sort products by price low to high
    And I add the first product to the cart
    Then the cart badge should show one item

  # ── Violation: no-partially-commented-tag-lines ──────────────────────────────
  @smoke
  @wip # @regression
  Scenario: Empty inventory scenario
    Given I am logged in as a standard user

  # ── Violation: no-scenario-outlines-without-examples ────────────────────────
  @smoke
  Scenario Outline: Sort inventory with different options
    Given I am logged in as a standard user
    When I sort products by "<sort_option>"
    Then products should be in the expected order
