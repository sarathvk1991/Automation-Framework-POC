const fs = require('fs');

const inputFile = 'sonar-readability-report.json';
const outputFile = 'sonar-readability-report.html';

if (!fs.existsSync(inputFile)) {
    console.error(`${inputFile} not found`);
    process.exit(1);
}

const sonarData = JSON.parse(fs.readFileSync(inputFile, 'utf8'));

const issues = sonarData.issues || [];

let html = `
<!DOCTYPE html>
<html>
<head>
    <title>SonarQube Maintainability Report</title>

    <style>
        body {
            font-family: Arial;
            margin: 20px;
            background: #f7f7f7;
        }

        h1 {
            color: #333;
        }

        .summary {
            margin-bottom: 20px;
            padding: 10px;
            background: white;
            border-radius: 5px;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            background: white;
        }

        th {
            background: #333;
            color: white;
            padding: 10px;
            text-align: left;
        }

        td {
            padding: 8px;
            border-bottom: 1px solid #ddd;
        }

        tr:hover {
            background: #f1f1f1;
        }

        .MAJOR {
            color: orange;
            font-weight: bold;
        }

        .CRITICAL {
            color: red;
            font-weight: bold;
        }

        .MINOR {
            color: blue;
            font-weight: bold;
        }

        .BLOCKER {
            color: darkred;
            font-weight: bold;
        }

        .OPEN {
            color: red;
            font-weight: bold;
        }

        .CLOSED {
            color: green;
            font-weight: bold;
        }
    </style>
</head>

<body>

<h1>SonarQube Maintainability Report</h1>

<div class="summary">
    <p><strong>Total Issues:</strong> ${sonarData.total}</p>
    <p><strong>Total Technical Debt:</strong> ${sonarData.effortTotal} minutes</p>
</div>

<table>
    <tr>
        <th>Severity</th>
        <th>Status</th>
        <th>Rule</th>
        <th>Component</th>
        <th>Line</th>
        <th>Message</th>
    </tr>
`;

issues.forEach(issue => {

    html += `
    <tr>
        <td class="${issue.severity}">
            ${issue.severity}
        </td>

        <td class="${issue.status}">
            ${issue.status}
        </td>

        <td>${issue.rule}</td>

        <td>${issue.component}</td>

        <td>${issue.line || '-'}</td>

        <td>${issue.message}</td>
    </tr>
    `;
});

html += `
</table>

</body>
</html>
`;

fs.writeFileSync(outputFile, html);

console.log(`${outputFile} generated successfully`);