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

    // ══════════════════════════════════════════════════════════════════════════
    // [S10] DUPLICATE LOGIN METHODS — same logic, different names
    // ══════════════════════════════════════════════════════════════════════════

    // [S10] Duplicate of doIt() — same credentials, same locators, different method name
    // [S2]  By.id("user-name"), By.id("password"), By.id("login-button") repeated again
    // [S12] Direct element access without wait
    public void loginAsDefaultUser() {
        driver.findElement(By.id("user-name")).sendKeys("standard_user"); // [S2][S12]
        driver.findElement(By.id("password")).sendKeys("secret_sauce");   // [S2][S12]
        driver.findElement(By.id("login-button")).click();                // [S2][S12]
        System.out.println("loginAsDefaultUser executed");                // [S6]
    }

    // [S10] Third copy of the login submit pattern — duplicate of doIt and loginAsDefaultUser
    // [S2]  By.id("user-name"), By.id("password"), By.id("login-button") each appear again
    // [S8]  Parameter names u, p — non-descriptive single letters
    // [S12] Direct element access without wait
    public void performLogin(String u, String p) { // [S8] poor param names
        WebElement tmp = driver.findElement(By.id("user-name")); // [S8][S12]
        tmp.clear();
        tmp.sendKeys(u);
        WebElement x = driver.findElement(By.id("password")); // [S8][S12]
        x.clear();
        x.sendKeys(p);
        driver.findElement(By.id("login-button")).click(); // [S2][S12]
    }

    // ══════════════════════════════════════════════════════════════════════════
    // [S11] LONG METHOD — login + inventory check + cart navigation combined
    // ══════════════════════════════════════════════════════════════════════════

    // [S11] Five concerns in one method: navigate, login, verify, add item, open cart
    // [S2]  By.id("user-name"), By.id("password"), By.id("login-button") repeated inline
    // [S8]  Variables: a, b, x, tmp
    // [S12] All element interactions direct — no explicit wait
    public String doLoginAndCheckInventory(String username, String password) {
        // Concern 1: navigate to application
        driver.get("https://www.saucedemo.com"); // [S2] hardcoded URL
        // Concern 2: fill login form — locators repeated, not extracted to constants
        WebElement a = driver.findElement(By.id("user-name")); // [S8][S12]
        a.clear();
        a.sendKeys(username);
        WebElement b = driver.findElement(By.id("password")); // [S8][S12]
        b.clear();
        b.sendKeys(password);
        driver.findElement(By.id("login-button")).click(); // [S2][S12]
        // Concern 3: verify we landed on inventory page
        String tmp = driver.getCurrentUrl(); // [S8]
        if (!tmp.contains("inventory")) {
            System.out.println("Unexpected URL after login: " + tmp); // [S6][S9] no throw
        }
        // Concern 4: add first available product to cart
        WebElement x = driver.findElement( // [S8][S12]
            By.cssSelector(".inventory_item button")
        );
        x.click();
        System.out.println("Clicked first add-to-cart button"); // [S6]
        // Concern 5: navigate to cart and read item name
        driver.findElement(By.cssSelector("[data-test='shopping-cart-link']")).click(); // [S12]
        String cartUrl = driver.getCurrentUrl();
        System.out.println("Cart URL: " + cartUrl); // [S6][S9] no assertion
        WebElement cartItem = driver.findElement(By.cssSelector(".cart_item_name")); // [S12]
        String itemName = cartItem.getText();
        System.out.println("Item in cart: " + itemName); // [S6]
        return itemName; // [S9] propagates exception if cart is empty
    }

    // ══════════════════════════════════════════════════════════════════════════
    // [S10][S2] DUPLICATE CHECKOUT METHODS — same logic, 6 different names
    // By.id("checkout") repeated inline 6 times instead of a shared constant
    // ══════════════════════════════════════════════════════════════════════════

    // [S10] First copy — checkout form fill logic that will be repeated 5 more times
    // [S2]  By.id("checkout") hardcoded; no constant, no reuse
    // [S2]  "John", "Doe", "12345" hardcoded in every method body
    public void doCheckout() {
        driver.findElement(By.id("checkout")).click();                      // [S2]
        driver.findElement(By.id("first-name")).sendKeys("John");           // [S2] hardcoded
        driver.findElement(By.id("last-name")).sendKeys("Doe");             // [S2] hardcoded
        driver.findElement(By.id("postal-code")).sendKeys("12345");         // [S2] hardcoded
        driver.findElement(By.id("continue")).click();
        driver.findElement(By.id("finish")).click();
        System.out.println("doCheckout done");                              // [S6]
    }

    // [S10] Exact duplicate of doCheckout() — different name, identical body
    public void performCheckout() {
        driver.findElement(By.id("checkout")).click();                      // [S2]
        driver.findElement(By.id("first-name")).sendKeys("John");           // [S2] hardcoded
        driver.findElement(By.id("last-name")).sendKeys("Doe");             // [S2] hardcoded
        driver.findElement(By.id("postal-code")).sendKeys("12345");         // [S2] hardcoded
        driver.findElement(By.id("continue")).click();
        driver.findElement(By.id("finish")).click();
        System.out.println("performCheckout done");                         // [S6]
    }

    // [S10] Third copy — By.id("checkout") appears for the 3rd time
    public void startCheckout() {
        driver.findElement(By.id("checkout")).click();                      // [S2]
        driver.findElement(By.id("first-name")).sendKeys("John");           // [S2] hardcoded
        driver.findElement(By.id("last-name")).sendKeys("Doe");             // [S2] hardcoded
        driver.findElement(By.id("postal-code")).sendKeys("12345");         // [S2] hardcoded
        driver.findElement(By.id("continue")).click();
        driver.findElement(By.id("finish")).click();
        System.out.println("startCheckout done");                           // [S6]
    }

    // [S10] Fourth copy — By.id("checkout") 4th inline occurrence
    public void beginCheckout() {
        driver.findElement(By.id("checkout")).click();                      // [S2]
        driver.findElement(By.id("first-name")).sendKeys("John");           // [S2] hardcoded
        driver.findElement(By.id("last-name")).sendKeys("Doe");             // [S2] hardcoded
        driver.findElement(By.id("postal-code")).sendKeys("12345");         // [S2] hardcoded
        driver.findElement(By.id("continue")).click();
        driver.findElement(By.id("finish")).click();
        System.out.println("beginCheckout done");                           // [S6]
    }

    // [S10] Fifth copy — By.id("checkout") 5th inline occurrence
    public void checkoutNow() {
        driver.findElement(By.id("checkout")).click();                      // [S2]
        driver.findElement(By.id("first-name")).sendKeys("John");           // [S2] hardcoded
        driver.findElement(By.id("last-name")).sendKeys("Doe");             // [S2] hardcoded
        driver.findElement(By.id("postal-code")).sendKeys("12345");         // [S2] hardcoded
        driver.findElement(By.id("continue")).click();
        driver.findElement(By.id("finish")).click();
        System.out.println("checkoutNow done");                             // [S6]
    }

    // [S10] Sixth copy — By.id("checkout") 6th inline occurrence
    public void submitCheckout() {
        driver.findElement(By.id("checkout")).click();                      // [S2]
        driver.findElement(By.id("first-name")).sendKeys("John");           // [S2] hardcoded
        driver.findElement(By.id("last-name")).sendKeys("Doe");             // [S2] hardcoded
        driver.findElement(By.id("postal-code")).sendKeys("12345");         // [S2] hardcoded
        driver.findElement(By.id("continue")).click();
        driver.findElement(By.id("finish")).click();
        System.out.println("submitCheckout done");                          // [S6]
    }

    // ══════════════════════════════════════════════════════════════════════════
    // [S11] LONG METHODS — login + cart + checkout combined in one method body
    // ══════════════════════════════════════════════════════════════════════════

    // [S11] ~48-line method: navigate + login + verify + add item + cart + checkout + confirm
    // [S2]  user-name, password, login-button, checkout all hardcoded inline — no constants
    // [S8]  Variables a, b, x, tmp throughout
    // [S9]  Returns null on failure
    // [S12] All element access direct, no explicit wait
    public String doFullShoppingFlow() {
        try {
            // Step 1: navigate
            driver.get("https://www.saucedemo.com");                        // [S2] hardcoded URL
            System.out.println("Navigated to saucedemo");                   // [S6]

            // Step 2: login
            WebElement a = driver.findElement(By.id("user-name"));         // [S2][S8][S12]
            a.clear();
            a.sendKeys("standard_user");                                     // [S2] hardcoded credential
            WebElement b = driver.findElement(By.id("password"));          // [S2][S8][S12]
            b.clear();
            b.sendKeys("secret_sauce");                                      // [S2] hardcoded credential
            driver.findElement(By.id("login-button")).click();              // [S2][S12]
            System.out.println("Logged in");                                // [S6]

            // Step 3: verify inventory page (no assertion — just a print)
            String tmp = driver.getCurrentUrl();                            // [S8]
            if (!tmp.contains("inventory")) {
                System.out.println("Not on inventory: " + tmp);            // [S6][S9] no throw
            }

            // Step 4: add first available item to cart
            WebElement x = driver.findElement(                              // [S8][S12]
                By.cssSelector(".inventory_item button")
            );
            x.click();
            System.out.println("Added item to cart");                       // [S6]

            // Step 5: open cart
            driver.findElement(By.cssSelector("[data-test='shopping-cart-link']")).click(); // [S12]
            System.out.println("Opened cart");                              // [S6]

            // Step 6: begin checkout — By.id("checkout") inline again
            driver.findElement(By.id("checkout")).click();                  // [S2][S12]
            driver.findElement(By.id("first-name")).sendKeys("John");       // [S2] hardcoded
            driver.findElement(By.id("last-name")).sendKeys("Doe");         // [S2] hardcoded
            driver.findElement(By.id("postal-code")).sendKeys("12345");     // [S2] hardcoded
            driver.findElement(By.id("continue")).click();
            System.out.println("Checkout info entered");                    // [S6]

            // Step 7: finish order and return confirmation
            driver.findElement(By.id("finish")).click();
            String orderConfirmation = driver.findElement(                  // [S12]
                By.cssSelector(".complete-header")
            ).getText();
            System.out.println("Order: " + orderConfirmation);             // [S6]
            return orderConfirmation;                                        // [S9] NPE if element missing

        } catch (Exception e) {                                             // [S4]
            System.out.println("Flow failed: " + e.getMessage());          // [S6]
            return null;                                                    // [S9]
        }
    }

    // [S11] Second long method — same flow as doFullShoppingFlow(), structurally duplicated
    // [S2]  user-name, password, login-button, checkout all inline again
    // [S10] Structural duplicate of doFullShoppingFlow
    // [S8]  Variables aa, bb, cc, result — still non-descriptive
    public String executeCompleteOrder() {
        // Navigate — hardcoded URL, no config reader
        driver.get("https://www.saucedemo.com");                            // [S2] hardcoded URL

        // Login — same three locators repeated inline
        WebElement aa = driver.findElement(By.id("user-name"));            // [S2][S8][S12]
        aa.clear();
        aa.sendKeys("standard_user");                                        // [S2] hardcoded credential
        WebElement bb = driver.findElement(By.id("password"));             // [S2][S8][S12]
        bb.clear();
        bb.sendKeys("secret_sauce");                                         // [S2] hardcoded credential
        driver.findElement(By.id("login-button")).click();                  // [S2][S12]

        // Verify login — no assertion, just a console print
        if (!driver.getTitle().contains("Swag")) {
            System.out.println("Unexpected title: " + driver.getTitle());  // [S6][S9]
        }

        // Add hardcoded product — selector baked in, not a constant
        driver.findElement(By.cssSelector("[data-test='add-to-cart-sauce-labs-backpack']")).click(); // [S2][S12]
        System.out.println("Added backpack");                               // [S6]

        // Navigate to cart and read item name
        driver.findElement(By.cssSelector("[data-test='shopping-cart-link']")).click(); // [S12]
        WebElement cc = driver.findElement(By.cssSelector(".cart_item_name")); // [S8][S12]
        String result = cc.getText();                                        // [S8]
        System.out.println("Cart item: " + result);                         // [S6]

        // Checkout — By.id("checkout") inline yet again
        driver.findElement(By.id("checkout")).click();                      // [S2][S12]
        driver.findElement(By.id("first-name")).sendKeys("Jane");           // [S2] hardcoded
        driver.findElement(By.id("last-name")).sendKeys("Smith");           // [S2] hardcoded
        driver.findElement(By.id("postal-code")).sendKeys("99999");         // [S2] hardcoded
        driver.findElement(By.id("continue")).click();

        // Print total and finish — no assertion on price
        String total = driver.findElement(By.cssSelector(".summary_total_label")).getText(); // [S12]
        System.out.println("Total: " + total);                              // [S6]
        driver.findElement(By.id("finish")).click();

        // Return confirmation — NPE if .complete-header not present
        return driver.findElement(By.cssSelector(".complete-header")).getText(); // [S12][S9]
    }

    // [S11] Third long method — login + two items + checkout over 45 lines
    // [S2]  All four locators (user-name, password, login-button, checkout) inline again
    // [S10] Same structure as executeCompleteOrder() — another duplicate block
    // [S9]  Returns null on any exception
    public String runEndToEndTest() {
        try {
            // Hard-navigate — no config reader, URL baked in
            driver.get("https://www.saucedemo.com");                        // [S2] hardcoded

            // Login block — same locators repeated for the Nth time in this file
            driver.findElement(By.id("user-name")).sendKeys("performance_glitch_user"); // [S2] hardcoded
            driver.findElement(By.id("password")).sendKeys("secret_sauce");             // [S2] hardcoded
            driver.findElement(By.id("login-button")).click();                          // [S2][S12]
            System.out.println("Logged in as performance_glitch_user");                 // [S6]

            // Add two hardcoded products
            driver.findElement(By.cssSelector("[data-test='add-to-cart-sauce-labs-backpack']")).click();   // [S2][S12]
            System.out.println("Added backpack");                           // [S6]
            driver.findElement(By.cssSelector("[data-test='add-to-cart-sauce-labs-bike-light']")).click(); // [S2][S12]
            System.out.println("Added bike light");                         // [S6]

            // Verify badge count — expected value hardcoded
            String badge = driver.findElement(By.cssSelector(".shopping_cart_badge")).getText(); // [S12]
            if (!badge.equals("2")) {                                       // [S2] hardcoded expected
                System.out.println("Expected 2 items, got: " + badge);    // [S6][S9] no assertion
            }

            // Open cart
            driver.findElement(By.cssSelector("[data-test='shopping-cart-link']")).click(); // [S12]

            // Checkout — By.id("checkout") inline again
            driver.findElement(By.id("checkout")).click();                  // [S2][S12]
            driver.findElement(By.id("first-name")).sendKeys("Bob");        // [S2] hardcoded
            driver.findElement(By.id("last-name")).sendKeys("Jones");       // [S2] hardcoded
            driver.findElement(By.id("postal-code")).sendKeys("54321");     // [S2] hardcoded
            driver.findElement(By.id("continue")).click();

            // Check total — will break silently if price changes
            String price = driver.findElement(By.cssSelector(".summary_total_label")).getText(); // [S12]
            System.out.println("Total price: " + price);                   // [S6]
            if (!price.contains("$")) {
                System.out.println("Price format unexpected: " + price);   // [S6][S9]
            }

            // Finish and return confirmation text
            driver.findElement(By.id("finish")).click();
            String confirmation = driver.findElement(By.cssSelector(".complete-header")).getText(); // [S12]
            System.out.println("Confirmation: " + confirmation);           // [S6]
            return confirmation;

        } catch (Exception e) {                                             // [S4]
            System.out.println("E2E test failed: " + e.getMessage());      // [S6]
            return null;                                                    // [S9]
        }
    }
}
