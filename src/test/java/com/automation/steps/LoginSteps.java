package com.automation.steps;

import com.automation.base.DriverFactory;
import com.automation.pages.DashboardPage;
import com.automation.pages.LoginPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;

public class LoginSteps {

    private LoginPage loginPage;

    // ── Given ─────────────────────────────────────────────────────────────────

    @Given("the user is on the login page")
    public void theUserIsOnTheLoginPage() {
        // BaseTest.setUp() already navigated to base.url before this step runs.
        loginPage = new LoginPage(DriverFactory.getDriver());
    }

    @Given("the user has failed to log in {int} consecutive times with username {string}")
    public void theUserHasFailedToLogIn(int times, String username) {
        // Submits the form with a known-bad password to build up the failure count.
        for (int i = 0; i < times; i++) {
            loginPage.loginExpectingFailure(username, "WrongPass@123");
        }
    }

    // ── When ──────────────────────────────────────────────────────────────────

    /**
     * Single step for any login attempt — success or failure.
     * The return value of clickLogin() is intentionally discarded here;
     * the outcome is verified by the subsequent @Then step.
     */
    @When("the user logs in with username {string} and password {string}")
    public void theUserLogsIn(String username, String password) {
        loginPage.enterUsername(username)
                 .enterPassword(password)
                 .clickLogin();
    }

    @When("the user clicks the {string} link")
    public void theUserClicksLink(String linkText) {
        // Extend this switch when more navigational links are added.
        switch (linkText) {
            case "Forgot password?" -> loginPage.clickForgotPassword();
            default -> throw new IllegalArgumentException("No action mapped for link: " + linkText);
        }
    }

    // ── Then ──────────────────────────────────────────────────────────────────

    @Then("the user should land on the dashboard")
    public void theUserShouldLandOnTheDashboard() {
        DashboardPage dashboardPage = new DashboardPage(DriverFactory.getDriver());
        Assertions.assertTrue(dashboardPage.isAt(),
                "Expected to land on dashboard page, but the heading was not found.");
    }

    @Then("the login error {string} should be displayed")
    public void theLoginErrorShouldBeDisplayed(String expectedError) {
        Assertions.assertTrue(loginPage.isErrorDisplayed(),
                "Expected a login error message to be visible.");
        Assertions.assertEquals(expectedError, loginPage.getErrorMessage(),
                "Login error message text did not match.");
    }

    @Then("the user should remain on the login page")
    public void theUserShouldRemainOnTheLoginPage() {
        Assertions.assertTrue(loginPage.isAt(),
                "Expected to remain on the login page after a failed login attempt.");
    }

    @Then("the login button should be disabled")
    public void theLoginButtonShouldBeDisabled() {
        Assertions.assertFalse(loginPage.isLoginButtonEnabled(),
                "Expected the login button to be disabled when credentials are empty.");
    }

    @Then("the user should be on the forgot password page")
    public void theUserShouldBeOnTheForgotPasswordPage() {
        // TODO: replace with ForgotPasswordPage.isAt() once ForgotPasswordPage is created.
        String currentUrl = DriverFactory.getDriver().getCurrentUrl();
        Assertions.assertTrue(currentUrl.contains("forgot-password"),
                "Expected URL to contain 'forgot-password' but was: " + currentUrl);
    }
}
