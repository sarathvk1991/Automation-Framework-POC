# UI Automation Framework — POC

A production-ready starter framework for web UI automation built with Java, Selenium WebDriver, and Cucumber (BDD).

## Prerequisites

| Tool | Version | Notes |
|---|---|---|
| Java JDK | 21 LTS | `java -version` to confirm |
| Maven | 3.9+ | `mvn -version` to confirm |
| Chrome / Firefox / Edge | Latest | WebDriverManager downloads the matching driver automatically |

## Project Structure

```
src/test/
├── java/com/automation/
│   ├── base/
│   │   ├── BaseTest.java       # @Before/@After — driver init and teardown
│   │   ├── Hooks.java          # @Before/@After — screenshot on failure, metadata logging
│   │   ├── DriverFactory.java  # ThreadLocal WebDriver, browser + headless resolution
│   │   └── Browser.java        # Enum: CHROME | FIREFOX | EDGE
│   ├── pages/
│   │   ├── BasePage.java       # Shared explicit-wait helpers for all page objects
│   │   ├── LoginPage.java      # Login page — locators and actions
│   │   └── DashboardPage.java  # Dashboard page stub
│   ├── runners/
│   │   └── RunCucumberTest.java  # JUnit Platform Suite runner
│   ├── steps/
│   │   └── LoginSteps.java     # Step definitions for login.feature
│   └── utils/
│       └── ConfigReader.java   # Reads config.properties from classpath
└── resources/
    ├── features/
    │   └── login.feature       # BDD scenarios
    ├── config.properties       # Browser, URL, and timeout settings
    └── logback-test.xml        # Console logging configuration
```

## Configuration

All runtime settings live in `src/test/resources/config.properties`:

```properties
browser=chrome          # chrome | firefox | edge
headless=false          # true to run without a visible browser window
base.url=https://example.com
implicit.wait=10        # seconds
explicit.wait=30        # seconds
```

Every property can be overridden at the command line — the JVM system property always takes precedence over the config file.

## Running Tests Locally

### Run all scenarios

```bash
mvn clean test
```

### Run by tag

```bash
# Smoke tests only
mvn test -Dcucumber.filter.tags="@smoke"

# All regression tests
mvn test -Dcucumber.filter.tags="@regression"

# Negative scenarios only
mvn test -Dcucumber.filter.tags="@negative"

# Combined expression
mvn test -Dcucumber.filter.tags="@regression and not @negative"
```

### Run headless (no browser window)

```bash
mvn test -Dheadless=true
```

### Run against a different browser

```bash
mvn test -Dbrowser=firefox
mvn test -Dbrowser=edge
```

### Combine options

```bash
mvn test -Dbrowser=firefox -Dheadless=true -Dcucumber.filter.tags="@smoke"
```

### Rerun only failed scenarios

After a test run, failed scenario URIs are written to `target/cucumber-reports/rerun.txt`. Pass that file back as the feature source to re-execute only failures:

```bash
mvn test -Dcucumber.features="@target/cucumber-reports/rerun.txt"
```

## Test Reports

Reports are generated under `target/cucumber-reports/` after every run:

| File | Format | Use |
|---|---|---|
| `report.html` | HTML | Open in a browser for a full visual report |
| `cucumber.json` | JSON | CI dashboards, Allure, ExtentReports integration |
| `cucumber.xml` | JUnit XML | Jenkins / GitHub Actions / GitLab CI trend graphs |
| `rerun.txt` | Plain text | URIs of failed scenarios for selective rerun |

Screenshots for failed scenarios are saved separately under `target/screenshots/` and are also embedded inline in `report.html`.

### Open the HTML report after a run

```bash
open target/cucumber-reports/report.html        # macOS
start target/cucumber-reports/report.html       # Windows
xdg-open target/cucumber-reports/report.html   # Linux
```

## Tagging Strategy

| Tag | Purpose |
|---|---|
| `@smoke` | Critical happy-path tests — run on every deployment |
| `@regression` | Full regression suite |
| `@negative` | Error and validation scenarios |
| `@wip` | In-progress scenarios — excluded from all runs by default |

Mark any scenario with `@wip` while developing it; it will be automatically excluded until the tag is removed.
