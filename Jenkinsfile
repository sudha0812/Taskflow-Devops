pipeline {
    agent any
    options { timestamps() }
    stages {
        stage('Checkout') {
            steps { checkout scm }
        }
        stage('Build and Test') {
            steps { bat 'mvn -B clean verify' }
            post { always { junit 'target/surefire-reports/*.xml' } }
        }
        stage('Build Docker Image') {
            steps {
                bat 'docker build -t taskflow:%BUILD_NUMBER% -t taskflow:latest .'
            }
        }
        
        stage('Deploy to Kubernetes') {
            steps {
                bat 'kubectl --kubeconfig "C:\\Windows\\System32\\config\\systemprofile\\.kube\\config" config current-context'
                bat 'kubectl --kubeconfig "C:\\Windows\\System32\\config\\systemprofile\\.kube\\config" apply -f k8s/deployment.yaml'
                bat 'kubectl --kubeconfig "C:\\Windows\\System32\\config\\systemprofile\\.kube\\config" apply -f k8s/service.yaml'
                bat 'kubectl --kubeconfig "C:\\Windows\\System32\\config\\systemprofile\\.kube\\config" set image deployment/taskflow taskflow=taskflow:%BUILD_NUMBER%'
                bat 'kubectl --kubeconfig "C:\\Windows\\System32\\config\\systemprofile\\.kube\\config" rollout status deployment/taskflow --timeout=120s'
            }
        }
        stage('Smoke Test') {
            steps {
                bat 'curl.exe --fail --retry 5 --retry-delay 3 http://localhost:30080/'
            }
        }
    }
    post {
        success { archiveArtifacts artifacts: 'target/taskflow.jar', fingerprint: true }
    }
}
