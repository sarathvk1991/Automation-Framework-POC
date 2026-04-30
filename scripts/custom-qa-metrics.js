const fs = require("fs");
const path = require("path");

const TARGET_DIRS = [
  "src/test/java/com/automation/pages",
  "src/test/java/com/automation/steps",
  "src/test/java/com/automation/utils"
];

const OUTPUT_FILE = "sonar-custom-qa-issues.json";
const LONG_METHOD_THRESHOLD = 40;

const rules = [
  rule("customqa:hardcoded-test-data", "Hardcoded Test Data", "Hardcoded test data should be moved to config/test data files."),
  rule("customqa:repeated-locator", "Repeated Locator", "Repeated locators reduce maintainability."),
  rule("customqa:generic-exception", "Poor Exception Handling", "Generic exception handling should be avoided."),
  rule("customqa:poor-naming", "Poor Naming", "Poor or unclear naming reduces readability."),
  rule("customqa:direct-webdriver-action", "Missing Retry / Wait Mechanism", "Direct WebDriver actions without wait/retry can cause flaky tests."),
  rule("customqa:non-reusable-step", "Non-reusable Step Definition", "Step definitions should reuse page objects/utilities instead of duplicating flow logic."),
  rule("customqa:duplicate-step-definition", "Duplicate Step Definition", "Duplicate or near-duplicate step definitions reduce BDD maintainability."),
  rule("customqa:long-method", "Long Method", "Long methods reduce readability and maintainability.")
];

function rule(id, name, description) {
  return {
    id,
    name,
    description,
    engineId: "custom-qa-metrics",
    cleanCodeAttribute: "CONVENTIONAL",
    type: "CODE_SMELL",
    impacts: [
      {
        softwareQuality: "MAINTAINABILITY",
        severity: "MEDIUM"
      }
    ]
  };
}

function getJavaFiles(dir) {
  if (!fs.existsSync(dir)) return [];

  const result = [];

  fs.readdirSync(dir, { withFileTypes: true }).forEach(entry => {
    const fullPath = path.join(dir, entry.name);

    if (entry.isDirectory()) {
      result.push(...getJavaFiles(fullPath));
    } else if (entry.isFile() && entry.name.endsWith(".java")) {
      result.push(fullPath);
    }
  });

  return result;
}

function issue(ruleId, filePath, line, message) {
  return {
    engineId: "custom-qa-metrics",
    ruleId,
    severity: getSeverity(ruleId),
    type: "CODE_SMELL",
    primaryLocation: {
      message,
      filePath,
      textRange: {
        startLine: line,
        endLine: line,
        startColumn: 1,
        endColumn: 1
      }
    }
  };
}

function getSeverity(ruleId) {
  switch (ruleId) {
    case "customqa:generic-exception":
    case "customqa:long-method":
      return "CRITICAL";

    case "customqa:non-reusable-step":
    case "customqa:direct-webdriver-action":
    case "customqa:duplicate-step-definition":
      return "MAJOR";

    case "customqa:hardcoded-test-data":
    case "customqa:repeated-locator":
      return "MINOR";

    case "customqa:poor-naming":
      return "INFO";

    default:
      return "MAJOR";
  }
}

const issues = [];

const hardcodedPatterns = [
  "standard_user",
  "secret_sauce",
  "Sauce Labs Backpack",
  "Sauce Labs Bike Light",
  "Sarath",
  "Tester",
  "695001"
];

const locatorPatterns = [
  'By.id("user-name")',
  'By.id("password")',
  'By.id("login-button")',
  'By.cssSelector(".shopping_cart_link")',
  'By.id("checkout")',
  'By.id("first-name")'
];

const poorNamingPatterns = [
  /\bdoIt\s*\(/,
  /\babc\s*\(/,
  /\bclick1\s*\(/,
  /\btestThing\s*\(/,
  /\bprocess\s*\(/,
  /\bString\s+x\b/,
  /\bString\s+y\b/,
  /\bString\s+tmp\b/,
  /\blogin_user\b/,
  /\bcart_count\b/,
  /\bvalidate_CART\b/,
  /\bCheckoutNow\s*\(/
];

const cucumberAnnotationPattern = /^\s*@(Given|When|Then|And|But)\s*\("(.+)"\)/;

const allFiles = TARGET_DIRS.flatMap(getJavaFiles);

allFiles.forEach(filePath => {
  const content = fs.readFileSync(filePath, "utf8");
  const lines = content.split(/\r?\n/);

  detectHardcodedTestData(filePath, lines);
  detectRepeatedLocators(filePath, lines);
  detectGenericExceptions(filePath, lines);
  detectPoorNaming(filePath, lines);
  detectDirectWebDriverActions(filePath, lines);
  detectNonReusableSteps(filePath, lines);
  detectDuplicateStepDefinitions(filePath, lines);
  detectLongMethods(filePath, lines);
});

function detectHardcodedTestData(filePath, lines) {
  hardcodedPatterns.forEach(pattern => {
    lines.forEach((line, index) => {
      if (line.includes(pattern)) {
        issues.push(issue(
          "customqa:hardcoded-test-data",
          filePath,
          index + 1,
          `Hardcoded test data found: ${pattern}`
        ));
      }
    });
  });
}

function detectRepeatedLocators(filePath, lines) {
  locatorPatterns.forEach(pattern => {
    lines.forEach((line, index) => {
      if (line.includes(pattern)) {
        issues.push(issue(
          "customqa:repeated-locator",
          filePath,
          index + 1,
          `Repeated locator found: ${pattern}`
        ));
      }
    });
  });
}

function detectGenericExceptions(filePath, lines) {
  lines.forEach((line, index) => {
    if (line.includes("catch (Exception")) {
      issues.push(issue(
        "customqa:generic-exception",
        filePath,
        index + 1,
        "Generic exception handling found."
      ));
    }
  });
}

function detectPoorNaming(filePath, lines) {
  poorNamingPatterns.forEach(pattern => {
    lines.forEach((line, index) => {
      if (pattern.test(line)) {
        issues.push(issue(
          "customqa:poor-naming",
          filePath,
          index + 1,
          "Poor naming convention detected."
        ));
      }
    });
  });
}

function detectDirectWebDriverActions(filePath, lines) {
  lines.forEach((line, index) => {
    if (
      line.includes("driver.findElement") &&
      (
        line.includes(".click()") ||
        line.includes(".sendKeys(") ||
        line.includes(".getText()")
      )
    ) {
      issues.push(issue(
        "customqa:direct-webdriver-action",
        filePath,
        index + 1,
        "Direct WebDriver action without explicit wait/retry detected."
      ));
    }
  });
}

function detectNonReusableSteps(filePath, lines) {
  if (!filePath.includes(`steps${path.sep}`)) {
    return;
  }

  lines.forEach((line, index) => {
    if (
      line.includes("driver.findElement") ||
      line.includes("new Bad") ||
      line.includes("standard_user") ||
      line.includes("secret_sauce") ||
      line.includes("Sauce Labs Backpack") ||
      line.includes("checkout")
    ) {
      issues.push(issue(
        "customqa:non-reusable-step",
        filePath,
        index + 1,
        "Step definition contains non-reusable implementation logic."
      ));
    }
  });
}

function detectDuplicateStepDefinitions(filePath, lines) {
  if (!filePath.includes(`steps${path.sep}`)) {
    return;
  }

  const stepFingerprints = new Map();

  for (let i = 0; i < lines.length; i++) {
    const annotationMatch = lines[i].match(cucumberAnnotationPattern);

    if (!annotationMatch) {
      continue;
    }

    const annotationLine = i + 1;
    const methodStart = findNextMethodStart(lines, i + 1);

    if (methodStart === -1) {
      continue;
    }

    const methodEnd = findMethodEnd(lines, methodStart);

    if (methodEnd === -1) {
      continue;
    }

    const body = lines
      .slice(methodStart, methodEnd + 1)
      .map(normalizeLineForDuplication)
      .filter(Boolean)
      .join("|");

    if (!body) {
      continue;
    }

    if (stepFingerprints.has(body)) {
      issues.push(issue(
        "customqa:duplicate-step-definition",
        filePath,
        annotationLine,
        `Duplicate or near-duplicate step implementation found. Similar implementation first seen at line ${stepFingerprints.get(body)}.`
      ));
    } else {
      stepFingerprints.set(body, annotationLine);
    }
  }
}

function detectLongMethods(filePath, lines) {
  for (let i = 0; i < lines.length; i++) {
    if (!isMethodDeclaration(lines[i])) {
      continue;
    }

    const methodEnd = findMethodEnd(lines, i);

    if (methodEnd === -1) {
      continue;
    }

    const methodLength = methodEnd - i + 1;

    if (methodLength >= LONG_METHOD_THRESHOLD) {
      issues.push(issue(
        "customqa:long-method",
        filePath,
        i + 1,
        `Long method detected. Method length is ${methodLength} lines; threshold is ${LONG_METHOD_THRESHOLD}.`
      ));
    }

    i = methodEnd;
  }
}

function findNextMethodStart(lines, startIndex) {
  for (let i = startIndex; i < lines.length; i++) {
    if (isMethodDeclaration(lines[i])) {
      return i;
    }
  }

  return -1;
}

function isMethodDeclaration(line) {
  return /^\s*(public|private|protected)\s+[\w<>\[\], ?]+\s+\w+\s*\([^)]*\)\s*(throws\s+\w+)?\s*\{?\s*$/.test(line)
    || /^\s*(public|private|protected)\s+void\s+\w+\s*\([^)]*\)\s*(throws\s+\w+)?\s*\{?\s*$/.test(line);
}

function findMethodEnd(lines, methodStartIndex) {
  let braceCount = 0;
  let started = false;

  for (let i = methodStartIndex; i < lines.length; i++) {
    const line = lines[i];

    for (const char of line) {
      if (char === "{") {
        braceCount++;
        started = true;
      } else if (char === "}") {
        braceCount--;
      }
    }

    if (started && braceCount === 0) {
      return i;
    }
  }

  return -1;
}

function normalizeLineForDuplication(line) {
  return line
    .trim()
    .replace(/"[^"]*"/g, "\"VALUE\"")
    .replace(/\d+/g, "NUMBER")
    .replace(/\s+/g, " ")
    .replace(/\/\/.*$/, "");
}

const report = {
  rules,
  issues
};

fs.writeFileSync(OUTPUT_FILE, JSON.stringify(report, null, 2));

console.log(`Custom QA Sonar report generated: ${OUTPUT_FILE}`);
console.log(`Custom QA issues found: ${issues.length}`);

const summary = issues.reduce((acc, item) => {
  acc[item.ruleId] = (acc[item.ruleId] || 0) + 1;
  return acc;
}, {});

console.log("Custom QA issue summary:");
Object.entries(summary).forEach(([ruleId, count]) => {
  console.log(`${ruleId}: ${count}`);
});

if (issues.length > 0) {
  process.exitCode = 0;
}