pipeline {

    agent any

    options {
        // Fixes the double-checkout issue by disabling the a   utomatic initial checkout
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

        stage('Compile') {
            steps {
                dir('app/springboot-app') {
                    sh 'mvn clean compile'
                }
            }
        }

        stage('Unit Test & JaCoCo') {
            steps {
                dir('app/springboot-app') {
                    sh 'mvn clean verify'
                }
            }
        }
        stage('Publish Test Results') {
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
                    withSonarQubeEnv('SonarQube') {
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

        stage('Archive Trivy Report') {
            steps {
                archiveArtifacts artifacts: 'reports/*.txt', fingerprint: true
            }
        }

        stage('Package') {
            steps {
                dir('app/springboot-app') {
                    sh 'mvn package -DskipTests'
                }
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
                echo "IMAGE_NAME=$IMAGE_NAME"
                echo "BUILD_NUMBER=$BUILD_NUMBER"
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

        stage('Archive Security Reports') {
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
                // Uses the APP_PORT variable dynamically instead of hardcoding 8082
                sh '''
                chmod +x scripts/docker-run.sh
                ./scripts/docker-run.sh
                '''
            }
        }   

        stage('Health Check') {
            steps {
                script {
                    sh '''
                    chmod +x scripts/docker-build.sh
                    ./scripts/docker-build.sh
                    '''
                }
            }
        }
    }
    
    post {
        success {
            echo 'Build completed successfully.'
        }

        failure {
           echo 'Build failed.'
        }

        always {
            cleanWs()
        }
    }
}