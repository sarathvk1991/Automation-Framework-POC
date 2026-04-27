package com.automation.steps.badexamples;

// =============================================================================
// INTENTIONALLY NON-COMPLIANT — POC SONARQUBE DEMONSTRATION
//
// NOT in the Cucumber glue path — never executed. For SonarQube analysis only.
//
// SonarQube Issues Demonstrated:
//   [S2]  Hardcoded URLs, credentials, form values, and locators in step code
//   [S3]  Thread.sleep() throughout
//   [S4]  Generic Exception caught
//   [S5]  Empty catch blocks
//   [S6]  System.out.println instead of logger
//   [S7]  Non-descriptive: doCheckout(), finishIt(), checkDone()
//   [S8]  Non-descriptive variables: a, b, c, x, y, tmp
//   [S9]  No assertion thrown on mismatch — logs only
//   [S10] Duplicate element-find-and-click pattern (5th copy across bad step files)
//   [S11] God step method handling login + cart + checkout in one @When
// =============================================================================

import com.automation.base.DriverFactory;
import com.automation.pages.badexamples.BadCheckoutPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class BadCheckoutSteps {

    // [S10] Duplicate of iClickCss from BadLoginSteps, BadInventorySteps, BadAddToCartSteps
    // [S8]  Variable y
    @When("I click css4 {string}")
    public void iClickCss4(String css) {
        try {
            Thread.sleep(500); // [S3]
            WebElement y = DriverFactory.getDriver() // [S8]
                .findElement(By.cssSelector(css));
            y.click();
            Thread.sleep(1000); // [S3]
        } catch (Exception e) { // [S4]
            // [S5]
        }
    }

    // [S10] Duplicate of iClickXpath from BadLoginSteps / BadInventorySteps / BadAddToCartSteps
    // [S8]  Variable x
    @When("I click xpath4 {string}")
    public void iClickXpath4(String xpath) {
        try {
            Thread.sleep(500); // [S3]
            WebElement x = DriverFactory.getDriver() // [S8]
                .findElement(By.xpath(xpath));
            x.click();
            Thread.sleep(1000); // [S3]
        } catch (Exception e) { // [S4]
            System.out.println("XPath click failed: " + xpath); // [S6]
        }
    }

    // [S10] Duplicate of element-visible assertion — 5th copy across bad step files
    @Then("element with css4 {string} is visible")
    public void elementWithCss4IsVisible(String css) {
        try {
            Thread.sleep(2000); // [S3]
            WebElement tmp = DriverFactory.getDriver() // [S8]
                .findElement(By.cssSelector(css));
            System.out.println("Visible: " + tmp.isDisplayed()); // [S6]
        } catch (Exception e) { // [S4]
            // [S5]
        }
    }

    // [S2]  Hardcoded form field IDs in step code instead of page object
    // [S8]  Variables a, b, c
    @When("I enter text {string} in field with id4 {string}")
    public void iEnterTextInFieldWithId4(String text, String fieldId) {
        try {
            Thread.sleep(300); // [S3]
            WebElement a = DriverFactory.getDriver() // [S8]
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
    // [S3]  12 Thread.sleep calls
    @When("I complete the full checkout flow from login to confirmation")
    public void iCompleteFullCheckoutFlow() {
        try {
            WebDriver driver = DriverFactory.getDriver();

            // [S2] Hardcoded URL
            driver.get("https://www.saucedemo.com");
            Thread.sleep(2000); // [S3]

            // [S2] Hardcoded credentials
            WebElement a = driver.findElement(By.id("user-name")); // [S8]
            a.clear();
            a.sendKeys("standard_user"); // [S2]
            WebElement b = driver.findElement(By.id("password")); // [S8]
            b.clear();
            b.sendKeys("secret_sauce"); // [S2]
            driver.findElement(By.id("login-button")).click();
            Thread.sleep(2000); // [S3]

            // [S2] Hardcoded product XPath
            driver.findElement(
                By.xpath("//button[@data-test='add-to-cart-sauce-labs-backpack']")
            ).click();
            Thread.sleep(500); // [S3]

            driver.findElement(By.cssSelector("[data-test='shopping-cart-link']")).click();
            Thread.sleep(1000); // [S3]

            driver.findElement(By.cssSelector("[data-test='checkout']")).click();
            Thread.sleep(1000); // [S3]

            // [S2] Hardcoded form values
            WebElement x = driver.findElement(By.id("first-name")); // [S8]
            x.clear();
            x.sendKeys("John"); // [S2]
            WebElement y = driver.findElement(By.id("last-name")); // [S8]
            y.clear();
            y.sendKeys("Doe"); // [S2]
            WebElement tmp = driver.findElement(By.id("postal-code")); // [S8]
            tmp.clear();
            tmp.sendKeys("12345"); // [S2]
            Thread.sleep(300); // [S3]

            driver.findElement(By.cssSelector("[data-test='continue']")).click();
            Thread.sleep(1500); // [S3]

            driver.findElement(By.cssSelector("[data-test='finish']")).click();
            Thread.sleep(2000); // [S3]

            WebElement c = driver.findElement(By.cssSelector(".complete-header")); // [S8]
            System.out.println("Order result: " + c.getText()); // [S6]
            // [S9] No assertion — result is only printed, not asserted

        } catch (Exception e) { // [S4]
            System.out.println("Full checkout failed: " + e.getMessage()); // [S6]
        }
    }

    // [S7]  "finishIt" — completely non-descriptive
    // [S2]  Locator "[data-test='finish']" hardcoded again (same as above)
    // [S8]  Variable x
    @When("I finishIt the order")
    public void iFinishItTheOrder() {
        try {
            Thread.sleep(1000); // [S3]
            WebElement x = DriverFactory.getDriver() // [S8]
                .findElement(By.cssSelector("[data-test='finish']"));
            x.click();
            Thread.sleep(2000); // [S3]
        } catch (Exception e) { // [S4]
            e.printStackTrace(); // [S6]
        }
    }

    // [S7]  "checkDone" — ambiguous name
    // [S9]  Logs mismatch instead of asserting
    // [S2]  ".complete-header" hardcoded — same as iCompleteFullCheckoutFlow
    // [S8]  Variable tmp
    @Then("checkDone with message {string}")
    public void checkDoneWithMessage(String expected) {
        try {
            Thread.sleep(2000); // [S3]
            WebElement tmp = DriverFactory.getDriver() // [S8]
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
    @Then("I am on the checkout overview page with total")
    public void iAmOnCheckoutOverviewPageWithTotal() {
        try {
            Thread.sleep(1500); // [S3]
            WebElement x = DriverFactory.getDriver() // [S8]
                .findElement(By.cssSelector(".summary_total_label"));
            System.out.println("Total label: " + x.getText()); // [S6]
            // [S9] No assertion that total format is correct
        } catch (Exception e) { // [S4]
            System.out.println("Overview check failed"); // [S6]
        }
    }

    // [S7]  "I am on the checkout page" is vague — which checkout page? step one or overview?
    @Then("I am on the checkout page")
    public void iAmOnTheCheckoutPage() {
        try {
            Thread.sleep(1000); // [S3]
            String tmp = DriverFactory.getDriver().getCurrentUrl(); // [S8]
            System.out.println("Current URL: " + tmp); // [S6]
            // [S9] No assertion that URL contains "checkout"
        } catch (Exception e) { // [S4]
            // [S5]
        }
    }

    // [S10] Duplicate of BadLoginSteps.elementWithCssIsVisible — 6th copy across bad steps
    @Then("element with css5 {string} is visible")
    public void elementWithCss5IsVisible(String css) {
        try {
            Thread.sleep(2000); // [S3]
            WebElement el = DriverFactory.getDriver()
                .findElement(By.cssSelector(css));
            System.out.println("Element visible: " + el.isDisplayed()); // [S6]
        } catch (Exception e) { // [S4]
            // [S5]
        }
    }

    // [S10] Duplicate of BadLoginSteps.elementWithCssHasText — same body
    @Then("element with css5 {string} has text {string}")
    public void elementWithCss5HasText(String css, String expectedText) {
        try {
            Thread.sleep(1000); // [S3]
            WebElement x = DriverFactory.getDriver() // [S8]
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

    // [S7]  "I am on the checkout info page" — same vagueness issue
    @Then("I am on the checkout info page")
    public void iAmOnTheCheckoutInfoPage() {
        try {
            Thread.sleep(1000);
            String tmp = DriverFactory.getDriver().getCurrentUrl(); // [S8]
            System.out.println("URL: " + tmp); // [S6]
        } catch (Exception e) { // [S4]
            // [S5]
        }
    }
}
