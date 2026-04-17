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
    When the user logs in with username "john.doe@example.com" and password "SecurePass@123"
    Then the user should land on the dashboard

  # ── Field validation ─────────────────────────────────────────────────────────

  @regression
  Scenario: Login button is disabled when both fields are empty
    Then the login button should be disabled

  # ── Invalid credentials ──────────────────────────────────────────────────────

  @negative @regression
  Scenario Outline: Login is rejected for invalid credentials
    When the user logs in with username "<username>" and password "<password>"
    Then the login error "<error_message>" should be displayed
    And the user should remain on the login page

    Examples:
      | username              | password        | error_message                    |
      | john.doe@example.com  | WrongPass@123   | Invalid username or password.    |
      | unknown@example.com   | SecurePass@123  | Invalid username or password.    |
      | john.doe@example.com  |                 | Password is required.            |
      |                       | SecurePass@123  | Username is required.            |
      |                       |                 | Username is required.            |

  # ── Account lockout ───────────────────────────────────────────────────────────

  @negative @regression
  Scenario: Account is locked after repeated failed login attempts
    Given the user has failed to log in 4 consecutive times with username "john.doe@example.com"
    When the user logs in with username "john.doe@example.com" and password "WrongPass@123"
    Then the login error "Your account has been locked. Please contact support." should be displayed

  # ── Navigation ───────────────────────────────────────────────────────────────

  @regression
  Scenario: User navigates to the forgot password page
    When the user clicks the "Forgot password?" link
    Then the user should be on the forgot password page

  # ── Recovery ─────────────────────────────────────────────────────────────────

  @regression
  Scenario: Successful login after a prior failed attempt clears the error
    When the user logs in with username "john.doe@example.com" and password "WrongPass@123"
    And the user logs in with username "john.doe@example.com" and password "SecurePass@123"
    Then the user should land on the dashboard
