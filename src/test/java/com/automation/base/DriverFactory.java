package com.automation.base;

import com.automation.utils.ConfigReader;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * Creates and manages WebDriver instances per thread.
 *
 * ThreadLocal isolation means each concurrent thread (scenario) gets its own
 * driver without any synchronisation overhead. Tests must call quitDriver()
 * at the end of every scenario to prevent browser leaks.
 *
 * Resolution order for configuration:
 *   1. JVM system property  (-Dbrowser=firefox  -Dheadless=true)
 *   2. config.properties
 *   3. Hard-coded default   (chrome, non-headless)
 */
public class DriverFactory {

    private static final Logger log = LoggerFactory.getLogger(DriverFactory.class);

    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    private DriverFactory() {}

    // ── Public API ────────────────────────────────────────────────────────────

    public static WebDriver getDriver() {
        return driverThreadLocal.get();
    }

    /**
     * Initialises a WebDriver for the current thread using resolved browser/headless config.
     * Applies implicit wait and maximises the window before returning control to the caller.
     */
    public static void initDriver() {
        Browser browser = resolvedBrowser();
        boolean headless = resolvedHeadless();

        WebDriver driver = createDriver(browser, headless);

        int implicitWaitSecs = ConfigReader.getInt("implicit.wait", 10);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWaitSecs));
        driver.manage().window().maximize();

        driverThreadLocal.set(driver);
        log.info("WebDriver started [browser={}, headless={}]", browser, headless);
    }

    /**
     * Quits the current thread's WebDriver and removes it from ThreadLocal.
     * Safe to call even when no driver has been initialised.
     */
    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            driver.quit();
            driverThreadLocal.remove();
            log.info("WebDriver quit and removed from ThreadLocal.");
        }
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    private static Browser resolvedBrowser() {
        String name = System.getProperty("browser", ConfigReader.get("browser", "chrome"));
        return Browser.from(name);
    }

    private static boolean resolvedHeadless() {
        String value = System.getProperty("headless", ConfigReader.get("headless", "false"));
        return Boolean.parseBoolean(value);
    }

    /**
     * Builds a driver instance for the requested browser.
     * Options objects are constructed here so each browser's flags stay isolated.
     */
    private static WebDriver createDriver(Browser browser, boolean headless) {
        return switch (browser) {
            case CHROME -> {
                ChromeOptions options = new ChromeOptions();
                if (headless) {
                    // --headless=new is the Chromium headless mode introduced in Chrome 112.
                    // --window-size is required because maximize() is a no-op without a real
                    // display; headless Chrome otherwise defaults to 800×600, pushing elements
                    // below the fold and causing click/visibility timeouts in CI.
                    // The background-networking / first-run / sync flags stop Chrome from making
                    // outbound calls on startup (update checks, phoning home, Safe Browsing
                    // downloads). On WSL2, those calls compete with test traffic over the same
                    // hypervisor NAT bridge and are the primary cause of the 30–60 s page-load
                    // delays observed mid-run.
                    options.addArguments(
                        "--headless=new",
                        "--no-sandbox",
                        "--disable-dev-shm-usage",
                        "--window-size=1920,1080",
                        "--disable-gpu",
                        "--disable-background-networking",
                        "--disable-default-apps",
                        "--disable-extensions",
                        "--disable-sync",
                        "--no-first-run",
                        "--no-default-browser-check",
                        "--metrics-recording-only",
                        "--mute-audio"
                    );
                }
                WebDriverManager.chromedriver().setup();
                yield new ChromeDriver(options);
            }
            case FIREFOX -> {
                FirefoxOptions options = new FirefoxOptions();
                if (headless) {
                    options.addArguments("-headless");
                }
                WebDriverManager.firefoxdriver().setup();
                yield new FirefoxDriver(options);
            }
            case EDGE -> {
                EdgeOptions options = new EdgeOptions();
                if (headless) {
                    options.addArguments("--headless=new");
                }
                WebDriverManager.edgedriver().setup();
                yield new EdgeDriver(options);
            }
        };
    }
}
