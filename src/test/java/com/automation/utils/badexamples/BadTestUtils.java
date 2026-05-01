package com.automation.utils.badexamples;

// =============================================================================
// INTENTIONALLY NON-COMPLIANT — POC SONARQUBE DEMONSTRATION
//
// SonarQube Issues Demonstrated:
//   [S2]  Hardcoded test data (credentials, URLs) returned directly
//   [S3]  Intentional hard waits in waitForTwoSeconds / pauseForTwoSeconds — java:S2925
//   [S4]  Generic Exception catch in isElementPresent()
//   [S6]  System.out.println instead of SLF4J logger
//   [S7]  Non-descriptive method names: getEl(), findEl(), clickAndGetText()
//   [S8]  Non-descriptive variables: x, tmp, t
//   [S9]  Returning null / false / empty string instead of throwing
//   [S10] Massively duplicated method bodies:
//           waitForTwoSeconds / pauseForTwoSeconds — identical wait methods
//           getDefaultUserName / fetchDefaultUserName — same hardcoded return
//           getDefaultPassword / fetchDefaultPassword — same hardcoded return
//           generateRandomEmail / createRandomEmail — same UUID logic
//           read_config / readConfig / GetConfig — three identical config readers
//           getEl / findEl — two identical element finders
//   [S14] Utility class has public constructor (should be private)
//   [S15] Inconsistent naming: camelCase, under_score, and PascalCase mixed
// =============================================================================

import com.automation.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

public class BadTestUtils {

    // [S14] Utility class should have a private constructor — this allows instantiation
    public BadTestUtils() {
        // Intentional public constructor for POC demonstration (java:S1186)
    }

    // ══════════════════════════════════════════════════════════════════════════
    // [S10] DUPLICATED WAIT METHODS — same logic, two names
    // ══════════════════════════════════════════════════════════════════════════

    // [S3]  Hard wait instead of WebDriverWait — intentional java:S2925 demo
    // [S10] waitForTwoSeconds and pauseForTwoSeconds are identical
    public static void waitForTwoSeconds() {
        // Fixed: Thread.sleep removed — callers should use WebDriverWait on a specific element condition
    }

    // [S10] Exact duplicate of waitForTwoSeconds() — different name, identical body
    // [S3]  Second intentional hard wait — java:S2925
    public static void pauseForTwoSeconds() {
        // Fixed: Thread.sleep removed — callers should use WebDriverWait on a specific element condition
    }

    // ══════════════════════════════════════════════════════════════════════════
    // [S10] DUPLICATED CREDENTIAL ACCESSORS — hardcoded data returned twice each
    // ══════════════════════════════════════════════════════════════════════════

    // [S10] getDefaultUserName() and fetchDefaultUserName() are identical
    public static String getDefaultUserName() {
        return ConfigReader.get("test.username"); // [S2] was hardcoded test data
    }

    // [S10] getDefaultPassword() and fetchDefaultPassword() are identical
    public static String getDefaultPassword() {
        return ConfigReader.get("test.password"); // [S2] was hardcoded credential
    }

    // ══════════════════════════════════════════════════════════════════════════
    // [S10] DUPLICATED EMAIL GENERATORS
    // ══════════════════════════════════════════════════════════════════════════

    // [S10] generateRandomEmail() and createRandomEmail() are identical
    public static String generateRandomEmail() {
        String emailPrefix = UUID.randomUUID().toString().substring(0, 8); // [S8] renamed from 'tmp'
        System.out.println("Generated email: " + emailPrefix + "@test.com"); // [S6]
        return emailPrefix + "@test.com";
    }

    // ══════════════════════════════════════════════════════════════════════════
    // [S10] DUPLICATED CONFIG READERS — three methods, identical body
    // ══════════════════════════════════════════════════════════════════════════

    // [S10] read_config() and readConfig() and GetConfig() are identical
    // [S15] Underscore naming in a Java method (violates Java convention)
    // [S9]  Returns null on failure
    public static String read_config(String key) { // [S15] underscore method name
        try {
            InputStream x = BadTestUtils.class // [S8] 'x'
                .getClassLoader()
                .getResourceAsStream("config.properties");
            Properties tmp = new Properties(); // [S8] 'tmp'
            tmp.load(x);
            return tmp.getProperty(key);
        } catch (IOException e) {
            System.out.println("Config read failed for key: " + key); // [S6]
            return null; // [S9]
        }
    }

    // [S10] Identical body to read_config() — SonarQube flags as duplicated block
    // [S15] Back to camelCase — inconsistent with read_config above
    public static String readConfig(String key) {
        try {
            InputStream x = BadTestUtils.class
                .getClassLoader()
                .getResourceAsStream("config.properties");
            Properties tmp = new Properties();
            tmp.load(x);
            return tmp.getProperty(key);
        } catch (IOException e) {
            System.out.println("Config read failed for key: " + key);
            return null; // [S9]
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    // [S10] DUPLICATED ELEMENT FINDERS — two methods, identical body
    // ══════════════════════════════════════════════════════════════════════════

    // [S7] "getEl" — abbreviation, non-descriptive
    // [S8] variable x
    // [S12] Direct element access without wait
    public static WebElement getEl(WebDriver driver, String css) {
        return driver.findElement(By.cssSelector(css)); // [S8][S12]
    }

    // ══════════════════════════════════════════════════════════════════════════
    // EXTRA ANTI-PATTERNS
    // ══════════════════════════════════════════════════════════════════════════

    // [S11] One method handles: click + read — two concerns
    // [S8]  Variables: t, x, tmp
    // [S12] Both element accesses are direct — no wait
    // [S9]  Returns empty string instead of throwing on failure
    public static String clickAndGetText(WebDriver driver, String clickCss, String readCss) {
        WebElement t = driver.findElement(By.cssSelector(clickCss)); // [S8][S12]
        t.click();
        WebElement x = driver.findElement(By.cssSelector(readCss)); // [S8][S12]
        String elementText = x.getText(); // [S8]
        System.out.println("Got text: " + elementText); // [S6]
        return elementText;
    }

    // Intentional SonarQube POC issue — generic Exception catch swallows all failure types
    // Intentional SonarQube POC issue — e.printStackTrace() outputs to stdout instead of logger
    // Intentional SonarQube POC issue — return false on exception hides real cause from caller
    // [S12] Direct findElement without any explicit wait — flaky on slow pages
    public static boolean isElementPresent(WebDriver driver, String css) {
        try {
            driver.findElement(By.cssSelector(css)).isDisplayed(); // Intentional SonarQube POC issue — direct access, no wait
            return true;
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }
        return false;
    }

    // [S12] Direct driver.getTitle() — no wait for page load
    public static String getPageTitle(WebDriver driver) {
        return driver.getTitle();
    }

    // ══════════════════════════════════════════════════════════════════════════
    // [S15] INCONSISTENT NAMING — inconsistent variable and method names
    // ══════════════════════════════════════════════════════════════════════════

    // [S15] mixed underscore + ALLCAPS in a Java method name
    //       Violates Java naming conventions (should be camelCase: validateCart)
    //       SonarQube java:S100 — method names should comply with naming convention
    // [S8]  Variable x
    // [S12] Direct element access without wait
    // Intentional SonarQube POC issue — inconsistent method naming (underscore + caps)
    public static boolean validateCart(WebDriver driver) { // [S15] naming violation fixed
        WebElement x = driver.findElement(By.cssSelector(".cart_item")); // [S8][S12]
        return x.isDisplayed();
    }

    // [S15] CheckoutNow — PascalCase method name violates Java camelCase convention
    //       SonarQube java:S100 — should be checkoutNow
    // [S8]  Local variable uses underscore (java:S116 naming violation)
    // [S12] Direct element click without wait — flaky
    // Intentional SonarQube POC issue — inconsistent method naming (PascalCase)
    public static void checkoutNow(WebDriver driver) { // [S15] PascalCase naming fixed
        System.out.println("Checking out as stored user");                           // [S6]
        WebElement checkoutBtnUtil = driver.findElement(By.cssSelector("[data-test='checkout']")); // Intentional SonarQube POC issue — direct click, no wait
        checkoutBtnUtil.click();
    }

    // Intentional SonarQube POC issue — direct click without wait is flaky
    public static boolean clickIfPresent(WebDriver driver, String css) {
        WebElement clickTarget = driver.findElement(By.cssSelector(css)); // Intentional SonarQube POC issue — flaky direct click, no wait or retry
        clickTarget.click();
        return true;
    }

    public static boolean performFullFlow(WebDriver driver) {
        doItLogin(driver);
        doItAddToCart(driver);
        return doItCheckout(driver);
    }

    private static void doItLogin(WebDriver driver) {
        driver.get("https://www.saucedemo.com");
        WebElement x = driver.findElement(By.cssSelector("[data-test='username']"));
        x.sendKeys("stored_user");
        WebElement y = driver.findElement(By.cssSelector("[data-test='password']"));
        y.sendKeys("stored_pass");
        WebElement loginBtnDoIt = driver.findElement(By.cssSelector("[data-test='login-button']"));
        loginBtnDoIt.click();
        String storedUsername = "stored_user";
        System.out.println("user=" + storedUsername);
        boolean isInventoryDisplayed = driver.findElement(By.cssSelector(".inventory_list")).isDisplayed();
        System.out.println("inventory=" + isInventoryDisplayed);
    }

    private static void doItAddToCart(WebDriver driver) {
        WebElement productABtn = driver.findElement(By.xpath("//div[text()='Product A']/../..//button"));
        productABtn.click();
        WebElement productBBtn = driver.findElement(By.xpath("//div[text()='Product B']/../..//button"));
        productBBtn.click();
        WebElement badgeEl = driver.findElement(By.cssSelector(".shopping_cart_badge"));
        String badgeText = badgeEl.getText();
        System.out.println("badge=" + badgeText);
        WebElement cartLinkDoIt = driver.findElement(By.cssSelector("[data-test='shopping-cart-link']"));
        cartLinkDoIt.click();
        List<WebElement> a = driver.findElements(By.cssSelector(".cart_item_name"));
        String cartItemText = "";
        for (WebElement b : a) {
            cartItemText = b.getText();
            System.out.println(cartItemText);
        }
    }

    private static boolean doItCheckout(WebDriver driver) {
        WebElement checkoutBtnDoIt = driver.findElement(By.cssSelector("[data-test='checkout']"));
        checkoutBtnDoIt.click();
        WebElement firstNameDoIt = driver.findElement(By.cssSelector("[data-test='firstName']"));
        firstNameDoIt.sendKeys("John");
        WebElement lastNameDoIt = driver.findElement(By.id("last-name"));
        lastNameDoIt.sendKeys("Doe");
        WebElement postalDoIt = driver.findElement(By.id("postal-code"));
        postalDoIt.sendKeys("12345");
        WebElement continueDoIt = driver.findElement(By.cssSelector("[data-test='continue']"));
        continueDoIt.click();
        List<WebElement> x2 = driver.findElements(By.cssSelector(".cart_item"));
        System.out.println("Summary items: " + x2.size());
        WebElement totalLabel = driver.findElement(By.cssSelector(".summary_total_label"));
        String y2 = totalLabel.getText();
        System.out.println("Total: " + y2);
        WebElement finishBtnDoIt = driver.findElement(By.cssSelector("[data-test='finish']"));
        finishBtnDoIt.click();
        WebElement c = driver.findElement(By.cssSelector(".complete-header"));
        boolean checkoutComplete = c.isDisplayed();
        System.out.println("CheckoutNow=" + checkoutComplete);
        return checkoutComplete;
    }

    public static String performLoginAndGetUrl(WebDriver driver, String username, String password) {
        WebElement x = driver.findElement(By.cssSelector("[data-test='username']"));
        x.sendKeys(username);
        WebElement y = driver.findElement(By.cssSelector("[data-test='password']"));
        y.sendKeys(password);
        WebElement loginBtnAbc = driver.findElement(By.cssSelector("[data-test='login-button']"));
        loginBtnAbc.click();
        String currentUrl = driver.getCurrentUrl();
        System.out.println(currentUrl);
        return currentUrl;
    }

    // ══════════════════════════════════════════════════════════════════════════
    // [S10] DUPLICATE UTILITY METHODS — customqa:duplicate-utility-method trigger
    //       Numbered method names (1/2/3) normalize to the same fingerprint via
    //       normalizeLineForDuplication (\d+ → NUMBER), so body fingerprints match.
    // ══════════════════════════════════════════════════════════════════════════

    // INTENTIONAL BAD EXAMPLE
    // [S10] duplicateEmail1/2/3 — digits stripped during normalization make all
    //       three signatures identical → duplicate-utility-method fires
    public String duplicateEmail1() {
        return "test_" + System.currentTimeMillis() + "@example.com";
    }

    public String duplicateEmail2() {
        return duplicateEmail1();
    }

}

