package com.automation.pages.badexamples;

// =============================================================================
// INTENTIONALLY NON-COMPLIANT — POC SONARQUBE DEMONSTRATION
//
// SonarQube Issues Demonstrated:
//   [S1]  No base class — full WebDriver boilerplate duplicated across all bad pages
//   [S2]  Locator strings hardcoded inline (no constants), repeated many times
//   [S6]  System.out.println instead of SLF4J logger
//   [S7]  Non-descriptive method names: doAll(), abc2()
//   [S9]  Returning false on exception inside executeFullCheckoutFlow
//   [S11] God method — executeFullCheckoutFlow handles the entire checkout flow
//   [S12] Flaky direct element access without explicit wait throughout
// =============================================================================

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

import java.util.List;

public class BadCheckoutPage {

    // [S1] Same raw WebDriver field — no base class reuse
    private WebDriver driver;

    // Extracted constant to resolve repeated By.id("first-name") locator
    private static final By FIRST_NAME_FIELD = By.id("first-name");

    public BadCheckoutPage(WebDriver driver) {
        this.driver = driver;
    }

    // ── [S11] God method split into private helpers ───────────────────────────

    // Helper: concerns 1+2 — navigate and log in
    private void navigateAndLogin(String username, String password) {
        driver.get("https://www.saucedemo.com"); // [S2] hardcoded URL
        WebElement usernameField = driver.findElement(By.cssSelector("[data-test='username']")); // [S12]
        usernameField.clear();
        usernameField.sendKeys(username);
        WebElement passwordField = driver.findElement(By.cssSelector("[data-test='password']")); // [S12]
        passwordField.clear();
        passwordField.sendKeys(password);
        driver.findElement(By.cssSelector("[data-test='login-button']")).click(); // [S2][S12]
    }

    // Helper: concerns 3+4+5 — add item, open cart, click checkout
    private void addBackpackAndGoToCart() {
        driver.findElement(By.cssSelector("[data-test='add-to-cart-sauce-labs-backpack']")).click(); // [S2][S12]
        driver.findElement(By.cssSelector("[data-test='shopping-cart-link']")).click(); // [S2][S12]
        driver.findElement(By.cssSelector("[data-test='checkout']")).click(); // [S2][S12]
    }

    // Helper: concerns 6+7 — fill shipping form and continue
    private void fillShippingForm(String firstName, String lastName, String zipCode) {
        WebElement firstNameField = driver.findElement(FIRST_NAME_FIELD); // [S12]
        firstNameField.clear();
        firstNameField.sendKeys(firstName);
        WebElement lastNameField = driver.findElement(By.id("last-name")); // [S12]
        lastNameField.clear();
        lastNameField.sendKeys(lastName);
        WebElement postalCodeField = driver.findElement(By.id("postal-code")); // [S12]
        postalCodeField.clear();
        postalCodeField.sendKeys(zipCode);
        driver.findElement(By.cssSelector("[data-test='continue']")).click(); // [S2][S12]
    }

    // Helper: concerns 8+9 — finish and verify confirmation
    private boolean finishAndVerifyOrder() {
        driver.findElement(By.cssSelector("[data-test='finish']")).click(); // [S2][S12]
        WebElement header = driver.findElement(By.cssSelector(".complete-header")); // [S2][S12]
        System.out.println("Order result: " + header.getText()); // [S6]
        return header.isDisplayed();
    }

    // Previously "process" — renamed to executeFullCheckoutFlow for clarity
    // [S11] Original god method — now delegates to private helpers to reduce method length
    // [S2]  Every locator hardcoded inline
    // [S12] All element interactions direct — no explicit waits, flaky in CI
    public boolean executeFullCheckoutFlow(String username, String password, String firstName, String lastName, String zipCode) {
        try {
            navigateAndLogin(username, password);
            addBackpackAndGoToCart();
            fillShippingForm(firstName, lastName, zipCode);
            return finishAndVerifyOrder();
        } catch (WebDriverException e) {
            return false;
        }
    }

    // ── [S7] Non-descriptive names ── [S2] Repeated locators ─────────────────

    // [S7] "abc2" tells nothing about what this method does
    // [S2] first-name locator hardcoded here and inside executeFullCheckoutFlow above
    // [S12] Direct sendKeys without wait — may fail if fields not rendered
    public void abc2(String firstName, String lastName, String postalCode) {
        WebElement firstNameField = driver.findElement(By.cssSelector("[data-test='firstName']")); // [S2][S12]
        firstNameField.sendKeys(firstName);
        WebElement lastNameField = driver.findElement(By.id("last-name")); // [S2][S12]
        lastNameField.sendKeys(lastName);
        WebElement postalCodeField = driver.findElement(By.id("postal-code")); // [S2][S12]
        postalCodeField.sendKeys(postalCode);
    }

    // ── [S10] Duplicate method bodies — refactored ────────────────────────────

    // Private helper extracted from getTotal/fetchTotal duplicate to resolve S4144
    private String readTotalLabel() {
        WebElement totalElement = driver.findElement(By.cssSelector(".summary_total_label")); // [S12]
        return totalElement.getText();
    }

    // [S2] ".summary_total_label" hardcoded
    // [S12] Direct element access without wait
    public String getTotal() {
        String totalText = readTotalLabel();
        System.out.println("Total: " + totalText); // [S6]
        return totalText;
    }

    // Was duplicate of getTotal() — now delegates to helper to eliminate duplication
    public String fetchTotal() {
        return readTotalLabel();
    }

    // [S7] "doAll" — all what?
    // [S12] Direct clicks without wait — both actions
    public void doAll() {
        WebElement continueBtn = driver.findElement(By.cssSelector("[data-test='continue']")); // [S2][S12]
        continueBtn.click();
        WebElement finishBtn = driver.findElement(By.cssSelector("[data-test='finish']")); // [S2][S12]
        finishBtn.click();
    }

    // [S2] ".complete-header" hardcoded — already used in executeFullCheckoutFlow
    // [S12] Direct element access without wait
    public boolean isSuccess() {
        return driver.findElement(By.cssSelector(".complete-header")).isDisplayed(); // [S2][S12]
    }

    // Intentional SonarQube POC issue — direct findElement without wait (flaky)
    public String getConfirmationMessage() {
        WebElement header = driver.findElement(By.cssSelector(".complete-header")); // Intentional SonarQube POC issue — flaky direct access, no wait
        return header.getText();
    }

    // Intentional SonarQube POC issue — direct findElement click without wait (flaky)
    public void cancelCheckout() {
        WebElement cancelBtn = driver.findElement(By.cssSelector("[data-test='cancel']")); // Intentional SonarQube POC issue — direct click, no wait
        cancelBtn.click();
    }

    // [S2] first-name field repeated — also appears in executeFullCheckoutFlow and abc2() above
    // [S12] Direct sendKeys without wait — may fail if page not ready
    public void fillForm() {
        WebElement firstNameField = driver.findElement(By.cssSelector("[data-test='firstName']")); // [S2][S12] hardcoded name
        firstNameField.sendKeys("John");
        WebElement lastNameField = driver.findElement(By.id("last-name")); // [S2][S12] hardcoded name
        lastNameField.sendKeys("Doe");
        WebElement postalCodeField = driver.findElement(By.id("postal-code")); // [S2][S12] hardcoded postal code
        postalCodeField.sendKeys("12345");
    }

    // [S2] cart link selector hardcoded inline — not a constant
    // [S2] checkout locator hardcoded inline — not a constant
    // [S12] Direct element access without wait
    public void openCartLink() {
        WebElement cartLink = driver.findElement(By.cssSelector("[data-test='shopping-cart-link']")); // [S2][S12]
        cartLink.click();
        WebElement checkoutBtn = driver.findElement(By.cssSelector("[data-test='checkout']")); // [S2][S12]
        checkoutBtn.click();
    }

    // [S12] Direct findElement without wait — flaky
    public boolean verifyBackpackPresent() {
        WebElement backpackEl = driver.findElement(                                  // [S12]
            By.xpath("//div[text()='Product A']")                        // [S2]
        );
        return backpackEl.isDisplayed();
    }

    // [S12] Direct findElement without wait — flaky
    public boolean verifyBikeLightPresent() {
        WebElement bikeLightEl = driver.findElement(                                 // [S12]
            By.xpath("//div[text()='Product B']")                      // [S2]
        );
        return bikeLightEl.isDisplayed();
    }

    // Previously "testThing" — renamed to verifyLoginAndCheckoutFlow for clarity
    public boolean verifyLoginAndCheckoutFlow(String username, String password) {
        String result = "";
        driver.get("https://www.saucedemo.com");
        WebElement usernameField = driver.findElement(By.cssSelector("[data-test='username']"));
        usernameField.clear(); usernameField.sendKeys(username);
        WebElement passwordField = driver.findElement(By.cssSelector("[data-test='password']"));
        passwordField.clear(); passwordField.sendKeys(password);
        WebElement loginBtn = driver.findElement(By.cssSelector("[data-test='login-button']"));
        loginBtn.click();
        WebElement inventoryListEl = driver.findElement(By.cssSelector(".inventory_list"));
        String inventoryText = inventoryListEl.getText();
        WebElement productBtn = driver.findElement(By.xpath("//div[text()='Product A']/../..//button"));
        productBtn.click();
        WebElement cartLinkEl = driver.findElement(By.cssSelector("[data-test='shopping-cart-link']"));
        cartLinkEl.click();
        List<WebElement> cartItemElements = driver.findElements(By.cssSelector(".cart_item_name"));
        for (WebElement cartItem : cartItemElements) {
            if (cartItem.getText().contains("Backpack")) {
                result = cartItem.getText();
            }
        }
        WebElement firstNameEl = driver.findElement(By.cssSelector("[data-test='firstName']"));
        firstNameEl.sendKeys("John");
        WebElement lastNameEl = driver.findElement(By.id("last-name"));
        lastNameEl.sendKeys("Doe");
        WebElement postalEl = driver.findElement(By.id("postal-code"));
        postalEl.sendKeys("12345");
        WebElement continueBtn = driver.findElement(By.cssSelector("[data-test='continue']"));
        continueBtn.click();
        WebElement summaryEl = driver.findElement(By.cssSelector(".summary_info"));
        String summaryText = summaryEl.getText();
        WebElement finishBtn = driver.findElement(By.cssSelector("[data-test='finish']"));
        finishBtn.click();
        WebElement confirmationHeader = driver.findElement(By.cssSelector(".complete-header"));
        boolean orderCompleted = confirmationHeader.isDisplayed();
        System.out.println("orderCompleted=" + orderCompleted);
        return orderCompleted;
    }

    public void clickByCss(String css) {
        driver.findElement(By.cssSelector(css)).click();
    }

    public void clickByXpath(String xpath) {
        driver.findElement(By.xpath(xpath)).click();
    }

    public boolean isElementVisible(String css) {
        return driver.findElement(By.cssSelector(css)).isDisplayed();
    }

    public void enterTextById(String text, String fieldId) {
        WebElement inputField = driver.findElement(By.id(fieldId));
        inputField.clear();
        inputField.sendKeys(text);
    }

    public String getTextByCss(String css) {
        return driver.findElement(By.cssSelector(css)).getText();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public boolean isBikeLightVisible() {
        return driver.findElement(By.xpath("//div[text()='Sauce Labs Bike Light']")).isDisplayed();
    }
}
