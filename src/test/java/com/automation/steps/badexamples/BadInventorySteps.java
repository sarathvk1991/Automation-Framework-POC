package com.automation.steps.badexamples;

// =============================================================================
// INTENTIONALLY NON-COMPLIANT — POC SONARQUBE DEMONSTRATION
//
// NOT in the Cucumber glue path — never executed. For SonarQube analysis only.
//
// SonarQube Issues Demonstrated:
//   [S2]  Hardcoded test data and locators inside step code
//   [S3]  Thread.sleep() instead of explicit waits
//   [S4]  Generic Exception caught
//   [S5]  Empty catch blocks
//   [S6]  System.out.println instead of logger
//   [S7]  Non-descriptive method names: checkIt(), doStuff(), test2()
//   [S8]  Non-descriptive variables: x, y, tmp, result
//   [S10] Duplicate assertion logic across multiple step methods
//   [S11] Steps that combine selection + validation in one method
// =============================================================================

import com.automation.base.DriverFactory;
import com.automation.pages.badexamples.BadInventoryPage;
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
    // [S2]  Locator string "[data-test='product-sort-container']" hardcoded
    // [S8]  Variables x, tmp
    @When("I select option {string} from css {string}")
    public void iSelectOptionFromCss(String optionText, String css) {
        try {
            Thread.sleep(1000); // [S3]
            WebElement x = DriverFactory.getDriver() // [S8]
                .findElement(By.cssSelector(css));
            Select tmp = new Select(x); // [S8]
            tmp.selectByVisibleText(optionText);
            Thread.sleep(500); // [S3] sleep after selection
            System.out.println("Selected: " + optionText + " from " + css); // [S6]
        } catch (Exception e) { // [S4]
            System.out.println("Select failed: " + e.getMessage()); // [S6]
        }
    }

    // [S7]  "checkIt" — check what? prices? names? counts?
    // [S10] Duplicate of price-collection logic also present in BadInventoryPage
    // [S8]  Variables x, y, result, prev
    @Then("prices are in ascending order")
    public void pricesAreInAscendingOrder() {
        try {
            Thread.sleep(1000); // [S3]
            List<WebElement> x = DriverFactory.getDriver() // [S8]
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
                    System.out.println("SORT FAILURE: " + p + " < " + prev); // [S6]
                    // [S5] No assertion thrown — test continues as if sort was correct
                    return;
                }
                prev = p;
            }
            System.out.println("Prices OK: " + result); // [S6]
        } catch (Exception e) { // [S4]
            System.out.println("Price check failed: " + e.getMessage()); // [S6]
        }
    }

    // [S7]  "doStuff" — completely opaque
    // [S11] Checks presence of element AND logs title — two concerns
    // [S8]  Variables x, tmp
    @Then("css {string} is present in DOM")
    public void cssIsPresentInDom(String css) {
        try {
            Thread.sleep(1500); // [S3]
            WebElement x = DriverFactory.getDriver() // [S8]
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
    @When("I click css2 {string}")
    public void iClickCss2(String css) {
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

    // [S10] Duplicate of BadLoginSteps.iClickXpath — same body
    @When("I click xpath2 {string}")
    public void iClickXpath2(String xpath) {
        try {
            Thread.sleep(500); // [S3]
            WebDriver driver = DriverFactory.getDriver();
            WebElement x = driver.findElement(By.xpath(xpath)); // [S8]
            x.click();
            Thread.sleep(1000); // [S3]
        } catch (Exception e) { // [S4]
            System.out.println("XPath click failed: " + xpath); // [S6]
        }
    }

    // [S10] Duplicate of BadLoginSteps.elementWithCssIsVisible — same body
    @Then("element with css2 {string} is visible")
    public void elementWithCss2IsVisible(String css) {
        try {
            Thread.sleep(2000); // [S3]
            WebElement tmp = DriverFactory.getDriver() // [S8]
                .findElement(By.cssSelector(css));
            System.out.println("Visible: " + tmp.isDisplayed()); // [S6]
        } catch (Exception e) { // [S4]
            // [S5]
        }
    }

    // [S7]  "test2" — completely meaningless method name
    // [S2]  Hardcoded "standard_user" / "secret_sauce" — test data in step code
    // [S11] Step enters credentials it manufactured itself (not from scenario)
    @When("I am logged in as default user")
    public void iAmLoggedInAsDefaultUser() {
        try {
            WebDriver driver = DriverFactory.getDriver();
            // [S2] Hardcoded credentials — test data belongs in feature file
            driver.findElement(By.id("user-name")).sendKeys("standard_user");
            Thread.sleep(300); // [S3]
            driver.findElement(By.id("password")).sendKeys("secret_sauce");
            Thread.sleep(300); // [S3]
            driver.findElement(By.id("login-button")).click();
            Thread.sleep(2000); // [S3]
        } catch (Exception e) { // [S4]
            System.out.println("Default login failed"); // [S6]
        }
    }

    // [S10] Third copy of the element-visible assertion pattern
    @Then("xpath {string} is visible")
    public void xpathIsVisible(String xpath) {
        try {
            Thread.sleep(2000); // [S3]
            WebElement x = DriverFactory.getDriver() // [S8]
                .findElement(By.xpath(xpath));
            System.out.println("XPath element visible: " + x.isDisplayed()); // [S6]
        } catch (Exception e) { // [S4]
            // [S5]
        }
    }
}
