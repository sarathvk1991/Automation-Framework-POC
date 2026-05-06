const fs = require('fs');
const path = require('path');

const reportPath = 'gherkin-lint-report.json';
const htmlReportPath = 'gherkin-lint-report.html';
const featureRootPath = 'src/test/resources/features';

if (!fs.existsSync(reportPath)) {
    console.error('gherkin-lint-report.json not found');
    process.exit(1);
}

const rawReport = fs.readFileSync(reportPath, 'utf8').trim();
const data = rawReport ? JSON.parse(rawReport) : [];

const metrics = {
    totalFiles: data.length,
    passedFiles: 0,
    totalIssues: 0,

    featureNamingViolations: 0,
    scenarioNamingViolations: 0,
    missingStepsOrEmptyScenarios: 0,
    taggingViolations: 0,
    duplicateScenarios: 0,
    formattingViolations: 0,
    structureViolations: 0,
    scenarioSizeViolations: 0,

    totalScenarios: 0,
    totalScenarioSteps: 0,
    maxScenarioLength: 0
};

function getErrors(file) {
    return Array.isArray(file.errors) ? file.errors : [];
}

function normalize(value) {
    return String(value || '').toLowerCase();
}

function isFeatureNamingViolation(error) {
    const rule = normalize(error.rule);
    const message = normalize(error.message);

    return (
        rule === 'no-unnamed-features' ||
        (rule === 'name-length' && message.includes('feature name'))
    );
}

function isScenarioNamingViolation(error) {
    const rule = normalize(error.rule);
    const message = normalize(error.message);

    return (
        rule === 'no-unnamed-scenarios' ||
        rule === 'no-dupe-scenario-names' ||
        (rule === 'name-length' && message.includes('scenario name'))
    );
}

function isMissingStepsOrEmptyScenario(error) {
    const rule = normalize(error.rule);
    const message = normalize(error.message);

    return (
        rule === 'no-empty-file' ||
        rule === 'no-files-without-scenarios' ||
        rule === 'no-empty-scenarios' ||
        rule === 'no-scenario-outlines-without-examples' ||
        message.includes('empty scenario') ||
        message.includes('missing step') ||
        message.includes('no steps')
    );
}

function isTaggingViolation(error) {
    const rule = normalize(error.rule);
    const message = normalize(error.message);

    return (
        rule === 'no-duplicate-tags' ||
        rule === 'no-homogenous-tags' ||
        rule === 'no-partially-commented-tag-lines' ||
        rule.includes('tag') ||
        message.includes('tag')
    );
}

function isDuplicateScenario(error) {
    return normalize(error.rule) === 'no-dupe-scenario-names';
}

function isFormattingViolation(error) {
    const rule = normalize(error.rule);

    return (
        rule === 'indentation' ||
        rule === 'no-multiple-empty-lines' ||
        rule === 'no-trailing-spaces' ||
        rule === 'new-line-at-eof'
    );
}

function isStructureViolation(error) {
    const rule = normalize(error.rule);

    return (
        rule === 'one-feature-per-file' ||
        rule === 'up-to-one-background-per-file'
    );
}

function isScenarioSizeViolation(error) {
    return normalize(error.rule) === 'scenario-size';
}

function getFeatureFiles(directory) {
    if (!fs.existsSync(directory)) {
        return [];
    }

    const entries = fs.readdirSync(directory, { withFileTypes: true });
    const files = [];

    entries.forEach(entry => {
        const fullPath = path.join(directory, entry.name);

        if (entry.isDirectory()) {
            files.push(...getFeatureFiles(fullPath));
        } else if (entry.isFile() && entry.name.endsWith('.feature')) {
            files.push(fullPath);
        }
    });

    return files;
}

function isScenarioLine(line) {
    const trimmed = line.trim();

    return (
        trimmed.startsWith('Scenario:') ||
        trimmed.startsWith('Scenario Outline:')
    );
}

function isStepLine(line) {
    const trimmed = line.trim();

    return /^(Given|When|Then|And|But)\b/.test(trimmed);
}

function isNonScenarioSection(line) {
    const trimmed = line.trim();

    return (
        trimmed.startsWith('Feature:') ||
        trimmed.startsWith('Background:') ||
        trimmed.startsWith('Examples:')
    );
}

function recordScenarioLength(stepCount) {
    metrics.totalScenarios += 1;
    metrics.totalScenarioSteps += stepCount;

    if (stepCount > metrics.maxScenarioLength) {
        metrics.maxScenarioLength = stepCount;
    }
}

function calculateScenarioLengthMetrics() {
    const featureFiles = getFeatureFiles(featureRootPath);

    featureFiles.forEach(filePath => {
        const content = fs.readFileSync(filePath, 'utf8');
        const lines = content.split(/\r?\n/);

        let insideScenario = false;
        let currentScenarioStepCount = 0;

        lines.forEach(line => {
            if (isScenarioLine(line)) {
                if (insideScenario) {
                    recordScenarioLength(currentScenarioStepCount);
                }

                insideScenario = true;
                currentScenarioStepCount = 0;
                return;
            }

            if (insideScenario && isNonScenarioSection(line)) {
                recordScenarioLength(currentScenarioStepCount);
                insideScenario = false;
                currentScenarioStepCount = 0;
                return;
            }

            if (insideScenario && isStepLine(line)) {
                currentScenarioStepCount += 1;
            }
        });

        if (insideScenario) {
            recordScenarioLength(currentScenarioStepCount);
        }
    });
}

data.forEach(file => {
    const errors = getErrors(file);

    metrics.totalIssues += errors.length;

    if (errors.length === 0) {
        metrics.passedFiles += 1;
    }

    errors.forEach(error => {
        if (isFeatureNamingViolation(error)) {
            metrics.featureNamingViolations += 1;
        }

        if (isScenarioNamingViolation(error)) {
            metrics.scenarioNamingViolations += 1;
        }

        if (isMissingStepsOrEmptyScenario(error)) {
            metrics.missingStepsOrEmptyScenarios += 1;
        }

        if (isTaggingViolation(error)) {
            metrics.taggingViolations += 1;
        }

        if (isDuplicateScenario(error)) {
            metrics.duplicateScenarios += 1;
        }

        if (isFormattingViolation(error)) {
            metrics.formattingViolations += 1;
        }

        if (isStructureViolation(error)) {
            metrics.structureViolations += 1;
        }

        if (isScenarioSizeViolation(error)) {
            metrics.scenarioSizeViolations += 1;
        }
    });
});

calculateScenarioLengthMetrics();

const compliantFeaturePercentage = metrics.totalFiles === 0
    ? '0.00'
    : ((metrics.passedFiles / metrics.totalFiles) * 100).toFixed(2);

const taggingCompliancePercentage = metrics.totalFiles === 0
    ? '0.00'
    : (((metrics.totalFiles - metrics.taggingViolations) / metrics.totalFiles) * 100).toFixed(2);

const avgScenarioLength = metrics.totalScenarios === 0
    ? '0.00'
    : (metrics.totalScenarioSteps / metrics.totalScenarios).toFixed(2);

console.log('Gherkin Lint Metrics:');
console.log(`Lint Violations Count: ${metrics.totalIssues}`);
console.log(`% Compliant Feature Files: ${compliantFeaturePercentage}%`);
console.log(`Feature Naming Violations: ${metrics.featureNamingViolations}`);
console.log(`Scenario Naming Violations: ${metrics.scenarioNamingViolations}`);
console.log(`Missing Steps / Empty Scenarios: ${metrics.missingStepsOrEmptyScenarios}`);
console.log(`Tagging Compliance %: ${taggingCompliancePercentage}%`);
console.log(`Tagging Violations: ${metrics.taggingViolations}`);
console.log(`Duplicate Scenarios: ${metrics.duplicateScenarios}`);
console.log(`Formatting Violations: ${metrics.formattingViolations}`);
console.log(`Structure Violations: ${metrics.structureViolations}`);
console.log(`Total Scenarios: ${metrics.totalScenarios}`);
console.log(`Avg Scenario Length: ${avgScenarioLength} steps`);
console.log(`Max Scenario Length: ${metrics.maxScenarioLength} steps`);
console.log(`Scenario Size Violations: ${metrics.scenarioSizeViolations}`);

let html = `
<!DOCTYPE html>
<html>
<head>
    <title>Gherkin Lint Report</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 24px;
            background: #f7f9fb;
            color: #222;
        }
        h1 {
            color: #1f2937;
        }
        .summary {
            padding: 16px;
            background: #ffffff;
            border-radius: 8px;
            margin-bottom: 24px;
            box-shadow: 0 1px 4px rgba(0,0,0,0.08);
        }
        .file {
            background: #ffffff;
            border-radius: 8px;
            padding: 16px;
            margin-bottom: 16px;
            box-shadow: 0 1px 4px rgba(0,0,0,0.08);
        }
        .file h2 {
            font-size: 18px;
            margin-bottom: 12px;
            color: #111827;
        }
        .pass {
            color: #15803d;
            font-weight: bold;
        }
        .fail {
            color: #dc2626;
            font-weight: bold;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 8px;
        }
        th, td {
            text-align: left;
            border-bottom: 1px solid #e5e7eb;
            padding: 8px;
        }
        th {
            background: #f3f4f6;
        }
        .metric-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(260px, 1fr));
            gap: 8px 24px;
        }
        .metric-grid p {
            margin: 4px 0;
        }
    </style>
</head>
<body>
    <h1>Gherkin Lint Report</h1>

    <div class="summary">
        <h2>Summary</h2>
        <div class="metric-grid">
            <p>Total Files Scanned: ${metrics.totalFiles}</p>
            <p>Total Issues Found: <span class="${metrics.totalIssues > 0 ? 'fail' : 'pass'}">${metrics.totalIssues}</span></p>
            <p>% Compliant Feature Files: ${compliantFeaturePercentage}%</p>
            <p>Feature Naming Violations: ${metrics.featureNamingViolations}</p>
            <p>Scenario Naming Violations: ${metrics.scenarioNamingViolations}</p>
            <p>Missing Steps / Empty Scenarios: ${metrics.missingStepsOrEmptyScenarios}</p>
            <p>Tagging Compliance %: ${taggingCompliancePercentage}%</p>
            <p>Tagging Violations: ${metrics.taggingViolations}</p>
            <p>Duplicate Scenarios: ${metrics.duplicateScenarios}</p>
            <p>Formatting Violations: ${metrics.formattingViolations}</p>
            <p>Structure Violations: ${metrics.structureViolations}</p>
            <p>Total Scenarios: ${metrics.totalScenarios}</p>
            <p>Avg Scenario Length: ${avgScenarioLength} steps</p>
            <p>Max Scenario Length: ${metrics.maxScenarioLength} steps</p>
            <p>Scenario Size Violations: ${metrics.scenarioSizeViolations}</p>
            <p>Status: <span class="${metrics.totalIssues > 0 ? 'fail' : 'pass'}">${metrics.totalIssues > 0 ? 'FAILED' : 'PASSED'}</span></p>
        </div>
    </div>
`;

data.forEach(file => {
    const errors = getErrors(file);

    html += `
    <div class="file">
        <h2>${file.filePath}</h2>
    `;

    if (errors.length === 0) {
        html += `<p class="pass">No issues found</p>`;
    } else {
        html += `
        <table>
            <thead>
                <tr>
                    <th>Line</th>
                    <th>Rule</th>
                    <th>Message</th>
                </tr>
            </thead>
            <tbody>
        `;

        errors.forEach(error => {
            html += `
                <tr>
                    <td>${error.line || '-'}</td>
                    <td>${error.rule || '-'}</td>
                    <td>${error.message || '-'}</td>
                </tr>
            `;
        });

        html += `
            </tbody>
        </table>
        `;
    }

    html += `</div>`;
});

html += `
</body>
</html>
`;

fs.writeFileSync(htmlReportPath, html);

console.log(`HTML report generated: ${htmlReportPath}`);

if (metrics.totalIssues > 0) {
    process.exit(1);
}