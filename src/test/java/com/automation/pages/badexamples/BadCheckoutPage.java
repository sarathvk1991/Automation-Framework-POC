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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class BadCheckoutPage {

    // [S1] Same raw WebDriver field — no base class reuse
    private WebDriver driver;
    private WebDriverWait wait;

    // Extracted constant to resolve repeated By.id("first-name") locator
    private static final By FIRST_NAME_FIELD = By.id("first-name");
    private static final By FIRST_NAME_DATA_TEST = By.cssSelector("[data-test='firstName']");

    public BadCheckoutPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // ── [S11] God method split into private helpers ───────────────────────────

    // Helper: concerns 1+2 — navigate and log in
    private void navigateAndLogin(String username, String password) {
        driver.get("https://www.saucedemo.com"); // [S2] hardcoded URL
        WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='username']")));
        usernameField.clear();
        usernameField.sendKeys(username);
        WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='password']")));
        passwordField.clear();
        passwordField.sendKeys(password);
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='login-button']" ))).click();
    }

    // Helper: concerns 3+4+5 — add item, open cart, click checkout
    private void addBackpackAndGoToCart() {
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='add-to-cart-sauce-labs-backpack']" ))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='shopping-cart-link']" ))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='checkout']" ))).click();
    }

    // Helper: concerns 6+7 — fill shipping form and continue
    private void fillShippingForm(String firstName, String lastName, String zipCode) {
        WebElement firstNameField = wait.until(ExpectedConditions.visibilityOfElementLocated(FIRST_NAME_FIELD));
        firstNameField.clear();
        firstNameField.sendKeys(firstName);
        WebElement lastNameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("last-name")));
        lastNameField.clear();
        lastNameField.sendKeys(lastName);
        WebElement postalCodeField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("postal-code")));
        postalCodeField.clear();
        postalCodeField.sendKeys(zipCode);
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='continue']" ))).click();
    }

    // Helper: concerns 8+9 — finish and verify confirmation
    private boolean finishAndVerifyOrder() {
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='finish']" ))).click();
        WebElement header = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".complete-header")));
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
        WebElement firstNameField = wait.until(ExpectedConditions.visibilityOfElementLocated(FIRST_NAME_DATA_TEST));
        firstNameField.sendKeys(firstName);
        WebElement lastNameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("last-name")));
        lastNameField.sendKeys(lastName);
        WebElement postalCodeField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("postal-code")));
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
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='continue']" ))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='finish']" ))).click();
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
        WebElement firstNameField = wait.until(ExpectedConditions.visibilityOfElementLocated(FIRST_NAME_DATA_TEST)); // [S2] hardcoded name
        firstNameField.sendKeys("John");
        WebElement lastNameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("last-name"))); // [S2] hardcoded name
        lastNameField.sendKeys("Doe");
        WebElement postalCodeField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("postal-code"))); // [S2] hardcoded postal code
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
        driver.get("https://www.saucedemo.com");
        WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='username']")));
        usernameField.clear(); usernameField.sendKeys(username);
        WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='password']")));
        passwordField.clear(); passwordField.sendKeys(password);
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='login-button']" ))).click();
        WebElement productBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[text()='Product A']/../..//button")));
        productBtn.click();
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='shopping-cart-link']" ))).click();
        List<WebElement> cartItemElements = driver.findElements(By.cssSelector(".cart_item_name"));
        for (WebElement cartItem : cartItemElements) {
            cartItem.getText().contains("Backpack");
        }
        WebElement firstNameEl = wait.until(ExpectedConditions.visibilityOfElementLocated(FIRST_NAME_DATA_TEST));
        firstNameEl.sendKeys("John");
        WebElement lastNameEl = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("last-name")));
        lastNameEl.sendKeys("Doe");
        WebElement postalEl = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("postal-code")));
        postalEl.sendKeys("12345");
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='continue']" ))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='finish']" ))).click();
        WebElement confirmationHeader = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".complete-header")));
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
