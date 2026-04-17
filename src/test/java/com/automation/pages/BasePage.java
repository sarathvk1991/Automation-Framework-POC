package com.automation.pages;

import com.automation.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Base class for all Page Objects.
 *
 * Provides a single WebDriverWait instance (timeout from config) and a set of
 * protected helper methods so subclasses never call driver.findElement() directly.
 * Removing direct findElement calls from page classes means waits are applied
 * consistently and element-not-found errors surface with a clear timeout message.
 */
public abstract class BasePage {

    protected final WebDriver driver;
    protected final WebDriverWait wait;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        int explicitWaitSecs = ConfigReader.getInt("explicit.wait", 30);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(explicitWaitSecs));
    }

    // ── Waits ─────────────────────────────────────────────────────────────────

    protected WebElement waitForVisibility(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement waitForClickability(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected void waitForInvisibility(By locator) {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    // ── Actions ───────────────────────────────────────────────────────────────

    /**
     * Clears an input field and types the given text.
     * Using clear() before sendKeys() prevents stale characters from prior test runs.
     */
    protected void type(By locator, String text) {
        WebElement element = waitForVisibility(locator);
        element.clear();
        element.sendKeys(text);
    }

    protected void click(By locator) {
        waitForClickability(locator).click();
    }

    // ── Queries ───────────────────────────────────────────────────────────────

    protected String getText(By locator) {
        return waitForVisibility(locator).getText();
    }

    protected String getAttribute(By locator, String attribute) {
        return waitForVisibility(locator).getAttribute(attribute);
    }

    /**
     * Returns false instead of throwing when the element is absent.
     * Use this only for optional elements; prefer waitForVisibility for required ones.
     */
    protected boolean isDisplayed(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    protected boolean isEnabled(By locator) {
        return waitForVisibility(locator).isEnabled();
    }

    // ── Page contract ─────────────────────────────────────────────────────────

    /**
     * Each page subclass must declare whether the browser is currently on that page.
     * Step definitions can assert isAt() after navigation to catch routing mistakes early.
     */
    public abstract boolean isAt();
}
