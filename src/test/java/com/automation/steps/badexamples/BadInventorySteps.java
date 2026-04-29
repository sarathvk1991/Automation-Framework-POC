package com.automation.steps.badexamples;

// =============================================================================
// INTENTIONALLY NON-COMPLIANT — POC SONARQUBE DEMONSTRATION
//
// NOT in the Cucumber glue path — never executed. For SonarQube analysis only.
//
// SonarQube Issues Demonstrated:
//   [S2]  Hardcoded test data and locators inside step code
//   [S3]  Intentional hard wait in iAmLoggedInAsDefaultUser — java:S2925
//   [S4]  Generic Exception caught
//   [S5]  Empty catch blocks
//   [S6]  System.out.println instead of logger
//   [S8]  Non-descriptive variables: x, y, tmp, result
//   [S9]  No assertion on mismatch — logs only
//   [S10] Duplicate assertion logic across multiple step methods
//   [S11] Steps that combine selection + validation in one method
//   [S12] Direct element access without wait throughout
// =============================================================================

import com.automation.base.DriverFactory;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.List;

public class BadInventorySteps {

    // [S11] Step selects AND validates — two concerns in a @When step
    // [S2]  Locator string hardcoded; visible text hardcoded in caller
    // [S8]  Variables x, tmp
    // [S12] Direct element access without wait
    @When("I select option {string} from css {string}")
    public void iSelectOptionFromCss(String optionText, String css) {
        try {
            WebElement x = DriverFactory.getDriver() // [S8][S12]
                .findElement(By.cssSelector(css));
            Select tmp = new Select(x); // [S8]
            tmp.selectByVisibleText(optionText);
            System.out.println("Selected: " + optionText + " from " + css); // [S6]
        } catch (Exception e) { // [S4]
            System.out.println("Select failed: " + e.getMessage()); // [S6]
        }
    }

    // [S10] Duplicate of price-collection logic also present in BadInventoryPage
    // [S8]  Variables x, y, result, prev
    // [S9]  No assertion thrown — sort failure only logged
    // [S12] Direct findElements without wait
    @Then("prices are in ascending order")
    public void pricesAreInAscendingOrder() {
        try {
            List<WebElement> x = DriverFactory.getDriver() // [S8][S12]
                .findElements(By.cssSelector(".inventory_item_price"));
            List<Double> result = new ArrayList<>(); // [S8]
            for (WebElement y : x) { // [S8]
                String text = y.getText().replace("$", "");
                result.add(Double.parseDouble(text));
            }
            // [S10] Same validation pattern duplicated from BadInventoryPage.doSortAndValidate
            double prev = -1; // [S8]
            for (double p : result) {
                if (p < prev) {
                    System.out.println("SORT FAILURE: " + p + " < " + prev); // [S6][S9]
                    return;
                }
                prev = p;
            }
            System.out.println("Prices OK: " + result); // [S6]
        } catch (Exception e) { // [S4]
            System.out.println("Price check failed: " + e.getMessage()); // [S6]
        }
    }

    // [S11] Checks element presence AND reads page title — two concerns mixed
    // [S8]  Variables x, tmp
    // [S12] Direct element access without wait
    @Then("css {string} is present in DOM")
    public void cssIsPresentInDom(String css) {
        try {
            WebElement x = DriverFactory.getDriver() // [S8][S12]
                .findElement(By.cssSelector(css));
            System.out.println("Found: " + x.isDisplayed()); // [S6]
            // [S11] Also reads title — mixed concern inside a presence check
            String tmp = DriverFactory.getDriver().getTitle(); // [S8]
            System.out.println("Page title: " + tmp); // [S6]
        } catch (Exception e) { // [S4]
            // [S5] Element not found is silently ignored — false positive
        }
    }

    // [S10] Duplicate of BadLoginSteps.iClickCss — same method body, different class
    // [S8]  Variable y
    // [S12] Direct click without wait
    @When("I click css2 {string}")
    public void iClickCss2(String css) {
        try {
            WebElement y = DriverFactory.getDriver() // [S8][S12]
                .findElement(By.cssSelector(css));
            y.click();
        } catch (Exception e) { // [S4]
            System.out.println("Click failed on: " + css); // [S6]
        }
    }

    // [S10] Duplicate of BadLoginSteps.iClickXpath — same body
    // [S12] Direct click without wait
    @When("I click xpath2 {string}")
    public void iClickXpath2(String xpath) {
        try {
            WebDriver driver = DriverFactory.getDriver();
            WebElement x = driver.findElement(By.xpath(xpath)); // [S8][S12]
            x.click();
        } catch (Exception e) { // [S4]
            System.out.println("XPath click failed: " + xpath); // [S6]
        }
    }

    // [S10] Duplicate of BadLoginSteps.elementWithCssIsVisible — same body
    // [S12] Direct element access without wait
    @Then("element with css2 {string} is visible")
    public void elementWithCss2IsVisible(String css) {
        try {
            WebElement tmp = DriverFactory.getDriver() // [S8][S12]
                .findElement(By.cssSelector(css));
            System.out.println("Visible: " + tmp.isDisplayed()); // [S6]
        } catch (Exception e) { // [S4]
            // [S5]
        }
    }

    // [S2]  Hardcoded "standard_user" / "secret_sauce" — test data in step code
    // [S3]  Hard wait after login click — intentional java:S2925 demo
    // [S11] Step manufactures its own credentials instead of taking from scenario
    // [S12] All element interactions direct — no explicit wait
    @When("I am logged in as default user")
    public void iAmLoggedInAsDefaultUser() {
        try {
            WebDriver driver = DriverFactory.getDriver();
            driver.findElement(By.id("user-name")).sendKeys("standard_user"); // [S2][S12]
            driver.findElement(By.id("password")).sendKeys("secret_sauce");   // [S2][S12]
            driver.findElement(By.id("login-button")).click();                // [S12]
            Thread.sleep(2000); // [S3] intentional hard wait — java:S2925
        } catch (Exception e) { // [S4]
            System.out.println("Default login failed"); // [S6]
        }
    }

    // [S10] Third copy of the element-visible assertion pattern
    // [S12] Direct XPath access without wait
    @Then("xpath {string} is visible")
    public void xpathIsVisible(String xpath) {
        try {
            WebElement x = DriverFactory.getDriver() // [S8][S12]
                .findElement(By.xpath(xpath));
            System.out.println("XPath element visible: " + x.isDisplayed()); // [S6]
        } catch (Exception e) { // [S4]
            // [S5]
        }
    }
}
