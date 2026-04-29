package com.automation.pages.badexamples;

// =============================================================================
// INTENTIONALLY NON-COMPLIANT — POC SONARQUBE DEMONSTRATION
//
// SonarQube Issues Demonstrated:
//   [S1]  No base class — boilerplate WebDriver code duplicated from BadLoginPage
//   [S2]  Locator strings repeated inline in every method (no constants)
//   [S3]  Thread.sleep() instead of explicit waits
//   [S4]  Generic Exception catch
//   [S5]  Empty catch blocks
//   [S6]  System.out.println instead of logger
//   [S7]  Non-descriptive method names: getData(), doSort(), click2()
//   [S8]  Non-descriptive variables: x, y, tmp, result
//   [S10] Duplicate method bodies (getNames and fetchNames are identical)
//   [S11] Long method mixing sort + collect + assert in one call
// =============================================================================

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class BadInventoryPage {

    // [S1] Copy-pasted from BadLoginPage — no shared base class
    private WebDriver driver;

    public BadInventoryPage(WebDriver driver) {
        this.driver = driver;
    }

    // ── [S7][S2] Non-descriptive method, repeated locator ─────────────────────

    // [S7] "getData" — data of what? returns what type? completely opaque
    // [S2] ".inventory_list" hardcoded — not a constant
    public boolean getData() {
        try {
            Thread.sleep(2000); // [S3][S12]
            WebElement x = driver.findElement(By.cssSelector(".inventory_list")); // [S8]
            return x.isDisplayed();
        } catch (Exception e) { // [S4]
            return false; // [S5] hides the real problem
        }
    }

    // ── [S7] Non-descriptive ── [S2] Repeated locator ─────────────────────────

    // [S7] "click2" gives no indication of what element is being clicked
    // [S2] "[data-test='product-sort-container']" hardcoded, repeated below
    public void click2(String x) { // [S8] parameter 'x' is the sort option text
        try {
            Thread.sleep(1000); // [S3]
            WebElement tmp = driver.findElement( // [S8] 'tmp'
                By.cssSelector("[data-test='product-sort-container']")
            );
            // [S11] Mixed concern: this method finds element, creates Select, AND interacts
            new org.openqa.selenium.support.ui.Select(tmp).selectByVisibleText(x);
        } catch (Exception e) { // [S4]
            System.out.println("Sort failed: " + e); // [S6]
        }
    }

    // ── [S7] Opaque method name ── [S3] Sleep ─────────────────────────────────

    // [S7] "doSort" — sort by what? ascending or descending?
    // [S2] Locator "[data-test='product-sort-container']" repeated (same as click2)
    public void doSort(String sortText) {
        try {
            Thread.sleep(1500); // [S3][S12]
            WebElement y = driver.findElement( // [S8] 'y'
                By.cssSelector("[data-test='product-sort-container']")
            );
            new org.openqa.selenium.support.ui.Select(y).selectByVisibleText(sortText);
            Thread.sleep(500); // [S3] extra sleep after sort
        } catch (Exception e) { // [S4]
            // [S5] Empty — sort failure is completely hidden
        }
    }

    // ── [S10] Duplicate method bodies ─────────────────────────────────────────

    // [S10] getNames() and fetchNames() below are structurally identical — SonarQube dupe block
    // [S2] ".inventory_item_name" hardcoded
    public List<String> getNames() {
        List<String> result = new ArrayList<>(); // [S8] variable 'result'
        try {
            Thread.sleep(1000); // [S3]
            List<WebElement> x = driver.findElements( // [S8] 'x'
                By.cssSelector(".inventory_item_name")
            );
            for (WebElement y : x) { // [S8] 'y'
                result.add(y.getText());
            }
        } catch (Exception e) { // [S4]
            System.out.println("Could not get names: " + e.getMessage()); // [S6]
        }
        return result;
    }

    // [S10] Identical body to getNames() — duplicate code block
    public List<String> fetchNames() {
        List<String> result = new ArrayList<>();
        try {
            Thread.sleep(1000);
            List<WebElement> x = driver.findElements(
                By.cssSelector(".inventory_item_name")
            );
            for (WebElement y : x) {
                result.add(y.getText());
            }
        } catch (Exception e) { // [S4]
            System.out.println("Could not get names: " + e.getMessage());
        }
        return result;
    }

    // ── [S11] Long method mixing multiple responsibilities ─────────────────────

    // [S11] This single method: sorts, waits, collects prices, validates order,
    //       logs result, and returns a boolean — far too many concerns.
    // [S3]  Multiple Thread.sleep calls
    // [S8]  Variables: x, y, tmp, prev
    public boolean doSortAndValidate(String sortOption) {
        try {
            Thread.sleep(1000); // [S3]
            // Concern 1: find sort dropdown
            WebElement x = driver.findElement( // [S8]
                By.cssSelector("[data-test='product-sort-container']")
            );
            new org.openqa.selenium.support.ui.Select(x).selectByVisibleText(sortOption);
            Thread.sleep(1500); // [S3] wait for re-sort
            // Concern 2: collect all prices
            List<WebElement> y = driver.findElements( // [S8]
                By.cssSelector(".inventory_item_price")
            );
            List<Double> prices = new ArrayList<>();
            for (WebElement tmp : y) { // [S8] 'tmp'
                String text = tmp.getText().replace("$", "");
                prices.add(Double.parseDouble(text));
            }
            // Concern 3: validate sort order
            double prev = -1; // [S8] 'prev'
            boolean sorted = true;
            for (double p : prices) {
                if (p < prev) {
                    sorted = false;
                    break;
                }
                prev = p;
            }
            // Concern 4: log outcome
            System.out.println("Sorted: " + sorted + " prices: " + prices); // [S6]
            return sorted;
        } catch (Exception e) { // [S4]
            System.out.println("Validation failed: " + e.getMessage()); // [S6]
            return false;
        }
    }

    // [S2] Locator ".shopping_cart_badge" hardcoded — already used in BadCartPage
    // [S8] variable 'tmp'
    public String getCartCount() {
        try {
            Thread.sleep(500); // [S3]
            WebElement tmp = driver.findElement(By.cssSelector(".shopping_cart_badge")); // [S8]
            return tmp.getText();
        } catch (Exception e) { // [S4]
            return "0"; // failure silently returns a default
        }
    }
}
