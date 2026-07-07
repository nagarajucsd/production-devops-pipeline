pipeline {

    agent any

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
                -t production-devops-pipeline:${BUILD_NUMBER} \
                -t production-devops-pipeline:latest .
                '''
            }
        }
        stage('Verify Docker Image') {
            steps {
                sh 'docker images | grep production-devops-pipeline'
            }
        }

        stage('Cleanup Old Container') {
            steps {
                sh '''
                docker rm -f springboot-container || true '''
            }
        }

        stage('Run Docker Container') {
            steps {
                sh '''
                docker run -d \
                --name springboot-container \
                -p 8082:8082 \
                production-devops-pipeline:${BUILD_NUMBER} '''
            }
        }   
        stage('Health Check') {
            steps {
                sh '''
                sleep 20
                curl http://localhost:8082/actuator/health
                '''
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