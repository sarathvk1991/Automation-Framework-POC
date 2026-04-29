package com.automation.pages.badexamples;

// =============================================================================
// INTENTIONALLY NON-COMPLIANT — POC SONARQUBE DEMONSTRATION
//
// This class exists solely to demonstrate code quality violations that
// SonarQube would flag. It is NOT used by the main test suite.
//
// SonarQube Issues Demonstrated:
//   [S1]  No base class — WebDriver boilerplate copy-pasted (violates DRY)
//   [S2]  Locators hardcoded inline in every method instead of constants
//   [S3]  Intentional hard wait in doIt() — java:S2925
//   [S4]  Generic Exception caught — masks real failures
//   [S5]  Empty catch blocks — exceptions silently swallowed
//   [S6]  System.out.println instead of SLF4J logger
//   [S7]  Non-descriptive method names: doIt(), abc(), click1()
//   [S8]  Non-descriptive variable names: x, y, tmp
//   [S9]  Returning null instead of throwing a meaningful exception
//   [S10] Duplicate code blocks — checkError() and isError() are identical
//   [S11] Long method with mixed responsibilities (doLoginAndGetPageTitle)
//   [S12] Flaky direct element access without explicit wait (most methods)
// =============================================================================

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class BadLoginPage {

    // [S1] No BasePage — driver stored as raw field with no shared wait utilities
    private WebDriver driver;

    public BadLoginPage(WebDriver driver) {
        this.driver = driver;
    }

    // ── [S7] Non-descriptive method name ─────────────────────────────────────

    // [S7] "doIt" communicates nothing about what action is performed
    // [S3] Hard wait used instead of WebDriverWait — intentional java:S2925 demo
    // [S5] Exception swallowed silently
    // [S2] By.id("user-name") hardcoded here and repeated in every other method
    public void doIt() {
        try {
            Thread.sleep(2000); // [S3] intentional hard wait — java:S2925
            driver.findElement(By.id("user-name")).sendKeys("standard_user"); // [S2]
            driver.findElement(By.id("password")).sendKeys("secret_sauce");   // [S2]
            driver.findElement(By.id("login-button")).click();                // [S2]
        } catch (Exception e) { // [S4]
            // [S5] Exception silently swallowed — test will appear to pass
        }
    }

    // ── [S7][S8] Poor method and parameter names ──────────────────────────────

    // [S7] "abc" is meaningless
    // [S8] parameter named "x" — caller cannot tell what to pass
    // [S2] By.id("user-name") repeated (not a constant)
    // [S12] Direct element access with no explicit wait — flaky in slow CI
    public void abc(String x) {
        try {
            WebElement y = driver.findElement(By.id("user-name")); // [S8] var 'y' [S12]
            y.clear();
            y.sendKeys(x);
        } catch (Exception e) { // [S4]
            System.out.println("error: " + e.getMessage()); // [S6]
        }
    }

    // ── [S7] click1 — non-descriptive ─────────────────────────────────────────

    // [S7] "click1" gives no indication of what is being clicked
    // [S2] By.id("login-button") repeated again (already used in doIt)
    // [S12] Direct click without wait — may fail if page not ready
    public void click1() {
        try {
            driver.findElement(By.id("login-button")).click(); // [S2][S12]
        } catch (Exception e) { // [S4]
            e.printStackTrace(); // [S6] stack trace to stdout
        }
    }

    // ── [S2][S10] Locator repeated, duplicate logic ───────────────────────────

    // [S10] This is structurally identical to abc() above — SonarQube flags as duplicate block
    // [S2] By.id("user-name") — 3rd copy of the same locator string
    // [S8] parameter "x", variable "tmp"
    public void setUsername(String x) {
        try {
            WebElement tmp = driver.findElement(By.id("user-name")); // [S8] 'tmp' [S12]
            tmp.clear();
            tmp.sendKeys(x);
        } catch (Exception e) { // [S4]
            // [S5] Empty catch — failure is invisible to the test runner
        }
    }

    // ── [S11] Long method with mixed responsibilities ─────────────────────────

    // [S11] This method navigates, logs in, checks URL, AND returns the title.
    //       Single Responsibility Principle violation — four concerns in one method.
    // [S9]  Returns null on failure instead of throwing
    // [S8]  Variables x, y, tmp throughout
    // [S12] All four element interactions are direct — no explicit wait
    public String doLoginAndGetPageTitle(String username, String password) {
        try {
            driver.get("https://www.saucedemo.com"); // [S2] hardcoded URL in page object
            WebElement x = driver.findElement(By.id("user-name")); // [S8][S12]
            x.clear();
            x.sendKeys(username);
            WebElement y = driver.findElement(By.id("password")); // [S8][S12]
            y.clear();
            y.sendKeys(password);
            driver.findElement(By.id("login-button")).click(); // [S2][S12]
            String tmp = driver.getTitle(); // [S8]
            // [S11] Mixed concern: navigation check embedded inside login method
            if (!driver.getCurrentUrl().contains("inventory")) {
                System.out.println("Not on inventory page!"); // [S6]
            }
            return tmp; // [S8] returning a variable named 'tmp'
        } catch (Exception e) { // [S4]
            System.out.println("Login failed: " + e.getMessage()); // [S6]
            return null; // [S9] caller must null-check or NPE
        }
    }

    // ── [S10] Duplicate blocks ────────────────────────────────────────────────

    // [S10] checkError() and isError() below are identical — SonarQube flags duplicated blocks
    // [S8] variables x, tmp
    // [S12] Direct element access without wait
    public boolean checkError() {
        try {
            WebElement x = driver.findElement(By.cssSelector("[data-test='error']")); // [S8][S12]
            String tmp = x.getText(); // [S8]
            if (tmp != null && !tmp.isEmpty()) {
                return true;
            }
            return false;
        } catch (Exception e) { // [S4]
            return false; // failure silently treated as "no error"
        }
    }

    // [S10] Exact duplicate of checkError() — different name, identical body
    public boolean isError() {
        try {
            WebElement x = driver.findElement(By.cssSelector("[data-test='error']")); // [S8][S12]
            String tmp = x.getText(); // [S8]
            if (tmp != null && !tmp.isEmpty()) {
                return true;
            }
            return false;
        } catch (Exception e) { // [S4]
            return false;
        }
    }

    // [S10] Third copy of the same null/empty check pattern
    public String getError() {
        try {
            WebElement x = driver.findElement(By.cssSelector("[data-test='error']")); // [S12]
            String tmp = x.getText();
            if (tmp != null && !tmp.isEmpty()) {
                return tmp;
            }
            return "";
        } catch (Exception e) { // [S4]
            System.out.println("Could not get error: " + e.getMessage()); // [S6]
            return null; // [S9]
        }
    }
}
