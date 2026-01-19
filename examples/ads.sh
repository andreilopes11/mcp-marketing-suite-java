#!/bin/bash
# MCP Marketing Suite - Ads Generation Example
# Description: Generate multi-platform ads (Google, Meta, LinkedIn)
# Usage: ./ads.sh

BASE_URL="http://localhost:8080"
ENDPOINT="/api/marketing/ads"

echo "📢 Generating Multi-Platform Ads..."
echo ""

curl -X POST "$BASE_URL$ENDPOINT" \
  -H "Content-Type: application/json" \
  -H "X-Request-Id: example-ads-001" \
  -d '{
    "product": "Cloud CRM Platform",
    "audience": "Small Business Owners",
    "brandVoice": "Professional and Approachable",
    "goals": "Generate 100 qualified leads per month",
    "language": "en",
    "platforms": ["google", "meta", "linkedin"],
    "budget": "5000",
    "duration": "3 months"
  }' | jq .

echo ""
echo "✅ Check ./outputs/ directory for generated content"
