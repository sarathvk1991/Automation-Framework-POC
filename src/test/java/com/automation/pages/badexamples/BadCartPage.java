package com.automation.pages.badexamples;

// =============================================================================
// INTENTIONALLY NON-COMPLIANT — POC SONARQUBE DEMONSTRATION
//
// SonarQube Issues Demonstrated:
//   [S1]  No base class — WebDriver boilerplate duplicated again
//   [S2]  Locator strings hardcoded inline in every method
//   [S4]  Generic Exception catch
//   [S5]  Empty catch blocks
//   [S6]  System.out.println instead of logger
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
        List<String> tmp = new ArrayList<>(); // [S8]
        try {
            List<WebElement> x = driver.findElements( // [S8][S12]
                By.cssSelector(".cart_item_name")
            );
            for (WebElement y : x) { // [S8]
                tmp.add(y.getText());
            }
        } catch (Exception e) { // [S4]
            // [S5] Swallowed — caller gets empty list and may not realise cart wasn't read
        }
        return tmp;
    }

    // ── [S10] Duplicate bodies ── [S13] Name vs behaviour mismatch ────────────

    // [S10] doCheck() and verify() below have identical implementations
    // [S13] Method returns true when item IS found but variable is named "notFound"
    // [S12] Direct findElements without wait
    public boolean doCheck(String itemName) {
        try {
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
        } catch (Exception e) { // [S4]
            System.out.println("Check failed for: " + itemName); // [S6]
            return false;
        }
    }

    // [S10] Exact duplicate of doCheck() — SonarQube flags this as duplicated block
    public boolean verify(String itemName) {
        try {
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
        } catch (Exception e) { // [S4]
            System.out.println("Check failed for: " + itemName);
            return false;
        }
    }

    // ── [S11] Long method ── [S2] Repeated locators ─────────────────────────

    // [S11] Reads cart title, counts items, finds specific item, clicks checkout — mixed responsibility
    // [S2] ".cart_item" and ".cart_item_name" repeated (same as getStuff above)
    // [S12] All interactions direct — no wait
    public String doEverything(String expected) {
        String tmp = null; // [S8][S9]
        try {
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
                    tmp = x.getText(); // [S8]
                }
            }
            // Concern 4: click checkout
            driver.findElement(By.cssSelector("[data-test='checkout']")).click(); // [S2][S12]
        } catch (Exception e) { // [S4]
            System.out.println("doEverything failed: " + e.getMessage()); // [S6]
        }
        return tmp; // [S9] may return null
    }

    // [S2] "[data-test='checkout']" hardcoded — already used in doEverything
    // [S12] Direct click without wait
    public void goCheckout() {
        try {
            driver.findElement(By.cssSelector("[data-test='checkout']")).click(); // [S2][S12]
        } catch (Exception e) { // [S4]
            e.printStackTrace(); // [S6]
        }
    }

    // [S2] ".cart_item" repeated a third time in this class
    // [S12] Direct findElements without wait
    public boolean isEmpty() {
        try {
            List<WebElement> x = driver.findElements(By.cssSelector(".cart_item")); // [S8][S12]
            return x.size() == 0;
        } catch (Exception e) { // [S4]
            return true; // [S5] hides failures by treating error as "empty cart"
        }
    }
}
