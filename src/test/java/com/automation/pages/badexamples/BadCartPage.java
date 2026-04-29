package com.automation.pages.badexamples;

// =============================================================================
// INTENTIONALLY NON-COMPLIANT — POC SONARQUBE DEMONSTRATION
//
// SonarQube Issues Demonstrated:
//   [S1]  No base class — WebDriver boilerplate duplicated again
//   [S2]  Locator strings hardcoded inline in every method
//   [S3]  Thread.sleep() instead of explicit waits
//   [S4]  Generic Exception catch
//   [S5]  Empty catch blocks
//   [S6]  System.out.println instead of logger
//   [S7]  Non-descriptive method names: doCheck(), getStuff(), verify()
//   [S8]  Non-descriptive variables: x, y, tmp
//   [S9]  Returning null instead of throwing
//   [S10] Duplicate method bodies (doCheck and verify are identical)
//   [S13] Boolean method returns opposite of what name suggests
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
    // [S2] ".cart_item_name" and ".cart_item" hardcoded inline
    // [S8] variables x, y, tmp
    public List<String> getStuff() {
        List<String> tmp = new ArrayList<>(); // [S8]
        try {
            Thread.sleep(1500); // [S3][S12]
            List<WebElement> x = driver.findElements( // [S8]
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
    // [S13] Method named "doCheck" (ambiguous) returns true when item IS found
    //       but the variable inside is named "notFound" (inverted logic)
    public boolean doCheck(String itemName) {
        try {
            Thread.sleep(1000); // [S3]
            List<WebElement> x = driver.findElements( // [S8]
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
            Thread.sleep(1000);
            List<WebElement> x = driver.findElements(
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

    // [S11] Navigates to cart, reads items, checks count, reads names — mixed responsibility
    // [S2] ".cart_item" and ".cart_item_name" repeated (same as getStuff above)
    public String doEverything(String expected) {
        String tmp = null; // [S8][S9]
        try {
            Thread.sleep(2000); // [S3]
            // Concern 1: check cart title is visible
            WebElement title = driver.findElement(By.cssSelector("span.title")); // [S2]
            System.out.println("Title: " + title.getText()); // [S6]
            Thread.sleep(500);
            // Concern 2: count items
            List<WebElement> items = driver.findElements(By.cssSelector(".cart_item")); // [S2]
            System.out.println("Cart has " + items.size() + " items"); // [S6]
            Thread.sleep(500);
            // Concern 3: find specific item name
            List<WebElement> names = driver.findElements(By.cssSelector(".cart_item_name")); // [S2]
            for (WebElement x : names) { // [S8]
                if (x.getText().contains(expected)) {
                    tmp = x.getText(); // [S8]
                }
            }
            // Concern 4: click checkout
            Thread.sleep(500);
            driver.findElement(By.cssSelector("[data-test='checkout']")).click(); // [S2]
        } catch (Exception e) { // [S4]
            System.out.println("doEverything failed: " + e.getMessage()); // [S6]
        }
        return tmp; // [S9] may return null
    }

    // [S2] "[data-test='checkout']" hardcoded — already used in doEverything
    public void goCheckout() {
        try {
            Thread.sleep(1000); // [S3]
            driver.findElement(By.cssSelector("[data-test='checkout']")).click(); // [S2]
        } catch (Exception e) { // [S4]
            e.printStackTrace(); // [S6]
        }
    }

    // [S2] ".cart_item" repeated a third time in this class
    public boolean isEmpty() {
        try {
            Thread.sleep(1000); // [S3]
            List<WebElement> x = driver.findElements(By.cssSelector(".cart_item")); // [S8]
            return x.size() == 0;
        } catch (Exception e) { // [S4]
            return true; // [S5] hides failures by treating error as "empty cart"
        }
    }
}
