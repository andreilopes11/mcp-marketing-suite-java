#!/bin/bash
# MCP Marketing Suite - SEO Plan Generation Example
# Description: Generate complete SEO strategy
# Usage: ./seo-plan.sh

BASE_URL="http://localhost:8080"
ENDPOINT="/api/marketing/seo-plan"

echo "🔍 Generating SEO Strategy..."
echo ""

curl -X POST "$BASE_URL$ENDPOINT" \
  -H "Content-Type: application/json" \
  -H "X-Request-Id: example-seo-001" \
  -d '{
    "product": "E-commerce Platform",
    "audience": "Online Retailers",
    "brandVoice": "Trustworthy and Efficient",
    "goals": "Increase organic traffic by 200%",
    "language": "en",
    "domain": "ecommerce-platform.com",
    "keywords": ["e-commerce", "online store", "sell online", "shopping cart"],
    "monthlyBudget": 5000
  }' | jq .

echo ""
echo "✅ Check ./outputs/ directory for generated content"
