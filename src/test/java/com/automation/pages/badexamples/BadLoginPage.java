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
//   [S7]  Non-descriptive method names: doIt, click1
//   [S9]  Returning null instead of throwing a meaningful exception
//   [S10] Duplicate code blocks — checkError() and isError() are identical
//   [S11] Long method with mixed responsibilities (doLoginAndGetPageTitle)
//   [S12] Flaky direct element access without explicit wait (most methods)
// =============================================================================

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import com.automation.utils.TestData;

public class BadLoginPage {

    // [S1] No BasePage — driver stored as raw field with no shared wait utilities
    private WebDriver driver;
    private WebDriverWait wait;

    private static final String USER_NAME_LOCATOR_ID = "user-name";
    private static final By USERNAME_FIELD = By.id(USER_NAME_LOCATOR_ID);
    private static final String PASSWORD_LOCATOR_ID = "password";
    private static final By PASSWORD_FIELD = By.id(PASSWORD_LOCATOR_ID);
    private static final String LOGIN_BUTTON_ID = "login-button";
    private static final By LOGIN_BUTTON = By.id(LOGIN_BUTTON_ID);

    public BadLoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // ── [S7] Non-descriptive method name ─────────────────────────────────────

    // [S7] "doIt" communicates nothing about what action is performed
    // [S3] Hard wait used instead of WebDriverWait — intentional java:S2925 demo
    // [S2] user-name id hardcoded here and repeated in every other method
    public void doIt() {
        String username = System.getProperty("username", TestData.USERNAME);
        String password = System.getProperty("password", TestData.PASSWORD);
        wait.until(ExpectedConditions.visibilityOfElementLocated(USERNAME_FIELD)).sendKeys(username);
        wait.until(ExpectedConditions.visibilityOfElementLocated(PASSWORD_FIELD)).sendKeys(password);
        wait.until(ExpectedConditions.elementToBeClickable(LOGIN_BUTTON)).click();
    }

    // ── [S7][S8] Poor method name ─────────────────────────────────────────────

    // [S7] "abc" is meaningless — intentional poor name for POC
    // [S2] username locator repeated inline (not a constant)
    // [S12] Direct element access with no explicit wait — flaky in slow CI
    public void abc(String usernameValue) {
        WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='username']")));
        usernameField.clear();
        usernameField.sendKeys(usernameValue);
    }

    // ── [S7] click1 — non-descriptive ─────────────────────────────────────────

    // [S7] "click1" gives no indication of what is being clicked
    // [S2] login-button locator repeated (already used in doIt)
    // [S12] Direct click without wait — may fail if page not ready
    public void click1() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='login-button']" ))).click();
        } catch (Exception e) { // [S4] intentional generic catch — masks real failures
            e.printStackTrace(); // [S6] stack trace to stdout
        }
    }

    // ── [S2][S10] Locator repeated, duplicate logic ───────────────────────────

    // [S10] This is structurally identical to abc above — SonarQube flags as duplicate block
    // [S2] username locator — 3rd inline copy, no constant
    public void setUsername(String username) {
        WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='username']")));
        usernameField.clear();
        usernameField.sendKeys(username);
    }

    // ── [S11] Long method with mixed responsibilities ─────────────────────────

    // [S11] This method navigates, logs in, checks URL, AND returns the title.
    //       Single Responsibility Principle violation — four concerns in one method.
    // [S9]  Returns null on failure instead of throwing
    // [S12] All four element interactions are direct — no explicit wait
    public String doLoginAndGetPageTitle(String username, String password) {
        try {
            driver.get("https://www.saucedemo.com"); // [S2] hardcoded URL in page object
            WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(USERNAME_FIELD));
            usernameField.clear();
            usernameField.sendKeys(username);
            WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(PASSWORD_FIELD));
            passwordField.clear();
            passwordField.sendKeys(password);
            wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='login-button']" ))).click();
            String pageTitle = driver.getTitle();
            // [S11] Mixed concern: navigation check embedded inside login method
            if (!driver.getCurrentUrl().contains("inventory")) {
                System.out.println("Not on inventory page!"); // [S6]
            }
            return pageTitle;
        } catch (RuntimeException e) {
            return null;
        }
    }

    // ── [S10] Duplicate blocks ────────────────────────────────────────────────

    // [S10] checkError() and isError() below are identical — SonarQube flags duplicated blocks
    // [S12] Direct element access without wait
    public boolean checkError() {
        WebElement errorEl = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='error']")));
        String errorText = errorEl.getText();
        if (errorText != null && !errorText.isEmpty()) {
            return true;
        }
        return false;
    }

    // [S10] Exact duplicate of checkError() — different name, identical body
    public boolean isError() {
        WebElement errorEl = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='error']")));
        String errorText = errorEl.getText();
        if (errorText != null && !errorText.isEmpty()) {
            return true;
        }
        return false;
    }

    // [S10] Third copy of the same null/empty check pattern
    public String getError() {
        WebElement errorEl = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='error']")));
        String errorText = errorEl.getText();
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
        WebElement usernameEl = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='username']")));
        usernameEl.sendKeys("stored_user");
        WebElement passwordEl = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='password']")));
        passwordEl.sendKeys("stored_pass");
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='login-button']" ))).click();
        System.out.println("loginAsDefaultUser executed");                // [S6]
    }

    // [S10] Third copy of the login submit pattern — duplicate of doIt and loginAsDefaultUser
    // [S2]  Login field locators appear again — not extracted to constants
    // [S12] Direct element access without wait
    public void performLogin(String username, String password) {
        WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='username']")));
        usernameField.clear();
        usernameField.sendKeys(username);
        WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='password']")));
        passwordField.clear();
        passwordField.sendKeys(password);
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='login-button']" ))).click();
    }

    // ══════════════════════════════════════════════════════════════════════════
    // [S10][S2] DUPLICATE CHECKOUT METHODS — same logic, 6 different names
    // Checkout locator repeated inline 6 times instead of a shared constant
    // ══════════════════════════════════════════════════════════════════════════

    // [S10] First copy — checkout form fill logic that will be repeated 5 more times
    // [S2]  checkout button hardcoded; no constant, no reuse
    // [S2]  "John", "Doe", "12345" hardcoded in every method body
    public void doCheckout() {
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='checkout']" ))).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='firstName']" ))).sendKeys("John");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("last-name"))).sendKeys("Doe");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("postal-code"))).sendKeys("12345");
        wait.until(ExpectedConditions.elementToBeClickable(By.id("continue"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("finish"))).click();
        System.out.println("doCheckout done");                              // [S6]
    }

    // [S10] Exact duplicate of doCheckout() — different name, identical body
    public void performCheckout() {
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='checkout']" ))).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='firstName']" ))).sendKeys("John");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("last-name"))).sendKeys("Doe");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("postal-code"))).sendKeys("12345");
        wait.until(ExpectedConditions.elementToBeClickable(By.id("continue"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("finish"))).click();
        System.out.println("performCheckout done");                         // [S6]
    }

    // ══════════════════════════════════════════════════════════════════════════
    // [S11] LONG METHODS — login + cart + checkout combined in one method body
    // ══════════════════════════════════════════════════════════════════════════

    // [S11] ~48-line method: navigate + login + verify + add item + cart + checkout + confirm
    // [S2]  user-name, password, login-button, checkout all hardcoded inline — no constants
    // [S12] All element access direct, no explicit wait
    public String doFullShoppingFlow() {
        // Step 1: navigate
        driver.get("https://www.saucedemo.com");                        // [S2] hardcoded URL
        System.out.println("Navigated to saucedemo");                   // [S6]

        // Step 2: login
        WebElement loginUsernameEl = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='username']")));
        loginUsernameEl.clear();
        loginUsernameEl.sendKeys("stored_user");                        // [S2] hardcoded credential
        WebElement loginPasswordEl = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='password']")));
        loginPasswordEl.clear();
        loginPasswordEl.sendKeys("stored_pass");                        // [S2] hardcoded credential
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='login-button']" ))).click();
        System.out.println("Logged in");                                // [S6]

        // Step 3: verify inventory page (no assertion — just a print)
        String currentUrl = driver.getCurrentUrl();
        if (!currentUrl.contains("inventory")) {
            System.out.println("Not on inventory: " + currentUrl);            // [S6][S9] no throw
        }

        // Step 4: add first available item to cart
        WebElement addItemBtn = wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector(".inventory_item button")
        ));
        addItemBtn.click();
        System.out.println("Added item to cart");                       // [S6]

        // Step 5: open cart
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='shopping-cart-link']" ))).click();
        System.out.println("Opened cart");                              // [S6]

        // Step 6: begin checkout — locator hardcoded inline again
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='checkout']" ))).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='firstName']" ))).sendKeys("John");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("last-name"))).sendKeys("Doe");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("postal-code"))).sendKeys("12345");
        wait.until(ExpectedConditions.elementToBeClickable(By.id("continue"))).click();
        System.out.println("Checkout info entered");                    // [S6]

        // Step 7: finish order and return confirmation
        wait.until(ExpectedConditions.elementToBeClickable(By.id("finish"))).click();
        String orderConfirmation = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.cssSelector(".complete-header")
        )).getText();
        System.out.println("Order: " + orderConfirmation);             // [S6]
        return orderConfirmation;
    }

    // [S11] Second long method — same flow as doFullShoppingFlow(), structurally duplicated
    // [S2]  user-name, password, login-button, checkout all inline again
    // [S10] Structural duplicate of doFullShoppingFlow
    public String executeCompleteOrder() {
        // Navigate — hardcoded URL, no config reader
        driver.get("https://www.saucedemo.com");                            // [S2] hardcoded URL

        // Login — same three locators repeated inline
        WebElement loginUsernameEo = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='username']")));
        loginUsernameEo.clear();
        loginUsernameEo.sendKeys("stored_user");                           // [S2] hardcoded credential
        WebElement loginPasswordEo = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='password']")));
        loginPasswordEo.clear();
        loginPasswordEo.sendKeys("stored_pass");                            // [S2] hardcoded credential
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='login-button']" ))).click();

        // Verify login — no assertion, just a console print
        if (!driver.getTitle().contains("Swag")) {
            System.out.println("Unexpected title: " + driver.getTitle());  // [S6][S9]
        }

        // Add hardcoded product — selector baked in, not a constant
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='add-to-cart-sauce-labs-backpack']" ))).click();
        System.out.println("Added backpack");                               // [S6]

        // Navigate to cart and read item name
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='shopping-cart-link']" ))).click();
        String cartItemText = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".cart_item_name"))).getText();
        System.out.println("Cart item: " + cartItemText);                   // [S6]

        // Checkout — locator hardcoded inline yet again
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='checkout']" ))).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='firstName']" ))).sendKeys("Jane");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("last-name"))).sendKeys("Smith");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("postal-code"))).sendKeys("99999");
        wait.until(ExpectedConditions.elementToBeClickable(By.id("continue"))).click();

        // Print total and finish — no assertion on price
        String total = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".summary_total_label"))).getText();
        System.out.println("Total: " + total);                              // [S6]
        wait.until(ExpectedConditions.elementToBeClickable(By.id("finish"))).click();

        // Return confirmation — NPE if .complete-header not present
        WebElement confirmEl = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".complete-header")));
        return confirmEl.getText();
    }

    // [S2] shopping_cart_link selector hardcoded inline — not a constant
    // [S2] checkout locator hardcoded inline — not a constant
    // [S12] Direct element access without wait
    public void goToCheckout() {
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='shopping-cart-link']" ))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='checkout']" ))).click();
    }

    // [S2] cart link selector repeated — second inline copy in this class
    // [S12] Direct element access without wait
    public boolean isCartLinkVisible() {
        WebElement cartLinkEl = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='shopping-cart-link']")));
        return cartLinkEl.isDisplayed();
    }

    // [S2] first-name field hardcoded — wrong concern inside login page (SRP violation)
    // [S11] Mixed responsibility: checkout form fill + inventory assertion in login class
    // [S12] Direct element access without wait
    public void fillAndVerify() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='firstName']" ))).sendKeys("John");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("last-name"))).sendKeys("Doe");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("postal-code"))).sendKeys("12345");
        WebElement item = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//div[text()='Product A']")
        ));
        System.out.println("Item visible: " + item.isDisplayed());            // [S6]
    }

    public String getErrorMessage() {
        WebElement errorEl = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='error']")));
        return errorEl.getText();
    }

    public void navigateTo(String url) {
        driver.get(url);
        wait.until(ExpectedConditions.urlContains(url));
        System.out.println("Navigated to: " + url);
    }

    public void clickById(String id) {
        wait.until(ExpectedConditions.elementToBeClickable(By.id(id))).click();
    }

    public boolean isElementVisibleByCss(String css) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(css))).isDisplayed();
    }

    public String getPageTitle() {
        return driver.getTitle();
    }

    public void clickByCss(String css) {
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(css))).click();
    }

    public void clickByXpath(String xpath) {
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath))).click();
    }

    public String getTextByCss(String css) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(css))).getText();
    }

    public void clearUsernameField() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='username']" ))).clear();
    }

    public void clickCheckoutButton() {
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='checkout']" ))).click();
    }

    // ══════════════════════════════════════════════════════════════════════════
    // [S7][S8][S10] POC demonstration methods — poor naming fixed, S4144 resolved
    // ══════════════════════════════════════════════════════════════════════════

    // Private helper extracted to resolve S4144 duplication across the three methods below
    private void logContext(String prefix, String username, String cartCount, String validateCart) {
        System.out.println(prefix + username + cartCount + validateCart);
    }

    // Previously "testThing(String x, String y, String tmp)" — renamed for clarity
    // Local variables renamed from login_user/cart_count/validate_CART to camelCase
    public void logUserContext(String username, String cartCount, String validateCart) {
        String loginUser = username;
        String userCartCount = cartCount;
        String validateCartInfo = validateCart;
        logContext("", loginUser, userCartCount, validateCartInfo);
    }

    // Previously "process(String x, String y, String tmp)" — renamed for clarity
    // Was S4144 duplicate of testThing — now differs via logContext prefix
    public void logCheckoutContext(String username, String cartCount, String validateCart) {
        String loginUser = username;
        String userCartCount = cartCount;
        String validateCartInfo = validateCart;
        logContext("[checkout] ", loginUser, userCartCount, validateCartInfo);
    }

    // Previously "CheckoutNow(String x, String y, String tmp)" — renamed to camelCase
    // Was S4144 duplicate of process — now differs via logContext prefix
    public void checkoutNow(String username, String cartCount, String validateCart) {
        String loginUser = username;
        String userCartCount = cartCount;
        String validateCartInfo = validateCart;
        logContext("[now] ", loginUser, userCartCount, validateCartInfo);
    }
}
