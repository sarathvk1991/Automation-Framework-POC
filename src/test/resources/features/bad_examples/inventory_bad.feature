@wip @BAD_EXAMPLE
Feature: Inventory Page: Product Display and Sorting

  @smoke
  Scenario: Products are visible after login
    Given I am logged in as a standard user
    When I navigate to the inventory page
    Then I should see at least one product listed

  @regression
  Scenario: Product names and prices are shown
    Given I am logged in as a standard user
    When I view the inventory page
    Then each product should display a name and a price

  @smoke @e2e
  Scenario: Price sort displays cheapest item at top of list
    Given I am logged in as a standard user
    When I select the "Price (low to high)" sort option
    Then the first product displayed should have the lowest price

  @smoke @wip
  Scenario: Sort order resets on next login
    Given I am logged in and have sorted products by name Z to A
    When I log out and log back in
    Then the sort order should default to the original order

  @regression
  Scenario: Product sort options can be selected
    Given I am logged in as a standard user
    When I open the sort dropdown on the inventory page
    Then I should see the available sort options

  @smoke
  Scenario: Cart icon shows zero items on fresh login
    Given I am logged in as a standard user
    When I view the inventory page
    Then the cart icon should show zero items
