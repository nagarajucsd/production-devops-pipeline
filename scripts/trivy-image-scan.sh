#!/bin/bash

set -e

IMAGE_NAME=${IMAGE_NAME:-production-devops-pipeline}
IMAGE_TAG=${IMAGE_TAG:-latest}

echo "====================================="
echo "| Running Trivy Docker Image Scan   |"
echo "====================================="

echo "Scanning image: ${IMAGE_NAME}:${IMAGE_TAG}"

mkdir -p reports

trivy image \
  --cache-dir .trivycache \
  --timeout 15m \
  --scanners vuln \
  --format table \
  --output reports/trivy-image-report.txt \
  "${IMAGE_NAME}:${IMAGE_TAG}"

echo "Docker image scan completed."