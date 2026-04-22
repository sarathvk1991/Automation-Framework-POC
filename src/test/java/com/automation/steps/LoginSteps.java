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

}
