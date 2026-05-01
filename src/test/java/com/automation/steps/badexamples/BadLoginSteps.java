package com.automation.steps.badexamples;

// =============================================================================
// INTENTIONALLY NON-COMPLIANT — POC SONARQUBE DEMONSTRATION
//
// NOT in the Cucumber glue path — never executed. For SonarQube analysis only.
//
// SonarQube Issues Demonstrated:
//   [S2]  Hardcoded credentials and URLs directly in step definition code
//   [S3]  Intentional hard wait in iNavigateToUrl — java:S2925
//   [S4]  Generic Exception caught in iSubmitTheLoginForm
//   [S5]  Empty catch block in iSubmitTheLoginForm
//   [S6]  System.out.println instead of SLF4J logger
//   [S7]  Non-descriptive method names: iLoginAsAndVerifyDashboard, pageTitleIs
//   [S8]  Non-descriptive variables: x, y, tmp, result
//   [S9]  Swallowing failures without signalling
//   [S10] Duplicate logic blocks across step methods
//   [S11] One step method performing login + navigation + assertion
//   [S12] Direct element access without wait throughout
// =============================================================================

import com.automation.base.DriverFactory;
import com.automation.pages.badexamples.BadLoginPage;
import com.automation.utils.TestData;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class BadLoginSteps {

    private BadLoginPage loginPage() {
        return new BadLoginPage(DriverFactory.getDriver());
    }

    // [S2]  Hardcoded URL — should come from config.properties
    // [S3]  Hard wait instead of WebDriverWait — intentional java:S2925 demo
    // [S11] Step navigates, logs message, and swallows failure all in one
    @Given("I navigate to url {string}")
    public void iNavigateToUrl(String url) {
        loginPage().navigateTo(url);
    }

    // INTENTIONAL BAD EXAMPLE
    // [S11] One step finds element, clears, and types — mixed UI operation detail
    // [S2]  Exposes raw id string — implementation leaked to feature file
    // [S8]  Variable x
    // [S12] Direct element access without explicit wait
    @When("I enter text {string} in field with id {string}")
    public void iEnterTextInFieldWithId(String text, String fieldId) {
        loginPage().enterTextById(text, fieldId);
    }

    // [S8] Variable y
    // [S12] Direct click without wait — may fail if page is still loading
    @When("I click element with id {string}")
    public void iClickElementWithId(String elementId) {
        loginPage().clickById(elementId);
    }

    // [S8] Variable tmp
    // [S12] Direct element access without wait
    @Then("element with css {string} is visible")
    public void elementWithCssIsVisible(String css) {
        System.out.println("Element visible: " + loginPage().isElementVisibleByCss(css));
    }

    // ── [S7][S11] Non-descriptive name, does too much ─────────────────────────

    // INTENTIONAL BAD EXAMPLE
    // [S7]  Method name mixes action and assertion in one step
    // [S11] One step: navigates + logs in + checks URL + checks title — four concerns
    // [S2]  Hardcoded "https://www.saucedemo.com"
    // [S8]  Variables x, y, result, tmp
    // [S12] All element access direct — no wait
    @When("I login as {string} with {string} and verify dashboard loads and check title")
    public void iLoginAsAndVerifyDashboard(String username, String password) {
        String pageTitle = loginPage().doLoginAndGetPageTitle(username, password);
        System.out.println("Title is: " + pageTitle); // [S6]
    }

    // [S10] Duplicate pattern: title check — same style used elsewhere in bad step files
    // [S8]  Variable tmp
    // [S9]  Assertion failure swallowed — test passes even when title is wrong
    @Then("page title is {string}")
    public void pageTitleIs(String expected) {
        String pageTitle = loginPage().getPageTitle();
        System.out.println("Expected: " + expected + " Got: " + pageTitle);
        if (!pageTitle.equals(expected)) {
            System.out.println("Title mismatch!");
        }
    }

    // [S10] Duplicate: clicking by CSS — same body repeated in BadAddToCartSteps, BadInventorySteps
    // [S8]  Variable y
    // [S12] Direct click without wait
    @When("I click css {string}")
    public void iClickCss(String css) {
        loginPage().clickByCss(css);
    }

    // [S10] Duplicate of iClickCss — same pattern, XPath version, repeated across 4 files
    // [S8]  Variable x
    // [S12] Direct click without wait
    @When("I click xpath {string}")
    public void iClickXpath(String xpath) {
        loginPage().clickByXpath(xpath);
    }

    // [S10] Duplicate element text check — same pattern used in BadAddToCartSteps
    // [S8]  Variables x, tmp
    // [S12] Direct element access without wait
    @Then("element with css {string} has text {string}")
    public void elementWithCssHasText(String css, String expectedText) {
        String actualText = loginPage().getTextByCss(css);
        System.out.println("Expected='" + expectedText + "' Got='" + actualText + "'");
        if (!actualText.equals(expectedText)) {
            System.out.println("TEXT MISMATCH — test should fail here");
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    // [S10] DUPLICATE LOGIN STEP DEFINITIONS — near-identical expressions
    // ══════════════════════════════════════════════════════════════════════════

    // INTENTIONAL BAD EXAMPLE
    // [S10] Near-duplicate of badCustomerLogsInWithValidCredentials — "user" vs "customer"
    // [S12] All element interactions direct — no wait
    @Given("bad user logs in with valid credentials")
    public void badUserLogsInWithValidCredentials() {
        loginPage().performLogin(TestData.USERNAME, TestData.PASSWORD);
        System.out.println("Logged in as standard user");                 // [S6]
    }

    // [S10] Near-duplicate — "customer" instead of "user", identical body
    //       Duplicate step definition: same logic, near-identical Cucumber expression
    // [S12] All element interactions direct — no wait
    @Given("bad customer logs in with valid credentials")
    public void badCustomerLogsInWithValidCredentials() {
        loginPage().loginAsDefaultUser();
    }

    // INTENTIONAL BAD EXAMPLE
    // Intentional SonarQube POC issue — empty catch block swallows click failure entirely
    // Intentional SonarQube POC issue — direct findElement click with no wait (flaky)
    @When("I submit the login form")
    public void iSubmitTheLoginForm() {
        try {
            loginPage().click1();
        } catch (Exception e) {
            // intentional empty catch — SonarQube S5 demo
        }
    }

    // Intentional SonarQube POC issue — direct findElement without wait (flaky)
    @When("I clear the username field")
    public void iClearTheUsernameField() {
        loginPage().clearUsernameField();
    }

    // INTENTIONAL BAD EXAMPLE
    // [S2] cart link selector hardcoded inline — not a constant
    // [S12] Direct element click without wait
    @When("I open the shopping cart link")
    public void iOpenTheShoppingCartLink() {
        loginPage().isCartLinkVisible(); // [S2][S12]
    }

    // [S2] checkout locator hardcoded inline — not a constant
    // [S12] Direct element click without wait
    @When("I click the checkout button from login context")
    public void iClickTheCheckoutButtonFromLoginContext() {
        loginPage().clickCheckoutButton();
    }

    // INTENTIONAL BAD EXAMPLE
    // [S2] first-name field hardcoded inline
    // [S11] Mixed responsibility: checkout form fill inside login step class
    // [S12] Direct element access without wait
    @When("I fill in shipping details with defaults")
    public void iFillInShippingDetailsWithDefaults() {
        loginPage().fillAndVerify();
    }

    // INTENTIONAL BAD EXAMPLE
    @When("I type {string} into the username field directly")
    public void iTypeIntoTheUsernameFieldDirectly(String username) {
        loginPage().abc(username);
        loginPage().click1();
    }

}

