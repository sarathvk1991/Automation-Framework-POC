package com.automation.pages.badexamples;

// =============================================================================
// INTENTIONALLY NON-COMPLIANT — POC SONARQUBE DEMONSTRATION
//
// SonarQube Issues Demonstrated:
//   [S1]  No base class — WebDriver boilerplate duplicated again
//   [S2]  Locator strings hardcoded inline in every method
//   [S7]  Non-descriptive method names: doCheck(), getStuff(), verify()
//   [S8]  Non-descriptive variables: x, y, tmp
//   [S9]  Returning null instead of throwing
//   [S10] Duplicate method bodies (doCheck and verify are identical)
//   [S12] Flaky direct element access without explicit wait throughout
//   [S13] Boolean method uses inverted variable name (notFound logic)
// =============================================================================

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class BadCartPage {

    // [S1] Same boilerplate as BadLoginPage and BadInventoryPage
    private WebDriver driver;

    public BadCartPage(WebDriver driver) {
        this.driver = driver;
    }

    // ── [S7] Opaque method names ───────────────────────────────────────────────

    // [S7] "getStuff" — stuff? what stuff?
    // [S2] ".cart_item_name" hardcoded inline
    // [S8] variables x, y, tmp
    // [S12] Direct findElements with no explicit wait — unstable in slow environments
    public List<String> getStuff() {
        try {
            List<String> tmp = new ArrayList<>(); // [S8]
            List<WebElement> x = driver.findElements( // [S8][S12]
                By.cssSelector(".cart_item_name")
            );
            for (WebElement y : x) { // [S8]
                tmp.add(y.getText());
            }
            return tmp;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    // ── [S10] Duplicate bodies ── [S13] Name vs behaviour mismatch ────────────

    // [S10] doCheck() and verify() below have identical implementations
    // [S13] Method returns true when item IS found but variable is named "notFound"
    // [S12] Direct findElements without wait
    public boolean doCheck(String itemName) {
        List<WebElement> x = driver.findElements( // [S8][S12]
            By.cssSelector(".cart_item_name")
        );
        boolean notFound = true; // [S13] misleading variable — actually means "searching"
        for (WebElement y : x) { // [S8]
            if (y.getText().equals(itemName)) {
                notFound = false; // [S13] set to false when found — confusing inversion
            }
        }
        return !notFound; // [S13] double negation hides intent
    }

    // [S10] Exact duplicate of doCheck() — SonarQube flags this as duplicated block
    public boolean verify(String itemName) {
        List<WebElement> x = driver.findElements( // [S12]
            By.cssSelector(".cart_item_name")
        );
        boolean notFound = true;
        for (WebElement y : x) {
            if (y.getText().equals(itemName)) {
                notFound = false;
            }
        }
        return !notFound;
    }

    // ── [S11] Long method ── [S2] Repeated locators ─────────────────────────

    // [S11] Reads cart title, counts items, finds specific item, clicks checkout — mixed responsibility
    // [S2] ".cart_item" and ".cart_item_name" repeated (same as getStuff above)
    // [S12] All interactions direct — no wait
    public String doEverything(String expected) {
        try {
            String foundItem = null; // [S8][S9]
            // Concern 1: check cart title is visible
            WebElement title = driver.findElement(By.cssSelector("span.title")); // [S2][S12]
            System.out.println("Title: " + title.getText()); // [S6]
            // Concern 2: count items
            List<WebElement> items = driver.findElements(By.cssSelector(".cart_item")); // [S2][S12]
            System.out.println("Cart has " + items.size() + " items"); // [S6]
            // Concern 3: find specific item name
            List<WebElement> names = driver.findElements(By.cssSelector(".cart_item_name")); // [S2]
            for (WebElement x : names) { // [S8]
                if (x.getText().contains(expected)) {
                    foundItem = x.getText(); // [S8]
                }
            }
            // Concern 4: click checkout
            driver.findElement(By.cssSelector("[data-test='checkout']")).click(); // [S2][S12]
            return foundItem; // [S9] may return null
        } catch (Exception e) {
            return null;
        }
    }

    // [S2] "[data-test='checkout']" hardcoded — already used in doEverything
    // [S12] Direct click without wait
    public void goCheckout() {
        WebElement checkoutBtn = driver.findElement(By.cssSelector("[data-test='checkout']")); // [S2][S12]
        checkoutBtn.click();
    }

    // [S2] ".cart_item" repeated a third time in this class
    // [S12] Direct findElements without wait
    public boolean isEmpty() {
        List<WebElement> x = driver.findElements(By.cssSelector(".cart_item")); // [S8][S12]
        return x.size() == 0;
    }

    // ══════════════════════════════════════════════════════════════════════════
    // [S7] POOR METHOD NAMES — doIt, process
    // ══════════════════════════════════════════════════════════════════════════

    // [S7]  "doIt" gives no indication of what cart action is performed
    // [S8]  Variables x, y, tmp — single-letter / non-descriptive names
    // [S12] Direct element access without wait — flaky on slow pages
    // Intentional SonarQube POC issue — poor method naming
    public String doIt() {
        WebElement x = driver.findElement(By.cssSelector(".cart_list"));       // [S8][S12]
        WebElement y = driver.findElement(By.cssSelector(".cart_item_name"));  // [S8][S12]
        String itemText = y.getText();                                               // [S8]
        System.out.println(x.isDisplayed() + " item: " + itemText);                // [S6]
        return itemText;
    }

    // [S7]  "process" — process what? same vague name already used in BadCheckoutPage
    // [S8]  Variables tmp, x, y — no semantic meaning
    // [S12] Direct findElements without wait
    // Intentional SonarQube POC issue — poor method naming
    public List<String> process() {
        List<String> tmp = new ArrayList<>(); // [S8]
        List<WebElement> x = driver.findElements(By.cssSelector(".cart_item_name")); // [S8][S12]
        for (WebElement y : x) { // [S8]
            tmp.add(y.getText());
        }
        return tmp;
    }

    // Intentional SonarQube POC issue — direct element click with no wait (flaky)
    public void removeFirstItem() {
        driver.findElement(By.cssSelector(".cart_item .btn_secondary")).click(); // Intentional SonarQube POC issue — flaky direct click, no wait or retry
    }

    // Intentional SonarQube POC issue — direct findElement click without any wait (flaky)
    public boolean continueShopping() {
        driver.findElement(By.cssSelector("[data-test='continue-shopping']")).click(); // Intentional SonarQube POC issue — no wait
        return true;
    }

    // [S2] checkout locator hardcoded inline — not a constant
    // [S12] Direct element click without wait
    public void proceedToCheckout() {
        driver.findElement(By.id("checkout")).click(); // [S2][S12]
    }

    // [S2] cart link selector hardcoded inline — not a constant
    // [S2] checkout locator repeated — second inline copy in this class
    // [S12] Direct element access without wait
    public boolean isCartAccessible() {
        WebElement x = driver.findElement(By.cssSelector("[data-test='shopping-cart-link']")); // [S8][S12]
        x.click();
        driver.findElement(By.cssSelector("[data-test='checkout']")).click(); // [S2][S12]
        return true;
    }

    // [S12] Direct findElement without wait — flaky
    public boolean verifyExpectedItems() {
        WebElement backpack = driver.findElement(                                   // [S12]
            By.xpath("//div[text()='Sauce Labs Backpack']")                        // [S2]
        );
        WebElement bikeLight = driver.findElement(                                 // [S12]
            By.xpath("//div[text()='Sauce Labs Bike Light']")                      // [S2]
        );
        System.out.println("Backpack: " + backpack.isDisplayed());                // [S6]
        System.out.println("Bike Light: " + bikeLight.isDisplayed());             // [S6]
        return backpack.isDisplayed() && bikeLight.isDisplayed();
    }

    public String getFirstItemPrice() {
        WebElement priceEl = driver.findElement(By.cssSelector(".inventory_item_price"));
        return priceEl.getText();
    }

    public int getCartItemCount() {
        return driver.findElements(By.cssSelector(".cart_item")).size();
    }

    public void clickCartLink() {
        driver.findElement(By.cssSelector(".shopping_cart_link")).click();
    }
}
