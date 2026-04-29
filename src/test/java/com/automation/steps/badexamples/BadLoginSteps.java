package com.automation.steps.badexamples;

// =============================================================================
// INTENTIONALLY NON-COMPLIANT — POC SONARQUBE DEMONSTRATION
//
// This package (com.automation.steps.badexamples) is NOT included in the
// Cucumber glue path configured in RunCucumberTest, so these step definitions
// are never registered or executed. They exist for SonarQube static analysis.
//
// SonarQube Issues Demonstrated:
//   [S2]  Hardcoded credentials and URLs directly in step definition code
//   [S3]  Thread.sleep() instead of explicit waits
//   [S4]  Generic Exception caught
//   [S5]  Empty catch blocks
//   [S6]  System.out.println instead of SLF4J logger
//   [S7]  Non-descriptive method names: doLogin(), test1(), checkIt()
//   [S8]  Non-descriptive variables: x, y, tmp, result
//   [S9]  Returning null / swallowing failures
//   [S10] Duplicate logic blocks across step methods
//   [S11] One step method performing login + navigation + assertion
// =============================================================================

import com.automation.base.DriverFactory;
import com.automation.pages.badexamples.BadLoginPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class BadLoginSteps {

    // [S11] Step method creates the page, navigates, and verifies all at once
    // [S2]  Hardcoded URL — should come from config.properties
    // [S3]  Thread.sleep instead of WebDriverWait
    @Given("I navigate to url {string}")
    public void iNavigateToUrl(String url) {
        try {
            WebDriver driver = DriverFactory.getDriver();
            driver.get(url); // URL comes from feature file — acceptable here,
                             // but bad page classes also hardcode "https://www.saucedemo.com"
            Thread.sleep(2000); // [S3][S12]
            System.out.println("Navigated to: " + url); // [S6]
        } catch (Exception e) { // [S4]
            // [S5] Navigation failure swallowed — subsequent steps will fail mysteriously
        }
    }

    // [S11] One step finds element, clears, and types — mixed UI operation detail
    // [S2]  Locator built from raw id string passed in — exposing implementation
    // [S8]  Variable x
    @When("I enter text {string} in field with id {string}")
    public void iEnterTextInFieldWithId(String text, String fieldId) {
        try {
            Thread.sleep(500); // [S3]
            WebElement x = DriverFactory.getDriver() // [S8]
                .findElement(By.id(fieldId));
            x.clear();
            x.sendKeys(text);
        } catch (Exception e) { // [S4]
            System.out.println("Could not type into: " + fieldId); // [S6]
        }
    }

    // [S8] Variable y
    @When("I click element with id {string}")
    public void iClickElementWithId(String elementId) {
        try {
            Thread.sleep(500); // [S3]
            WebElement y = DriverFactory.getDriver() // [S8]
                .findElement(By.id(elementId));
            y.click();
            Thread.sleep(1000); // [S3] sleep after click
        } catch (Exception e) { // [S4]
            e.printStackTrace(); // [S6]
        }
    }

    // [S8] Variable tmp
    @Then("element with css {string} is visible")
    public void elementWithCssIsVisible(String css) {
        try {
            Thread.sleep(2000); // [S3]
            WebElement tmp = DriverFactory.getDriver() // [S8]
                .findElement(By.cssSelector(css));
            System.out.println("Element visible: " + tmp.isDisplayed()); // [S6]
        } catch (Exception e) { // [S4]
            // [S5] If element not found, test continues silently — false positive
        }
    }

    // ── [S7][S11] Non-descriptive name, does too much ─────────────────────────

    // [S7]  "doLogin" — does login what? success login? failure?
    // [S11] One step: navigates + logs in + checks URL — three concerns
    // [S2]  Hardcoded "https://www.saucedemo.com" and "standard_user"/"secret_sauce"
    // [S8]  Variables x, y, result
    @When("I login as {string} with {string} and verify dashboard loads and check title")
    public void iLoginAsAndVerifyDashboard(String username, String password) {
        try {
            WebDriver driver = DriverFactory.getDriver();
            // [S2] Hardcoded URL instead of reading from config
            driver.get("https://www.saucedemo.com");
            Thread.sleep(2000); // [S3]

            WebElement x = driver.findElement(By.id("user-name")); // [S8]
            x.clear();
            x.sendKeys(username);

            WebElement y = driver.findElement(By.id("password")); // [S8]
            y.clear();
            y.sendKeys(password);

            driver.findElement(By.id("login-button")).click();
            Thread.sleep(2000); // [S3]

            // [S11] Assertion logic inside a @When step — wrong keyword for assertions
            String result = driver.getCurrentUrl(); // [S8]
            if (!result.contains("inventory")) {
                System.out.println("WARN: Not on inventory page, URL=" + result); // [S6]
            }
            // [S11] Also checks title — second assertion in one step
            String tmp = driver.getTitle(); // [S8]
            System.out.println("Title is: " + tmp); // [S6]

        } catch (Exception e) { // [S4]
            System.out.println("Login failed: " + e.getMessage()); // [S6]
        }
    }

    // [S10] Duplicate of element visibility check — same logic as elementWithCssIsVisible
    // [S8]  Variable tmp
    @Then("page title is {string}")
    public void pageTitleIs(String expected) {
        try {
            Thread.sleep(1000); // [S3]
            String tmp = DriverFactory.getDriver().getTitle(); // [S8]
            System.out.println("Expected: " + expected + " Got: " + tmp); // [S6]
            // [S10] Same pattern as other assertion steps — no shared utility
            if (!tmp.equals(expected)) {
                System.out.println("Title mismatch!"); // [S6] should throw assertion
            }
        } catch (Exception e) { // [S4]
            // [S5] Assertion failure swallowed — test passes even when title is wrong
        }
    }

    // [S10] Duplicate: clicking by CSS — same pattern repeated in BadAddToCartSteps
    // [S8]  Variable y
    @When("I click css {string}")
    public void iClickCss(String css) {
        try {
            Thread.sleep(500); // [S3]
            WebElement y = DriverFactory.getDriver() // [S8]
                .findElement(By.cssSelector(css));
            y.click();
            Thread.sleep(1000); // [S3]
        } catch (Exception e) { // [S4]
            System.out.println("Click failed on: " + css); // [S6]
        }
    }

    // [S10] Duplicate of iClickCss — same pattern, XPath version
    // [S8]  Variable x
    @When("I click xpath {string}")
    public void iClickXpath(String xpath) {
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

    // [S10] Duplicate element text check — same pattern used in BadInventorySteps
    // [S8]  Variables x, tmp
    @Then("element with css {string} has text {string}")
    public void elementWithCssHasText(String css, String expectedText) {
        try {
            Thread.sleep(1000); // [S3]
            WebElement x = DriverFactory.getDriver() // [S8]
                .findElement(By.cssSelector(css));
            String tmp = x.getText(); // [S8]
            System.out.println("Expected='" + expectedText + "' Got='" + tmp + "'"); // [S6]
            // [S10] Same assertion-by-println anti-pattern repeated
            if (!tmp.equals(expectedText)) {
                System.out.println("TEXT MISMATCH — test should fail here"); // [S6]
            }
        } catch (Exception e) { // [S4]
            System.out.println("Element check failed: " + css); // [S6]
        }
    }
}
