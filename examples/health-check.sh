#!/bin/bash
# MCP Marketing Suite - Health Check Example
# Description: Check API health status
# Usage: ./health-check.sh

BASE_URL="http://localhost:8080"

echo "🏥 Checking API Health Status..."
echo ""

# Basic health check
echo "1️⃣  Basic Health Check:"
curl -s "$BASE_URL/health" | jq .
echo ""

# Detailed health check with all components
echo "2️⃣  Detailed Health Check (with components):"
curl -s "$BASE_URL/actuator/health" | jq .
echo ""

# Application info
echo "3️⃣  Application Information:"
curl -s "$BASE_URL/actuator/info" | jq .
echo ""

echo "✅ API is running and healthy!"
