#!/bin/bash

# Exit immediately if a command exits with a non-zero status
set -e

echo "=== Cleaning up existing Docker containers and images ==="
# Stop and remove containers
docker compose -p account-management-system down || true

# Remove images if they exist
docker rmi -f backend frontend || true

echo "=== Building Backend ==="
cd account-management-backend

echo "--- Cleaning ---"
./mvnw clean

echo "--- Compiling ---"
./mvnw compile

echo "--- Running Tests ---"
./mvnw test

echo "--- Packaging ---"
./mvnw package

echo "--- Building Backend Docker Image ---"
docker build -t backend .

cd ..

echo "=== Building Frontend ==="
cd account-management-frontend

echo "--- Installing Dependencies ---"
npm install

echo "--- Building Frontend Docker Image ---"
docker build -t frontend .

cd ..

echo "=== Starting Docker Compose ==="
docker compose -p account-management-system up
