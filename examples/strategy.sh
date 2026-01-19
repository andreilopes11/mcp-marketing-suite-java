#!/bin/bash
# MCP Marketing Suite - Strategy Generation Example
# Description: Generate integrated marketing strategy
# Usage: ./strategy.sh

BASE_URL="http://localhost:8080"
ENDPOINT="/api/marketing/strategy"

echo "🎯 Generating Integrated Marketing Strategy..."
echo ""

curl -X POST "$BASE_URL$ENDPOINT" \
  -H "Content-Type: application/json" \
  -H "X-Request-Id: example-strategy-001" \
  -d '{
    "product": "Project Management Software",
    "audience": "Team Leads and Project Managers",
    "brandVoice": "Collaborative and Empowering",
    "goals": "Acquire 1000 customers in Q1 2026",
    "language": "en",
    "marketSegment": "Mid-market companies (50-500 employees)",
    "competitorAnalysis": "Main competitors: Asana, Monday.com, Jira",
    "channels": ["content-marketing", "paid-ads", "partnerships", "email"],
    "timeframe": "Q1 2026"
  }' | jq .

echo ""
echo "✅ Check ./outputs/ directory for generated content"
