package com.automation.steps;

import com.automation.base.DriverFactory;
import com.automation.pages.CartPage;
import com.automation.pages.CheckoutOverviewPage;
import com.automation.pages.CheckoutPage;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;

public class CheckoutStepDefinitions {

    private CheckoutPage orderInfoPage;
    private CheckoutOverviewPage orderSummaryPage;

    // ── When ──────────────────────────────────────────────────────────────────

    /**
     * Driver is on the cart page at this point (placed there by the prior
     * "navigates to cart" step). A fresh CartPage wrapper is created here
     * rather than sharing state across step definition classes.
     */
    @When("the user proceeds to place an order")
    public void theUserProceedsToPlaceAnOrder() {
        orderInfoPage = new CartPage(DriverFactory.getDriver()).clickCheckout();
    }

    @When("the user enters order details with first name {string}, last name {string}, and postal code {string}")
    public void theUserEntersOrderDetails(String firstName, String lastName, String postalCode) {
        orderInfoPage.enterFirstName(firstName)
                     .enterLastName(lastName)
                     .enterPostalCode(postalCode);
    }

    @When("the user continues to order overview")
    public void theUserContinuesToOrderOverview() {
        orderInfoPage.clickContinue();
        orderSummaryPage = new CheckoutOverviewPage(DriverFactory.getDriver());
    }

    /**
     * Clicks Continue without filling any fields — triggers a validation error
     * that the following @Then step asserts against.
     */
    @When("the user continues without entering order details")
    public void theUserContinuesWithoutEnteringOrderDetails() {
        orderInfoPage.clickContinue();
    }

    @When("the user finishes the order")
    public void theUserFinishesTheOrder() {
        orderSummaryPage.clickFinish();
    }

    // ── Then ──────────────────────────────────────────────────────────────────

    @Then("the order total should be displayed")
    public void theOrderTotalShouldBeDisplayed() {
        String total = orderSummaryPage.getTotalAmount();
        Assertions.assertFalse(total == null || total.isBlank(),
                "Expected the order total label to be visible and non-empty.");
        Assertions.assertTrue(total.startsWith("Total:"),
                "Expected total label to start with 'Total:' but was: " + total);
    }

    @Then("the order should be placed successfully")
    public void theOrderShouldBePlacedSuccessfully() {
        Assertions.assertTrue(orderSummaryPage.isOrderSuccessful(),
                "Expected order confirmation header to be visible after finishing the order.");
    }

    @Then("the order error {string} should be displayed")
    public void theOrderErrorShouldBeDisplayed(String expectedError) {
        Assertions.assertTrue(orderInfoPage.isErrorDisplayed(),
                "Expected an order error message to be visible.");
        Assertions.assertEquals(expectedError, orderInfoPage.getErrorMessage(),
                "Order error message text did not match.");
    }
}