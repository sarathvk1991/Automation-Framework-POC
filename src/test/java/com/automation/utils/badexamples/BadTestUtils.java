package com.automation.utils.badexamples;

// =============================================================================
// INTENTIONALLY NON-COMPLIANT — POC SONARQUBE DEMONSTRATION
//
// SonarQube Issues Demonstrated:
//   [S3]  Intentional hard waits in waitForTwoSeconds / pauseForTwoSeconds — java:S2925
//   [S4]  Generic Exception caught everywhere
//   [S5]  Empty catch blocks — exceptions silently swallowed
//   [S6]  System.out.println instead of SLF4J logger
//   [S7]  Non-descriptive method names: getEl(), findEl(), clickAndGetText()
//   [S8]  Non-descriptive variables: x, tmp, t
//   [S9]  Returning null instead of throwing meaningful exceptions
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

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Properties;
import java.util.UUID;
import java.io.InputStream;

public class BadTestUtils {

    // [S14] Utility class should have a private constructor — this allows instantiation
    public BadTestUtils() {}

    // ══════════════════════════════════════════════════════════════════════════
    // [S10] DUPLICATED WAIT METHODS — same logic, two names
    // ══════════════════════════════════════════════════════════════════════════

    // [S3]  Hard wait instead of WebDriverWait — intentional java:S2925 demo
    // [S10] waitForTwoSeconds and pauseForTwoSeconds are identical
    public static void waitForTwoSeconds() {
        try {
            Thread.sleep(2000); // [S3] intentional hard wait — java:S2925
        } catch (Exception e) { // [S4]
            // [S5] InterruptedException silently swallowed
        }
    }

    // [S10] Exact duplicate of waitForTwoSeconds() — different name, identical body
    // [S3]  Second intentional hard wait — java:S2925
    public static void pauseForTwoSeconds() {
        try {
            Thread.sleep(2000); // [S3] intentional hard wait — java:S2925
        } catch (Exception e) { // [S4]
            // [S5]
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    // [S10] DUPLICATED CREDENTIAL ACCESSORS — hardcoded data returned twice each
    // ══════════════════════════════════════════════════════════════════════════

    // [S2] Hardcoded "standard_user" — should come from config.properties
    // [S10] getDefaultUserName() and fetchDefaultUserName() are identical
    public static String getDefaultUserName() {
        return "standard_user"; // [S2] hardcoded test data
    }

    // [S10] Exact duplicate of getDefaultUserName()
    public static String fetchDefaultUserName() {
        return "standard_user"; // [S2] hardcoded test data — duplicate return
    }

    // [S2] Hardcoded "secret_sauce" — credential in source code
    // [S10] getDefaultPassword() and fetchDefaultPassword() are identical
    public static String getDefaultPassword() {
        return "secret_sauce"; // [S2] hardcoded credential
    }

    // [S10] Exact duplicate of getDefaultPassword()
    public static String fetchDefaultPassword() {
        return "secret_sauce"; // [S2] hardcoded credential — duplicate return
    }

    // ══════════════════════════════════════════════════════════════════════════
    // [S10] DUPLICATED EMAIL GENERATORS
    // ══════════════════════════════════════════════════════════════════════════

    // [S10] generateRandomEmail() and createRandomEmail() are identical
    public static String generateRandomEmail() {
        String tmp = UUID.randomUUID().toString().substring(0, 8); // [S8] 'tmp'
        System.out.println("Generated email: " + tmp + "@test.com"); // [S6]
        return tmp + "@test.com";
    }

    // [S10] Exact duplicate of generateRandomEmail()
    public static String createRandomEmail() {
        String tmp = UUID.randomUUID().toString().substring(0, 8); // [S8] 'tmp'
        System.out.println("Generated email: " + tmp + "@test.com"); // [S6]
        return tmp + "@test.com";
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
        } catch (Exception e) { // [S4]
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
        } catch (Exception e) { // [S4]
            System.out.println("Config read failed for key: " + key);
            return null; // [S9]
        }
    }

    // [S10] Third copy — GetConfig uses PascalCase method name (Java violation)
    // [S15] PascalCase method name — inconsistent with all others
    public static String GetConfig(String key) { // [S15] PascalCase method
        try {
            InputStream x = BadTestUtils.class
                .getClassLoader()
                .getResourceAsStream("config.properties");
            Properties tmp = new Properties();
            tmp.load(x);
            return tmp.getProperty(key);
        } catch (Exception e) { // [S4]
            return null; // [S5][S9] completely silent failure
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    // [S10] DUPLICATED ELEMENT FINDERS — two methods, identical body
    // ══════════════════════════════════════════════════════════════════════════

    // [S7] "getEl" — abbreviation, non-descriptive
    // [S8] variables x, t
    // [S12] Direct element access without wait
    public static WebElement getEl(WebDriver driver, String css) {
        try {
            WebElement x = driver.findElement(By.cssSelector(css)); // [S8][S12]
            return x;
        } catch (Exception e) { // [S4]
            System.out.println("Element not found: " + css); // [S6]
            return null; // [S9]
        }
    }

    // [S10] Identical to getEl() — just a different method name
    // [S7] "findEl" — barely better than "getEl"
    public static WebElement findEl(WebDriver driver, String css) {
        try {
            WebElement x = driver.findElement(By.cssSelector(css)); // [S12]
            return x;
        } catch (Exception e) { // [S4]
            System.out.println("Element not found: " + css);
            return null; // [S9]
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    // EXTRA ANTI-PATTERNS
    // ══════════════════════════════════════════════════════════════════════════

    // [S11] One method handles: click + read — two concerns
    // [S8]  Variables: t, x, tmp
    // [S12] Both element accesses are direct — no wait
    // [S9]  Returns empty string instead of throwing on failure
    public static String clickAndGetText(WebDriver driver, String clickCss, String readCss) {
        try {
            WebElement t = driver.findElement(By.cssSelector(clickCss)); // [S8][S12]
            t.click();
            WebElement x = driver.findElement(By.cssSelector(readCss)); // [S8][S12]
            String tmp = x.getText(); // [S8]
            System.out.println("Got text: " + tmp); // [S6]
            return tmp;
        } catch (Exception e) { // [S4]
            System.out.println("clickAndGetText failed"); // [S6] no exception detail
            return ""; // [S9] empty string hides failure from caller
        }
    }

    // Intentional SonarQube POC issue — catch (Exception e) swallows all failure types
    // Intentional SonarQube POC issue — e.printStackTrace() outputs to stdout instead of logger
    // Intentional SonarQube POC issue — return false on exception hides real cause from caller
    // [S12] Direct findElement without any explicit wait — flaky on slow pages
    public static boolean isElementPresent(WebDriver driver, String css) {
        try {
            driver.findElement(By.cssSelector(css)).isDisplayed(); // Intentional SonarQube POC issue — direct access, no wait
            return true;
        } catch (Exception e) { // Intentional SonarQube POC issue — overly broad catch
            e.printStackTrace(); // Intentional SonarQube POC issue — stack trace to stdout
            return false; // Intentional SonarQube POC issue — caller cannot distinguish absent vs error
        }
    }

    // Intentional SonarQube POC issue — empty catch block silently swallows exception
    // Intentional SonarQube POC issue — return null forces every caller to null-check or NPE
    public static String getPageTitle(WebDriver driver) {
        try {
            return driver.getTitle();
        } catch (Exception e) { // Intentional SonarQube POC issue — catch (Exception e)
            // Intentional SonarQube POC issue — empty catch, no logging, failure invisible
            return null; // Intentional SonarQube POC issue — return null
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    // [S15] INCONSISTENT NAMING — validate_CART, CheckoutNow, login_user variable
    // ══════════════════════════════════════════════════════════════════════════

    // [S15] validate_CART — mixed underscore + ALLCAPS in a Java method name
    //       Violates Java naming conventions (should be camelCase: validateCart)
    //       SonarQube java:S100 — method names should comply with naming convention
    // [S8]  Variable x
    // [S12] Direct element access without wait
    // Intentional SonarQube POC issue — inconsistent method naming (underscore + caps)
    public static boolean validate_CART(WebDriver driver) { // [S15] naming violation
        try {
            WebElement x = driver.findElement(By.cssSelector(".cart_item")); // [S8][S12]
            return x.isDisplayed();
        } catch (Exception e) { // [S4]
            return false; // [S9]
        }
    }

    // [S15] CheckoutNow — PascalCase method name violates Java camelCase convention
    //       SonarQube java:S100 — should be checkoutNow
    // [S8]  Local variable login_user uses underscore (java:S116 naming violation)
    // [S12] Direct element click without wait — flaky
    // Intentional SonarQube POC issue — inconsistent method naming (PascalCase)
    public static void CheckoutNow(WebDriver driver) { // [S15] PascalCase method
        try {
            String login_user = "standard_user"; // Intentional SonarQube POC issue — underscore variable name (java:S116)
            System.out.println("Checking out as: " + login_user);                           // [S6]
            driver.findElement(By.cssSelector("[data-test='checkout']")).click();            // Intentional SonarQube POC issue — direct click, no wait
        } catch (Exception e) { // [S4]
            // Intentional SonarQube POC issue — empty catch block
        }
    }

    // Intentional SonarQube POC issue — catch (Exception e) used for control flow
    // Intentional SonarQube POC issue — direct click without wait is flaky
    // Intentional SonarQube POC issue — return false on exception masks real failures
    public static boolean clickIfPresent(WebDriver driver, String css) {
        try {
            driver.findElement(By.cssSelector(css)).click(); // Intentional SonarQube POC issue — flaky direct click, no wait or retry
            return true;
        } catch (Exception e) { // Intentional SonarQube POC issue — exception used as "not found" signal
            return false; // Intentional SonarQube POC issue — caller has no idea why it failed
        }
    }
}
