package com.automation.base;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Cross-cutting Cucumber hooks — screenshot capture, metadata logging, and
 * disk persistence. Runs alongside BaseTest hooks in a defined order:
 *
 *   @Before(order=0)  BaseTest.setUp()               — init driver, navigate to base URL
 *   @Before(order=1)  Hooks.logScenarioMetadata()    — log tags (driver is already ready)
 *
 *   @After (order=1)  Hooks.captureScreenshotOnFailure() — screenshot while browser is open
 *   @After (order=0)  BaseTest.tearDown()                — quit driver (always last)
 *
 * For @After, Cucumber executes higher order values first, so order=1 is
 * guaranteed to run before order=0. This ensures the driver is never quit
 * before the screenshot is taken.
 */
public class Hooks {

    private static final Logger log = LoggerFactory.getLogger(Hooks.class);

    private static final String SCREENSHOT_DIR = "target/screenshots";
    private static final DateTimeFormatter TIMESTAMP_FMT =
            DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    // ── Before ────────────────────────────────────────────────────────────────

    /**
     * Logs scenario tags and the parent feature file name after the driver has
     * been initialised by BaseTest (order=0). Placing this at order=1 keeps
     * driver lifecycle and diagnostic logging in separate, single-purpose classes.
     */
    @Before(order = 1)
    public void logScenarioMetadata(Scenario scenario) {
        log.info("Feature  : {}", resolveFeatureName(scenario));
        log.info("Tags     : {}", scenario.getSourceTagNames());
    }

    // ── After ─────────────────────────────────────────────────────────────────

    /**
     * Captures a screenshot when a scenario fails and persists it two ways:
     *
     *   1. Embedded in the Cucumber report — the PNG bytes are attached to the
     *      scenario via scenario.attach(), making the image appear inline in the
     *      HTML and JSON reports without any extra tooling.
     *
     *   2. Saved to target/screenshots/ — a separate file per failure, named
     *      with the scenario name and a timestamp, suitable for archiving as a
     *      CI build artefact (e.g. with actions/upload-artifact or Jenkins).
     *
     * Runs at order=1 so it executes before BaseTest.tearDown() (order=0) quits
     * the browser. If the driver is null (failure before browser opened), the
     * method logs a warning and returns safely rather than throwing.
     */
    @After(order = 1)
    public void captureScreenshotOnFailure(Scenario scenario) {
        if (!scenario.isFailed()) {
            return;
        }
        WebDriver driver = DriverFactory.getDriver();
        if (driver == null) {
            log.warn("Screenshot skipped — driver is null for scenario: '{}'", scenario.getName());
            return;
        }
        try {
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", scenario.getName() + " — failure");
            saveScreenshotToDisk(scenario.getName(), screenshot);
        } catch (Exception e) {
            log.warn("Could not capture screenshot for '{}': {}", scenario.getName(), e.getMessage());
        }
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    /**
     * Writes screenshot bytes to target/screenshots/<scenario>_<timestamp>.png.
     * Creates the directory if it does not already exist.
     */
    private void saveScreenshotToDisk(String scenarioName, byte[] screenshot) {
        try {
            Path dir = Paths.get(SCREENSHOT_DIR);
            Files.createDirectories(dir);

            String fileName = sanitiseFileName(scenarioName)
                    + "_" + TIMESTAMP_FMT.format(LocalDateTime.now())
                    + ".png";

            Path filePath = dir.resolve(fileName);
            Files.write(filePath, screenshot);
            log.info("Screenshot saved: {}", filePath.toAbsolutePath());
        } catch (IOException e) {
            log.warn("Could not write screenshot to disk: {}", e.getMessage());
        }
    }

    /**
     * Removes characters that are illegal in file names on Windows, macOS, and Linux,
     * then collapses runs of underscores so the file name stays readable.
     */
    private String sanitiseFileName(String name) {
        return name.replaceAll("[^a-zA-Z0-9._-]", "_")
                   .toLowerCase()
                   .replaceAll("_+", "_")
                   .replaceAll("^_|_$", "");
    }

    /**
     * Derives the feature file name from the scenario URI.
     * e.g. "classpath:features/login.feature" → "login"
     */
    private String resolveFeatureName(Scenario scenario) {
        String uri   = scenario.getUri().toString();
        int    slash = uri.lastIndexOf('/');
        int    dot   = uri.lastIndexOf('.');
        return (slash >= 0 && dot > slash) ? uri.substring(slash + 1, dot) : uri;
    }
}
