@wip @BAD_EXAMPLE
Feature: Inventory Page — Displaying, Sorting, and Filtering the Product Catalogue for Authenticated Users
  # [LINT] name-length: Feature name exceeds 70-char maximum

  @smoke
  Scenario: Products are visible after login
    Given I am logged in as a standard user
    When I navigate to the inventory page
    Then I should see at least one product listed

  @smoke
  Scenario: Product names and prices are shown
    Given I am logged in as a standard user
    When I view the inventory page
    Then each product should display a name and a price

  @smoke @e2e @e2e
  Scenario: standard user logs into the application and sorts the product list by price low to high and confirms the cheapest item appears at the top of the page
    Given I am logged in as a standard user
    When I select the "Price (low to high)" sort option
    Then the first product displayed should have the lowest price

  # Verify sort preference does not persist after logout
  @smoke
  @wip # @regression
  Scenario: Sort order resets on next login
    Given I am logged in and have sorted products by name Z to A
    When I log out and log back in
    Then the sort order should default to the original order

  @smoke
  Scenario:
    Given I am logged in as a standard user
    When I view the inventory page
    Then the cart icon should show zero items
