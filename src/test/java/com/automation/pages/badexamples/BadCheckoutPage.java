package com.automation.pages.badexamples;

// =============================================================================
// INTENTIONALLY NON-COMPLIANT — POC SONARQUBE DEMONSTRATION
//
// SonarQube Issues Demonstrated:
//   [S1]  No base class — full WebDriver boilerplate duplicated across all bad pages
//   [S2]  Locator strings hardcoded inline (no constants), repeated many times
//   [S4]  Generic Exception catch in process
//   [S6]  System.out.println instead of SLF4J logger
//   [S7]  Non-descriptive method names: process, doAll(), abc2()
//   [S8]  Non-descriptive variable names: x, y, tmp, a, b, c
//   [S9]  Returning false on exception inside process
//   [S10] Duplicate method bodies (getTotal and fetchTotal are identical)
//   [S11] God method — process handles the entire checkout flow
//   [S12] Flaky direct element access without explicit wait throughout
// =============================================================================

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class BadCheckoutPage {

    // [S1] Same raw WebDriver field — no base class reuse
    private WebDriver driver;

    public BadCheckoutPage(WebDriver driver) {
        this.driver = driver;
    }

    // ── [S11] God method — entire checkout flow in one method ────────────────

    // [S11] process does: navigate + login + add to cart + checkout info +
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
            WebElement a = driver.findElement(By.cssSelector("[data-test='username']")); // [S8][S12]
            a.clear();
            a.sendKeys(u);
            WebElement b = driver.findElement(By.cssSelector("[data-test='password']")); // [S8][S12]
            b.clear();
            b.sendKeys(p);
            driver.findElement(By.cssSelector("[data-test='login-button']")).click(); // [S2][S12]

            // Concern 3: add product
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

        } catch (Exception e) {
            return false;
        }
    }

    // ── [S7] Non-descriptive names ── [S2] Repeated locators ─────────────────

    // [S7] "abc2" tells nothing about what this method does
    // [S2] first-name locator hardcoded here and inside process above
    // [S12] Direct sendKeys without wait — may fail if fields not rendered
    public void abc2(String a, String b, String c) { // [S8] params a, b, c
        WebElement firstNameField = driver.findElement(By.cssSelector("[data-test='firstName']")); // [S2][S12]
        firstNameField.sendKeys(a);
        WebElement lastNameField = driver.findElement(By.id("last-name")); // [S2][S12]
        lastNameField.sendKeys(b);
        WebElement postalCodeField = driver.findElement(By.id("postal-code")); // [S2][S12]
        postalCodeField.sendKeys(c);
    }

    // ── [S10] Duplicate method bodies ─────────────────────────────────────────

    // [S10] getTotal() and fetchTotal() are identical — SonarQube duplicated code block
    // [S2] ".summary_total_label" hardcoded
    // [S12] Direct element access without wait
    public String getTotal() {
        WebElement x = driver.findElement(By.cssSelector(".summary_total_label")); // [S8][S12]
        String totalText = x.getText(); // [S8]
        System.out.println("Total: " + totalText); // [S6]
        return totalText;
    }

    // [S10] Identical body to getTotal()
    public String fetchTotal() {
        WebElement x = driver.findElement(By.cssSelector(".summary_total_label")); // [S12]
        String totalText = x.getText();
        System.out.println("Total: " + totalText);
        return totalText;
    }

    // [S7] "doAll" — all what?
    // [S12] Direct clicks without wait — both actions
    public void doAll() {
        WebElement continueBtn = driver.findElement(By.cssSelector("[data-test='continue']")); // [S2][S12]
        continueBtn.click();
        WebElement finishBtn = driver.findElement(By.cssSelector("[data-test='finish']")); // [S2][S12]
        finishBtn.click();
    }

    // [S2] ".complete-header" hardcoded — already used in process
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

    // [S2] first-name field repeated — also appears in process and abc2() above
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
        WebElement x = driver.findElement(                                         // [S12]
            By.xpath("//div[text()='Product A']")                        // [S2]
        );
        return x.isDisplayed();
    }

    // [S12] Direct findElement without wait — flaky
    public boolean verifyBikeLightPresent() {
        WebElement x = driver.findElement(                                         // [S12]
            By.xpath("//div[text()='Product B']")                      // [S2]
        );
        return x.isDisplayed();
    }

    public boolean testThing(String a, String b) {
        String result = "";
        driver.get("https://www.saucedemo.com");
        WebElement x = driver.findElement(By.cssSelector("[data-test='username']"));
        x.clear(); x.sendKeys(a);
        WebElement y = driver.findElement(By.cssSelector("[data-test='password']"));
        y.clear(); y.sendKeys(b);
        WebElement loginBtn = driver.findElement(By.cssSelector("[data-test='login-button']"));
        loginBtn.click();
        WebElement inventoryListEl = driver.findElement(By.cssSelector(".inventory_list"));
        String inventoryText = inventoryListEl.getText();
        WebElement productBtn = driver.findElement(By.xpath("//div[text()='Product A']/../..//button"));
        productBtn.click();
        WebElement cartLinkEl = driver.findElement(By.cssSelector("[data-test='shopping-cart-link']"));
        cartLinkEl.click();
        List<WebElement> x2 = driver.findElements(By.cssSelector(".cart_item_name"));
        for (WebElement y2 : x2) {
            if (y2.getText().contains("Backpack")) {
                result = y2.getText();
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
        WebElement c = driver.findElement(By.cssSelector(".complete-header"));
        boolean orderCompleted = c.isDisplayed();
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
        WebElement a = driver.findElement(By.id(fieldId));
        a.clear();
        a.sendKeys(text);
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
