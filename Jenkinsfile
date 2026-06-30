pipeline {
    agent any

    stages {

        stage('Checkout') {
          steps {
            git branch: 'main',
               url: 'https://github.com/nagarajucsd/production-devops-pipeline.git'
    }
}

        stage('Build') {
            steps {
                dir('app/springboot-app') {
                    sh './mvnw clean package'
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t springboot-app:v1 .'
            }
        }

        stage('Run Docker Container') {
            steps {
                sh '''
                docker rm -f springboot-container || true
                docker run -d --name springboot-container -p 8081:8081 springboot-app:v1
                '''
            }
        }

    }
}