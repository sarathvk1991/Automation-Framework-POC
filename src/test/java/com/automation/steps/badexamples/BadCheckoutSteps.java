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
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class BadCheckoutSteps {

    // [S10] Duplicate of iClickCss from BadLoginSteps, BadInventorySteps, BadAddToCartSteps
    // [S8]  Variable y
    // [S12] Direct click without wait
    @When("I click css4 {string}")
    public void iClickCss4(String css) {
        WebElement y = DriverFactory.getDriver() // [S8][S12]
            .findElement(By.cssSelector(css));
        y.click();
    }

    // [S10] Duplicate of iClickXpath from BadLoginSteps / BadInventorySteps / BadAddToCartSteps
    // [S8]  Variable x
    // [S12] Direct click without wait
    @When("I click xpath4 {string}")
    public void iClickXpath4(String xpath) {
        WebElement x = DriverFactory.getDriver() // [S8][S12]
            .findElement(By.xpath(xpath));
        x.click();
    }

    // [S10] Duplicate of element-visible assertion — 5th copy across bad step files
    // [S12] Direct element access without wait
    @Then("element with css4 {string} is visible")
    public void elementWithCss4IsVisible(String css) {
        WebElement tmp = DriverFactory.getDriver() // [S8][S12]
            .findElement(By.cssSelector(css));
        System.out.println("Visible: " + tmp.isDisplayed()); // [S6]
    }

    // [S2]  Hardcoded form field IDs in step code instead of page object
    // [S8]  Variables a
    // [S12] Direct element access without wait
    @When("I enter text {string} in field with id4 {string}")
    public void iEnterTextInFieldWithId4(String text, String fieldId) {
        WebElement a = DriverFactory.getDriver() // [S8][S12]
            .findElement(By.id(fieldId));
        a.clear();
        a.sendKeys(text);
    }

    // ── [S11] God step — entire E2E checkout flow ─────────────────────────────

    // [S11] One @When method: navigates + logs in + adds item + opens cart +
    //       clicks checkout + fills form + continues + finishes = 9 concerns
    // [S2]  Everything hardcoded: URL, credentials, product, form data, locators
    // [S8]  Variables: a, b, c, x, y, tmp
    // [S3]  Hard wait after login — intentional java:S2925 demo
    // [S12] Most element access direct — no explicit wait
    @When("I complete the full checkout flow from login to confirmation")
    public void iCompleteFullCheckoutFlow() {
        WebDriver driver = DriverFactory.getDriver();

        driver.get("https://www.saucedemo.com"); // [S2] hardcoded URL

        WebElement a = driver.findElement(By.id("user-name")); // [S8][S12]
        a.clear();
        a.sendKeys("standard_user"); // [S2] hardcoded credential
        WebElement b = driver.findElement(By.id("password")); // [S8][S12]
        b.clear();
        b.sendKeys("secret_sauce"); // [S2] hardcoded credential
        driver.findElement(By.id("login-button")).click();
        try {
            Thread.sleep(2000); // [S3] intentional hard wait — java:S2925
        } catch (InterruptedException e) {
            // InterruptedException swallowed
        }

        // [S2] Hardcoded product XPath
        driver.findElement(
            By.xpath("//button[@data-test='add-to-cart-sauce-labs-backpack']")
        ).click(); // [S12]

        driver.findElement(By.cssSelector("[data-test='shopping-cart-link']")).click(); // [S12]
        driver.findElement(By.cssSelector("[data-test='checkout']")).click();           // [S12]

        // [S2] Hardcoded form values — "Sarath", "Tester", "695001"
        WebElement x = driver.findElement(By.id("first-name")); // [S8][S12]
        x.clear();
        x.sendKeys("Sarath"); // [S2] hardcoded first name
        WebElement y = driver.findElement(By.id("last-name")); // [S8][S12]
        y.clear();
        y.sendKeys("Tester"); // [S2] hardcoded last name
        WebElement tmp = driver.findElement(By.id("postal-code")); // [S8][S12]
        tmp.clear();
        tmp.sendKeys("695001"); // [S2] hardcoded postal code

        driver.findElement(By.cssSelector("[data-test='continue']")).click(); // [S12]
        driver.findElement(By.cssSelector("[data-test='finish']")).click();   // [S12]

        WebElement c = driver.findElement(By.cssSelector(".complete-header")); // [S8][S12]
        System.out.println("Order result: " + c.getText()); // [S6][S9] no assertion
    }

    // [S7]  "finishIt" — completely non-descriptive
    // [S2]  Locator "[data-test='finish']" hardcoded again (same as above)
    // [S8]  Variable x
    // [S12] Direct click without wait
    @When("I finishIt the order")
    public void iFinishItTheOrder() {
        WebElement x = DriverFactory.getDriver() // [S8][S12]
            .findElement(By.cssSelector("[data-test='finish']"));
        x.click();
    }

    // [S7]  "checkDone" — ambiguous name
    // [S9]  Logs mismatch instead of asserting
    // [S2]  ".complete-header" hardcoded — same as iCompleteFullCheckoutFlow
    // [S8]  Variable tmp
    // [S12] Direct element access without wait
    @Then("checkDone with message {string}")
    public void checkDoneWithMessage(String expected) {
        WebElement tmp = DriverFactory.getDriver() // [S8][S12]
            .findElement(By.cssSelector(".complete-header"));
        String actual = tmp.getText();
        System.out.println("Expected='" + expected + "' Got='" + actual + "'"); // [S6]
        if (!actual.contains(expected)) {
            System.out.println("ORDER MESSAGE MISMATCH"); // [S6][S9] no assertion thrown
        }
    }

    // [S10] Duplicate summary-visible check — same pattern as BadInventorySteps
    // [S2]  ".summary_total_label" hardcoded
    // [S9]  No assertion that total format is correct
    // [S12] Direct element access without wait
    @Then("I am on the checkout overview page with total")
    public void iAmOnCheckoutOverviewPageWithTotal() {
        WebElement x = DriverFactory.getDriver() // [S8][S12]
            .findElement(By.cssSelector(".summary_total_label"));
        System.out.println("Total label: " + x.getText()); // [S6][S9]
    }

    // [S9]  No assertion that URL contains "checkout"
    // [S12] Direct URL read — no wait for page to settle
    @Then("I am on the checkout page")
    public void iAmOnTheCheckoutPage() {
        String tmp = DriverFactory.getDriver().getCurrentUrl(); // [S8]
        System.out.println("Current URL: " + tmp); // [S6][S9]
    }

    // [S10] Duplicate of BadLoginSteps.elementWithCssIsVisible — 6th copy across bad steps
    // [S12] Direct element access without wait
    @Then("element with css5 {string} is visible")
    public void elementWithCss5IsVisible(String css) {
        WebElement el = DriverFactory.getDriver() // [S12]
            .findElement(By.cssSelector(css));
        System.out.println("Element visible: " + el.isDisplayed()); // [S6]
    }

    // [S10] Duplicate of BadLoginSteps.elementWithCssHasText — same body
    // [S12] Direct element access without wait
    @Then("element with css5 {string} has text {string}")
    public void elementWithCss5HasText(String css, String expectedText) {
        WebElement x = DriverFactory.getDriver() // [S8][S12]
            .findElement(By.cssSelector(css));
        String tmp = x.getText(); // [S8]
        System.out.println("Expected='" + expectedText + "' Got='" + tmp + "'"); // [S6]
        if (!tmp.equals(expectedText)) {
            System.out.println("TEXT MISMATCH"); // [S6][S9] no assertion
        }
    }

    // [S9]  No assertion that URL indicates correct checkout step
    // [S12] Direct URL read — no wait
    @Then("I am on the checkout info page")
    public void iAmOnTheCheckoutInfoPage() {
        String tmp = DriverFactory.getDriver().getCurrentUrl(); // [S8]
        System.out.println("URL: " + tmp); // [S6][S9]
    }

    // ══════════════════════════════════════════════════════════════════════════
    // [S10] DUPLICATE CHECKOUT STEP DEFINITIONS — near-identical expressions
    // ══════════════════════════════════════════════════════════════════════════

    // [S2]  "Sarath", "Tester", "695001" hardcoded form values — not read from config
    // [S10] Near-duplicate of badUserEntersCheckoutInformation — "fills in" vs "enters"
    // [S12] All field interactions direct — no wait
    @When("bad user fills in checkout information")
    public void badUserFillsInCheckoutInformation() {
        WebDriver driver = DriverFactory.getDriver();
        driver.findElement(By.id("first-name")).sendKeys("Sarath");          // [S2][S12]
        driver.findElement(By.id("last-name")).sendKeys("Tester");           // [S2][S12]
        driver.findElement(By.id("postal-code")).sendKeys("695001");         // [S2][S12]
        driver.findElement(By.cssSelector("[data-test='continue']")).click(); // [S12]
        System.out.println("Filled checkout info for Sarath Tester");        // [S6]
    }

    // [S10] Near-duplicate — "enters" instead of "fills in", identical body
    //       Duplicate step definition: same logic, near-identical Cucumber expression
    // [S12] All field interactions direct — no wait
    @When("bad user enters checkout information")
    public void badUserEntersCheckoutInformation() {
        WebDriver driver = DriverFactory.getDriver();
        driver.findElement(By.id("first-name")).sendKeys("Sarath");          // [S2][S12]
        driver.findElement(By.id("last-name")).sendKeys("Tester");           // [S2][S12]
        driver.findElement(By.id("postal-code")).sendKeys("695001");         // [S2][S12]
        driver.findElement(By.cssSelector("[data-test='continue']")).click(); // [S12]
        System.out.println("Entered checkout info for Sarath Tester");       // [S6]
    }

    // Intentional SonarQube POC issue — e.printStackTrace() dumps to stdout instead of logger
    // Intentional SonarQube POC issue — direct findElement click without any wait (flaky)
    @When("I click the continue button")
    public void iClickTheContinueButton() {
        DriverFactory.getDriver().findElement(By.cssSelector("[data-test='continue']")).click(); // Intentional SonarQube POC issue — direct click, no wait, no retry
    }

    // Intentional SonarQube POC issue — direct findElement click without wait (flaky)
    @When("I cancel the checkout")
    public void iCancelTheCheckout() {
        DriverFactory.getDriver().findElement(By.cssSelector("[data-test='cancel']")).click(); // Intentional SonarQube POC issue — flaky direct click
    }

    // [S2] By.cssSelector(".shopping_cart_link") hardcoded inline — not a constant
    // [S12] Direct element click without wait
    @When("I open the cart from the checkout context")
    public void iOpenTheCartFromTheCheckoutContext() {
        DriverFactory.getDriver().findElement(By.cssSelector(".shopping_cart_link")).click(); // [S2][S12]
    }

    // [S2] By.id("checkout") hardcoded inline — not a constant
    // [S12] Direct element click without wait
    @When("I click the checkout button")
    public void iClickTheCheckoutButton() {
        DriverFactory.getDriver().findElement(By.id("checkout")).click(); // [S2][S12]
    }

    // [S2] "Sauce Labs Backpack" hardcoded product name — not from config
    // [S9] No assertion — logs only
    // [S12] Direct findElement without wait
    @Then("I see Sauce Labs Backpack in the order summary")
    public void iSeeSauceLabsBackpackInTheOrderSummary() {
        WebElement x = DriverFactory.getDriver()                                   // [S12]
            .findElement(By.xpath("//div[text()='Sauce Labs Backpack']"));          // [S2]
        System.out.println("Backpack visible: " + x.isDisplayed());               // [S6][S9]
    }

    // [S2] "Sauce Labs Bike Light" hardcoded product name — not from config
    // [S9] No assertion — logs only
    // [S12] Direct findElement without wait
    @Then("I see Sauce Labs Bike Light in the order summary")
    public void iSeeSauceLabsBikeLightInTheOrderSummary() {
        WebElement x = DriverFactory.getDriver()                                   // [S12]
            .findElement(By.xpath("//div[text()='Sauce Labs Bike Light']"));        // [S2]
        System.out.println("Bike Light visible: " + x.isDisplayed());             // [S6][S9]
    }

    @Then("the order total label is displayed directly")
    public void theOrderTotalLabelIsDisplayedDirectly() {
        String total = DriverFactory.getDriver().findElement(By.cssSelector(".summary_total_label")).getText();
        System.out.println("Order total: " + total);
    }

    @When("bad user submits checkout details")
    public void badUserSubmitsCheckoutDetails() {
        WebDriver driver = DriverFactory.getDriver();
        driver.findElement(By.id("first-name")).sendKeys("Sarath");
        driver.findElement(By.id("last-name")).sendKeys("Tester");
        driver.findElement(By.id("postal-code")).sendKeys("695001");
        driver.findElement(By.cssSelector("[data-test='continue']")).click();
        System.out.println("Checkout details submitted");
    }

    @When("bad buyer provides checkout information")
    public void badBuyerProvidesCheckoutInformation() {
        WebDriver driver = DriverFactory.getDriver();
        driver.findElement(By.id("first-name")).sendKeys("Sarath");
        driver.findElement(By.id("last-name")).sendKeys("Tester");
        driver.findElement(By.id("postal-code")).sendKeys("695001");
        driver.findElement(By.cssSelector("[data-test='continue']")).click();
        System.out.println("Checkout info provided by buyer");
    }
}
