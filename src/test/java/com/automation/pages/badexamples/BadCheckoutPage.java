package com.automation.pages.badexamples;

// =============================================================================
// INTENTIONALLY NON-COMPLIANT — POC SONARQUBE DEMONSTRATION
//
// SonarQube Issues Demonstrated:
//   [S1]  No base class — full WebDriver boilerplate duplicated across all bad pages
//   [S2]  Locator strings hardcoded inline (no constants), repeated many times
//   [S4]  Generic Exception catch throughout
//   [S5]  Empty catch blocks
//   [S6]  System.out.println instead of SLF4J logger
//   [S7]  Non-descriptive method names: process(), doAll(), abc2()
//   [S8]  Non-descriptive variable names: x, y, tmp, a, b, c
//   [S9]  Returning null / false on exception instead of propagating
//   [S10] Duplicate method bodies (getTotal and fetchTotal are identical)
//   [S11] God method — process() handles the entire checkout flow
//   [S12] Flaky direct element access without explicit wait throughout
// =============================================================================

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class BadCheckoutPage {

    // [S1] Same raw WebDriver field — no base class reuse
    private WebDriver driver;

    public BadCheckoutPage(WebDriver driver) {
        this.driver = driver;
    }

    // ── [S11] God method — entire checkout flow in one method ────────────────

    // [S11] process() does: navigate + login + add to cart + checkout info +
    //       continue + overview + finish + confirmation assertion.
    //       One method, 8 concerns — extreme SRP violation.
    // [S8]  Variables: a, b, c, x, y, tmp
    // [S2]  Every locator hardcoded inline
    // [S12] All element interactions direct — no explicit waits, flaky in CI
    public boolean process(String u, String p, String first, String last, String zip) {
        try {
            // Concern 1: navigate
            driver.get("https://www.saucedemo.com"); // [S2] hardcoded URL

            // Concern 2: login
            WebElement a = driver.findElement(By.id("user-name")); // [S8][S12]
            a.clear();
            a.sendKeys(u);
            WebElement b = driver.findElement(By.id("password")); // [S8][S12]
            b.clear();
            b.sendKeys(p);
            driver.findElement(By.id("login-button")).click(); // [S2][S12]

            // Concern 3: add product — hardcoded Sauce Labs Backpack
            WebElement c = driver.findElement( // [S8][S12]
                By.cssSelector("[data-test='add-to-cart-sauce-labs-backpack']") // [S2]
            );
            c.click();

            // Concern 4: navigate to cart
            driver.findElement(By.cssSelector("[data-test='shopping-cart-link']")).click(); // [S2][S12]

            // Concern 5: click checkout
            driver.findElement(By.cssSelector("[data-test='checkout']")).click(); // [S2][S12]

            // Concern 6: fill checkout form with hardcoded field IDs
            WebElement x = driver.findElement(By.id("first-name")); // [S8][S12]
            x.clear();
            x.sendKeys(first);
            WebElement y = driver.findElement(By.id("last-name")); // [S8][S12]
            y.clear();
            y.sendKeys(last);
            WebElement tmp = driver.findElement(By.id("postal-code")); // [S8][S12]
            tmp.clear();
            tmp.sendKeys(zip);

            // Concern 7: continue to overview
            driver.findElement(By.cssSelector("[data-test='continue']")).click(); // [S2][S12]

            // Concern 8: finish
            driver.findElement(By.cssSelector("[data-test='finish']")).click(); // [S2][S12]

            // Concern 9: verify success
            WebElement header = driver.findElement(By.cssSelector(".complete-header")); // [S2][S12]
            System.out.println("Order result: " + header.getText()); // [S6]
            return header.isDisplayed();

        } catch (Exception e) { // [S4]
            System.out.println("process() failed: " + e.getMessage()); // [S6]
            return false; // [S9]
        }
    }

    // ── [S7] Non-descriptive names ── [S2] Repeated locators ─────────────────

    // [S7] "abc2" tells nothing about what this method does
    // [S2] By.id("first-name") hardcoded here and inside process() above
    // [S12] Direct sendKeys without wait — may fail if fields not rendered
    public void abc2(String a, String b, String c) { // [S8] params a, b, c
        try {
            driver.findElement(By.id("first-name")).sendKeys(a); // [S2][S12]
            driver.findElement(By.id("last-name")).sendKeys(b);  // [S2][S12]
            driver.findElement(By.id("postal-code")).sendKeys(c); // [S2][S12]
        } catch (Exception e) { // [S4]
            // [S5] Empty — form fill failure completely hidden
        }
    }

    // ── [S10] Duplicate method bodies ─────────────────────────────────────────

    // [S10] getTotal() and fetchTotal() are identical — SonarQube duplicated code block
    // [S2] ".summary_total_label" hardcoded
    // [S12] Direct element access without wait
    public String getTotal() {
        try {
            WebElement x = driver.findElement(By.cssSelector(".summary_total_label")); // [S8][S12]
            String tmp = x.getText(); // [S8]
            System.out.println("Total: " + tmp); // [S6]
            return tmp;
        } catch (Exception e) { // [S4]
            return null; // [S9]
        }
    }

    // [S10] Identical body to getTotal()
    public String fetchTotal() {
        try {
            WebElement x = driver.findElement(By.cssSelector(".summary_total_label")); // [S12]
            String tmp = x.getText();
            System.out.println("Total: " + tmp);
            return tmp;
        } catch (Exception e) { // [S4]
            return null; // [S9]
        }
    }

    // [S7] "doAll" — all what?
    // [S12] Direct clicks without wait — both actions
    public void doAll() {
        try {
            driver.findElement(By.cssSelector("[data-test='continue']")).click(); // [S2][S12]
            driver.findElement(By.cssSelector("[data-test='finish']")).click();   // [S2][S12]
        } catch (Exception e) { // [S4]
            e.printStackTrace(); // [S6] stack trace to stdout
        }
    }

    // [S2] ".complete-header" hardcoded — already used in process()
    // [S9] Returns false instead of throwing on unexpected condition
    // [S12] Direct element access without wait
    public boolean isSuccess() {
        try {
            return driver.findElement(By.cssSelector(".complete-header")).isDisplayed(); // [S2][S12]
        } catch (Exception e) { // [S4]
            return false; // [S9] hides whether this failed due to an error or a real miss
        }
    }

    // Intentional SonarQube POC issue — catch (Exception e) catches everything including NPE
    // Intentional SonarQube POC issue — return null forces caller to null-check or face NPE
    // Intentional SonarQube POC issue — direct findElement without wait (flaky)
    public String getConfirmationMessage() {
        try {
            return driver.findElement(By.cssSelector(".complete-header")).getText(); // Intentional SonarQube POC issue — flaky direct access, no wait
        } catch (Exception e) { // Intentional SonarQube POC issue — overly broad catch
            return null; // Intentional SonarQube POC issue — return null, caller may NPE
        }
    }

    // Intentional SonarQube POC issue — empty catch block hides element-not-found failures
    // Intentional SonarQube POC issue — direct sequential findElement clicks without any wait
    public void cancelCheckout() {
        try {
            driver.findElement(By.cssSelector("[data-test='cancel']")).click(); // Intentional SonarQube POC issue — direct click, no wait
        } catch (Exception e) { // Intentional SonarQube POC issue — catch (Exception e)
            // Intentional SonarQube POC issue — empty catch block, cancel failure silently ignored
        }
    }
}
