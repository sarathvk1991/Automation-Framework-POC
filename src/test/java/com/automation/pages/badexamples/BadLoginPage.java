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
//   [S3]  Intentional hard wait in doIt — java:S2925
//   [S4]  Generic Exception caught — masks real failures
//   [S5]  Empty catch blocks — exceptions silently swallowed
//   [S6]  System.out.println instead of SLF4J logger
//   [S7]  Non-descriptive method names: doIt, abc, click1
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
    // [S2] user-name id hardcoded here and repeated in every other method
    public void doIt() {
        try {
            Thread.sleep(2000); // [S3] intentional hard wait — java:S2925
        } catch (InterruptedException e) {
            // [S5] InterruptedException swallowed
        }
        driver.findElement(By.id("user-name")).sendKeys("standard_user"); // [S2]
        driver.findElement(By.id("password")).sendKeys("secret_sauce");   // [S2]
        driver.findElement(By.id("login-button")).click();                // [S2]
    }

    // ── [S7][S8] Poor method and parameter names ──────────────────────────────

    // [S7] "abc" is meaningless
    // [S8] parameter named "x" — caller cannot tell what to pass
    // [S2] username locator repeated inline (not a constant)
    // [S12] Direct element access with no explicit wait — flaky in slow CI
    public void abc(String x) {
        WebElement y = driver.findElement(By.cssSelector("[data-test='username']")); // [S8] var 'y' [S12]
        y.clear();
        y.sendKeys(x);
    }

    // ── [S7] click1 — non-descriptive ─────────────────────────────────────────

    // [S7] "click1" gives no indication of what is being clicked
    // [S2] login-button locator repeated (already used in doIt)
    // [S12] Direct click without wait — may fail if page not ready
    public void click1() {
        try {
            driver.findElement(By.cssSelector("[data-test='login-button']")).click(); // [S2][S12]
        } catch (Exception e) { // [S4] intentional generic catch — masks real failures
            e.printStackTrace(); // [S6] stack trace to stdout
        }
    }

    // ── [S2][S10] Locator repeated, duplicate logic ───────────────────────────

    // [S10] This is structurally identical to abc above — SonarQube flags as duplicate block
    // [S2] username locator — 3rd inline copy, no constant
    // [S8] parameter "x", variable "tmp"
    public void setUsername(String username) {
        WebElement tmp = driver.findElement(By.cssSelector("[data-test='username']")); // [S8] 'tmp' [S12]
        tmp.clear();
        tmp.sendKeys(username);
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
            WebElement loginBtnEl = driver.findElement(By.cssSelector("[data-test='login-button']")); // [S2][S12]
            loginBtnEl.click();
            String tmp = driver.getTitle(); // [S8]
            // [S11] Mixed concern: navigation check embedded inside login method
            if (!driver.getCurrentUrl().contains("inventory")) {
                System.out.println("Not on inventory page!"); // [S6]
            }
            return tmp; // [S8] returning a variable named 'tmp'
        } catch (RuntimeException e) {
            return null;
        }
    }

    // ── [S10] Duplicate blocks ────────────────────────────────────────────────

    // [S10] checkError() and isError() below are identical — SonarQube flags duplicated blocks
    // [S8] variables x, tmp
    // [S12] Direct element access without wait
    public boolean checkError() {
        WebElement x = driver.findElement(By.cssSelector("[data-test='error']")); // [S8][S12]
        String errorText = x.getText(); // [S8]
        if (errorText != null && !errorText.isEmpty()) {
            return true;
        }
        return false;
    }

    // [S10] Exact duplicate of checkError() — different name, identical body
    public boolean isError() {
        WebElement x = driver.findElement(By.cssSelector("[data-test='error']")); // [S8][S12]
        String errorText = x.getText(); // [S8]
        if (errorText != null && !errorText.isEmpty()) {
            return true;
        }
        return false;
    }

    // [S10] Third copy of the same null/empty check pattern
    public String getError() {
        WebElement x = driver.findElement(By.cssSelector("[data-test='error']")); // [S12]
        String errorText = x.getText();
        if (errorText != null && !errorText.isEmpty()) {
            return errorText;
        }
        return "";
    }

    // ══════════════════════════════════════════════════════════════════════════
    // [S10] DUPLICATE LOGIN METHODS — same logic, different names
    // ══════════════════════════════════════════════════════════════════════════

    // [S10] Duplicate of doIt — same credentials, same locators, different method name
    // [S2]  Login field locators repeated inline — no shared constants
    // [S12] Direct element access without wait
    public void loginAsDefaultUser() {
        WebElement usernameEl = driver.findElement(By.cssSelector("[data-test='username']")); // [S2][S12]
        usernameEl.sendKeys("stored_user");
        WebElement passwordEl = driver.findElement(By.cssSelector("[data-test='password']")); // [S2][S12]
        passwordEl.sendKeys("stored_pass");
        WebElement loginBtnEl2 = driver.findElement(By.cssSelector("[data-test='login-button']")); // [S2][S12]
        loginBtnEl2.click();
        System.out.println("loginAsDefaultUser executed");                // [S6]
    }

    // [S10] Third copy of the login submit pattern — duplicate of doIt and loginAsDefaultUser
    // [S2]  Login field locators appear again — not extracted to constants
    // [S8]  Parameter names u, p — non-descriptive single letters
    // [S12] Direct element access without wait
    public void performLogin(String u, String p) { // [S8] poor param names
        WebElement tmp = driver.findElement(By.cssSelector("[data-test='username']")); // [S8][S12]
        tmp.clear();
        tmp.sendKeys(u);
        WebElement x = driver.findElement(By.cssSelector("[data-test='password']")); // [S8][S12]
        x.clear();
        x.sendKeys(p);
        driver.findElement(By.cssSelector("[data-test='login-button']")).click(); // [S2][S12]
    }

    // ══════════════════════════════════════════════════════════════════════════
    // [S10][S2] DUPLICATE CHECKOUT METHODS — same logic, 6 different names
    // Checkout locator repeated inline 6 times instead of a shared constant
    // ══════════════════════════════════════════════════════════════════════════

    // [S10] First copy — checkout form fill logic that will be repeated 5 more times
    // [S2]  checkout button hardcoded; no constant, no reuse
    // [S2]  "John", "Doe", "12345" hardcoded in every method body
    public void doCheckout() {
        WebElement checkoutBtn = driver.findElement(By.cssSelector("[data-test='checkout']")); // [S2]
        checkoutBtn.click();
        WebElement firstNameFld = driver.findElement(By.cssSelector("[data-test='firstName']")); // [S2] hardcoded
        firstNameFld.sendKeys("John");
        WebElement lastNameFld = driver.findElement(By.id("last-name")); // [S2] hardcoded
        lastNameFld.sendKeys("Doe");
        WebElement postalFld = driver.findElement(By.id("postal-code")); // [S2] hardcoded
        postalFld.sendKeys("12345");
        WebElement continueBtn = driver.findElement(By.id("continue"));
        continueBtn.click();
        WebElement finishBtn = driver.findElement(By.id("finish"));
        finishBtn.click();
        System.out.println("doCheckout done");                              // [S6]
    }

    // [S10] Exact duplicate of doCheckout() — different name, identical body
    public void performCheckout() {
        WebElement checkoutBtn = driver.findElement(By.cssSelector("[data-test='checkout']")); // [S2]
        checkoutBtn.click();
        WebElement firstNameFld = driver.findElement(By.cssSelector("[data-test='firstName']")); // [S2] hardcoded
        firstNameFld.sendKeys("John");
        WebElement lastNameFld = driver.findElement(By.id("last-name")); // [S2] hardcoded
        lastNameFld.sendKeys("Doe");
        WebElement postalFld = driver.findElement(By.id("postal-code")); // [S2] hardcoded
        postalFld.sendKeys("12345");
        WebElement continueBtn = driver.findElement(By.id("continue"));
        continueBtn.click();
        WebElement finishBtn = driver.findElement(By.id("finish"));
        finishBtn.click();
        System.out.println("performCheckout done");                         // [S6]
    }

    // ══════════════════════════════════════════════════════════════════════════
    // [S11] LONG METHODS — login + cart + checkout combined in one method body
    // ══════════════════════════════════════════════════════════════════════════

    // [S11] ~48-line method: navigate + login + verify + add item + cart + checkout + confirm
    // [S2]  user-name, password, login-button, checkout all hardcoded inline — no constants
    // [S8]  Variables a, b, x, tmp throughout
    // [S12] All element access direct, no explicit wait
    public String doFullShoppingFlow() {
        // Step 1: navigate
        driver.get("https://www.saucedemo.com");                        // [S2] hardcoded URL
        System.out.println("Navigated to saucedemo");                   // [S6]

        // Step 2: login
        WebElement a = driver.findElement(By.cssSelector("[data-test='username']"));         // [S2][S8][S12]
        a.clear();
        a.sendKeys("stored_user");                                     // [S2] hardcoded credential
        WebElement b = driver.findElement(By.cssSelector("[data-test='password']"));          // [S2][S8][S12]
        b.clear();
        b.sendKeys("stored_pass");                                      // [S2] hardcoded credential
        WebElement loginBtnFl = driver.findElement(By.cssSelector("[data-test='login-button']")); // [S2][S12]
        loginBtnFl.click();
        System.out.println("Logged in");                                // [S6]

        // Step 3: verify inventory page (no assertion — just a print)
        String currentUrl = driver.getCurrentUrl();                            // [S8]
        if (!currentUrl.contains("inventory")) {
            System.out.println("Not on inventory: " + currentUrl);            // [S6][S9] no throw
        }

        // Step 4: add first available item to cart
        WebElement x = driver.findElement(                              // [S8][S12]
            By.cssSelector(".inventory_item button")
        );
        x.click();
        System.out.println("Added item to cart");                       // [S6]

        // Step 5: open cart
        WebElement cartLinkFl = driver.findElement(By.cssSelector("[data-test='shopping-cart-link']")); // [S12]
        cartLinkFl.click();
        System.out.println("Opened cart");                              // [S6]

        // Step 6: begin checkout — locator hardcoded inline again
        WebElement checkoutFl = driver.findElement(By.cssSelector("[data-test='checkout']")); // [S2][S12]
        checkoutFl.click();
        WebElement firstNameFl = driver.findElement(By.cssSelector("[data-test='firstName']")); // [S2] hardcoded
        firstNameFl.sendKeys("John");
        WebElement lastNameFl = driver.findElement(By.id("last-name")); // [S2] hardcoded
        lastNameFl.sendKeys("Doe");
        WebElement postalFl = driver.findElement(By.id("postal-code")); // [S2] hardcoded
        postalFl.sendKeys("12345");
        WebElement continueFl = driver.findElement(By.id("continue"));
        continueFl.click();
        System.out.println("Checkout info entered");                    // [S6]

        // Step 7: finish order and return confirmation
        WebElement finishFl = driver.findElement(By.id("finish"));
        finishFl.click();
        String orderConfirmation = driver.findElement(                  // [S12]
            By.cssSelector(".complete-header")
        ).getText();
        System.out.println("Order: " + orderConfirmation);             // [S6]
        return orderConfirmation;
    }

    // [S11] Second long method — same flow as doFullShoppingFlow(), structurally duplicated
    // [S2]  user-name, password, login-button, checkout all inline again
    // [S10] Structural duplicate of doFullShoppingFlow
    // [S8]  Variables aa, bb, cc, result — still non-descriptive
    public String executeCompleteOrder() {
        // Navigate — hardcoded URL, no config reader
        driver.get("https://www.saucedemo.com");                            // [S2] hardcoded URL

        // Login — same three locators repeated inline
        WebElement aa = driver.findElement(By.cssSelector("[data-test='username']"));            // [S2][S8][S12]
        aa.clear();
        aa.sendKeys("stored_user");                                        // [S2] hardcoded credential
        WebElement bb = driver.findElement(By.cssSelector("[data-test='password']"));             // [S2][S8][S12]
        bb.clear();
        bb.sendKeys("stored_pass");                                         // [S2] hardcoded credential
        WebElement loginBtnEo = driver.findElement(By.cssSelector("[data-test='login-button']")); // [S2][S12]
        loginBtnEo.click();

        // Verify login — no assertion, just a console print
        if (!driver.getTitle().contains("Swag")) {
            System.out.println("Unexpected title: " + driver.getTitle());  // [S6][S9]
        }

        // Add hardcoded product — selector baked in, not a constant
        WebElement addBackpackBtn = driver.findElement(By.cssSelector("[data-test='add-to-cart-sauce-labs-backpack']")); // [S2][S12]
        addBackpackBtn.click();
        System.out.println("Added backpack");                               // [S6]

        // Navigate to cart and read item name
        WebElement cartLinkEo = driver.findElement(By.cssSelector("[data-test='shopping-cart-link']")); // [S12]
        cartLinkEo.click();
        WebElement cc = driver.findElement(By.cssSelector(".cart_item_name")); // [S8][S12]
        String result = cc.getText();                                        // [S8]
        System.out.println("Cart item: " + result);                         // [S6]

        // Checkout — locator hardcoded inline yet again
        WebElement checkoutEo = driver.findElement(By.cssSelector("[data-test='checkout']")); // [S2][S12]
        checkoutEo.click();
        WebElement firstNameEo = driver.findElement(By.cssSelector("[data-test='firstName']")); // [S2] hardcoded
        firstNameEo.sendKeys("Jane");
        WebElement lastNameEo = driver.findElement(By.id("last-name")); // [S2] hardcoded
        lastNameEo.sendKeys("Smith");
        WebElement postalEo = driver.findElement(By.id("postal-code")); // [S2] hardcoded
        postalEo.sendKeys("99999");
        WebElement continueEo = driver.findElement(By.id("continue"));
        continueEo.click();

        // Print total and finish — no assertion on price
        WebElement totalEl = driver.findElement(By.cssSelector(".summary_total_label")); // [S12]
        String total = totalEl.getText();
        System.out.println("Total: " + total);                              // [S6]
        WebElement finishEo = driver.findElement(By.id("finish"));
        finishEo.click();

        // Return confirmation — NPE if .complete-header not present
        WebElement confirmEl = driver.findElement(By.cssSelector(".complete-header")); // [S12][S9]
        return confirmEl.getText();
    }

    // [S2] By.cssSelector(".shopping_cart_link") hardcoded inline — not a constant
    // [S2] checkout locator hardcoded inline — not a constant
    // [S12] Direct element access without wait
    public void goToCheckout() {
        WebElement cartLinkGo = driver.findElement(By.cssSelector("[data-test='shopping-cart-link']")); // [S2][S12]
        cartLinkGo.click();
        WebElement checkoutGo = driver.findElement(By.cssSelector("[data-test='checkout']")); // [S2][S12]
        checkoutGo.click();
    }

    // [S2] cart link selector repeated — second inline copy in this class
    // [S12] Direct element access without wait
    public boolean isCartLinkVisible() {
        WebElement x = driver.findElement(By.cssSelector("[data-test='shopping-cart-link']")); // [S8][S12]
        return x.isDisplayed();
    }

    // [S2] first-name field hardcoded — wrong concern inside login page (SRP violation)
    // [S11] Mixed responsibility: checkout form fill + inventory assertion in login class
    // [S12] Direct element access without wait
    public void fillAndVerify() {
        WebElement firstNameFv = driver.findElement(By.cssSelector("[data-test='firstName']")); // [S2][S12]
        firstNameFv.sendKeys("John");
        WebElement lastNameFv = driver.findElement(By.id("last-name")); // [S2][S12]
        lastNameFv.sendKeys("Doe");
        WebElement postalFv = driver.findElement(By.id("postal-code")); // [S2][S12]
        postalFv.sendKeys("12345");
        WebElement item = driver.findElement(                                      // [S12]
            By.xpath("//div[text()='Product A']")                        // [S2]
        );
        System.out.println("Item visible: " + item.isDisplayed());            // [S6]
    }

    public String getErrorMessage() {
        WebElement errorEl = driver.findElement(By.cssSelector("[data-test='error']"));
        return errorEl.getText();
    }

    public void navigateTo(String url) {
        driver.get(url);
        try { Thread.sleep(2000); } catch (InterruptedException e) { }
        System.out.println("Navigated to: " + url);
    }

    public void clickById(String id) {
        driver.findElement(By.id(id)).click();
    }

    public boolean isElementVisibleByCss(String css) {
        return driver.findElement(By.cssSelector(css)).isDisplayed();
    }

    public String getPageTitle() {
        return driver.getTitle();
    }

    public void clickByCss(String css) {
        driver.findElement(By.cssSelector(css)).click();
    }

    public void clickByXpath(String xpath) {
        driver.findElement(By.xpath(xpath)).click();
    }

    public String getTextByCss(String css) {
        return driver.findElement(By.cssSelector(css)).getText();
    }

    public void clearUsernameField() {
        driver.findElement(By.cssSelector("[data-test='username']")).clear();
    }

    public void clickCheckoutButton() {
        driver.findElement(By.cssSelector("[data-test='checkout']")).click();
    }
}
