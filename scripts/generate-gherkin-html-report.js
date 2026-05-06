const fs = require('fs');

const reportPath = 'gherkin-lint-report.json';
const htmlReportPath = 'gherkin-lint-report.html';

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
    scenarioNamingViolations: 0,
    missingStepsOrEmptyScenarios: 0,
    taggingViolations: 0,
    duplicateScenarios: 0,
    scenarioSizeViolations: 0,
    scenarioLengthTotal: 0,
    scenarioLengthCount: 0
};

function getErrors(file) {
    return Array.isArray(file.errors) ? file.errors : [];
}

function normalize(value) {
    return String(value || '').toLowerCase();
}

function isScenarioNamingViolation(error) {
    const rule = normalize(error.rule);
    const message = normalize(error.message);

    return (
        rule === 'name-length' &&
        message.includes('scenario')
    ) || message.includes('scenario name');
}

function isMissingStepsOrEmptyScenario(error) {
    const rule = normalize(error.rule);
    const message = normalize(error.message);

    return (
        rule.includes('no-empty-scenario') ||
        rule.includes('no-files-without-scenarios') ||
        message.includes('empty scenario') ||
        message.includes('missing step') ||
        message.includes('no steps')
    );
}

function isTaggingViolation(error) {
    const rule = normalize(error.rule);
    const message = normalize(error.message);

    return rule.includes('tag') || message.includes('tag');
}

function isDuplicateScenario(error) {
    const rule = normalize(error.rule);
    const message = normalize(error.message);

    return (
        rule.includes('no-dupe-scenario-names') ||
        rule.includes('duplicate-scenario') ||
        message.includes('duplicate scenario')
    );
}

function isScenarioSizeViolation(error) {
    const rule = normalize(error.rule);

    return rule === 'scenario-size';
}

function extractScenarioLength(error) {
    const message = normalize(error.message);

    const matches = message.match(/\d+/g);
    if (!matches || matches.length === 0) {
        return null;
    }

    return Number(matches[matches.length - 1]);
}

data.forEach(file => {
    const errors = getErrors(file);

    metrics.totalIssues += errors.length;

    if (errors.length === 0) {
        metrics.passedFiles++;
    }

    errors.forEach(error => {
        if (isScenarioNamingViolation(error)) {
            metrics.scenarioNamingViolations++;
        }

        if (isMissingStepsOrEmptyScenario(error)) {
            metrics.missingStepsOrEmptyScenarios++;
        }

        if (isTaggingViolation(error)) {
            metrics.taggingViolations++;
        }

        if (isDuplicateScenario(error)) {
            metrics.duplicateScenarios++;
        }

        if (isScenarioSizeViolation(error)) {
            metrics.scenarioSizeViolations++;

            const scenarioLength = extractScenarioLength(error);
            if (scenarioLength !== null) {
                metrics.scenarioLengthTotal += scenarioLength;
                metrics.scenarioLengthCount++;
            }
        }
    });
});

const compliantFeaturePercentage = metrics.totalFiles === 0
    ? '0.00'
    : ((metrics.passedFiles / metrics.totalFiles) * 100).toFixed(2);

const taggingCompliancePercentage = metrics.totalFiles === 0
    ? '0.00'
    : (((metrics.totalFiles - metrics.taggingViolations) / metrics.totalFiles) * 100).toFixed(2);

const avgScenarioLength = metrics.scenarioLengthCount === 0
    ? 'N/A'
    : (metrics.scenarioLengthTotal / metrics.scenarioLengthCount).toFixed(2);

console.log('Gherkin Lint Metrics:');
console.log(`Lint Violations Count: ${metrics.totalIssues}`);
console.log(`% Compliant Feature Files: ${compliantFeaturePercentage}%`);
console.log(`Scenario Naming Violations: ${metrics.scenarioNamingViolations}`);
console.log(`Missing Steps / Empty Scenarios: ${metrics.missingStepsOrEmptyScenarios}`);
console.log(`Tagging Compliance %: ${taggingCompliancePercentage}%`);
console.log(`Duplicate Scenarios: ${metrics.duplicateScenarios}`);
console.log(`Avg Scenario Length: ${avgScenarioLength}`);
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
    </style>
</head>
<body>
    <h1>Gherkin Lint Report</h1>

    <div class="summary">
        <h2>Summary</h2>
        <p>Total Files Scanned: ${metrics.totalFiles}</p>
        <p>Total Issues Found: <span class="${metrics.totalIssues > 0 ? 'fail' : 'pass'}">${metrics.totalIssues}</span></p>
        <p>% Compliant Feature Files: ${compliantFeaturePercentage}%</p>
        <p>Scenario Naming Violations: ${metrics.scenarioNamingViolations}</p>
        <p>Missing Steps / Empty Scenarios: ${metrics.missingStepsOrEmptyScenarios}</p>
        <p>Tagging Compliance %: ${taggingCompliancePercentage}%</p>
        <p>Duplicate Scenarios: ${metrics.duplicateScenarios}</p>
        <p>Avg Scenario Length: ${avgScenarioLength}</p>
        <p>Scenario Size Violations: ${metrics.scenarioSizeViolations}</p>
        <p>Status: <span class="${metrics.totalIssues > 0 ? 'fail' : 'pass'}">${metrics.totalIssues > 0 ? 'FAILED' : 'PASSED'}</span></p>
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