# Intentional bad Gherkin examples for lint failure demo
@wip @BAD_EXAMPLE
Feature: Add To Cart Bad Feature — Demonstrates Intentional Gherkin Lint Anti-Patterns For Lint Demo
# [LINT] name-length: Feature name exceeds 70-char maximum

  # ── Violation: no-homogenous-tags ───────────────────────────────────────────
  # @smoke appears on every scenario below. It should be on the Feature.

  # ── Violation: no-unnamed-scenarios ─────────────────────────────────────────
  @smoke
  Scenario:
    Given I am logged in as a standard user
    When I add a product to the cart
    Then the cart badge should update

  # ── Violation: no-dupe-scenario-names (first occurrence) ────────────────────
  @smoke
  Scenario: User adds product to cart
    Given I am logged in as a standard user
    When I add the backpack to the cart
    Then the cart badge should show one item

  # ── Violation: no-dupe-scenario-names (duplicate) ───────────────────────────
  @smoke
  Scenario: User adds product to cart
    Given I am logged in as a standard user
    When I add the bike light to the cart
    Then the cart badge should show one item

  # ── Violation: no-duplicate-tags (@regression twice) ─────────────────────────
  # ── Violation: name-length (Scenario name exceeds 90-char maximum) ───────────
  @smoke @regression @regression
  Scenario: user logs in and adds multiple products to cart and then removes one and verifies the cart count is correct
    Given I am logged in as a standard user
    When I add the backpack to the cart
    And I add the bike light to the cart
    And I remove the backpack from the cart
    Then the cart badge should show one item

  # ── Violation: no-partially-commented-tag-lines ──────────────────────────────
  @smoke
  @wip # @sanity
  Scenario: Empty cart scenario
    Given I am logged in as a standard user

  # ── Violation: no-scenario-outlines-without-examples ────────────────────────
  @smoke
  Scenario Outline: Add different products to cart
    Given I am logged in as a standard user
    When I add "<product>" to the cart
    Then the cart badge should show the correct count
