pipeline {

    agent any

    tools {
        maven 'Maven3'
        jdk 'jdk21'
        nodejs 'node-18'
    }

    parameters {
        choice(
            name: 'BRANCH_NAME',
            choices: [
                'main',
                'gherkin-lint',
                'gherkin-lint-with-fixes',
                'sonarquality-test',
                'sonarquality-fix',
                'sonar-html-report'
            ],
            description: 'Select Git branch to run the pipeline'
        )
    }

    stages {

        stage('Checkout') {
            steps {
                echo "Checking out branch: ${params.BRANCH_NAME}"

                git branch: "${params.BRANCH_NAME}",
                    url: 'https://github.com/sarathvk1991/Automation-Framework-POC.git'

                sh 'ls -la'
            }
        }

        stage('Install Node Dependencies') {
            steps {
                sh '''
                    node -v
                    npm -v
                    npm install
                '''
            }
        }

        stage('Gherkin Lint') {
            steps {
                sh 'npm run lint:gherkin:html'
            }

            post {
                always {
                    archiveArtifacts artifacts: 'gherkin-lint-report.html, gherkin-lint-report.json',
                                     allowEmptyArchive: true
                }
            }
        }

        stage('Custom QA Metrics') {
            when {
                expression {
                    params.BRANCH_NAME != 'gherkin-lint-with-fixes'
                }
            }

            steps {
                sh '''
                    npm run custom:qa-metrics
                    ls -la sonar-custom-qa-issues.json
                '''
            }

            post {
                always {
                    archiveArtifacts artifacts: 'sonar-custom-qa-issues.json',
                                     allowEmptyArchive: true
                }
            }
        }

        stage('Maven Build and SonarQube Analysis') {
            when {
                expression {
                    params.BRANCH_NAME != 'gherkin-lint-with-fixes'
                }
            }

            steps {
                withSonarQubeEnv('sonarqube-docker') {
                    sh '''
                        mvn clean verify \
                          -Dheadless=true \
                          org.sonarsource.scanner.maven:sonar-maven-plugin:4.0.0.4121:sonar \
                          -Dsonar.projectKey=automation-framework \
                          -Dsonar.projectName=Automation-framework \
                          -Dsonar.sources=src/main/java \
                          -Dsonar.tests=src/test/java \
                          -Dsonar.externalIssuesReportPaths=sonar-custom-qa-issues.json \
                          -Dsonar.java.binaries=target/classes \
                          -Dsonar.sourceEncoding=UTF-8
                    '''
                }
            }
        }
        
      

        stage('SonarQube Quality Gate') {
            when {
                expression {
                    params.BRANCH_NAME != 'gherkin-lint-with-fixes'
                }
            }

            steps {
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }
    }

    post {
        always {
            echo 'Downloading SonarQube readability (Maintainability) report'

            withCredentials([string(credentialsId: 'sonar-token', variable: 'SONAR_TOKEN')]) {
                sh '''
                    curl -s -u ${SONAR_TOKEN}: \
                    "http://localhost:9000/api/issues/search?componentKeys=automation-framework&resolved=false&ps=500" \
                    -o sonar-readability-report.json

                    npm run sonar:html
                '''
            }

            archiveArtifacts artifacts: 'sonar-readability-report.json, sonar-readability-report.html',
                             fingerprint: true
        }
    }
}