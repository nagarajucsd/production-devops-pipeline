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
        stage('Clean Workspace') {
            steps {
                cleanWs()
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