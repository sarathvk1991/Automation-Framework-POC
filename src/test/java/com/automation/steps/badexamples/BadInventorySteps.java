package com.automation.steps.badexamples;

// =============================================================================
// INTENTIONALLY NON-COMPLIANT — POC SONARQUBE DEMONSTRATION
//
// NOT in the Cucumber glue path — never executed. For SonarQube analysis only.
//
// SonarQube Issues Demonstrated:
//   [S2]  Hardcoded test data and locators inside step code
//   [S3]  Intentional hard wait in iAmLoggedInAsDefaultUser — java:S2925
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
        WebElement x = DriverFactory.getDriver() // [S8][S12]
            .findElement(By.cssSelector(css));
        Select tmp = new Select(x); // [S8]
        tmp.selectByVisibleText(optionText);
        System.out.println("Selected: " + optionText + " from " + css); // [S6]
    }

    // [S10] Duplicate of price-collection logic also present in BadInventoryPage
    // [S8]  Variables x, y, result, prev
    // [S9]  No assertion thrown — sort failure only logged
    // [S12] Direct findElements without wait
    @Then("prices are in ascending order")
    public void pricesAreInAscendingOrder() {
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
    }

    // [S11] Checks element presence AND reads page title — two concerns mixed
    // [S8]  Variables x, tmp
    // [S12] Direct element access without wait
    @Then("css {string} is present in DOM")
    public void cssIsPresentInDom(String css) {
        WebElement x = DriverFactory.getDriver() // [S8][S12]
            .findElement(By.cssSelector(css));
        System.out.println("Found: " + x.isDisplayed()); // [S6]
        // [S11] Also reads title — mixed concern inside a presence check
        String tmp = DriverFactory.getDriver().getTitle(); // [S8]
        System.out.println("Page title: " + tmp); // [S6]
    }

    // [S10] Duplicate of BadLoginSteps.iClickCss — same method body, different class
    // [S8]  Variable y
    // [S12] Direct click without wait
    @When("I click css2 {string}")
    public void iClickCss2(String css) {
        WebElement y = DriverFactory.getDriver() // [S8][S12]
            .findElement(By.cssSelector(css));
        y.click();
    }

    // [S10] Duplicate of BadLoginSteps.iClickXpath — same body
    // [S12] Direct click without wait
    @When("I click xpath2 {string}")
    public void iClickXpath2(String xpath) {
        WebDriver driver = DriverFactory.getDriver();
        WebElement x = driver.findElement(By.xpath(xpath)); // [S8][S12]
        x.click();
    }

    // [S10] Duplicate of BadLoginSteps.elementWithCssIsVisible — same body
    // [S12] Direct element access without wait
    @Then("element with css2 {string} is visible")
    public void elementWithCss2IsVisible(String css) {
        WebElement tmp = DriverFactory.getDriver() // [S8][S12]
            .findElement(By.cssSelector(css));
        System.out.println("Visible: " + tmp.isDisplayed()); // [S6]
    }

    // [S2]  Hardcoded "standard_user" / "secret_sauce" — test data in step code
    // [S3]  Hard wait after login click — intentional java:S2925 demo
    // [S11] Step manufactures its own credentials instead of taking from scenario
    // [S12] All element interactions direct — no explicit wait
    @When("I am logged in as default user")
    public void iAmLoggedInAsDefaultUser() {
        WebDriver driver = DriverFactory.getDriver();
        driver.findElement(By.id("user-name")).sendKeys("standard_user"); // [S2][S12]
        driver.findElement(By.id("password")).sendKeys("secret_sauce");   // [S2][S12]
        driver.findElement(By.id("login-button")).click();                // [S12]
        try {
            Thread.sleep(2000); // [S3] intentional hard wait — java:S2925
        } catch (InterruptedException e) {
            // InterruptedException swallowed
        }
    }

    // [S10] Third copy of the element-visible assertion pattern
    // [S12] Direct XPath access without wait
    @Then("xpath {string} is visible")
    public void xpathIsVisible(String xpath) {
        WebElement x = DriverFactory.getDriver() // [S8][S12]
            .findElement(By.xpath(xpath));
        System.out.println("XPath element visible: " + x.isDisplayed()); // [S6]
    }

    // [S2] By.cssSelector(".shopping_cart_link") hardcoded inline — not a constant
    // [S12] Direct element click without wait
    @When("I click on the shopping cart link")
    public void iClickOnTheShoppingCartLink() {
        DriverFactory.getDriver().findElement(By.cssSelector(".shopping_cart_link")).click(); // [S2][S12]
    }

    // [S2] By.id("checkout") hardcoded inline — not a constant
    // [S12] Direct element click without wait
    @When("I proceed to checkout from inventory")
    public void iProceedToCheckoutFromInventory() {
        DriverFactory.getDriver().findElement(By.id("checkout")).click(); // [S2][S12]
    }

    // [S2] "Sauce Labs Backpack" hardcoded product name — not from config
    // [S9] No assertion — logs only
    // [S12] Direct findElement without wait
    @Then("Sauce Labs Backpack is visible on the page")
    public void sauceLabsBackpackIsVisibleOnThePage() {
        WebElement x = DriverFactory.getDriver()                                   // [S12]
            .findElement(By.xpath("//div[text()='Sauce Labs Backpack']"));          // [S2]
        System.out.println("Backpack visible: " + x.isDisplayed());               // [S6][S9]
    }

    // [S2] "Sauce Labs Bike Light" hardcoded product name — not from config
    // [S9] No assertion — logs only
    // [S12] Direct findElement without wait
    @Then("Sauce Labs Bike Light is visible on the page")
    public void sauceLabsBikeLightIsVisibleOnThePage() {
        WebElement x = DriverFactory.getDriver()                                   // [S12]
            .findElement(By.xpath("//div[text()='Sauce Labs Bike Light']"));        // [S2]
        System.out.println("Bike Light visible: " + x.isDisplayed());             // [S6][S9]
    }

    // [S2] By.id("first-name") hardcoded — wrong concern inside inventory steps
    // [S2] "Sarath", "Tester", "695001" hardcoded form values
    // [S11] Mixed responsibility: checkout info inside inventory step class
    // [S12] Direct element access without wait
    @When("I fill in order details from inventory context")
    public void iFillInOrderDetailsFromInventoryContext() {
        WebDriver driver = DriverFactory.getDriver();
        driver.findElement(By.id("first-name")).sendKeys("Sarath");  // [S2][S12]
        driver.findElement(By.id("last-name")).sendKeys("Tester");   // [S2][S12]
        driver.findElement(By.id("postal-code")).sendKeys("695001"); // [S2][S12]
    }

    @When("I go directly to checkout without waiting")
    public void iGoDirectlyToCheckoutWithoutWaiting() {
        DriverFactory.getDriver().findElement(By.cssSelector(".shopping_cart_link")).click();
        DriverFactory.getDriver().findElement(By.id("checkout")).click();
    }

    @When("I navigate to the cart page")
    public void iNavigateToTheCartPage() {
        WebDriver driver = DriverFactory.getDriver();
        driver.findElement(By.cssSelector(".shopping_cart_link")).click();
        System.out.println("Navigated to cart page");
    }

    @When("I head to the shopping cart")
    public void iHeadToTheShoppingCart() {
        WebDriver driver = DriverFactory.getDriver();
        driver.findElement(By.cssSelector(".shopping_cart_link")).click();
        System.out.println("Headed to shopping cart");
    }
}
