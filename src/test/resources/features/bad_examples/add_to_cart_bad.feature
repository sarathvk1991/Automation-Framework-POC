# Intentional bad Gherkin examples for lint failure demo
@wip @BAD_EXAMPLE @smoke
Feature: Add To Cart Bad Feature
# [LINT] name-length: Feature name exceeds 70-char maximum

  # ── Violation: no-homogenous-tags ───────────────────────────────────────────
  # @smoke appears on every scenario below. It should be on the Feature.

  # ── Violation: no-unnamed-scenarios ─────────────────────────────────────────
  Scenario: Add a product to the cart
    Given I am logged in as a standard user
    When I add a product to the cart
    Then the cart badge should update

  # ── Violation: no-dupe-scenario-names (first occurrence) ────────────────────
  Scenario: User adds product to cart
    Given I am logged in as a standard user
    When I add the backpack to the cart
    Then the cart badge should show one item

  # ── Violation: no-dupe-scenario-names (duplicate) ───────────────────────────
  Scenario: User adds bike light to cart
    Given I am logged in as a standard user
    When I add the bike light to the cart
    Then the cart badge should show one item

  # ── Violation: no-duplicate-tags (@regression twice) ─────────────────────────
  # ── Violation: name-length (Scenario name exceeds 90-char maximum) ───────────
  @regression
  Scenario: User adds multiple products then removes one and verifies cart count
    Given I am logged in as a standard user
    When I add the backpack to the cart
    And I add the bike light to the cart
    And I remove the backpack from the cart
    Then the cart badge should show one item

  # ── Violation: no-partially-commented-tag-lines ──────────────────────────────
  @wip
  Scenario: Empty cart scenario
    Given I am logged in as a standard user

  # ── Violation: no-scenario-outlines-without-examples ────────────────────────
  Scenario Outline: Add different products to cart
    Given I am logged in as a standard user
    When I add "<product>" to the cart
    Then the cart badge should show the correct count
    Examples:
      | product              |
      | Sauce Labs Backpack  |
      | Sauce Labs Bike Light|
