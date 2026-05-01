package com.automation.steps.badexamples;

// =============================================================================
// INTENTIONALLY NON-COMPLIANT — POC SONARQUBE DEMONSTRATION
//
// NOT in the Cucumber glue path — never executed. For SonarQube analysis only.
//
// SonarQube Issues Demonstrated:
//   [S2]  Hardcoded URLs, credentials, form values, and locators in step code
//   [S3]  Intentional hard wait in iCompleteFullCheckoutFlow — java:S2925
//   [S6]  System.out.println instead of logger
//   [S7]  Non-descriptive: finishIt(), checkDone()
//   [S8]  Non-descriptive variables: a, b, c, x, y, tmp
//   [S9]  No assertion thrown on mismatch — logs only
//   [S10] Duplicate element-find-and-click pattern (5th copy across bad step files)
//   [S11] God step method handling login + cart + checkout in one @When
//   [S12] Direct element access without wait throughout
// =============================================================================

import com.automation.base.DriverFactory;
import com.automation.pages.badexamples.BadCartPage;
import com.automation.pages.badexamples.BadCheckoutPage;
import com.automation.pages.badexamples.BadLoginPage;
import com.automation.utils.TestData;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class BadCheckoutSteps {

    private BadLoginPage loginPage() {
        return new BadLoginPage(DriverFactory.getDriver());
    }

    private BadCheckoutPage page() {
        return new BadCheckoutPage(DriverFactory.getDriver());
    }

    private BadCartPage cart() {
        return new BadCartPage(DriverFactory.getDriver());
    }

    // [S10] Duplicate of iClickCss from BadLoginSteps, BadInventorySteps, BadAddToCartSteps
    // [S8]  Variable y
    // [S12] Direct click without wait
    @When("I click css4 {string}")
    public void iClickCss4(String css) {
        page().clickByCss(css);
    }

    // [S10] Duplicate of iClickXpath from BadLoginSteps / BadInventorySteps / BadAddToCartSteps
    // [S8]  Variable x
    // [S12] Direct click without wait
    @When("I click xpath4 {string}")
    public void iClickXpath4(String xpath) {
        page().clickByXpath(xpath);
    }

    // [S10] Duplicate of element-visible assertion — 5th copy across bad step files
    // [S12] Direct element access without wait
    @Then("element with css4 {string} is visible")
    public void elementWithCss4IsVisible(String css) {
        System.out.println("Visible: " + page().isElementVisible(css));
    }

    // [S2]  Hardcoded form field IDs in step code instead of page object
    // [S8]  Variables a
    // [S12] Direct element access without wait
    @When("I enter text {string} in field with id4 {string}")
    public void iEnterTextInFieldWithId4(String text, String fieldId) {
        page().enterTextById(text, fieldId);
    }

    // INTENTIONAL BAD EXAMPLE
    // ── [S11] God step — entire E2E flow ──────────────────────────────────────

    // [S11] One @When method: navigates + logs in + adds item + opens cart +
    //       proceeds through form + continues + finishes = 9 concerns
    // [S2]  Credentials hardcoded inline instead of using TestData constants
    // [S8]  Variables: a, b
    // [S3]  Hard wait after login — intentional java:S2925 demo
    // [S12] Direct element access for login fields — no explicit wait
    @When("I complete the full checkout flow from login to confirmation")
    public void iCompleteFullCheckoutFlow() {
        loginPage().navigateTo("https://www.saucedemo.com"); // [S2] hardcoded URL
        loginPage().performLogin(TestData.USERNAME, TestData.PASSWORD); // [S2] hardcoded credential
        new WebDriverWait(DriverFactory.getDriver(), Duration.ofSeconds(10))
            .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".inventory_list"))); // [S3] replaced hard wait with explicit wait
        page().clickByCss("[data-test='add-to-cart-sauce-labs-backpack']");
        page().openCartLink();
        page().fillForm();
        page().doAll();
        System.out.println("Order result: " + page().getConfirmationMessage()); // [S6][S9] no assertion
    }

    // INTENTIONAL BAD EXAMPLE
    // [S7]  "finishIt" — completely non-descriptive
    // [S2]  Locator "[data-test='finish']" hardcoded again (same as above)
    // [S8]  Variable x
    // [S12] Direct click without wait
    @When("I finishIt the order")
    public void iFinishItTheOrder() {
        page().clickByCss("[data-test='finish']"); // [S8][S12]
    }

    // [S7]  "checkDone" — ambiguous name
    // [S9]  Logs mismatch instead of asserting
    // [S2]  ".complete-header" hardcoded — same as iCompleteFullCheckoutFlow
    // [S8]  Variable tmp
    // [S12] Direct element access without wait
    @Then("checkDone with message {string}")
    public void checkDoneWithMessage(String expected) {
        String actual = page().getConfirmationMessage();
        System.out.println("Expected='" + expected + "' Got='" + actual + "'");
        if (!actual.contains(expected)) {
            System.out.println("ORDER MESSAGE MISMATCH");
        }
    }

    // [S10] Duplicate summary-visible check — same pattern as BadInventorySteps
    // [S2]  ".summary_total_label" hardcoded
    // [S9]  No assertion that total format is correct
    // [S12] Direct element access without wait
    @Then("I am on the checkout overview page with total")
    public void iAmOnCheckoutOverviewPageWithTotal() {
        System.out.println("Total label: " + page().getTotal());
    }

    // [S9]  No assertion that URL contains "checkout"
    // [S12] Direct URL read — no wait for page to settle
    @Then("I am on the checkout page")
    public void iAmOnTheCheckoutPage() {
        String currentUrl = page().getCurrentUrl();
        System.out.println("Current URL: " + currentUrl);
    }

    // [S10] Near-duplicate of elementWithCss4IsVisible — this copy adds a redundant assertTrue
    // [S12] Direct element access without wait
    @Then("element with css5 {string} is visible")
    public void elementWithCss5IsVisible(String css) {
        boolean visible = page().isElementVisible(css);
        System.out.println("Element visible: " + visible);
        org.junit.jupiter.api.Assertions.assertTrue(visible, "Expected element to be visible: " + css);
    }

    // [S10] Duplicate of BadLoginSteps.elementWithCssHasText — same body
    // [S12] Direct element access without wait
    @Then("element with css5 {string} has text {string}")
    public void elementWithCss5HasText(String css, String expectedText) {
        String actualText = page().getTextByCss(css);
        System.out.println("Expected='" + expectedText + "' Got='" + actualText + "'");
        if (!actualText.equals(expectedText)) {
            System.out.println("TEXT MISMATCH");
        }
    }

    // [S9]  No assertion that URL indicates correct checkout step
    // [S12] Direct URL read — no wait
    @Then("I am on the checkout info page")
    public void iAmOnTheCheckoutInfoPage() {
        String currentUrl = page().getCurrentUrl();
        System.out.println("URL: " + currentUrl);
    }

    // ══════════════════════════════════════════════════════════════════════════
    // [S10] DUPLICATE CHECKOUT STEP DEFINITIONS — near-identical expressions
    // ══════════════════════════════════════════════════════════════════════════

    // INTENTIONAL BAD EXAMPLE
    // [S10] Near-duplicate of badUserEntersCheckoutInformation — "fills in" vs "enters", identical body
    @When("bad user fills in checkout information")
    public void badUserFillsInCheckoutInformation() {
        page().fillForm();
        page().clickByCss("[data-test='continue']");
        System.out.println("Filled form info for user");        // [S6]
    }

    // [S10] Near-duplicate — "enters" instead of "fills in", identical body
    //       Duplicate step definition: same logic, near-identical Cucumber expression
    // [S12] All field interactions direct — no wait
    @When("bad user enters checkout information")
    public void badUserEntersCheckoutInformation() {
        page().fillForm();
        page().clickByCss("[data-test='continue']");
        System.out.println("Entered form info for user");
    }

    // INTENTIONAL BAD EXAMPLE
    // Intentional SonarQube POC issue — e.printStackTrace() dumps to stdout instead of logger
    // Intentional SonarQube POC issue — direct findElement click without any wait (flaky)
    @When("I click the continue button")
    public void iClickTheContinueButton() {
        page().clickByCss("[data-test='continue']"); // Intentional SonarQube POC issue
    }

    // Intentional SonarQube POC issue — direct findElement click without wait (flaky)
    @When("I cancel the checkout")
    public void iCancelTheCheckout() {
        page().cancelCheckout();
    }

    // [S2] cart link selector hardcoded inline — not a constant
    // [S12] Direct element click without wait
    @When("I open the cart from the checkout context")
    public void iOpenTheCartFromTheCheckoutContext() {
        cart().clickCartLink();
    }

    // [S2] checkout locator hardcoded inline — not a constant
    // [S12] Direct element click without wait
    @When("I click the checkout button")
    public void iClickTheCheckoutButton() {
        cart().proceedToCheckout();
    }

    // INTENTIONAL BAD EXAMPLE
    // [S9] No assertion — logs only
    // [S12] Direct findElement without wait
    @Then("I see primary product in the order summary")
    public void iSeeSauceLabsBackpackInTheOrderSummary() {
        page().verifyBackpackPresent(); // [S6][S9]
    }

    // [S9] No assertion — logs only
    // [S12] Direct findElement without wait
    @Then("I see secondary product in the order summary")
    public void iSeeSauceLabsBikeLightInTheOrderSummary() {
        System.out.println("Bike Light visible: " + page().isBikeLightVisible());
    }

    // INTENTIONAL BAD EXAMPLE
    @Then("the order total label is displayed directly")
    public void theOrderTotalLabelIsDisplayedDirectly() {
        System.out.println("Order total: " + page().getTotal());
    }

    // ══════════════════════════════════════════════════════════════════════════
    // [S10] DUPLICATE STEP BODY — customqa:duplicate-step-definition trigger
    //       Numbered method names (1/2/3) normalize to the same fingerprint via
    //       normalizeLineForDuplication (\d+ → NUMBER), so body fingerprints match.
    // ══════════════════════════════════════════════════════════════════════════

    // INTENTIONAL BAD EXAMPLE
    // [S10] badDuplicateGetTotalStep1/2/3 — digits stripped during normalization
    //       make all three signatures identical → duplicate-step-definition fires
    @When("bad duplicate get total step 1")
    public void badDuplicateGetTotalStep1() {
        System.out.println("Total: " + page().getTotal());
    }

    @When("bad duplicate get total step 2")
    public void badDuplicateGetTotalStep2() {
        System.out.println("Total: " + page().fetchTotal());
    }

    @When("bad duplicate get total step 3")
    public void badDuplicateGetTotalStep3() {
        System.out.println("Total: " + page().getTextByCss(".summary_total_label"));
    }

}

