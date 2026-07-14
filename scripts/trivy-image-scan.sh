#!/bin/bash

set -e

IMAGE_NAME=${IMAGE_NAME:-production-devops-pipeline}
IMAGE_TAG=${IMAGE_TAG:-latest}

mkdir -p reports

echo "====================================="
echo "| Running Trivy Docker Image Scan   |"
echo "====================================="

echo "Scanning image: ${IMAGE_NAME}:${IMAGE_TAG}"

trivy image \
  --cache-dir ~/.cache/trivy \
  --timeout 45m \
  --skip-version-check \
  --scanners vuln \
  --format table \
  --output reports/trivy-image-report.txt \
  "${IMAGE_NAME}:${IMAGE_TAG}"

echo "Docker image scan completed."