pipeline {

    agent any

    options {
        skipDefaultCheckout()
    }

    environment {
        APP_NAME       = 'production-devops-pipeline'
        IMAGE_NAME     = 'production-devops-pipeline'
        CONTAINER_NAME = 'springboot-container'
        APP_PORT       = '8082'
        SONARQUBE_ENV  = 'SonarQube'
    }

    tools {
        jdk 'JDK17'
        maven 'Maven-3.8.7'
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build & Test') {
            steps {
                dir('app/springboot-app') {
                    sh 'mvn clean verify'
                }
            }
        }

        stage('Publish Unit Test Results') {
            steps {
                junit 'app/springboot-app/target/surefire-reports/*.xml'
            }
        }

        stage('Archive JaCoCo Report') {
            steps {
                archiveArtifacts artifacts: 'app/springboot-app/target/site/jacoco/**', fingerprint: true
            }
        }

        stage('SonarQube Analysis') {
            steps {
                dir('app/springboot-app') {
                    withSonarQubeEnv("${SONARQUBE_ENV}") {
                        sh '''
                        mvn sonar:sonar \
                        -Dsonar.projectKey=production-devops-pipeline \
                        -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml
                        '''
                    }
                }
            }
        }

        stage('Trivy Filesystem Scan') {
            steps {
                sh '''
                chmod +x scripts/trivy-fs-scan.sh
                ./scripts/trivy-fs-scan.sh
                '''
            }
        }

        stage('Archive Trivy Filesystem Report') {
            steps {
                archiveArtifacts artifacts: 'reports/*.txt', fingerprint: true
            }
        }

        stage('Build Docker Image') {
            steps {
                sh '''
                chmod +x scripts/docker-build.sh
                ./scripts/docker-build.sh
                '''
            }
        }
        stage('Verify Docker Image') {
            steps {
                sh '''
                echo "Available Docker Images:"
                docker images
                '''
            }
        }

        stage('Trivy Image Scan') {
            steps {
                sh '''
                chmod +x scripts/trivy-image-scan.sh
                ./scripts/trivy-image-scan.sh
                '''
            }
        }

        stage('Archive Trivy Image Report') {
            steps {
                archiveArtifacts artifacts: 'reports/*.txt', fingerprint: true
            }
        }

        stage('Verify Docker Image') {
            steps {
                sh 'docker images | grep ${IMAGE_NAME}'
            }
        }

        stage('Cleanup Old Container') {
            steps {
                sh '''
                chmod +x scripts/docker-cleanup.sh
                ./scripts/docker-cleanup.sh
                '''
            }
        }

        stage('Run Docker Container') {
            steps {
                sh '''
                chmod +x scripts/docker-run.sh
                ./scripts/docker-run.sh
                '''
            }
        }

        stage('Health Check') {
            steps {
                sh '''
                sleep 30
                curl --fail http://localhost:${APP_PORT}/actuator/health
                '''
            }
        }
    }

    post {

        success {
            echo 'Pipeline completed successfully.'
        }

        failure {
            echo 'Pipeline failed.'
        }

        always {

            junit allowEmptyResults: true,
                  testResults: 'app/springboot-app/target/surefire-reports/*.xml'

            archiveArtifacts artifacts: 'app/springboot-app/target/site/jacoco/**',
                             allowEmptyArchive: true

            archiveArtifacts artifacts: 'reports/*.txt',
                             allowEmptyArchive: true

            cleanWs()
        }
    }
}