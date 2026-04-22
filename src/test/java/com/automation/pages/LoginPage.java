package com.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page Object for the Login page.
 *
 * Rules enforced here:
 *  - Locators are private static final — they are fixed selectors, not per-instance state.
 *  - No WebDriver or WebDriverWait calls appear directly; all interactions go through BasePage helpers.
 *  - Action methods return page objects, not void, so callers can express a navigation chain
 *    (e.g. loginPage.enterUsername(u).enterPassword(p).clickLogin()).
 *  - Two login paths exist: one that expects success (returns DashboardPage) and one that
 *    expects failure (stays on LoginPage), preventing the caller from asserting on the wrong page.
 */
public class LoginPage extends BasePage {

    // ── Locators ──────────────────────────────────────────────────────────────
    // Static because selectors are fixed per build; final because they must never change at runtime.
    // Update these to match the actual application under test.

    private static final By USERNAME_FIELD = By.id("user-name");
    private static final By PASSWORD_FIELD = By.id("password");
    private static final By LOGIN_BUTTON   = By.id("login-button");
    private static final By ERROR_MESSAGE  = By.cssSelector("[data-test='error']");
    private static final By PAGE_HEADING   = By.cssSelector(".login_logo");

    // ── Constructor ───────────────────────────────────────────────────────────

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    // ── Page contract ─────────────────────────────────────────────────────────

    /**
     * Confirms the browser is on the login page by checking for the page heading.
     * Call this at the start of a scenario before interacting with any element.
     */
    @Override
    public boolean isAt() {
        return isDisplayed(PAGE_HEADING);
    }

    // ── Actions ───────────────────────────────────────────────────────────────

    /**
     * Types into the username field after clearing any pre-filled value.
     * Returns {@code this} to allow fluent chaining with enterPassword().
     */
    public LoginPage enterUsername(String username) {
        type(USERNAME_FIELD, username);
        return this;
    }

    /**
     * Types into the password field after clearing any pre-filled value.
     * Returns {@code this} to allow fluent chaining with clickLogin().
     */
    public LoginPage enterPassword(String password) {
        type(PASSWORD_FIELD, password);
        return this;
    }

    /**
     * Clicks the login button and returns the next page.
     * Use this when valid credentials are supplied and the test expects success.
     */
    public DashboardPage clickLogin() {
        click(LOGIN_BUTTON);
        return new DashboardPage(driver);
    }

    /**
     * Full happy-path login in a single call.
     *
     * <pre>
     *     DashboardPage dashboard = loginPage.login("admin", "secret");
     * </pre>
     */
    public DashboardPage login(String username, String password) {
        return enterUsername(username)
                .enterPassword(password)
                .clickLogin();
    }

    /**
     * Attempts login with credentials expected to be invalid.
     * Stays on LoginPage so the caller can assert on the error message
     * without risking a ClassCastException from the wrong page object.
     *
     * <pre>
     *     loginPage.loginExpectingFailure("bad", "creds")
     *              .assertErrorMessage("Invalid username or password.");
     * </pre>
     */
    public LoginPage loginExpectingFailure(String username, String password) {
        type(USERNAME_FIELD, username);
        type(PASSWORD_FIELD, password);
        click(LOGIN_BUTTON);
        return this;
    }

    // ── Queries ───────────────────────────────────────────────────────────────

    public String getErrorMessage() {
        return getText(ERROR_MESSAGE);
    }

    public boolean isErrorDisplayed() {
        return isDisplayed(ERROR_MESSAGE);
    }

    public boolean isLoginButtonEnabled() {
        return isEnabled(LOGIN_BUTTON);
    }
}
