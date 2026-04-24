package com.automation.steps;

import com.automation.base.DriverFactory;
import com.automation.pages.InventoryPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;

public class InventoryStepDefinitions {

    private InventoryPage inventoryPage;

    // ── Given ─────────────────────────────────────────────────────────────────

    /**
     * Used in scenarios that already navigated to the inventory page (e.g. via Background login).
     * Initialises the page object and asserts we are on the correct page.
     */
    @Given("the user is on the inventory page")
    public void theUserIsOnTheInventoryPage() {
        inventoryPage = new InventoryPage(DriverFactory.getDriver());
        Assertions.assertTrue(inventoryPage.isAt(),
                "Expected to be on the inventory page but the 'Products' heading was not found.");
    }

    // ── When ──────────────────────────────────────────────────────────────────

    @When("the user sorts products by {string}")
    public void theUserSortsProductsBy(String sortOption) {
        inventoryPage.sortBy(sortOption);
    }

    // ── Then ──────────────────────────────────────────────────────────────────

    @Then("the user should land on the inventory page")
    public void theUserShouldLandOnTheInventoryPage() {
        inventoryPage = new InventoryPage(DriverFactory.getDriver());
        Assertions.assertTrue(inventoryPage.isAt(),
                "Expected to land on the inventory page after login but 'Products' heading was not found.");
    }

    @Then("the product list should be displayed")
    public void theProductListShouldBeDisplayed() {
        Assertions.assertTrue(inventoryPage.isInventoryDisplayed(),
                "Expected the inventory list container to be visible.");
    }

    @Then("the products should be sorted by price from low to high")
    public void theProductsShouldBeSortedByPriceFromLowToHigh() {
        Assertions.assertTrue(inventoryPage.validateSortingLowToHigh(),
                "Products are not in ascending price order after sorting low to high.");
    }

    @Then("the products should not be sorted by price from low to high")
    public void theProductsShouldNotBeSortedByPriceFromLowToHigh() {
        Assertions.assertFalse(inventoryPage.validateSortingLowToHigh(),
                "Products appear to be in ascending price order — expected descending after high-to-low sort.");
    }
}
