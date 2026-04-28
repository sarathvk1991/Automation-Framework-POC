package com.automation.base;

import com.automation.utils.ConfigReader;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * Cucumber lifecycle hooks that bracket every scenario with browser setup and teardown.
 *
 * Why Cucumber @Before/@After instead of JUnit @BeforeEach/@AfterEach:
 *   Cucumber manages its own test lifecycle. Using io.cucumber.java hooks ensures
 *   the driver is ready before the first step runs and torn down after the last step,
 *   regardless of how the runner is configured.
 *
 * Cucumber instantiates this class once per scenario, so no shared state between
 * scenarios can accidentally leak through instance fields.
 */
public class BaseTest {

    private static final Logger log = LoggerFactory.getLogger(BaseTest.class);

    /**
     * Runs before every scenario.
     * Accepts {@link Scenario} so we can log the scenario name and ID for traceability.
     */
    @Before(order = 0)
    public void setUp(Scenario scenario) {
        log.info("▶ Scenario: {} [{}]", scenario.getName(), scenario.getId());

        DriverFactory.initDriver();

        String baseUrl = ConfigReader.get("base.url");
        if (baseUrl != null && !baseUrl.isBlank()) {
            WebDriver driver = DriverFactory.getDriver();
            driver.get(baseUrl);
            log.info("Navigated to base URL: {}", baseUrl);

            // pageLoadStrategy NONE: driver.get() returns before the page is ready.
            // WSL2's NAT bridge can transiently block a Chrome navigation for 60+ s while
            // the very next scenario loads in under 2 s. One refresh is enough to cancel
            // the stalled connection and get a fresh TCP slot past the congestion window.
            for (int attempt = 1; attempt <= 2; attempt++) {
                if (attempt > 1) {
                    log.warn("Page load stalled on attempt 1 — refreshing navigation");
                    driver.navigate().refresh();
                }
                try {
                    new WebDriverWait(driver, Duration.ofSeconds(30))
                        .until(d -> "complete".equals(
                            ((JavascriptExecutor) d).executeScript("return document.readyState")));
                    log.info("Page DOM ready.");
                    break;
                } catch (TimeoutException te) {
                    if (attempt == 2) throw te;
                }
            }
        }
    }

    /**
     * Runs after every scenario — even on failure.
     * Logs a warning for failed scenarios before the browser is closed so that
     * downstream reporting tools can pick up the status.
     */
    @After(order = 0)
    public void tearDown(Scenario scenario) {
        if (scenario.isFailed()) {
            log.warn("✖ Scenario FAILED: {}", scenario.getName());
        } else {
            log.info("✔ Scenario PASSED: {}", scenario.getName());
        }
        DriverFactory.quitDriver();
    }
}
