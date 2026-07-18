#!/bin/bash

set -e

echo "Stopping existing Compose stack..."
docker compose \
  --env-file docker-compose/.env \
  -f docker-compose/docker-compose.yml down

echo "Building and starting services..."
docker compose \
  --env-file docker-compose/.env \
  -f docker-compose/docker-compose.yml up -d

echo "Deployment completed."