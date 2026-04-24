package com.automation.steps;

import com.automation.base.DriverFactory;
import com.automation.pages.CartPage;
import com.automation.pages.InventoryPage;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;

public class AddToCartStepDefinitions {

    private InventoryPage inventoryPage;
    private CartPage cartPage;

    /**
     * Lazily initialises InventoryPage on first access.
     * By the time any @When step in this class runs, the Background has already
     * logged in (via LoginSteps) and confirmed the inventory page is loaded
     * (via InventoryStepDefinitions). Creating a fresh wrapper here is safe —
     * page objects are stateless driver wrappers.
     */
    private InventoryPage inventoryPage() {
        if (inventoryPage == null) {
            inventoryPage = new InventoryPage(DriverFactory.getDriver());
        }
        return inventoryPage;
    }

    // ── When ──────────────────────────────────────────────────────────────────

    @When("the user adds {string} to the cart")
    public void theUserAddsToTheCart(String productName) {
        inventoryPage().addProductToCart(productName);
    }

    @When("the user navigates to the cart")
    public void theUserNavigatesToTheCart() {
        cartPage = inventoryPage().clickCartIcon();
    }

    // ── Then ──────────────────────────────────────────────────────────────────

    @Then("the cart count should be {string}")
    public void theCartCountShouldBe(String expectedCount) {
        Assertions.assertEquals(expectedCount, inventoryPage().getCartCount(),
                "Cart badge count did not match expected value.");
    }

    @Then("the cart should contain {string}")
    public void theCartShouldContain(String productName) {
        Assertions.assertTrue(cartPage.isProductPresent(productName),
                "Expected product '" + productName + "' was not found in the cart.");
    }

    @Then("the cart should be empty")
    public void theCartShouldBeEmpty() {
        Assertions.assertTrue(cartPage.isCartEmpty(),
                "Expected an empty cart but cart items were present.");
    }
}
