package com.automation.steps.badexamples;

// =============================================================================
// INTENTIONALLY NON-COMPLIANT — POC SONARQUBE DEMONSTRATION
//
// NOT in the Cucumber glue path — never executed. For SonarQube analysis only.
//
// SonarQube Issues Demonstrated:
//   [S2]  Hardcoded product names, locators, and URLs in step code
//   [S3]  Thread.sleep() instead of explicit waits
//   [S4]  Generic Exception caught
//   [S5]  Empty catch blocks
//   [S6]  System.out.println instead of logger
//   [S7]  Non-descriptive: addItem(), goCart(), checkCart()
//   [S8]  Non-descriptive variables: x, y, tmp, el
//   [S9]  Returning without signalling failure
//   [S10] Duplicate logic across step methods (element-click, element-visible)
//   [S11] Steps that mix login, add-to-cart, and navigation in one method
// =============================================================================

import com.automation.base.DriverFactory;
import com.automation.pages.badexamples.BadCartPage;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class BadAddToCartSteps {

    // [S11] This step clicks the button AND waits AND verifies the badge — three concerns
    // [S2]  XPath built with hardcoded data-test attribute pattern
    // [S8]  Variables el, tmp
    @When("I click xpath {string} to add product")
    public void iClickXpathToAddProduct(String xpath) {
        try {
            Thread.sleep(1000); // [S3]
            WebElement el = DriverFactory.getDriver() // [S8]
                .findElement(By.xpath(xpath));
            el.click();
            Thread.sleep(1500); // [S3] arbitrary post-click sleep
            // [S11] Badge assertion inside a @When step — wrong concern
            List<WebElement> tmp = DriverFactory.getDriver() // [S8]
                .findElements(By.cssSelector(".shopping_cart_badge"));
            System.out.println("Cart badge count: " + // [S6]
                (tmp.isEmpty() ? "0" : tmp.get(0).getText()));
        } catch (Exception e) { // [S4]
            System.out.println("Add to cart failed: " + e); // [S6]
        }
    }

    // [S10] Duplicate of BadLoginSteps.iClickXpath — same method body, third class
    // [S8]  Variable x
    @When("I click xpath3 {string}")
    public void iClickXpath3(String xpath) {
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

    // [S10] Duplicate of BadLoginSteps.iClickCss — same body, different class
    // [S8]  Variable y
    @When("I click css3 {string}")
    public void iClickCss3(String css) {
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

    // [S10] Duplicate of element-visible assertion — four copies across bad step files
    // [S8]  Variable tmp
    @Then("element with css3 {string} is visible")
    public void elementWithCss3IsVisible(String css) {
        try {
            Thread.sleep(2000); // [S3]
            WebElement tmp = DriverFactory.getDriver() // [S8]
                .findElement(By.cssSelector(css));
            System.out.println("Is displayed: " + tmp.isDisplayed()); // [S6]
        } catch (Exception e) { // [S4]
            // [S5] element absence silently ignored
        }
    }

    // [S10] Duplicate of element-text-check — same as BadLoginSteps version
    // [S8]  Variables x, tmp
    @Then("element with css3 {string} has text {string}")
    public void elementHasText(String css, String expectedText) {
        try {
            Thread.sleep(1000); // [S3]
            WebElement x = DriverFactory.getDriver() // [S8]
                .findElement(By.cssSelector(css));
            String tmp = x.getText(); // [S8]
            if (!tmp.equals(expectedText)) {
                System.out.println("Mismatch: expected=" + expectedText + " got=" + tmp); // [S6]
                // [S9] No assertion thrown — test continues with wrong state
            }
        } catch (Exception e) { // [S4]
            System.out.println("Text check failed"); // [S6]
        }
    }

    // [S11] One step navigates to URL + logs in + adds specific product — entirely wrong granularity
    // [S2]  Hardcoded URL, hardcoded product XPath, hardcoded credentials
    // [S8]  Variables a, b, c
    @When("I add backpack to cart as logged in user")
    public void iAddBackpackToCartAsLoggedInUser() {
        try {
            WebDriver driver = DriverFactory.getDriver();
            // [S2] Hardcoded URL in step code
            driver.get("https://www.saucedemo.com");
            Thread.sleep(2000); // [S3]
            // [S2] Hardcoded credentials in step code
            WebElement a = driver.findElement(By.id("user-name")); // [S8]
            a.sendKeys("standard_user");
            WebElement b = driver.findElement(By.id("password")); // [S8]
            b.sendKeys("secret_sauce");
            driver.findElement(By.id("login-button")).click();
            Thread.sleep(2000); // [S3]
            // [S2] Hardcoded product-specific XPath
            WebElement c = driver.findElement( // [S8]
                By.xpath("//button[@data-test='add-to-cart-sauce-labs-backpack']")
            );
            c.click();
            Thread.sleep(1000); // [S3]
        } catch (Exception e) { // [S4]
            System.out.println("Add backpack failed: " + e.getMessage()); // [S6]
        }
    }

    // [S2]  Badge locator hardcoded — same as in BadInventorySteps
    // [S8]  Variable x
    // [S9]  Returns "0" on exception — caller cannot tell if cart was actually empty
    @Then("cart has {string} items")
    public void cartHasItems(String expectedCount) {
        try {
            Thread.sleep(1000); // [S3]
            List<WebElement> x = DriverFactory.getDriver() // [S8]
                .findElements(By.cssSelector(".shopping_cart_badge"));
            String actual = x.isEmpty() ? "0" : x.get(0).getText();
            System.out.println("Cart count expected=" + expectedCount + " actual=" + actual); // [S6]
            // [S9] No assertion — mismatch is only logged, not asserted
        } catch (Exception e) { // [S4]
            // [S5]
        }
    }

    // [S2]  ".cart_item" hardcoded — third repetition across bad files
    // [S5]  Failure hidden — empty list returned on error
    @Then("the cart page shows items")
    public void theCartPageShowsItems() {
        try {
            Thread.sleep(1500); // [S3]
            List<WebElement> items = DriverFactory.getDriver()
                .findElements(By.cssSelector(".cart_item")); // [S2]
            System.out.println("Items in cart: " + items.size()); // [S6]
            // [S9] No assertion on whether items.size() > 0
        } catch (Exception e) { // [S4]
            System.out.println("Cart page check failed"); // [S6]
        }
    }
}
