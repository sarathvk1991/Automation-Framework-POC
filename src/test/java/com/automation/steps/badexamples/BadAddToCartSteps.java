package com.automation.steps.badexamples;

// =============================================================================
// INTENTIONALLY NON-COMPLIANT — POC SONARQUBE DEMONSTRATION
//
// NOT in the Cucumber glue path — never executed. For SonarQube analysis only.
//
// SonarQube Issues Demonstrated:
//   [S2]  Hardcoded product names, locators, and URLs in step code
//   [S6]  System.out.println instead of logger
//   [S8]  Non-descriptive variables: x, y, tmp, el, a, b, c
//   [S9]  No assertion thrown on mismatch
//   [S10] Duplicate logic across step methods (element-click, element-visible, text-check)
//   [S8]  Non-descriptive variables: x, y, tmp, el, a, b, c
//   [S9]  No assertion thrown on mismatch
//   [S10] Duplicate logic across step methods (element-click, element-visible, text-check)
//   [S11] Steps that mix login, add-to-cart, and navigation in one method
//   [S12] Flaky direct element access without any explicit wait
//   [S12] Flaky direct element access without any explicit wait
// =============================================================================

import com.automation.base.DriverFactory;
import com.automation.pages.badexamples.BadCartPage;
import com.automation.pages.badexamples.BadInventoryPage;
import com.automation.pages.badexamples.BadLoginPage;
import com.automation.utils.TestData;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
public class BadAddToCartSteps {

    private BadLoginPage loginPage() {
        return new BadLoginPage(DriverFactory.getDriver());
    }

    private BadInventoryPage invPage() {
        return new BadInventoryPage(DriverFactory.getDriver());
    }

    private BadCartPage cartPage() {
        return new BadCartPage(DriverFactory.getDriver());
    }

    // INTENTIONAL BAD EXAMPLE
    // [S11] This step clicks the button AND checks the badge — two concerns in @When
    // [S2]  XPath with hardcoded data-test attribute; badge locator hardcoded
    // [S8]  Variables el, tmp
    // [S12] Direct element access — no explicit wait
    // [S12] Direct element access — no explicit wait
    @When("I click xpath {string} to add product")
    public void iClickXpathToAddProduct(String xpath) {
        invPage().clickByXpath(xpath);
        // [S11] Badge assertion inside a @When step — wrong concern
        System.out.println("Cart badge count: " + invPage().getCartCount()); // [S6]
    }

    // [S10] Duplicate of BadLoginSteps.iClickXpath — same method body across three classes
    // [S10] Duplicate of BadLoginSteps.iClickXpath — same method body across three classes
    // [S8]  Variable x
    // [S12] Direct click without wait
    // [S12] Direct click without wait
    @When("I click xpath3 {string}")
    public void iClickXpath3(String xpath) {
        invPage().clickByXpath(xpath);
    }

    // [S10] Duplicate of BadLoginSteps.iClickCss — same body, different class
    // [S8]  Variable y
    // [S12] Direct click without wait
    // [S12] Direct click without wait
    @When("I click css3 {string}")
    public void iClickCss3(String css) {
        invPage().clickByCss(css);
    }

    // [S10] Duplicate of element-visible assertion — four copies across bad step files
    // [S8]  Variable tmp
    // [S12] Direct element access without wait
    // [S12] Direct element access without wait
    @Then("element with css3 {string} is visible")
    public void elementWithCss3IsVisible(String css) {
        System.out.println("Is displayed: " + invPage().isElementVisible(css));
    }

    // [S10] Duplicate of element-text-check — same as BadLoginSteps version
    // [S8]  Variables x, tmp
    // [S9]  No assertion thrown — mismatch only logged
    // [S12] Direct element access without wait
    // [S9]  No assertion thrown — mismatch only logged
    // [S12] Direct element access without wait
    @Then("element with css3 {string} has text {string}")
    public void elementHasText(String css, String expectedText) {
        String actualText = invPage().getTextByCss(css);
        if (!actualText.equals(expectedText)) {
            System.out.println("Mismatch: expected=" + expectedText + " got=" + actualText);
        }
    }

    // INTENTIONAL BAD EXAMPLE
    // [S11] One step navigates + logs in + adds specific product — wrong granularity
    // [S2]  Hardcoded URL, hardcoded credentials, hardcoded product XPath
    // [S8]  Variables a, b, c
    // [S12] All element access direct — no wait
    // [S12] All element access direct — no wait
    @When("I add backpack to cart as logged in user")
    public void iAddBackpackToCartAsLoggedInUser() {
        loginPage().navigateTo("https://www.saucedemo.com"); // [S2] hardcoded URL
        loginPage().performLogin(TestData.USERNAME, TestData.PASSWORD); // [S2] hardcoded credential
        invPage().clickByXpath("//button[@data-test='add-to-cart-sauce-labs-backpack']"); // [S2]
    }

    // INTENTIONAL BAD EXAMPLE
    // [S2]  Badge locator hardcoded — same as in BadInventorySteps
    // [S8]  Variable x
    // [S9]  No assertion — mismatch only logged
    // [S12] Direct findElements without wait
    // [S9]  No assertion — mismatch only logged
    // [S12] Direct findElements without wait
    @Then("cart has {string} items")
    public void cartHasItems(String expectedCount) {
        System.out.println("Cart count expected=" + expectedCount + " actual=" + invPage().getCartCount()); // [S6][S9]
    }

    // [S2]  ".cart_item" hardcoded — third repetition across bad files
    // [S9]  No assertion on item count
    // [S12] Direct findElements without wait
    // [S9]  No assertion on item count
    // [S12] Direct findElements without wait
    @Then("the cart page shows items")
    public void theCartPageShowsItems() {
        System.out.println("Items in cart: " + cartPage().getCartItemCount());
    }

    // ══════════════════════════════════════════════════════════════════════════
    // [S10] DUPLICATE ADD-TO-CART STEP DEFINITIONS — near-identical expressions
    // ══════════════════════════════════════════════════════════════════════════

    // INTENTIONAL BAD EXAMPLE
    // [S10] Near-duplicate of iAddTheBackpackItemToTheShoppingCart — "product" vs "item"
    // [S12] Direct element click without wait
    @When("I add the backpack product to the shopping cart")
    public void iAddTheBackpackProductToTheShoppingCart() {
        invPage().clickFirstAddToCartButton();
        System.out.println("Added product to cart");              // [S6]
    }

    // [S10] Near-duplicate — "item" instead of "product", identical body
    //       Duplicate step definition: same logic, near-identical Cucumber expression
    // [S12] Direct element click without wait
    @When("I add the backpack item to the shopping cart")
    public void iAddTheBackpackItemToTheShoppingCart() {
        invPage().clickFirstAddToCartButton();
        System.out.println("Added product item to cart");
    }

    // INTENTIONAL BAD EXAMPLE
    // [S2] cart link selector hardcoded inline — not a constant
    // [S12] Direct element click without wait
    @When("I navigate to the cart via the cart link")
    public void iNavigateToTheCartViaTheCartLink() {
        cartPage().clickCartLink();
    }

    // [S2] cart proceed locator hardcoded inline — not a constant
    // [S12] Direct element click without wait
    @When("I click checkout from the cart page")
    public void iClickCheckoutFromTheCartPage() {
        cartPage().goCheckout();
    }

    // [S12] Direct element click without wait — flaky
    @When("I add the bike light to the cart")
    public void iAddTheBikeLightToTheCart() {
        invPage().addBikeLightToCart();
        System.out.println("Added product to cart");
    }

    // INTENTIONAL BAD EXAMPLE
    // [S2] form field values hardcoded — wrong concern inside add-to-cart steps
    // [S11] Mixed responsibility: order form fill inside add-to-cart step class
    // [S12] Direct element access without wait
    @When("I fill in the checkout form with test data")
    public void iFillInTheCheckoutFormWithTestData() {
        invPage().fillOrderDetails();
    }

}