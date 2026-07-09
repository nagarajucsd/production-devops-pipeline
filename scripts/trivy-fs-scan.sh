#!/bin/bash

set -e

echo "=================================="
echo "| Running Trivy Filesystem Scan  |"
echo "=================================="

mkdir -p reports

trivy fs \
--format table \
-o reports/trivy-fs-report.txt .

echo "Filesystem scan completed."