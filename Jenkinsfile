pipeline {

    agent any

    options {
        // Fixes the double-checkout issue by disabling the automatic initial checkout
        skipDefaultCheckout()
    }

    environment {
        APP_NAME       = 'production-devops-pipeline'
        IMAGE_NAME     = 'production-devops-pipeline'
        CONTAINER_NAME = 'springboot-container'
        APP_PORT       = '8082'
        SONARQUBE_ENV="SonarQube"
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

        stage('Unit Test') {
            steps {
                dir('app/springboot-app') {
                    sh 'mvn test'
                }
            }
        }

        stage('SonarQube Analysis') {

            steps {
                dir('app/springboot-app') {
                    withSonarQubeEnv('SonarQube') {
                        sh '''
                        mvn sonar:sonar \
                        -Dsonar.projectKey=production-devops-pipeline \
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

        stage('Archive Artifact') {
            steps {
                archiveArtifacts artifacts: 'app/springboot-app/target/*.jar',
                                 fingerprint: true
            }
        }

        stage('Build Docker Image') {
            steps {
                sh '''
                docker build \
                -f docker/Dockerfile \
                -t ${IMAGE_NAME}:${BUILD_NUMBER} \
                -t ${IMAGE_NAME}:latest .
                '''
            }
        }

        stage('Verify Docker Image') {
            steps {
                sh 'docker images | grep ${IMAGE_NAME}'
            }
        }

        stage('Cleanup Old Container') {
            steps {
                sh 'docker rm -f ${CONTAINER_NAME} || true'
            }
        }

        stage('Run Docker Container') {
            steps {
                // Uses the APP_PORT variable dynamically instead of hardcoding 8082
                sh '''
                docker run -d \
                --name ${CONTAINER_NAME} \
                -p ${APP_PORT}:${APP_PORT} \
                ${IMAGE_NAME}:${BUILD_NUMBER}
                '''
            }
        }   

        stage('Health Check') {
            steps {
                script {
                    timeout(time: 2, unit: 'MINUTES') {
                        waitUntil {
                            def status = sh(
                                script: '''
                                curl -s http://localhost:8082/actuator/health | grep '"status":"UP"'
                                ''',
                                returnStatus: true
                            )
                            return (status == 0)
                        }
                    }
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
            junit 'app/springboot-app/target/surefire-reports/*.xml'
            cleanWs()
        }
    }
}