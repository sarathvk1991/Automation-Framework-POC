# UI Automation Framework — POC

A production-ready BDD automation framework for web applications, built with Java 21, Selenium WebDriver, and Cucumber. The framework is application-agnostic — the included test suite targets [SauceDemo](https://www.saucedemo.com) as a reference implementation, but every layer is designed to be replaced for any target application.

## Prerequisites

| Tool | Version | Notes |
|---|---|---|
| Java JDK | 21 LTS | `java -version` to confirm |
| Maven | 3.9+ | `mvn -version` to confirm |
| Chrome / Firefox / Edge | Latest | WebDriverManager downloads the matching driver automatically |

---

## Project Structure

```
src/test/
├── java/com/automation/
│   ├── base/
│   │   ├── BaseTest.java              # @Before/@After — driver init and teardown per scenario
│   │   ├── Hooks.java                 # @Before/@After — screenshot on failure, metadata logging
│   │   ├── DriverFactory.java         # ThreadLocal WebDriver; resolves browser + headless from config/JVM
│   │   └── Browser.java               # Enum: CHROME | FIREFOX | EDGE
│   ├── pages/
│   │   ├── BasePage.java              # Shared explicit-wait helpers (click, type, getText, isDisplayed…)
│   │   ├── LoginPage.java             # Login page — locators, login(), loginExpectingFailure()
│   │   ├── DashboardPage.java         # Landing page stub — isAt() check after login
│   │   ├── InventoryPage.java         # Product list — sort, add to cart, cart badge, cart nav
│   │   ├── CartPage.java              # Cart page — item list, presence check, proceed to checkout
│   │   ├── CheckoutPage.java          # Checkout step 1 — form fill, continue, error message
│   │   └── CheckoutOverviewPage.java  # Checkout step 2 + confirmation — total, finish, success check
│   ├── runners/
│   │   └── RunCucumberTest.java       # JUnit Platform Suite runner
│   ├── steps/
│   │   ├── LoginSteps.java            # Step definitions for login.feature
│   │   ├── InventoryStepDefinitions.java   # Step definitions for inventory.feature
│   │   ├── AddToCartStepDefinitions.java   # Step definitions for add_to_cart.feature
│   │   └── CheckoutStepDefinitions.java    # Step definitions for checkout.feature
│   └── utils/
│       └── ConfigReader.java          # Loads config.properties from classpath
└── resources/
    ├── features/
    │   ├── login.feature              # User authentication scenarios
    │   ├── inventory.feature          # Product listing and sorting scenarios
    │   ├── add_to_cart.feature        # Add to cart scenarios
    │   └── checkout.feature           # End-to-end checkout scenarios
    ├── config.properties              # Browser, base URL, and timeout settings
    └── logback-test.xml               # Console logging configuration
```

---

## Current Test Coverage (SauceDemo reference suite)

### Login (`login.feature`) — 8 scenarios
| Scenario | Tags |
|---|---|
| Successful login with valid credentials | `@smoke` |
| Login rejected — wrong password | `@negative @regression` |
| Login rejected — unknown user | `@negative @regression` |
| Login rejected — missing password | `@negative @regression` |
| Login rejected — missing username | `@negative @regression` |
| Login rejected — empty credentials | `@negative @regression` |
| Locked-out user cannot log in | `@negative @regression` |
| Successful login after prior failure clears error | `@regression` |

### Inventory (`inventory.feature`) — 3 scenarios
| Scenario | Tags |
|---|---|
| Product inventory is displayed after login | `@smoke` |
| Products sorted by price low to high | `@regression` |
| Products sorted high to low are not in ascending order | `@negative @regression` |

### Add to Cart (`add_to_cart.feature`) — 3 scenarios
| Scenario | Tags |
|---|---|
| Add a single product to the cart | `@smoke` |
| Add multiple products to the cart | `@regression` |
| Cart is empty when no products added | `@negative @regression` |

### Checkout (`checkout.feature`) — 3 scenarios
| Scenario | Tags |
|---|---|
| Complete checkout with a single item | `@smoke` |
| Complete checkout with multiple items | `@regression` |
| Checkout fails when required fields are missing | `@negative @regression` |

**Total: 17 scenarios across 4 features.**

---

## Configuration

All runtime settings live in `src/test/resources/config.properties`:

```properties
browser=chrome                      # chrome | firefox | edge
headless=false                      # true to run without a visible browser window
base.url=https://www.saucedemo.com  # entry point — the URL opened before every scenario
implicit.wait=10                    # seconds
explicit.wait=30                    # seconds
```

Every property can be overridden at the command line — the JVM system property always takes precedence over the config file.

---

## Running Tests

### Run all scenarios

```bash
mvn clean test
```

### Run headless (no browser window)

```bash
mvn test -Dheadless=true
```

### Run by tag

```bash
# Smoke tests only
mvn test -Dcucumber.filter.tags="@smoke"

# Full regression suite
mvn test -Dcucumber.filter.tags="@regression"

# Negative scenarios only
mvn test -Dcucumber.filter.tags="@negative"

# Combined expression
mvn test -Dcucumber.filter.tags="@regression and not @negative"

# Single feature
mvn test -Dcucumber.filter.tags="@checkout"
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

After a test run, failed scenario URIs are written to `target/cucumber-reports/rerun.txt`. Pass that file back to re-execute only failures:

```bash
mvn test -Dcucumber.features="@target/cucumber-reports/rerun.txt"
```

---

## Test Reports

Reports are generated under `target/cucumber-reports/` after every run:

| File | Format | Use |
|---|---|---|
| `report.html` | HTML | Open in a browser for a full visual report |
| `cucumber.json` | JSON | CI dashboards, Allure, ExtentReports integration |
| `cucumber.xml` | JUnit XML | Jenkins / GitHub Actions / GitLab CI trend graphs |
| `rerun.txt` | Plain text | URIs of failed scenarios for selective rerun |

Screenshots for failed scenarios are saved under `target/screenshots/` and embedded inline in `report.html`.

### Open the HTML report after a run

```bash
open target/cucumber-reports/report.html        # macOS
start target/cucumber-reports/report.html       # Windows
xdg-open target/cucumber-reports/report.html   # Linux
```

---

## Tagging Strategy

| Tag | Purpose |
|---|---|
| `@smoke` | Critical happy-path tests — run on every deployment |
| `@regression` | Full regression suite |
| `@negative` | Error and validation scenarios |
| `@login` / `@inventory` / `@cart` / `@checkout` | Feature-level isolation |
| `@wip` | In-progress scenarios — excluded from all runs by default |

Mark any scenario with `@wip` while developing; it is automatically excluded until the tag is removed.

---

## Adapting the Framework to a Different Application

The framework layer (`base/`, `utils/`, runner, config) is fully reusable. Only the application-specific layer (page objects, step definitions, feature files) needs to change. Follow the steps below to point the suite at a new target application.

### 1. Update `config.properties`

Change `base.url` to the entry point of your application:

```properties
base.url=https://your-application.com
```

All other properties (`browser`, `headless`, timeouts) work as-is.

### 2. Replace or update Page Objects (`src/test/java/.../pages/`)

Each page object models one screen of the application under test. For a new application:

- **Keep** `BasePage.java` — it contains no application-specific code.
- **Replace** every other page class with ones that reflect your application's pages.
- Identify the correct locators (`By.id`, `By.cssSelector`, `By.xpath`) from your application's DOM.
- Follow the same pattern: `private static final By` fields, intent-revealing public methods, extend `BasePage`.

```java
// Example skeleton for a new page
public class MyPage extends BasePage {

    private static final By SOME_BUTTON = By.id("submit-btn");
    private static final By RESULT_TEXT = By.cssSelector(".result-message");

    public MyPage(WebDriver driver) { super(driver); }

    @Override
    public boolean isAt() {
        try { waitForVisibility(RESULT_TEXT); return true; }
        catch (TimeoutException e) { return false; }
    }

    public void clickSubmit() { click(SOME_BUTTON); }
    public String getResult() { return getText(RESULT_TEXT); }
}
```

### 3. Replace Feature Files (`src/test/resources/features/`)

Write Gherkin scenarios that describe your application's behaviour. The background, steps, and tags are entirely free-form — there are no reserved step texts in the framework.

```gherkin
@myfeature
Feature: My Application Flow
  Background:
    Given the user is on the home page

  @smoke
  Scenario: User completes the main flow
    When the user submits the form with "valid data"
    Then the confirmation message should be displayed
```

### 4. Replace Step Definitions (`src/test/java/.../steps/`)

Create step definition classes in the `com.automation.steps` package (already on the Cucumber glue path). Use `DriverFactory.getDriver()` to obtain the driver — never inject it directly.

```java
public class MyStepDefinitions {

    private MyPage myPage;

    @Given("the user is on the home page")
    public void theUserIsOnTheHomePage() {
        myPage = new MyPage(DriverFactory.getDriver());
        Assertions.assertTrue(myPage.isAt());
    }

    @When("the user submits the form with {string}")
    public void theUserSubmitsTheForm(String data) {
        myPage.fillForm(data);
        myPage.clickSubmit();
    }

    @Then("the confirmation message should be displayed")
    public void theConfirmationMessageShouldBeDisplayed() {
        Assertions.assertFalse(myPage.getResult().isBlank());
    }
}
```

### 5. Review Headless Click Behaviour

If your application renders pages where action buttons fall below the default headless Chrome viewport (~800 × 600), a plain `click()` can silently miss. Scroll the element into view first:

```java
WebElement btn = waitForClickability(BUTTON_LOCATOR);
((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", btn);
btn.click();
```

### Quick Checklist

| Step | File(s) to change |
|---|---|
| Point at the new site | `config.properties` → `base.url` |
| Model each page | Add/replace classes in `pages/` extending `BasePage` |
| Describe behaviour | Add `.feature` files in `resources/features/` |
| Implement steps | Add classes in `steps/` using `DriverFactory.getDriver()` |
| Adjust credentials / test data | Feature file `Examples` tables or a dedicated data file |
| Add feature-level tag | `@Feature` on each `.feature` file |

No changes are required to `base/`, `utils/`, `RunCucumberTest`, `pom.xml`, or `logback-test.xml`.

---

## Design Decisions

### Page Object Model
Every page is a class that extends `BasePage`. Locators are `private static final By` fields. Methods expose intent (`loginPage.login(user, pass)`) rather than raw WebDriver calls. Page classes hold no test state — they are stateless wrappers around the driver.

### ThreadLocal WebDriver
`DriverFactory` stores the `WebDriver` in a `ThreadLocal`. Each Cucumber scenario gets its own browser instance, enabling safe parallel execution without synchronisation overhead.

### Hooks over JUnit lifecycle
`BaseTest` uses Cucumber `@Before`/`@After` hooks (not JUnit `@BeforeEach`) so the driver is initialised before the first step and torn down after the last, regardless of runner configuration.

### Explicit waits only
All element interactions go through `BasePage` helpers backed by `WebDriverWait`. There is no `Thread.sleep` anywhere in the framework. Timeouts are externalised to `config.properties` (`explicit.wait`, `implicit.wait`).

### Step definition state without shared context
Cucumber creates one instance of each step definition class per scenario. Page objects are instantiated inside the step that first needs them (lazy init) rather than passed between classes, avoiding the need for a DI container such as PicoContainer.
