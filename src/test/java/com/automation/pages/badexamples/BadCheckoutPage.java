package com.automation.pages.badexamples;

// =============================================================================
// INTENTIONALLY NON-COMPLIANT — POC SONARQUBE DEMONSTRATION
//
// SonarQube Issues Demonstrated:
//   [S1]  No base class — full WebDriver boilerplate duplicated across all bad pages
//   [S2]  Locator strings hardcoded inline (no constants), repeated many times
//   [S4]  Generic Exception catch in process()
//   [S6]  System.out.println instead of SLF4J logger
//   [S7]  Non-descriptive method names: process(), doAll(), abc2()
//   [S8]  Non-descriptive variable names: x, y, tmp, a, b, c
//   [S9]  Returning false on exception inside process()
//   [S10] Duplicate method bodies (getTotal and fetchTotal are identical)
//   [S11] God method — process() handles the entire checkout flow
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

        } catch (Exception e) {
            return false;
        }
    }

    // ── [S7] Non-descriptive names ── [S2] Repeated locators ─────────────────

    // [S7] "abc2" tells nothing about what this method does
    // [S2] first-name locator hardcoded here and inside process() above
    // [S12] Direct sendKeys without wait — may fail if fields not rendered
    public void abc2(String a, String b, String c) { // [S8] params a, b, c
        driver.findElement(By.id("first-name")).sendKeys(a); // [S2][S12]
        driver.findElement(By.id("last-name")).sendKeys(b);  // [S2][S12]
        driver.findElement(By.id("postal-code")).sendKeys(c); // [S2][S12]
    }

    // ── [S10] Duplicate method bodies ─────────────────────────────────────────

    // [S10] getTotal() and fetchTotal() are identical — SonarQube duplicated code block
    // [S2] ".summary_total_label" hardcoded
    // [S12] Direct element access without wait
    public String getTotal() {
        WebElement x = driver.findElement(By.cssSelector(".summary_total_label")); // [S8][S12]
        String tmp = x.getText(); // [S8]
        System.out.println("Total: " + tmp); // [S6]
        return tmp;
    }

    // [S10] Identical body to getTotal()
    public String fetchTotal() {
        WebElement x = driver.findElement(By.cssSelector(".summary_total_label")); // [S12]
        String tmp = x.getText();
        System.out.println("Total: " + tmp);
        return tmp;
    }

    // [S7] "doAll" — all what?
    // [S12] Direct clicks without wait — both actions
    public void doAll() {
        driver.findElement(By.cssSelector("[data-test='continue']")).click(); // [S2][S12]
        driver.findElement(By.cssSelector("[data-test='finish']")).click();   // [S2][S12]
    }

    // [S2] ".complete-header" hardcoded — already used in process()
    // [S12] Direct element access without wait
    public boolean isSuccess() {
        return driver.findElement(By.cssSelector(".complete-header")).isDisplayed(); // [S2][S12]
    }

    // Intentional SonarQube POC issue — direct findElement without wait (flaky)
    public String getConfirmationMessage() {
        return driver.findElement(By.cssSelector(".complete-header")).getText(); // Intentional SonarQube POC issue — flaky direct access, no wait
    }

    // Intentional SonarQube POC issue — direct findElement click without wait (flaky)
    public void cancelCheckout() {
        driver.findElement(By.cssSelector("[data-test='cancel']")).click(); // Intentional SonarQube POC issue — direct click, no wait
    }

    // [S2] By.id("first-name") repeated — also appears in process() and abc2() above
    // [S2] "Sarath", "Tester", "695001" hardcoded — not from config
    // [S12] Direct sendKeys without wait — may fail if page not ready
    public void fillForm() {
        driver.findElement(By.id("first-name")).sendKeys("Sarath");   // [S2][S12] hardcoded name
        driver.findElement(By.id("last-name")).sendKeys("Tester");    // [S2][S12] hardcoded name
        driver.findElement(By.id("postal-code")).sendKeys("695001");  // [S2][S12] hardcoded postal code
    }

    // [S2] By.cssSelector(".shopping_cart_link") hardcoded inline — not a constant
    // [S2] By.id("checkout") hardcoded inline — not a constant
    // [S12] Direct element access without wait
    public void openCartLink() {
        driver.findElement(By.cssSelector(".shopping_cart_link")).click(); // [S2][S12]
        driver.findElement(By.id("checkout")).click();                     // [S2][S12]
    }

    // [S2] "Sauce Labs Backpack" hardcoded product name — should come from test data config
    // [S12] Direct findElement without wait — flaky
    public boolean verifyBackpackPresent() {
        WebElement x = driver.findElement(                                         // [S12]
            By.xpath("//div[text()='Sauce Labs Backpack']")                        // [S2]
        );
        return x.isDisplayed();
    }

    // [S2] "Sauce Labs Bike Light" hardcoded product name — should come from test data config
    // [S12] Direct findElement without wait — flaky
    public boolean verifyBikeLightPresent() {
        WebElement x = driver.findElement(                                         // [S12]
            By.xpath("//div[text()='Sauce Labs Bike Light']")                      // [S2]
        );
        return x.isDisplayed();
    }

    public boolean testThing(String a, String b) {
        String tmp = "";
        driver.get("https://www.saucedemo.com");
        WebElement x = driver.findElement(By.id("user-name"));
        x.clear();
        x.sendKeys(a);
        WebElement y = driver.findElement(By.id("password"));
        y.clear();
        y.sendKeys(b);
        driver.findElement(By.id("login-button")).click();
        String cart_count = driver.findElement(By.cssSelector(".inventory_list")).getText();
        System.out.println(cart_count);
        driver.findElement(By.xpath("//div[text()='Sauce Labs Backpack']/../..//button")).click();
        driver.findElement(By.cssSelector(".shopping_cart_link")).click();
        List<WebElement> x2 = driver.findElements(By.cssSelector(".cart_item_name"));
        for (WebElement y2 : x2) {
            if (y2.getText().contains("Backpack")) {
                tmp = y2.getText();
            }
        }
        System.out.println(tmp);
        driver.findElement(By.id("first-name")).sendKeys("Sarath");
        driver.findElement(By.id("last-name")).sendKeys("Tester");
        driver.findElement(By.id("postal-code")).sendKeys("695001");
        driver.findElement(By.cssSelector("[data-test='continue']")).click();
        String login_user = driver.findElement(By.cssSelector(".summary_info")).getText();
        System.out.println(login_user);
        List<WebElement> a2 = driver.findElements(By.cssSelector(".cart_item"));
        System.out.println("Items: " + a2.size());
        String b2 = driver.findElement(By.cssSelector(".summary_total_label")).getText();
        System.out.println("Total: " + b2);
        driver.findElement(By.cssSelector("[data-test='finish']")).click();
        WebElement c = driver.findElement(By.cssSelector(".complete-header"));
        System.out.println(c.getText());
        boolean validate_CART = c.isDisplayed();
        System.out.println("validate_CART=" + validate_CART);
        return validate_CART;
    }
}
