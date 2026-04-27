package com.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class CheckoutOverviewPage extends BasePage {

    private static final By PAGE_TITLE     = By.cssSelector("span.title");
    private static final By TOTAL_LABEL    = By.cssSelector(".summary_total_label");
    private static final By FINISH_BUTTON  = By.cssSelector("[data-test='finish']");
    // Confirmation page (/checkout-complete.html) uses the CSS class; no data-test on this element
    private static final By SUCCESS_HEADER = By.cssSelector(".complete-header");

    public CheckoutOverviewPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isAt() {
        try {
            waitForVisibility(PAGE_TITLE);
            return getText(PAGE_TITLE).contains("Overview");
        } catch (TimeoutException e) {
            return false;
        }
    }

    // ── Actions ───────────────────────────────────────────────────────────────

    /**
     * Scrolls the Finish button into view and clicks it.
     * The overview page can be taller than the headless viewport, leaving the button
     * below the fold. A plain click() finds the element (isDisplayed is true off-screen)
     * but fails to register in headless Chrome — scrollIntoView fixes this.
     */
    public void clickFinish() {
        WebElement btn = waitForClickability(FINISH_BUTTON);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView({block:'center'});", btn);
        js.executeScript("arguments[0].click();", btn);
    }

    // ── Queries ───────────────────────────────────────────────────────────────

    /**
     * Returns the full total label text, e.g. "Total: $32.39".
     * Waits for the element to be visible so it is safe to call immediately
     * after the overview page loads.
     */
    public String getTotalAmount() {
        return getText(TOTAL_LABEL);
    }

    /**
     * Returns true when the order confirmation header is visible.
     * Must be called after {@link #clickFinish()} has navigated to checkout-complete.html.
     */
    public boolean isOrderSuccessful() {
        try {
            waitForVisibility(SUCCESS_HEADER);
            return isDisplayed(SUCCESS_HEADER);
        } catch (TimeoutException e) {
            return false;
        }
    }
}
