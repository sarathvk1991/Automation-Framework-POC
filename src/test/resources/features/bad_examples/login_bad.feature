# Intentional bad Gherkin examples for lint failure demo
@wip @BAD_EXAMPLE @regression
Feature: Login Bad Feature
# [LINT] name-length: Feature name exceeds 70-char maximum

  # ── Violation: no-homogenous-tags ───────────────────────────────────────────
  # @regression appears on every scenario below. It should be on the Feature.

  # ── Violation: no-unnamed-scenarios ─────────────────────────────────────────
  Scenario: Log in with valid credentials
    Given I am on the login page
    When I enter valid credentials
    Then I should be logged in

  # ── Violation: no-dupe-scenario-names (first occurrence) ────────────────────
  Scenario: Login test
    Given I am on the login page
    When I enter valid credentials
    Then I should be logged in

  # ── Violation: no-dupe-scenario-names (duplicate) ───────────────────────────
  Scenario: Login test with invalid credentials
    Given I am on the login page
    When I enter wrong credentials
    Then I should see an error message

  # ── Violation: no-duplicate-tags (@smoke twice) ─────────────────────────────
  # ── Violation: name-length (Scenario name exceeds 90-char maximum) ───────────
  @smoke
  Scenario: User logs in and navigates to inventory page
    Given I navigate to the application
    When I log in as a standard user
    And I add an item to the cart
    And I proceed to checkout
    Then the order confirmation is displayed

  # ── Violation: no-partially-commented-tag-lines ──────────────────────────────
  @wip
  Scenario: Empty login scenario
    Given I am on the login page

  # ── Violation: no-scenario-outlines-without-examples ────────────────────────
  Scenario Outline: Login with different user types
    Given user enters "<username>" and "<password>"
    When user submits the login form
    Then user should see the inventory page
    Examples:
      | username      | password     |
      | standard_user | secret_sauce |
      | problem_user  | secret_sauce |
