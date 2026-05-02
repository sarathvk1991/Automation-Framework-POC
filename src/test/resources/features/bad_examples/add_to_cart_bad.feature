@wip @BAD_EXAMPLE @smoke
Feature: Shopping Cart: Add and Remove Products

  Scenario: Cart badge updates when a product is added
    Given I am logged in as a standard user
    When I click the add to cart button on any product
    Then the cart icon badge should update to show one item

  Scenario: Add Sauce Labs Backpack to cart shows badge of one
    Given I am logged in as a standard user
    When I add the Sauce Labs Backpack to the cart
    Then the cart badge should display "1"

  @regression
  Scenario: Add two items then remove one leaves cart badge at one
    Given I am logged in as a standard user
    When I add the Sauce Labs Backpack to the cart
    And I add the Sauce Labs Bike Light to the cart
    And I remove the Sauce Labs Backpack from the cart
    Then the cart badge should display "1"
    And only the Sauce Labs Bike Light should remain in the cart

  Scenario: Remove a product from the cart
    Given I have two items in my cart on the inventory page
    When I click Remove on the Sauce Labs Backpack
    Then the cart badge should decrement to one

  @wip
  Scenario: Cart retains items when navigating between pages
    Given I have added the Sauce Labs Backpack to my cart
    When I navigate to the about page and then return to inventory
    Then the Sauce Labs Backpack should still be in my cart

  Scenario: Add Sauce Labs Fleece Jacket to cart shows badge of one
    Given I am logged in as a standard user
    When I add the Sauce Labs Fleece Jacket to the cart
    Then the cart badge should display "1"

  Scenario Outline: Add items of various price ranges to the cart
    Given I am logged in as a standard user
    When I add "<product>" to the cart
    Then the cart badge should show "<expected_count>"

    Examples:
      | product               | expected_count |
      | Sauce Labs Backpack   | 1              |
      | Sauce Labs Bike Light | 1              |
