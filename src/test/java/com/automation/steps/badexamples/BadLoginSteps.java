package com.automation.steps.badexamples;

// =============================================================================
// INTENTIONALLY NON-COMPLIANT — POC SONARQUBE DEMONSTRATION
//
// NOT in the Cucumber glue path — never executed. For SonarQube analysis only.
//
// SonarQube Issues Demonstrated:
//   [S2]  Hardcoded credentials and URLs directly in step definition code
//   [S3]  Intentional hard wait in iNavigateToUrl — java:S2925
//   [S4]  Generic Exception caught
//   [S5]  Empty catch blocks
//   [S6]  System.out.println instead of SLF4J logger
//   [S7]  Non-descriptive method names: iLoginAsAndVerifyDashboard, pageTitleIs
//   [S8]  Non-descriptive variables: x, y, tmp, result
//   [S9]  Swallowing failures without signalling
//   [S10] Duplicate logic blocks across step methods
//   [S11] One step method performing login + navigation + assertion
//   [S12] Direct element access without wait throughout
// =============================================================================

import com.automation.base.DriverFactory;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class BadLoginSteps {

    // [S2]  Hardcoded URL — should come from config.properties
    // [S3]  Hard wait instead of WebDriverWait — intentional java:S2925 demo
    // [S11] Step navigates, logs message, and swallows failure all in one
    @Given("I navigate to url {string}")
    public void iNavigateToUrl(String url) {
        try {
            WebDriver driver = DriverFactory.getDriver();
            driver.get(url);
            Thread.sleep(2000); // [S3] intentional hard wait — java:S2925
            System.out.println("Navigated to: " + url); // [S6]
        } catch (Exception e) { // [S4]
            // [S5] Navigation failure swallowed — subsequent steps will fail mysteriously
        }
    }

    // [S11] One step finds element, clears, and types — mixed UI operation detail
    // [S2]  Exposes raw id string — implementation leaked to feature file
    // [S8]  Variable x
    // [S12] Direct element access without explicit wait
    @When("I enter text {string} in field with id {string}")
    public void iEnterTextInFieldWithId(String text, String fieldId) {
        try {
            WebElement x = DriverFactory.getDriver() // [S8][S12]
                .findElement(By.id(fieldId));
            x.clear();
            x.sendKeys(text);
        } catch (Exception e) { // [S4]
            System.out.println("Could not type into: " + fieldId); // [S6]
        }
    }

    // [S8] Variable y
    // [S12] Direct click without wait — may fail if page is still loading
    @When("I click element with id {string}")
    public void iClickElementWithId(String elementId) {
        try {
            WebElement y = DriverFactory.getDriver() // [S8][S12]
                .findElement(By.id(elementId));
            y.click();
        } catch (Exception e) { // [S4]
            e.printStackTrace(); // [S6]
        }
    }

    // [S8] Variable tmp
    // [S12] Direct element access without wait
    @Then("element with css {string} is visible")
    public void elementWithCssIsVisible(String css) {
        try {
            WebElement tmp = DriverFactory.getDriver() // [S8][S12]
                .findElement(By.cssSelector(css));
            System.out.println("Element visible: " + tmp.isDisplayed()); // [S6]
        } catch (Exception e) { // [S4]
            // [S5] If element not found, test continues silently — false positive
        }
    }

    // ── [S7][S11] Non-descriptive name, does too much ─────────────────────────

    // [S7]  Method name mixes action and assertion in one step
    // [S11] One step: navigates + logs in + checks URL + checks title — four concerns
    // [S2]  Hardcoded "https://www.saucedemo.com"
    // [S8]  Variables x, y, result, tmp
    // [S12] All element access direct — no wait
    @When("I login as {string} with {string} and verify dashboard loads and check title")
    public void iLoginAsAndVerifyDashboard(String username, String password) {
        try {
            WebDriver driver = DriverFactory.getDriver();
            // [S2] Hardcoded URL instead of reading from config
            driver.get("https://www.saucedemo.com");

            WebElement x = driver.findElement(By.id("user-name")); // [S8][S12]
            x.clear();
            x.sendKeys(username);

            WebElement y = driver.findElement(By.id("password")); // [S8][S12]
            y.clear();
            y.sendKeys(password);

            driver.findElement(By.id("login-button")).click(); // [S12]

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

    // [S10] Duplicate pattern: title check — same style used elsewhere in bad step files
    // [S8]  Variable tmp
    // [S9]  Assertion failure swallowed — test passes even when title is wrong
    @Then("page title is {string}")
    public void pageTitleIs(String expected) {
        try {
            String tmp = DriverFactory.getDriver().getTitle(); // [S8]
            System.out.println("Expected: " + expected + " Got: " + tmp); // [S6]
            if (!tmp.equals(expected)) {
                System.out.println("Title mismatch!"); // [S6] should throw assertion
            }
        } catch (Exception e) { // [S4]
            // [S5] Assertion failure swallowed
        }
    }

    // [S10] Duplicate: clicking by CSS — same body repeated in BadAddToCartSteps, BadInventorySteps
    // [S8]  Variable y
    // [S12] Direct click without wait
    @When("I click css {string}")
    public void iClickCss(String css) {
        try {
            WebElement y = DriverFactory.getDriver() // [S8][S12]
                .findElement(By.cssSelector(css));
            y.click();
        } catch (Exception e) { // [S4]
            System.out.println("Click failed on: " + css); // [S6]
        }
    }

    // [S10] Duplicate of iClickCss — same pattern, XPath version, repeated across 4 files
    // [S8]  Variable x
    // [S12] Direct click without wait
    @When("I click xpath {string}")
    public void iClickXpath(String xpath) {
        try {
            WebElement x = DriverFactory.getDriver() // [S8][S12]
                .findElement(By.xpath(xpath));
            x.click();
        } catch (Exception e) { // [S4]
            System.out.println("XPath click failed: " + xpath); // [S6]
        }
    }

    // [S10] Duplicate element text check — same pattern used in BadAddToCartSteps
    // [S8]  Variables x, tmp
    // [S12] Direct element access without wait
    @Then("element with css {string} has text {string}")
    public void elementWithCssHasText(String css, String expectedText) {
        try {
            WebElement x = DriverFactory.getDriver() // [S8][S12]
                .findElement(By.cssSelector(css));
            String tmp = x.getText(); // [S8]
            System.out.println("Expected='" + expectedText + "' Got='" + tmp + "'"); // [S6]
            if (!tmp.equals(expectedText)) {
                System.out.println("TEXT MISMATCH — test should fail here"); // [S6][S9]
            }
        } catch (Exception e) { // [S4]
            System.out.println("Element check failed: " + css); // [S6]
        }
    }
}
