#!/bin/bash

set -e

IMAGE_NAME=${IMAGE_NAME:-production-devops-pipeline}
BUILD_NUMBER=${BUILD_NUMBER:-latest}

echo "Building Docker image..."

docker build \
  -f docker/Dockerfile \
  -t "${IMAGE_NAME}:${BUILD_NUMBER}" \
  -t "${IMAGE_NAME}:latest" .

echo "Docker image built successfully."