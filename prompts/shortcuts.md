# Prompt Shortcuts Index

Standardized prompt templates for AI-assisted test automation using GitHub Copilot.

Pseudo slash commands mapped to prompt templates in this project.
Each command maps to a prompt template file — open the template, fill in the placeholders, and paste into Copilot Chat.

---

## When to Use These Commands

Use these commands when working with GitHub Copilot to:

- Generate new test artifacts (feature files, steps, page objects)
- Fix lint, Sonar, or custom QA issues
- Analyze quality reports
- Refactor test automation code
- Debug failures in tests or pipelines

---

## How to Use with GitHub Copilot

1. Open **Copilot Chat** in your editor.
2. Identify the command you need (e.g. `/create-feature`) from the tables below.
3. Open the corresponding template file from the `prompts/` folder.
4. Replace all placeholders such as `{{REQUIREMENT}}`, `{{TARGET_FILE}}`, etc. with your actual values.
5. Paste the filled-in prompt into Copilot Chat and execute.

---

## Generation

| Command | Template |
|---|---|
| `/create-feature` | `prompts/generate/generate-feature.md` |
| `/create-steps` | `prompts/generate/generate-step-definitions.md` |
| `/create-page` | `prompts/generate/generate-page-object.md` |
| `/create-test-data` | `prompts/generate/generate-test-data.md` |

## Fix

| Command | Template |
|---|---|
| `/fix-gherkin` | `prompts/fix/fix-gherkin-lint.md` |
| `/fix-custom-rule` | `prompts/fix/fix-custom-qa-rule.md` |
| `/fix-sonar` | `prompts/fix/fix-sonar-issues.md` |
| `/fix-build` | `prompts/fix/fix-build-error.md` |

## Analysis

| Command | Template |
|---|---|
| `/analyze-gherkin` | `prompts/analysis/analyze-gherkin-lint-report.md` |
| `/analyze-custom-qa` | `prompts/analysis/analyze-custom-qa-report.md` |
| `/analyze-sonar` | `prompts/analysis/analyze-sonar-report.md` |

## Refactor

| Command | Template |
|---|---|
| `/refactor-steps` | `prompts/refactor/refactor-step-definitions.md` |
| `/refactor-page` | `prompts/refactor/refactor-page-object.md` |
| `/refactor-test-data` | `prompts/refactor/refactor-test-data.md` |

## Validate

| Command | Template |
|---|---|
| `/validate-feature` | `prompts/validate/validate-generated-feature.md` |
| `/validate-quality` | `prompts/validate/validate-framework-quality.md` |

## Custom QA

| Command | Template |
|---|---|
| `/create-custom-qa` | `prompts/custom-qa/generate-custom-qa-metrics.md` |
| `/update-custom-qa` | `prompts/custom-qa/update-custom-qa-metrics.md` |
| `/create-custom-config` | `prompts/custom-qa/generate-custom-qa-config.md` |
| `/update-custom-config` | `prompts/custom-qa/update-custom-qa-config.md` |
| `/debug-custom-qa` | `prompts/custom-qa/debug-custom-qa-metrics.md` |

## Debug

| Command | Template |
|---|---|
| `/debug-test` | `prompts/debug/debug-test-failure.md` |
| `/debug-pipeline` | `prompts/debug/debug-pipeline-failure.md` |
| `/debug-sonar` | `prompts/debug/debug-sonar-failure.md` |
| `/debug-flaky` | `prompts/debug/debug-flaky-test.md` |

---

## Usage Example

**Command:** `/create-feature`

**Step 1:** Open `prompts/generate/generate-feature.md`

**Step 2:** Replace placeholders:

```
Requirement:
User can login with valid credentials

Target file:
src/test/resources/features/login.feature
```

**Step 3:** Paste the filled-in template into Copilot Chat and submit.

**Step 4:** Validate output

Depending on the task, run:

- Gherkin lint:
  npm run lint:gherkin:html

- Custom QA metrics:
  npm run custom:qa-metrics

- Maven tests:
  mvn clean verify -Dheadless=true

- Sonar (if configured):
  mvn clean verify -Dheadless=true sonar:sonar

## Pro Tip

Keep prompt templates unchanged.
Always copy the template, fill placeholders, and run it in Copilot Chat.

This ensures consistency and repeatability across the team.

## Why Use Templates

Using standardized prompt templates ensures:
- Consistent code generation
- Reduced prompt variability
- Better Copilot output quality
- Repeatable workflows across the team