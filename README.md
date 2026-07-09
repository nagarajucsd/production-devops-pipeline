# Production DevOps Pipeline

## Overview

This repository demonstrates a production-inspired DevOps pipeline for deploying a Spring Boot application using modern DevOps practices.

---

## Tech Stack

- Java 17
- Spring Boot
- Maven
- Git
- GitHub
- Docker
- Jenkins
- Kubernetes
- Terraform
- AWS
- Prometheus
- Grafana

---

## Project Structure

app/

docker/

jenkins/

terraform/

kubernetes/

monitoring/

docs/

scripts/

---

## Features

- REST APIs
- Health Check
- Version API
- Employee API
- Spring Boot Actuator
- Logging

---
## Docker

### Build

```bash
docker build -f docker/Dockerfile -t production-devops-pipeline:v1 .
```

### Run

```bash
docker run -d --name springboot-container -p 8082:8082 production-devops-pipeline:v1
```
## Docker

### Build

```bash
docker build -f docker/Dockerfile -t production-devops-pipeline:v1.0.0 .
```

### Run

```bash
docker run -d \
-p 8082:8082 \
production-devops-pipeline:v1.0.0
``` 

### Image Details

|Property  | Value |
| :--- | :--- |
| **Image** | production-devops-pipeline |
| **Version** | v1.0.0 |
| **Java** | 17 |
| **Spring Boot** |	3.x |

## Project Roadmap

- ✅ Spring Boot
- ⏳ Docker
- ⏳ Jenkins
- ⏳ SonarQube
- ⏳ Trivy
- ⏳ Kubernetes
- ⏳ AWS
- ⏳ Terraform
- ⏳ Monitoring
# Repository transferred
