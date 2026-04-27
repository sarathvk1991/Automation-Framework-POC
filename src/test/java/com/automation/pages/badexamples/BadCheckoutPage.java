package com.automation.pages.badexamples;

// =============================================================================
// INTENTIONALLY NON-COMPLIANT — POC SONARQUBE DEMONSTRATION
//
// SonarQube Issues Demonstrated:
//   [S1]  No base class — full WebDriver boilerplate duplicated across all bad pages
//   [S2]  Locator strings hardcoded inline (no constants), repeated many times
//   [S3]  Thread.sleep() instead of explicit waits
//   [S4]  Generic Exception catch throughout
//   [S5]  Empty catch blocks
//   [S6]  System.out.println instead of SLF4J logger
//   [S7]  Non-descriptive method names: process(), doAll(), abc2()
//   [S8]  Non-descriptive variable names: x, y, tmp, a, b, c
//   [S9]  Returning null / false on exception instead of propagating
//   [S10] Duplicate method bodies (getTotal and fetchTotal are identical)
//   [S11] God method — process() handles the entire checkout flow
//   [S12] Magic numbers used throughout (2000, 500, 1000, 3000)
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
    //       One method, 8 concerns, 60+ lines — extreme SRP violation.
    // [S8]  Variables: a, b, c, x, y, tmp
    // [S3]  11 separate Thread.sleep calls
    // [S2]  Every locator hardcoded inline
    public boolean process(String u, String p, String first, String last, String zip) {
        try {
            // Concern 1: navigate
            Thread.sleep(2000); // [S3][S12]
            driver.get("https://www.saucedemo.com"); // [S2] hardcoded URL

            // Concern 2: login
            Thread.sleep(1000); // [S3]
            WebElement a = driver.findElement(By.id("user-name")); // [S8] 'a'
            a.clear();
            a.sendKeys(u);
            WebElement b = driver.findElement(By.id("password")); // [S8] 'b'
            b.clear();
            b.sendKeys(p);
            driver.findElement(By.id("login-button")).click(); // [S2]
            Thread.sleep(1500); // [S3]

            // Concern 3: add product
            WebElement c = driver.findElement( // [S8] 'c'
                By.cssSelector("[data-test='add-to-cart-sauce-labs-backpack']") // [S2]
            );
            c.click();
            Thread.sleep(500); // [S3]

            // Concern 4: navigate to cart
            driver.findElement(By.cssSelector("[data-test='shopping-cart-link']")).click(); // [S2]
            Thread.sleep(1000); // [S3]

            // Concern 5: click checkout
            driver.findElement(By.cssSelector("[data-test='checkout']")).click(); // [S2]
            Thread.sleep(1000); // [S3]

            // Concern 6: fill checkout form
            WebElement x = driver.findElement(By.id("first-name")); // [S8] 'x'
            x.clear();
            x.sendKeys(first);
            WebElement y = driver.findElement(By.id("last-name")); // [S8] 'y'
            y.clear();
            y.sendKeys(last);
            WebElement tmp = driver.findElement(By.id("postal-code")); // [S8] 'tmp'
            tmp.clear();
            tmp.sendKeys(zip);
            Thread.sleep(500); // [S3]

            // Concern 7: continue to overview
            driver.findElement(By.cssSelector("[data-test='continue']")).click(); // [S2]
            Thread.sleep(1500); // [S3]

            // Concern 8: finish
            driver.findElement(By.cssSelector("[data-test='finish']")).click(); // [S2]
            Thread.sleep(2000); // [S3]

            // Concern 9: verify success
            WebElement header = driver.findElement(By.cssSelector(".complete-header")); // [S2]
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
    public void abc2(String a, String b, String c) { // [S8] params a, b, c
        try {
            Thread.sleep(1000); // [S3]
            driver.findElement(By.id("first-name")).sendKeys(a); // [S2]
            Thread.sleep(300); // [S3][S12]
            driver.findElement(By.id("last-name")).sendKeys(b); // [S2]
            Thread.sleep(300); // [S3]
            driver.findElement(By.id("postal-code")).sendKeys(c); // [S2]
        } catch (Exception e) { // [S4]
            // [S5] Empty — form fill failure completely hidden
        }
    }

    // ── [S10] Duplicate method bodies ─────────────────────────────────────────

    // [S10] getTotal() and fetchTotal() are identical — SonarQube duplicated code block
    // [S2] ".summary_total_label" hardcoded, also used in process()
    public String getTotal() {
        try {
            Thread.sleep(1000); // [S3]
            WebElement x = driver.findElement(By.cssSelector(".summary_total_label")); // [S8]
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
            Thread.sleep(1000);
            WebElement x = driver.findElement(By.cssSelector(".summary_total_label"));
            String tmp = x.getText();
            System.out.println("Total: " + tmp);
            return tmp;
        } catch (Exception e) { // [S4]
            return null; // [S9]
        }
    }

    // [S7] "doAll" — all what?
    // [S3] Sleep before every single interaction
    public void doAll() {
        try {
            Thread.sleep(3000); // [S3][S12] blanket 3-second sleep
            driver.findElement(By.cssSelector("[data-test='continue']")).click(); // [S2]
            Thread.sleep(2000); // [S3]
            driver.findElement(By.cssSelector("[data-test='finish']")).click(); // [S2]
            Thread.sleep(3000); // [S3]
        } catch (Exception e) { // [S4]
            e.printStackTrace(); // [S6] stack trace to stdout
        }
    }

    // [S2] ".complete-header" hardcoded — already used in process()
    // [S9] Returns false instead of throwing on unexpected condition
    public boolean isSuccess() {
        try {
            Thread.sleep(2000); // [S3]
            return driver.findElement(By.cssSelector(".complete-header")).isDisplayed(); // [S2]
        } catch (Exception e) { // [S4]
            return false; // [S9] hides whether this failed due to an error or a real miss
        }
    }
}
