# GitHub Copilot Instructions

This repository is a reusable Java/Selenium/Cucumber/Maven UI automation framework.
It is not tied to any specific application under test (AUT) — keep all generated code AUT-agnostic unless parameters are explicitly passed.

---

## 1. Project Stack

| Layer | Technology |
|---|---|
| Language | Java 21 (latest LTS) |
| Build | Maven |
| Browser automation | Selenium WebDriver + WebDriverManager |
| BDD | Cucumber (Gherkin) |
| Test runner | JUnit Platform (`@Suite`) |
| Linting | gherkin-lint (`.gherkin-lintrc`) |
| Static analysis | SonarQube |
| Custom QA metrics | `scripts/custom-qa-metrics.js` |

---

## 2. Folder Conventions

```
src/test/resources/features/                          # Feature files
src/test/resources/features/bad_examples/             # Anti-pattern examples (do not imitate)
src/test/java/com/automation/pages/                   # Page Object classes
src/test/java/com/automation/pages/badexamples/       # Bad page object examples (do not imitate)
src/test/java/com/automation/steps/                   # Step definitions
src/test/java/com/automation/steps/badexamples/       # Bad step examples (do not imitate)
src/test/java/com/automation/utils/                   # Utilities, config readers, test data
prompts/                                              # Reusable Copilot/AI prompt templates
```

- Files under badexamples/ and bad_examples/ must never be used as a reference for generation
- They are intentionally incorrect and exist only for validation and QA testing purposes

---

## 3. Feature File Rules

- Feature names: ≤ 70 characters
- Scenario names: ≤ 90 characters
- Step text: ≤ 120 characters
- No duplicate scenario names within the same feature file
- No duplicate tags on the same scenario
- Use correct Gherkin indentation (follow `.gherkin-lintrc`)
- Use `Background` only when the steps apply to **all** scenarios in the feature
- Steps must be business-readable and reusable across scenarios
- Do not hardcode application-specific values (URLs, usernames, product names) in step text — pass them as Cucumber parameters or table data instead

---

## 3a. Feature Refactoring Rules

Use `/refactor-feature` when improving Gherkin test design — not for lint violations, build failures, or custom QA issues.

- Do not treat design duplication as a lint issue; use `refactor-feature` for that
- Remove or consolidate duplicated scenarios only when explicitly requested
- Prefer `Scenario Outline` when scenarios differ only by input data
- Do not change step text unless required for reuse or explicitly requested
- Preserve step definition compatibility — all mapped steps must continue to resolve
- Do not add new scenarios unless explicitly instructed
- When the command is /refactor-feature, always prioritize prompts/refactor/refactor-feature.md over other refactor templates.
- Do not use refactor-step-definitions.md or other refactor templates for feature-level changes.

---

## 4. Step Definition Rules

- Step definitions are thin glue code only — no business logic
- **No direct WebDriver calls** in step definitions
- **No `Thread.sleep`** — use explicit waits via `WebDriverWait`
- **No hardcoded test data** — read from `TestData`, `ConfigReader`, or Cucumber parameters
- Delegate all UI interactions to the appropriate page object
- Do not instantiate page objects manually unless the existing project style requires it
- Do not change step binding text (the string in `@Given`/`@When`/`@Then`) unless the linked feature files are explicitly updated at the same time
- Each step definition should map to a single page object method where possible
- Avoid splitting one step across multiple page calls unless necessary

---

## 5. Page Object Rules

- Follow the Page Object Model strictly
- Locators must be declared as `private static final By` constants
- All public methods must represent user actions or readable state checks — not low-level Selenium operations
- Use `WebDriverWait` for all interactions — do not call `driver.findElement(...)` directly in public methods
- Do not repeat the same locator in multiple places
- No hardcoded test data in page objects
- Method names must be business-readable: prefer `loginPage.submitLoginForm()` over `loginPage.clickButton()`
- Avoid unstable locators such as index-based XPath or dynamic attributes unless no alternative exists

---

## 6. Test Data Rules

- Store test data in `TestData` classes, `ConfigReader`, `config.properties`, or as Cucumber `Examples` / `DataTable` parameters
- Never hardcode usernames, passwords, URLs, or product names inline in steps or page objects
- Do not expose sensitive credentials in source files

---

## 7. Custom QA Metrics Rules

- **Do not modify `scripts/custom-qa-metrics.js`** unless explicitly instructed
- Do not weaken or bypass quality rules without explicit instruction
- Maintain output compatibility with SonarQube external issues format (`sonar-custom-qa-issues.json`)
- Do not introduce code patterns that will trigger custom QA violations

---

## 8. Validation Commands

Run these before committing or raising a PR:

```bash
# Lint all feature files and generate HTML report
npm run lint:gherkin:html

# Run custom QA metric checks
npm run custom:qa-metrics

# Full build and test (headless)
mvn clean verify -Dheadless=true

# Full build, test, and SonarQube analysis
mvn clean verify -Dheadless=true sonar:sonar
```

---

## 9. Prompt Templates

Reusable prompt templates live under `prompts/`. Copilot snippets and AI-assisted generation tasks may reference these templates.

- Do not modify files under `prompts/` unless explicitly requested
- Always prefer using existing prompt templates from prompts/ when generating:
  - feature files
  - step definitions
  - page objects
  - test data
- Do not generate these artifacts free-form unless explicitly instructed

---

## General Guidelines

- All generated code must be AUT-agnostic and reusable
- Prefer explicit waits over implicit waits or sleeps
- Keep step definitions, page objects, and feature files in sync — a change to step binding text requires updating the feature file too
- Follow existing naming conventions and package structure in the codebase
- Do not proactively refactor or "improve" existing code unless explicitly instructed
- Do not modify multiple layers (feature, steps, page) unless the task requires it
