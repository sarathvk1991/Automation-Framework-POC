@login
Feature: User Authentication — Login
  As a registered user
  I want to log in with my credentials
  So that I can securely access my account

  Background:
    Given the user is on the login page

  # ── Happy path ──────────────────────────────────────────────────────────────

  @smoke
  Scenario: Successful login with valid credentials
    When the user logs in with username "standard_user" and password "secret_sauce"
    Then the user should land on the dashboard

  # ── Invalid credentials ──────────────────────────────────────────────────────
  # INTENTIONAL FAILURE (Jenkins reporting demo): the expected error_message in
  # the first Examples row below is deliberately wrong, causing that one
  # generated scenario to fail deterministically at the assertEquals in
  # LoginSteps#theLoginErrorShouldBeDisplayed. Revert this row to restore the
  # correct expectation.

  @negative @regression
  Scenario Outline: Login is rejected for invalid credentials
    When the user logs in with username "<username>" and password "<password>"
    Then the login error "<error_message>" should be displayed
    And the user should remain on the login page

    Examples:
      | username      | password     | error_message                                                                  |
      | standard_user | wrong_pass   | INTENTIONAL FAILURE: deliberately wrong expected message for Jenkins demo       |
      | unknown_user  | secret_sauce | Epic sadface: Username and password do not match any user in this service      |
      | standard_user |              | Epic sadface: Password is required                                             |
      |               | secret_sauce | Epic sadface: Username is required                                             |
      |               |              | Epic sadface: Username is required                                             |

  # ── Account lockout ───────────────────────────────────────────────────────────

  @negative @regression
  Scenario: Locked-out user cannot log in
    When the user logs in with username "locked_out_user" and password "secret_sauce"
    Then the login error "Epic sadface: Sorry, this user has been locked out." should be displayed
    And the user should remain on the login page

  # ── Recovery ─────────────────────────────────────────────────────────────────

  @regression
  Scenario: Successful login after a prior failed attempt clears the error
    When the user logs in with username "standard_user" and password "wrong_pass"
    And the user logs in with username "standard_user" and password "secret_sauce"
    Then the user should land on the dashboard
