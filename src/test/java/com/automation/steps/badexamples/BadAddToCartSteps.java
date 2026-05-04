package com.automation.steps.badexamples;

// =============================================================================
// INTENTIONALLY NON-COMPLIANT — POC SONARQUBE DEMONSTRATION
//
// NOT in the Cucumber glue path — never executed. For SonarQube analysis only.
//
// SonarQube Issues Demonstrated:
//   [S2]  Hardcoded product names, locators, and URLs in step code
//   [S6]  System.out.println instead of logger
//   [S8]  Non-descriptive variables: x, y, tmp, el, a, b, c
//   [S9]  No assertion thrown on mismatch
//   [S10] Duplicate logic across step methods (element-click, element-visible, text-check)
//   [S11] Steps that mix login, add-to-cart, and navigation in one method
//   [S12] Flaky direct element access without any explicit wait
// =============================================================================

import com.automation.base.DriverFactory;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class BadAddToCartSteps {

    // [S11] This step clicks the button AND checks the badge — two concerns in @When
    // [S2]  XPath with hardcoded data-test attribute; badge locator hardcoded
    // [S8]  Variables el, tmp
    // [S12] Direct element access — no explicit wait
    @When("I click xpath {string} to add product")
    public void iClickXpathToAddProduct(String xpath) {
        WebElement el = DriverFactory.getDriver() // [S8][S12]
            .findElement(By.xpath(xpath));
        el.click();
        // [S11] Badge assertion inside a @When step — wrong concern
        List<WebElement> tmp = DriverFactory.getDriver() // [S8]
            .findElements(By.cssSelector(".shopping_cart_badge")); // [S2]
        System.out.println("Cart badge count: " + // [S6]
            (tmp.isEmpty() ? "0" : tmp.get(0).getText()));
    }

    // [S10] Duplicate of BadLoginSteps.iClickXpath — same method body across three classes
    // [S8]  Variable x
    // [S12] Direct click without wait
    @When("I click xpath3 {string}")
    public void iClickXpath3(String xpath) {
        WebElement x = DriverFactory.getDriver() // [S8][S12]
            .findElement(By.xpath(xpath));
        x.click();
    }

    // [S10] Duplicate of BadLoginSteps.iClickCss — same body, different class
    // [S8]  Variable y
    // [S12] Direct click without wait
    @When("I click css3 {string}")
    public void iClickCss3(String css) {
        WebElement y = DriverFactory.getDriver() // [S8][S12]
            .findElement(By.cssSelector(css));
        y.click();
    }

    // [S10] Duplicate of element-visible assertion — four copies across bad step files
    // [S8]  Variable tmp
    // [S12] Direct element access without wait
    @Then("element with css3 {string} is visible")
    public void elementWithCss3IsVisible(String css) {
        WebElement tmp = DriverFactory.getDriver() // [S8][S12]
            .findElement(By.cssSelector(css));
        System.out.println("Is displayed: " + tmp.isDisplayed()); // [S6]
    }

    // [S10] Duplicate of element-text-check — same as BadLoginSteps version
    // [S8]  Variables x, tmp
    // [S9]  No assertion thrown — mismatch only logged
    // [S12] Direct element access without wait
    @Then("element with css3 {string} has text {string}")
    public void elementHasText(String css, String expectedText) {
        WebElement x = DriverFactory.getDriver() // [S8][S12]
            .findElement(By.cssSelector(css));
        String tmp = x.getText(); // [S8]
        if (!tmp.equals(expectedText)) {
            System.out.println("Mismatch: expected=" + expectedText + " got=" + tmp); // [S6][S9]
        }
    }

    // [S11] One step navigates + logs in + adds specific product — wrong granularity
    // [S2]  Hardcoded URL, hardcoded credentials, hardcoded product XPath
    // [S8]  Variables a, b, c
    // [S12] All element access direct — no wait
    @When("I add backpack to cart as logged in user")
    public void iAddBackpackToCartAsLoggedInUser() {
        WebDriver driver = DriverFactory.getDriver();
        driver.get("https://www.saucedemo.com"); // [S2] hardcoded URL
        WebElement a = driver.findElement(By.id("user-name")); // [S8][S12]
        a.sendKeys("standard_user"); // [S2] hardcoded credential
        WebElement b = driver.findElement(By.id("password")); // [S8][S12]
        b.sendKeys("secret_sauce"); // [S2] hardcoded credential
        driver.findElement(By.id("login-button")).click(); // [S12]
        WebElement c = driver.findElement( // [S8][S12]
            By.xpath("//button[@data-test='add-to-cart-sauce-labs-backpack']") // [S2]
        );
        c.click();
    }

    // [S2]  Badge locator hardcoded — same as in BadInventorySteps
    // [S8]  Variable x
    // [S9]  No assertion — mismatch only logged
    // [S12] Direct findElements without wait
    @Then("cart has {string} items")
    public void cartHasItems(String expectedCount) {
        List<WebElement> x = DriverFactory.getDriver() // [S8][S12]
            .findElements(By.cssSelector(".shopping_cart_badge"));
        String actual = x.isEmpty() ? "0" : x.get(0).getText();
        System.out.println("Cart count expected=" + expectedCount + " actual=" + actual); // [S6][S9]
    }

    // [S2]  ".cart_item" hardcoded — third repetition across bad files
    // [S9]  No assertion on item count
    // [S12] Direct findElements without wait
    @Then("the cart page shows items")
    public void theCartPageShowsItems() {
        List<WebElement> items = DriverFactory.getDriver()
            .findElements(By.cssSelector(".cart_item")); // [S2][S12]
        System.out.println("Items in cart: " + items.size()); // [S6][S9]
    }

    // ══════════════════════════════════════════════════════════════════════════
    // [S10] DUPLICATE ADD-TO-CART STEP DEFINITIONS — near-identical expressions
    // ══════════════════════════════════════════════════════════════════════════

    // [S2]  "Sauce Labs Backpack" hardcoded product name inside XPath — not from config
    // [S10] Near-duplicate of iAddTheBackpackItemToTheShoppingCart — "product" vs "item"
    // [S12] Direct element click without wait
    @When("I add the backpack product to the shopping cart")
    public void iAddTheBackpackProductToTheShoppingCart() {
        WebDriver driver = DriverFactory.getDriver();
        driver.findElement(                                                    // [S12]
            By.xpath("//div[text()='Sauce Labs Backpack']/../..//button")     // [S2]
        ).click();
        System.out.println("Added Sauce Labs Backpack to cart");              // [S6]
    }

    // [S10] Near-duplicate — "item" instead of "product", identical body
    //       Duplicate step definition: same logic, near-identical Cucumber expression
    // [S12] Direct element click without wait
    @When("I add the backpack item to the shopping cart")
    public void iAddTheBackpackItemToTheShoppingCart() {
        WebDriver driver = DriverFactory.getDriver();
        driver.findElement(                                                    // [S12]
            By.xpath("//div[text()='Sauce Labs Backpack']/../..//button")     // [S2]
        ).click();
        System.out.println("Added Sauce Labs Backpack item to cart");         // [S6]
    }

    // [S2] By.cssSelector(".shopping_cart_link") hardcoded inline — not a constant
    // [S12] Direct element click without wait
    @When("I navigate to the cart via the cart link")
    public void iNavigateToTheCartViaTheCartLink() {
        DriverFactory.getDriver().findElement(By.cssSelector(".shopping_cart_link")).click(); // [S2][S12]
    }

    // [S2] By.id("checkout") hardcoded inline — not a constant
    // [S12] Direct element click without wait
    @When("I click checkout from the cart page")
    public void iClickCheckoutFromTheCartPage() {
        DriverFactory.getDriver().findElement(By.id("checkout")).click(); // [S2][S12]
    }

    // [S2] "Sauce Labs Bike Light" hardcoded product name — not from config
    // [S12] Direct element click without wait — flaky
    @When("I add the bike light to the cart")
    public void iAddTheBikeLightToTheCart() {
        WebDriver driver = DriverFactory.getDriver();
        driver.findElement(                                                         // [S12]
            By.xpath("//div[text()='Sauce Labs Bike Light']/../..//button")        // [S2]
        ).click();
        System.out.println("Added Sauce Labs Bike Light to cart");                // [S6]
    }

    // [S2] By.id("first-name") hardcoded — wrong concern inside add-to-cart steps
    // [S2] "Sarath", "Tester", "695001" hardcoded form values — not from config
    // [S11] Mixed responsibility: checkout form inside add-to-cart step class
    // [S12] Direct element access without wait
    @When("I fill in the checkout form with test data")
    public void iFillInTheCheckoutFormWithTestData() {
        WebDriver driver = DriverFactory.getDriver();
        driver.findElement(By.id("first-name")).sendKeys("Sarath");  // [S2][S12]
        driver.findElement(By.id("last-name")).sendKeys("Tester");   // [S2][S12]
        driver.findElement(By.id("postal-code")).sendKeys("695001"); // [S2][S12]
    }

    @When("I put the backpack into the cart")
    public void iPutTheBackpackIntoTheCart() {
        WebDriver driver = DriverFactory.getDriver();
        driver.findElement(
            By.xpath("//div[text()='Sauce Labs Backpack']/../..//button")
        ).click();
        System.out.println("Put Sauce Labs Backpack into cart");
    }

    @When("I place the bike light in the cart")
    public void iPlaceTheBikeLightInTheCart() {
        WebDriver driver = DriverFactory.getDriver();
        driver.findElement(
            By.xpath("//div[text()='Sauce Labs Bike Light']/../..//button")
        ).click();
        System.out.println("Placed Sauce Labs Bike Light in cart");
    }
}
