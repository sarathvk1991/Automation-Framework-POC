@wip @BAD_EXAMPLE
Feature: Add To Cart — Managing Cart State, Product Selection, and Removal Operations Throughout the Shopping Session
  # [LINT] name-length: Feature name exceeds 70-char maximum

  @smoke
  Scenario:
    Given I am logged in as a standard user
    When I click the add to cart button on any product
    Then the cart icon badge should update to show one item

  @smoke
  Scenario: Add a single product to the cart
    Given I am logged in as a standard user
    When I add the Sauce Labs Backpack to the cart
    Then the cart badge should display "1"

  @smoke @regression @regression
  Scenario: authenticated user adds the sauce labs backpack and the sauce labs bike light to the cart then removes the backpack and verifies the cart badge shows one item remaining
    Given I am logged in as a standard user
    When I add the Sauce Labs Backpack to the cart
    And I add the Sauce Labs Bike Light to the cart
    And I remove the Sauce Labs Backpack from the cart
    Then the cart badge should display "1"
    And only the Sauce Labs Bike Light should remain in the cart

  @smoke
  Scenario: Remove a product from the cart
    Given I have two items in my cart on the inventory page
    When I click Remove on the Sauce Labs Backpack
    Then the cart badge should decrement to one

  @smoke
  @wip # @sanity
  Scenario: Cart retains items when navigating between pages
    Given I have added the Sauce Labs Backpack to my cart
    When I navigate to the about page and then return to inventory
    Then the Sauce Labs Backpack should still be in my cart

  @smoke
  Scenario: Add a single product to the cart
    Given I am logged in as a standard user
    When I add the Sauce Labs Fleece Jacket to the cart
    Then the cart badge should display "1"

  @smoke
  Scenario Outline: Add items of various price ranges to the cart
    Given I am logged in as a standard user
    When I add "<product>" to the cart
    Then the cart badge should show "<expected_count>"
