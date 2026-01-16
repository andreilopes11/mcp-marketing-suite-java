#!/bin/bash
# MCP Marketing Suite - CRM Sequences Generation Example
# Description: Generate email nurture sequences
# Usage: ./crm-sequences.sh

BASE_URL="http://localhost:8080"
ENDPOINT="/api/marketing/crm-sequences"

echo "📧 Generating CRM Email Sequences..."
echo ""

curl -X POST "$BASE_URL$ENDPOINT" \
  -H "Content-Type: application/json" \
  -H "X-Request-Id: example-crm-001" \
  -d '{
    "product": "SaaS Analytics Tool",
    "audience": "Data Analysts",
    "brandVoice": "Technical and Helpful",
    "goals": "Convert trial users to paying customers",
    "language": "en",
    "sequenceLength": 5,
    "channels": ["email", "in-app"],
    "conversionGoal": "Upgrade to premium plan"
  }' | jq .

echo ""
echo "✅ Check ./outputs/ directory for generated content"
