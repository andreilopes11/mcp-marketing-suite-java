# MCP Marketing Suite - Examples & Scripts

## 📋 Overview

This directory contains ready-to-use examples for testing the MCP Marketing Suite REST API.

---

## 🚀 Quick Start

### 1. Start the Server

```bash
cd /path/to/mcp-marketing-suite-java
mvn spring-boot:run
```

Server will start at: **http://localhost:8080**

### 2. Run Examples

All scripts are in this directory. Make them executable and run:

```bash
chmod +x *.sh
./ads.sh
./seo-plan.sh
./crm-sequences.sh
./strategy.sh
./health-check.sh
```

---

## 📜 Available Scripts

### 1. Health Check

**File**: `health-check.sh`

**Description**: Verify API is running and healthy

**Command**:
```bash
./health-check.sh
```

**What it does**:
- ✅ Basic health check
- ✅ Detailed health with all components
- ✅ Application information

---

### 2. Generate Ads

**File**: `ads.sh`

**Description**: Generate multi-platform ads (Google, Meta, LinkedIn)

**Command**:
```bash
./ads.sh
```

**Input**:
- Product: Cloud CRM Platform
- Audience: Small Business Owners
- Budget: $5,000
- Duration: 3 months
- Platforms: Google, Meta, LinkedIn

**Output**: File saved to `./outputs/ads_*.json`

---

### 3. Generate SEO Plan

**File**: `seo-plan.sh`

**Description**: Generate complete SEO strategy

**Command**:
```bash
./seo-plan.sh
```

**Input**:
- Product: E-commerce Platform
- Audience: Online Retailers
- Domain: ecommerce-platform.com
- Keywords: e-commerce, online store, sell online
- Monthly Budget: $5,000

**Output**: File saved to `./outputs/seo-plan_*.json`

---

### 4. Generate CRM Sequences

**File**: `crm-sequences.sh`

**Description**: Generate email nurture sequences

**Command**:
```bash
./crm-sequences.sh
```

**Input**:
- Product: SaaS Analytics Tool
- Audience: Data Analysts
- Sequence Length: 5 emails
- Conversion Goal: Upgrade to premium

**Output**: File saved to `./outputs/crm-sequences_*.json`

---

### 5. Generate Strategy

**File**: `strategy.sh`

**Description**: Generate integrated marketing strategy

**Command**:
```bash
./strategy.sh
```

**Input**:
- Product: Project Management Software
- Audience: Team Leads & Project Managers
- Goal: Acquire 1000 customers in Q1 2026
- Channels: Content, Ads, Partnerships, Email

**Output**: File saved to `./outputs/strategy_*.json`

---

## 📁 Payload Files

Pre-configured JSON request payloads in `payloads/` directory:

- `ads-request.json` - Ads generation payload
- `seo-plan-request.json` - SEO plan payload
- `crm-sequences-request.json` - CRM sequences payload
- `strategy-request.json` - Strategy payload

### How to Use

#### Option 1: Copy/Paste into Script

```bash
curl -X POST http://localhost:8080/api/marketing/ads \
  -H "Content-Type: application/json" \
  -d @payloads/ads-request.json
```

#### Option 2: Modify and Use

```bash
# Copy payload
cp payloads/ads-request.json my-ads-request.json

# Edit as needed
vim my-ads-request.json

# Use it
curl -X POST http://localhost:8080/api/marketing/ads \
  -H "Content-Type: application/json" \
  -d @my-ads-request.json
```

---

## 🔍 Full cURL Examples

### Ads Generation

```bash
curl -X POST http://localhost:8080/api/marketing/ads \
  -H "Content-Type: application/json" \
  -H "X-Request-Id: my-ads-001" \
  -d '{
    "product": "Cloud CRM Platform",
    "audience": "Small Business Owners",
    "brandVoice": "Professional and Approachable",
    "goals": "Generate 100 qualified leads per month",
    "language": "en",
    "platforms": ["google", "meta", "linkedin"],
    "budget": "5000",
    "duration": "3 months"
  }'
```

### SEO Plan Generation

```bash
curl -X POST http://localhost:8080/api/marketing/seo-plan \
  -H "Content-Type: application/json" \
  -d '{
    "product": "E-commerce Platform",
    "audience": "Online Retailers",
    "brandVoice": "Trustworthy and Efficient",
    "goals": "Increase organic traffic by 200%",
    "language": "en",
    "domain": "ecommerce-platform.com",
    "keywords": ["e-commerce", "online store", "sell online"],
    "monthlyBudget": 5000
  }'
```

### CRM Sequences Generation

```bash
curl -X POST http://localhost:8080/api/marketing/crm-sequences \
  -H "Content-Type: application/json" \
  -d '{
    "product": "SaaS Analytics Tool",
    "audience": "Data Analysts",
    "brandVoice": "Technical and Helpful",
    "goals": "Convert trial users to paying customers",
    "language": "en",
    "sequenceLength": 5,
    "channels": ["email", "in-app"],
    "conversionGoal": "Upgrade to premium plan"
  }'
```

### Strategy Generation

```bash
curl -X POST http://localhost:8080/api/marketing/strategy \
  -H "Content-Type: application/json" \
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
  }'
```

---

## 📊 Expected Responses

### Success Response

```json
{
  "requestId": "abc-123-uuid",
  "timestamp": "2026-01-16T18:30:45.123Z",
  "status": 200,
  "success": true,
  "data": {
    "artifact_type": "ads",
    "execution_time_ms": 250,
    "result": {
      "googleAds": {...},
      "metaAds": {...},
      "linkedinAds": {...},
      "qaScore": 85,
      "recommendations": [...]
    },
    "output_path": "./outputs/ads_abc-123_20260116_183045.json"
  }
}
```

### Error Response

```json
{
  "requestId": "abc-123-uuid",
  "timestamp": "2026-01-16T18:30:45.123Z",
  "status": 400,
  "success": false,
  "error": "VALIDATION_ERROR",
  "message": "audience is required",
  "executionTimeMs": 15
}
```

---

## 📂 Output Location

All generated content is saved to:

```
./outputs/
├── ads_<request-id>_<timestamp>.json
├── seo-plan_<request-id>_<timestamp>.json
├── crm-sequences_<request-id>_<timestamp>.json
└── strategy_<request-id>_<timestamp>.json
```

View outputs:

```bash
# List all outputs
ls -la ./outputs/

# View latest output
cat ./outputs/ads_*.json | tail -1 | jq .

# Count outputs
ls ./outputs/ | wc -l
```

---

## 🧪 Testing Workflow

### Step 1: Start Server

```bash
mvn spring-boot:run
```

Wait for: `MCP Marketing Server initialized successfully`

### Step 2: Check Health

```bash
./health-check.sh
```

Should show: `"status":"UP"`

### Step 3: Generate Content

```bash
./ads.sh
./seo-plan.sh
./crm-sequences.sh
./strategy.sh
```

### Step 4: Verify Outputs

```bash
ls -la ./outputs/
```

Should show 4 JSON files created.

### Step 5: View Results

```bash
cat ./outputs/ads_*.json | jq .data.result
```

---

## 🔧 Customization

### Modify Payload

Edit any script to change parameters:

```bash
# Edit ads.sh
vim ads.sh

# Change this line:
"product": "Cloud CRM Platform",
# To:
"product": "My Custom Product",
```

### Use Your Own Data

Replace example values with real data:

```bash
curl -X POST http://localhost:8080/api/marketing/ads \
  -H "Content-Type: application/json" \
  -d '{
    "product": "YOUR PRODUCT NAME",
    "audience": "YOUR TARGET AUDIENCE",
    "brandVoice": "YOUR BRAND TONE",
    "goals": "YOUR MARKETING GOALS",
    "language": "en"
  }'
```

---

## 📊 Common Use Cases

### 1. Quick Test

```bash
# Just check if API is running
./health-check.sh
```

### 2. Generate All Content

```bash
# Run all generators
for script in ads.sh seo-plan.sh crm-sequences.sh strategy.sh; do
  echo "Running $script..."
  ./$script
  echo ""
done
```

### 3. Batch Processing

Create `batch.sh`:

```bash
#!/bin/bash
# Process multiple products

PRODUCTS=("Product A" "Product B" "Product C")

for product in "${PRODUCTS[@]}"; do
  echo "Processing $product..."
  curl -X POST http://localhost:8080/api/marketing/ads \
    -H "Content-Type: application/json" \
    -d "{
      \"product\": \"$product\",
      \"audience\": \"Target Audience\",
      \"brandVoice\": \"Professional\",
      \"goals\": \"100 leads/month\",
      \"language\": \"en\"
    }"
  echo ""
done
```

---

## 🐛 Troubleshooting

### Issue: Connection Refused

**Problem**: `curl: (7) Failed to connect to localhost:8080`

**Solution**:
```bash
# Check if server is running
curl http://localhost:8080/health

# If not, start it
mvn spring-boot:run
```

### Issue: JSON Parse Error

**Problem**: `jq: parse error`

**Solution**: Ensure `-d @payloads/...json` uses `@` symbol for file reference

```bash
# ❌ Wrong
curl -X POST ... -d payloads/ads-request.json

# ✅ Correct
curl -X POST ... -d @payloads/ads-request.json
```

### Issue: jq Not Found

**Problem**: `command not found: jq`

**Solution**: Install jq

```bash
# macOS
brew install jq

# Linux (Ubuntu/Debian)
sudo apt-get install jq

# Windows (with Chocolatey)
choco install jq
```

Or remove `| jq .` from scripts to see raw JSON.

---

## 📚 Additional Resources

- **[README.md](../README.md)** - Project overview
- **[MCP_QUICK_START.md](../MCP_QUICK_START.md)** - MCP Server guide
- **[docs/COMPLETE_CONFIGURATION.md](../docs/COMPLETE_CONFIGURATION.md)** - Configuration details
- **[docs/MCP_SERVER_COMPLETE.md](../docs/MCP_SERVER_COMPLETE.md)** - MCP implementation

---

## ✅ Quick Reference

| What | Command |
|------|---------|
| Start Server | `mvn spring-boot:run` |
| Health Check | `./health-check.sh` |
| Generate Ads | `./ads.sh` |
| Generate SEO | `./seo-plan.sh` |
| Generate CRM | `./crm-sequences.sh` |
| Generate Strategy | `./strategy.sh` |
| View Outputs | `ls -la ./outputs/` |
| View Latest | `cat ./outputs/ads_*.json \| jq .` |

---

**Happy testing! 🚀**

*For issues or questions, check the documentation or open an issue on GitHub.*
