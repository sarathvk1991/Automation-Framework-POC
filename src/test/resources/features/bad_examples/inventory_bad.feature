# Intentional bad Gherkin examples for lint failure demo
@wip @BAD_EXAMPLE @smoke
Feature: Inventory Bad Feature
# [LINT] name-length: Feature name exceeds 70-char maximum

  # ── Violation: no-homogenous-tags ───────────────────────────────────────────
  # @smoke appears on every scenario below. It should be on the Feature.

  # ── Violation: no-unnamed-scenarios ─────────────────────────────────────────
  Scenario: View product inventory
    Given I am logged in as a standard user
    When I am on the inventory page
    Then products should be displayed

  # ── Violation: no-dupe-scenario-names (first occurrence) ────────────────────
  Scenario: Check inventory
    Given I am logged in as a standard user
    When I am on the inventory page
    Then products should be displayed

  # ── Violation: no-dupe-scenario-names (duplicate) ───────────────────────────
  Scenario: Check inventory sorting by price
    Given I am logged in as a standard user
    When I sort products by price
    Then products should be in order

  # ── Violation: no-duplicate-tags (@e2e twice) ────────────────────────────────
  # ── Violation: name-length (Scenario name exceeds 90-char maximum) ───────────
  @e2e
  Scenario: User sorts inventory by price and adds cheapest product to cart
    Given I am logged in as a standard user
    When I sort products by price low to high
    And I add the first product to the cart
    Then the cart badge should show one item

  # ── Violation: no-partially-commented-tag-lines ──────────────────────────────
  @wip
  Scenario: Empty inventory scenario
    Given I am logged in as a standard user

  # ── Violation: no-scenario-outlines-without-examples ────────────────────────
  Scenario Outline: Sort inventory with different options
    Given I am logged in as a standard user
    When I sort products by "<sort_option>"
    Then products should be in the expected order
    Examples:
      | sort_option        |
      | Price (low to high)|
      | Price (high to low)|
