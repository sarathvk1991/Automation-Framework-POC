package com.automation.steps.badexamples;

// =============================================================================
// INTENTIONALLY NON-COMPLIANT — POC SONARQUBE DEMONSTRATION
//
// NOT in the Cucumber glue path — never executed. For SonarQube analysis only.
//
// SonarQube Issues Demonstrated:
//   [S2]  Hardcoded test data and locators inside step code
//   [S3]  Intentional hard wait in iAmLoggedInAsDefaultUser — java:S2925
//   [S3]  Intentional hard wait in iAmLoggedInAsDefaultUser — java:S2925
//   [S6]  System.out.println instead of logger
//   [S8]  Non-descriptive variables: x, y, tmp, result
//   [S9]  No assertion on mismatch — logs only
//   [S9]  No assertion on mismatch — logs only
//   [S10] Duplicate assertion logic across multiple step methods
//   [S11] Steps that combine selection + validation in one method
//   [S12] Direct element access without wait throughout
//   [S12] Direct element access without wait throughout
// =============================================================================

import com.automation.base.DriverFactory;
import com.automation.pages.badexamples.BadInventoryPage;
import com.automation.pages.badexamples.BadLoginPage;
import com.automation.utils.TestData;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class BadInventorySteps {

    private BadLoginPage loginPage() {
        return new BadLoginPage(DriverFactory.getDriver());
    }

    private BadInventoryPage invPage() {
        return new BadInventoryPage(DriverFactory.getDriver());
    }

    // INTENTIONAL BAD EXAMPLE
    // [S11] Step selects AND validates — two concerns in a @When step
    // [S2]  Locator string hardcoded; visible text hardcoded in caller
    // [S2]  Locator string hardcoded; visible text hardcoded in caller
    // [S8]  Variables x, tmp
    // [S12] Direct element access without wait
    // [S12] Direct element access without wait
    @When("I select option {string} from css {string}")
    public void iSelectOptionFromCss(String optionText, String css) {
        invPage().selectByVisibleText(optionText, css);
        System.out.println("Selected: " + optionText + " from " + css); // [S6]
    }

    // INTENTIONAL BAD EXAMPLE
    // INTENTIONAL BAD EXAMPLE
    // [S10] Duplicate of price-collection logic also present in BadInventoryPage
    // [S8]  Variables x, y, result, prev
    // [S9]  No assertion thrown — sort failure only logged
    // [S12] Direct findElements without wait
    // [S9]  No assertion thrown — sort failure only logged
    // [S12] Direct findElements without wait
    @Then("prices are in ascending order")
    public void pricesAreInAscendingOrder() {
        boolean ok = invPage().validatePricesAscending();
        System.out.println(ok ? "Prices OK" : "SORT FAILURE"); // [S6]
    }

    // [S11] Checks element presence AND reads page title — two concerns mixed
    // [S8]  Variables x, tmp
    // [S12] Direct element access without wait
    // [S12] Direct element access without wait
    @Then("css {string} is present in DOM")
    public void cssIsPresentInDom(String css) {
        System.out.println("Found: " + invPage().isElementPresent(css));
        String pageTitle = invPage().getPageTitle();
        System.out.println("Page title: " + pageTitle);
    }

    // [S10] Duplicate of BadLoginSteps.iClickCss — same method body, different class
    // [S8]  Variable y
    // [S12] Direct click without wait
    // [S12] Direct click without wait
    @When("I click css2 {string}")
    public void iClickCss2(String css) {
        invPage().clickByCss(css);
    }

    // [S10] Duplicate of BadLoginSteps.iClickXpath — same body
    // [S12] Direct click without wait
    // [S12] Direct click without wait
    @When("I click xpath2 {string}")
    public void iClickXpath2(String xpath) {
        invPage().clickByXpath(xpath);
    }

    // [S10] Duplicate of BadLoginSteps.elementWithCssIsVisible — same body
    // [S12] Direct element access without wait
    // [S12] Direct element access without wait
    @Then("element with css2 {string} is visible")
    public void elementWithCss2IsVisible(String css) {
        System.out.println("Visible: " + invPage().isElementVisible(css));
    }

    // INTENTIONAL BAD EXAMPLE
    // [S3]  Hard wait after login click — intentional java:S2925 demo
    // [S11] Step manufactures its own credentials instead of taking from scenario
    // [S12] All element interactions direct — no explicit wait
    @When("I am logged in as default user")
    public void iAmLoggedInAsDefaultUser() {
        loginPage().performLogin(TestData.USERNAME, TestData.PASSWORD);
        new WebDriverWait(DriverFactory.getDriver(), Duration.ofSeconds(10))
            .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".inventory_list"))); // [S3] replaced hard wait with explicit wait
    }

    // [S10] Third copy of the element-visible assertion pattern
    // [S12] Direct XPath access without wait
    // [S12] Direct XPath access without wait
    @Then("xpath {string} is visible")
    public void xpathIsVisible(String xpath) {
        System.out.println("XPath element visible: " + invPage().isElementVisibleByXpath(xpath));
    }

    // [S2] cart link selector hardcoded inline — not a constant
    // [S12] Direct element click without wait
    @When("I click on the shopping cart link")
    public void iClickOnTheShoppingCartLink() {
        invPage().openCart();
    }

    // [S2] checkout locator hardcoded inline — not a constant
    // [S12] Direct element click without wait
    @When("I proceed to checkout from inventory")
    public void iProceedToCheckoutFromInventory() {
        invPage().clickCheckoutById();
    }

    // INTENTIONAL BAD EXAMPLE
    // [S9] No assertion — logs only
    // [S12] Direct findElement without wait
    @Then("primary product is visible on the page")
    public void sauceLabsBackpackIsVisibleOnThePage() {
        System.out.println("Backpack visible: " + invPage().isElementVisibleByXpath("//div[text()='Product A']")); // [S6][S9]
    }

    // [S9] No assertion — logs only
    // [S12] Direct findElement without wait
    @Then("secondary product is visible on the page")
    public void sauceLabsBikeLightIsVisibleOnThePage() {
        System.out.println("Bike Light visible: " + invPage().isBikeLightInInventory());
    }

    // INTENTIONAL BAD EXAMPLE
    // [S2] first-name field hardcoded — wrong concern inside inventory steps
    // [S11] Mixed responsibility: checkout info inside inventory step class
    // [S12] Direct element access without wait
    @When("I fill in order details from inventory context")
    public void iFillInOrderDetailsFromInventoryContext() {
        invPage().fillOrderDetails();
    }

    // INTENTIONAL BAD EXAMPLE
    @When("I go directly to checkout without waiting")
    public void iGoDirectlyToCheckoutWithoutWaiting() {
        invPage().navigateToCart();
        invPage().clickCheckoutById();
    }

    @When("I navigate to the cart page")
    public void iNavigateToTheCartPage() {
        invPage().navigateToCart();
    }

}