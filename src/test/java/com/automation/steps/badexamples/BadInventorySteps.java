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
import com.automation.pages.badexamples.BadInventoryPage;
import com.automation.utils.TestData;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.List;

public class BadInventorySteps {

    // INTENTIONAL BAD EXAMPLE
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

    // INTENTIONAL BAD EXAMPLE
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
        BadInventoryPage inventoryPage = new BadInventoryPage(DriverFactory.getDriver());
        System.out.println("Found: " + inventoryPage.isElementPresent(css));
        String tmp = inventoryPage.getPageTitle();
        System.out.println("Page title: " + tmp);
    }

    // [S10] Duplicate of BadLoginSteps.iClickCss — same method body, different class
    // [S8]  Variable y
    // [S12] Direct click without wait
    @When("I click css2 {string}")
    public void iClickCss2(String css) {
        new BadInventoryPage(DriverFactory.getDriver()).clickByCss(css);
    }

    // [S10] Duplicate of BadLoginSteps.iClickXpath — same body
    // [S12] Direct click without wait
    @When("I click xpath2 {string}")
    public void iClickXpath2(String xpath) {
        new BadInventoryPage(DriverFactory.getDriver()).clickByXpath(xpath);
    }

    // [S10] Duplicate of BadLoginSteps.elementWithCssIsVisible — same body
    // [S12] Direct element access without wait
    @Then("element with css2 {string} is visible")
    public void elementWithCss2IsVisible(String css) {
        System.out.println("Visible: " + new BadInventoryPage(DriverFactory.getDriver()).isElementVisible(css));
    }

    // INTENTIONAL BAD EXAMPLE
    // [S3]  Hard wait after login click — intentional java:S2925 demo
    // [S11] Step manufactures its own credentials instead of taking from scenario
    // [S12] All element interactions direct — no explicit wait
    @When("I am logged in as default user")
    public void iAmLoggedInAsDefaultUser() {
        WebDriver driver = DriverFactory.getDriver();
        WebElement usernameField = driver.findElement(By.cssSelector("[data-test='username']")); // [S2][S12]
        usernameField.sendKeys(TestData.USERNAME);
        WebElement passwordField = driver.findElement(By.cssSelector("[data-test='password']")); // [S2][S12]
        passwordField.sendKeys(TestData.PASSWORD);
        WebElement loginBtn = driver.findElement(By.cssSelector("[data-test='login-button']")); // [S12]
        loginBtn.click();
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
        System.out.println("XPath element visible: " + new BadInventoryPage(DriverFactory.getDriver()).isElementVisibleByXpath(xpath));
    }

    // [S2] cart link selector hardcoded inline — not a constant
    // [S12] Direct element click without wait
    @When("I click on the shopping cart link")
    public void iClickOnTheShoppingCartLink() {
        new BadInventoryPage(DriverFactory.getDriver()).openCart();
    }

    // [S2] checkout locator hardcoded inline — not a constant
    // [S12] Direct element click without wait
    @When("I proceed to checkout from inventory")
    public void iProceedToCheckoutFromInventory() {
        new BadInventoryPage(DriverFactory.getDriver()).clickCheckoutById();
    }

    // INTENTIONAL BAD EXAMPLE
    // [S9] No assertion — logs only
    // [S12] Direct findElement without wait
    @Then("primary product is visible on the page")
    public void sauceLabsBackpackIsVisibleOnThePage() {
        WebElement x = DriverFactory.getDriver()                                   // [S12]
            .findElement(By.xpath("//div[text()='Product A']"));          // [S2]
        System.out.println("Backpack visible: " + x.isDisplayed());               // [S6][S9]
    }

    // [S9] No assertion — logs only
    // [S12] Direct findElement without wait
    @Then("secondary product is visible on the page")
    public void sauceLabsBikeLightIsVisibleOnThePage() {
        System.out.println("Bike Light visible: " + new BadInventoryPage(DriverFactory.getDriver()).isBikeLightInInventory());
    }

    // INTENTIONAL BAD EXAMPLE
    // [S2] first-name field hardcoded — wrong concern inside inventory steps
    // [S11] Mixed responsibility: checkout info inside inventory step class
    // [S12] Direct element access without wait
    @When("I fill in order details from inventory context")
    public void iFillInOrderDetailsFromInventoryContext() {
        WebDriver driver = DriverFactory.getDriver();
        WebElement firstNameField = driver.findElement(By.cssSelector("[data-test='firstName']")); // [S2][S12]
        firstNameField.sendKeys("John");
        WebElement lastNameField = driver.findElement(By.id("last-name")); // [S2][S12]
        lastNameField.sendKeys("Doe");
        WebElement postalField = driver.findElement(By.id("postal-code")); // [S2][S12]
        postalField.sendKeys("12345");
    }

    // INTENTIONAL BAD EXAMPLE
    @When("I go directly to checkout without waiting")
    public void iGoDirectlyToCheckoutWithoutWaiting() {
        DriverFactory.getDriver().findElement(By.cssSelector("[data-test='shopping-cart-link']")).click();
        DriverFactory.getDriver().findElement(By.cssSelector("[data-test='checkout']")).click();
    }

    @When("I navigate to the cart page")
    public void iNavigateToTheCartPage() {
        new BadInventoryPage(DriverFactory.getDriver()).navigateToCart();
    }

}

