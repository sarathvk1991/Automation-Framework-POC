package com.automation.utils.badexamples;

// =============================================================================
// INTENTIONALLY NON-COMPLIANT — POC SONARQUBE DEMONSTRATION
//
// SonarQube Issues Demonstrated:
//   [S3]  Thread.sleep() used as the primary wait strategy
//   [S4]  Generic Exception caught everywhere
//   [S5]  Empty catch blocks — exceptions silently swallowed
//   [S6]  System.out.println instead of SLF4J logger
//   [S7]  Non-descriptive method names: wait1(), wait2(), doWait(), pause()
//   [S8]  Non-descriptive variables: x, tmp, t
//   [S9]  Returning null instead of throwing meaningful exceptions
//   [S10] Massively duplicated method bodies across wait1/wait2/doWait/pause
//   [S12] Magic numbers throughout (1000, 2000, 3000, 5000)
//   [S14] Utility class has public constructor (should be private)
//   [S15] Inconsistent naming: camelCase mixed with under_score style
// =============================================================================

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Properties;
import java.io.InputStream;

public class BadTestUtils {

    // [S14] Utility class should have a private constructor — this allows instantiation
    public BadTestUtils() {}

    // ══════════════════════════════════════════════════════════════════════════
    // [S10] DUPLICATED WAIT METHODS — four methods that do the same thing
    // ══════════════════════════════════════════════════════════════════════════

    // [S7] "wait1" — wait for how long? in what unit? why?
    // [S3] Uses Thread.sleep instead of WebDriverWait
    public static void wait1() {
        try {
            Thread.sleep(1000); // [S12] magic number
        } catch (Exception e) { // [S4]
            // [S5] Empty — InterruptedException silently swallowed
        }
    }

    // [S10] Identical to wait1() except for the sleep duration — should be a parameter
    // [S7] "wait2" — different from wait1 only in name and magic number
    public static void wait2() {
        try {
            Thread.sleep(2000); // [S12]
        } catch (Exception e) { // [S4]
            // [S5]
        }
    }

    // [S10] Third duplicate — doWait() does exactly what wait1() and wait2() do
    // [S7] "doWait" is marginally better but still non-descriptive
    public static void doWait() {
        try {
            Thread.sleep(3000); // [S12]
        } catch (Exception e) { // [S4]
            System.out.println("Sleep interrupted"); // [S6]
        }
    }

    // [S10] Fourth duplicate — pause() again does the same thing
    // [S15] "pause" uses a different naming style from wait1/wait2/doWait
    public static void pause() {
        try {
            Thread.sleep(5000); // [S12]
        } catch (Exception e) { // [S4]
            e.printStackTrace(); // [S6] stdout
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    // [S10] DUPLICATED CONFIG READERS
    // ══════════════════════════════════════════════════════════════════════════

    // [S10] read_config() and readConfig() below are identical — different name style
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
    // [S10] DUPLICATED ELEMENT FINDERS
    // ══════════════════════════════════════════════════════════════════════════

    // [S7] "getEl" — abbreviation, non-descriptive
    // [S3] Thread.sleep before every find
    // [S8] variables x, t
    public static WebElement getEl(WebDriver driver, String css) {
        try {
            Thread.sleep(1000); // [S3][S12]
            WebElement x = driver.findElement(By.cssSelector(css)); // [S8]
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
            Thread.sleep(1000);
            WebElement x = driver.findElement(By.cssSelector(css));
            return x;
        } catch (Exception e) { // [S4]
            System.out.println("Element not found: " + css);
            return null; // [S9]
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    // EXTRA ANTI-PATTERNS
    // ══════════════════════════════════════════════════════════════════════════

    // [S11] One method handles: wait + find + click + wait + find + read
    // [S3]  Multiple Thread.sleep calls
    // [S8]  Variables: t, x, tmp
    public static String clickAndGetText(WebDriver driver, String clickCss, String readCss) {
        try {
            Thread.sleep(1000); // [S3]
            WebElement t = driver.findElement(By.cssSelector(clickCss)); // [S8]
            t.click();
            Thread.sleep(2000); // [S3][S12] arbitrary delay after click
            WebElement x = driver.findElement(By.cssSelector(readCss)); // [S8]
            String tmp = x.getText(); // [S8]
            System.out.println("Got text: " + tmp); // [S6]
            return tmp;
        } catch (Exception e) { // [S4]
            System.out.println("clickAndGetText failed"); // [S6] no exception detail
            return ""; // [S9] empty string hides failure from caller
        }
    }

    // [S7] "hardSleep" — at least descriptive, but still an anti-pattern
    // [S3] Unconditional Thread.sleep with a configurable duration
    // [S12] Caller is expected to pass magic numbers (e.g. hardSleep(3000))
    public static void hardSleep(int ms) {
        try {
            Thread.sleep(ms); // [S3]
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Sleep interrupted after " + ms + "ms"); // [S6]
        }
    }

    // [S10] Identical to hardSleep but named differently
    // [S15] "sleep_ms" uses underscore naming — inconsistent convention
    public static void sleep_ms(int ms) { // [S15]
        try {
            Thread.sleep(ms); // [S3]
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Sleep interrupted after " + ms + "ms");
        }
    }
}
