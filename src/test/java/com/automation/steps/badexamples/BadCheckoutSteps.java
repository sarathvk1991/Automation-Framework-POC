package com.automation.steps.badexamples;

// =============================================================================
// INTENTIONALLY NON-COMPLIANT — POC SONARQUBE DEMONSTRATION
//
// NOT in the Cucumber glue path — never executed. For SonarQube analysis only.
//
// SonarQube Issues Demonstrated:
//   [S2]  Hardcoded URLs, credentials, form values, and locators in step code
//   [S3]  Intentional hard wait in iCompleteFullCheckoutFlow — java:S2925
//   [S4]  Generic Exception caught
//   [S5]  Empty catch blocks
//   [S6]  System.out.println instead of logger
//   [S7]  Non-descriptive: finishIt(), checkDone()
//   [S8]  Non-descriptive variables: a, b, c, x, y, tmp
//   [S9]  No assertion thrown on mismatch — logs only
//   [S10] Duplicate element-find-and-click pattern (5th copy across bad step files)
//   [S11] God step method handling login + cart + checkout in one @When
//   [S12] Direct element access without wait throughout
// =============================================================================

import com.automation.base.DriverFactory;
import io.cucumber.java.en.And;
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
        try {
            WebElement y = DriverFactory.getDriver() // [S8][S12]
                .findElement(By.cssSelector(css));
            y.click();
        } catch (Exception e) { // [S4]
            // [S5]
        }
    }

    // [S10] Duplicate of iClickXpath from BadLoginSteps / BadInventorySteps / BadAddToCartSteps
    // [S8]  Variable x
    // [S12] Direct click without wait
    @When("I click xpath4 {string}")
    public void iClickXpath4(String xpath) {
        try {
            WebElement x = DriverFactory.getDriver() // [S8][S12]
                .findElement(By.xpath(xpath));
            x.click();
        } catch (Exception e) { // [S4]
            System.out.println("XPath click failed: " + xpath); // [S6]
        }
    }

    // [S10] Duplicate of element-visible assertion — 5th copy across bad step files
    // [S12] Direct element access without wait
    @Then("element with css4 {string} is visible")
    public void elementWithCss4IsVisible(String css) {
        try {
            WebElement tmp = DriverFactory.getDriver() // [S8][S12]
                .findElement(By.cssSelector(css));
            System.out.println("Visible: " + tmp.isDisplayed()); // [S6]
        } catch (Exception e) { // [S4]
            // [S5]
        }
    }

    // [S2]  Hardcoded form field IDs in step code instead of page object
    // [S8]  Variables a
    // [S12] Direct element access without wait
    @When("I enter text {string} in field with id4 {string}")
    public void iEnterTextInFieldWithId4(String text, String fieldId) {
        try {
            WebElement a = DriverFactory.getDriver() // [S8][S12]
                .findElement(By.id(fieldId));
            a.clear();
            a.sendKeys(text);
        } catch (Exception e) { // [S4]
            System.out.println("Type failed in field: " + fieldId); // [S6]
        }
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
        try {
            WebDriver driver = DriverFactory.getDriver();

            driver.get("https://www.saucedemo.com"); // [S2] hardcoded URL

            WebElement a = driver.findElement(By.id("user-name")); // [S8][S12]
            a.clear();
            a.sendKeys("standard_user"); // [S2] hardcoded credential
            WebElement b = driver.findElement(By.id("password")); // [S8][S12]
            b.clear();
            b.sendKeys("secret_sauce"); // [S2] hardcoded credential
            driver.findElement(By.id("login-button")).click();
            Thread.sleep(2000); // [S3] intentional hard wait — java:S2925

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

        } catch (Exception e) { // [S4]
            System.out.println("Full checkout failed: " + e.getMessage()); // [S6]
        }
    }

    // [S7]  "finishIt" — completely non-descriptive
    // [S2]  Locator "[data-test='finish']" hardcoded again (same as above)
    // [S8]  Variable x
    // [S12] Direct click without wait
    @When("I finishIt the order")
    public void iFinishItTheOrder() {
        try {
            WebElement x = DriverFactory.getDriver() // [S8][S12]
                .findElement(By.cssSelector("[data-test='finish']"));
            x.click();
        } catch (Exception e) { // [S4]
            e.printStackTrace(); // [S6]
        }
    }

    // [S7]  "checkDone" — ambiguous name
    // [S9]  Logs mismatch instead of asserting
    // [S2]  ".complete-header" hardcoded — same as iCompleteFullCheckoutFlow
    // [S8]  Variable tmp
    // [S12] Direct element access without wait
    @Then("checkDone with message {string}")
    public void checkDoneWithMessage(String expected) {
        try {
            WebElement tmp = DriverFactory.getDriver() // [S8][S12]
                .findElement(By.cssSelector(".complete-header"));
            String actual = tmp.getText();
            System.out.println("Expected='" + expected + "' Got='" + actual + "'"); // [S6]
            if (!actual.contains(expected)) {
                System.out.println("ORDER MESSAGE MISMATCH"); // [S6][S9] no assertion thrown
            }
        } catch (Exception e) { // [S4]
            // [S5]
        }
    }

    // [S10] Duplicate summary-visible check — same pattern as BadInventorySteps
    // [S2]  ".summary_total_label" hardcoded
    // [S9]  No assertion that total format is correct
    // [S12] Direct element access without wait
    @Then("I am on the checkout overview page with total")
    public void iAmOnCheckoutOverviewPageWithTotal() {
        try {
            WebElement x = DriverFactory.getDriver() // [S8][S12]
                .findElement(By.cssSelector(".summary_total_label"));
            System.out.println("Total label: " + x.getText()); // [S6][S9]
        } catch (Exception e) { // [S4]
            System.out.println("Overview check failed"); // [S6]
        }
    }

    // [S9]  No assertion that URL contains "checkout"
    // [S12] Direct URL read — no wait for page to settle
    @Then("I am on the checkout page")
    public void iAmOnTheCheckoutPage() {
        try {
            String tmp = DriverFactory.getDriver().getCurrentUrl(); // [S8]
            System.out.println("Current URL: " + tmp); // [S6][S9]
        } catch (Exception e) { // [S4]
            // [S5]
        }
    }

    // [S10] Duplicate of BadLoginSteps.elementWithCssIsVisible — 6th copy across bad steps
    // [S12] Direct element access without wait
    @Then("element with css5 {string} is visible")
    public void elementWithCss5IsVisible(String css) {
        try {
            WebElement el = DriverFactory.getDriver() // [S12]
                .findElement(By.cssSelector(css));
            System.out.println("Element visible: " + el.isDisplayed()); // [S6]
        } catch (Exception e) { // [S4]
            // [S5]
        }
    }

    // [S10] Duplicate of BadLoginSteps.elementWithCssHasText — same body
    // [S12] Direct element access without wait
    @Then("element with css5 {string} has text {string}")
    public void elementWithCss5HasText(String css, String expectedText) {
        try {
            WebElement x = DriverFactory.getDriver() // [S8][S12]
                .findElement(By.cssSelector(css));
            String tmp = x.getText(); // [S8]
            System.out.println("Expected='" + expectedText + "' Got='" + tmp + "'"); // [S6]
            if (!tmp.equals(expectedText)) {
                System.out.println("TEXT MISMATCH"); // [S6][S9] no assertion
            }
        } catch (Exception e) { // [S4]
            System.out.println("Text check failed on: " + css); // [S6]
        }
    }

    // [S9]  No assertion that URL indicates correct checkout step
    // [S12] Direct URL read — no wait
    @Then("I am on the checkout info page")
    public void iAmOnTheCheckoutInfoPage() {
        try {
            String tmp = DriverFactory.getDriver().getCurrentUrl(); // [S8]
            System.out.println("URL: " + tmp); // [S6][S9]
        } catch (Exception e) { // [S4]
            // [S5]
        }
    }
}
