package com.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;

/**
 * Page Object for the Dashboard — the landing page after a successful login.
 *
 * Stub: add locators and actions as the test suite grows.
 * Its primary role right now is to be the correct return type of LoginPage.clickLogin(),
 * so step definitions can assert isAt() immediately after login without casting.
 */
public class DashboardPage extends BasePage {

    private static final By PAGE_HEADING = By.cssSelector("span.title");

    public DashboardPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Waits up to explicit.wait seconds for the dashboard heading to appear.
     * Returns false (rather than throwing) so callers can use assertTrue(isAt(), message).
     */
    @Override
    public boolean isAt() {
        try {
            waitForVisibility(PAGE_HEADING);
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }
}
