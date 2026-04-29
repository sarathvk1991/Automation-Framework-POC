const fs = require('fs');

const reportPath = 'gherkin-lint-report.json';
const htmlReportPath = 'gherkin-lint-report.html';

if (!fs.existsSync(reportPath)) {
    console.error('gherkin-lint-report.json not found');
    process.exit(1);
}

const rawReport = fs.readFileSync(reportPath, 'utf8').trim();
const data = rawReport ? JSON.parse(rawReport) : [];

let totalIssues = 0;

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
        .issue {
            border-left: 4px solid #dc2626;
            background: #fff1f2;
            padding: 10px;
            margin: 8px 0;
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
`;

data.forEach(file => {
    if (file.errors) {
        totalIssues += file.errors.length;
    }
});

html += `
    <div class="summary">
        <h2>Summary</h2>
        <p>Total Files Scanned: ${data.length}</p>
        <p>Total Issues Found: <span class="${totalIssues > 0 ? 'fail' : 'pass'}">${totalIssues}</span></p>
        <p>Status: <span class="${totalIssues > 0 ? 'fail' : 'pass'}">${totalIssues > 0 ? 'FAILED' : 'PASSED'}</span></p>
    </div>
`;

data.forEach(file => {
    html += `
    <div class="file">
        <h2>${file.filePath}</h2>
    `;

    if (!file.errors || file.errors.length === 0) {
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

        file.errors.forEach(error => {
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

if (totalIssues > 0) {
    process.exit(1);
}