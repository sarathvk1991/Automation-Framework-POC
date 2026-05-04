# Intentional bad Gherkin examples for lint failure demo
@wip @BAD_EXAMPLE @e2e
Feature: Checkout Bad Feature
# [LINT] name-length: Feature name exceeds 70-char maximum

  # ── Violation: no-homogenous-tags ───────────────────────────────────────────
  # @e2e appears on every scenario below. It should be on the Feature.

  # ── Violation: no-unnamed-scenarios ─────────────────────────────────────────
  Scenario: View checkout form
    Given I have an item in my cart
    When I proceed to checkout
    Then I should see the checkout form

  # ── Violation: no-dupe-scenario-names (first occurrence) ────────────────────
  Scenario: Complete checkout
    Given I have an item in my cart
    When I fill in the checkout form
    And I confirm the order
    Then I should see the order confirmation

  # ── Violation: no-dupe-scenario-names (duplicate) ───────────────────────────
  Scenario: Complete checkout with two items
    Given I have two items in my cart
    When I fill in the checkout form
    And I confirm the order
    Then I should see the order confirmation

  # ── Violation: no-duplicate-tags (@smoke twice) ─────────────────────────────
  # ── Violation: name-length (Scenario name exceeds 90-char maximum) ───────────
  @smoke
  Scenario: User submits checkout form and verifies order confirmation
    Given I am logged in as a standard user
    When I add a product to the cart
    And I proceed to checkout
    And I fill in first name, last name, and postal code
    And I click continue and then finish
    Then I should see the order confirmation page

  # ── Violation: no-partially-commented-tag-lines ──────────────────────────────
  @wip
  Scenario: Empty checkout scenario
    Given I have an item in my cart

  # ── Violation: no-scenario-outlines-without-examples ────────────────────────
  Scenario Outline: Checkout fails with missing fields
    Given I have an item in my cart
    When I leave "<field>" empty and submit the checkout form
    Then I should see a validation error
    Examples:
      | field       |
      | first name  |
      | postal code |
