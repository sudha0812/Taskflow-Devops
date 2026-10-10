# TaskFlow — DevOps CI/CD Case Study

TaskFlow is a small student task tracker used to demonstrate a GitHub → Maven → Jenkins → Docker → Kubernetes workflow.

## Requirements
- JDK 21
- Maven 3.9+
- Git
- Docker Desktop
- Kubernetes enabled in Docker Desktop
- Jenkins with Pipeline, Git, and JUnit support; Jenkins service account must be able to use Docker and kubectl

## Run locally
```powershell
mvn clean verify
mvn spring-boot:run
```
Open http://localhost:8080 and http://localhost:8080/api/tasks.

## Build and run with Docker
```powershell
mvn clean package
docker build -t taskflow:local .
docker run --rm -p 8081:8080 taskflow:local
```
Open http://localhost:8081.

## Deploy to Docker Desktop Kubernetes
```powershell
mvn clean package
docker build -t taskflow:latest .
kubectl config current-context
kubectl get nodes
kubectl apply -f k8s/deployment.yaml
kubectl apply -f k8s/service.yaml
kubectl rollout status deployment/taskflow
kubectl get pods
```
Open http://localhost:30080.

## GitHub
Create an empty repository named `TaskFlow-DevOps`, then:
```powershell
git init
git add .
git commit -m "Initial TaskFlow application"
git branch -M main
git remote add origin https://github.com/YOUR_USERNAME/TaskFlow-DevOps.git
git push -u origin main
```

## Jenkins
Create a Pipeline job named `TaskFlow-CICD`, choose **Pipeline script from SCM**, set SCM to Git, enter the GitHub repository URL, branch `*/main`, and script path `Jenkinsfile`. Ensure Jenkins can run `java`, `mvn`, `git`, `docker`, `kubectl`, and access the Kubernetes context. Trigger a build and review each stage's console output.

Note: Task data is stored in memory and resets when the app restarts; persistence is outside this case study's scope.
