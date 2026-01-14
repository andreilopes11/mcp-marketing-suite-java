# 🚀 Execution Guide - MCP Marketing Suite

## How to Generate a Valid Campaign with Real Data

---

## 📋 Prerequisites

- ✅ Java 17+ installed
- ✅ Maven installed
- ✅ (Optional) OpenAI API key for AI features

---

## 🔧 Step 1: Configure Environment Variables

### Option A: With OpenAI AI (Recommended)

```bash
# Windows (PowerShell)
$env:OPENAI_API_KEY="sk-your-key-here"

# Windows (CMD)
set OPENAI_API_KEY=sk-your-key-here

# Linux/Mac
export OPENAI_API_KEY="sk-your-key-here"
```

### Option B: Without AI (Using Templates)

No configuration needed! The system will work with intelligent templates.

---

## ▶️ Step 2: Start the Application

```bash
cd mcp-marketing-suite-java

# Compile and run
mvn clean spring-boot:run
```

**Wait until you see**:
```
Started Application in X.XXX seconds
```

The application will be running at: **http://localhost:8080**

---

## 📝 Step 3: Create a Real Campaign

### Example: Campaign for a CRM Software Startup

Create a file `real-crm-campaign.json`:

```json
{
  "product": "CloudCRM - Cloud-based CRM system with integrated AI for sales automation and customer service. Includes real-time analytics dashboard, complete sales pipeline management, intelligent follow-up automation, native WhatsApp/Email/Telephony integration, customizable reports, and mobile app for field management",
  "audience": "Small and medium-sized businesses (10-100 employees) in retail, services, technology, and consulting sectors looking to digitize and professionalize their sales processes, improve customer relationships, and increase commercial team productivity",
  "brand_voice": "Professional yet accessible, inspiring and educational. Consultative and strategic partner tone, focused on tangible results and proven ROI. Avoids excessive technical jargon and prioritizes practical day-to-day benefits. Uses cases and data to prove effectiveness",
  "goals": [
    "Generate 500 qualified B2B leads in 90 days",
    "Increase free trial to paid plan conversion rate by 25%",
    "Reduce CAC (Customer Acquisition Cost) by 30% through inbound marketing",
    "Establish CloudCRM as an authority in the CRM market for SMBs",
    "Achieve NPS above 70 through efficient onboarding"
  ],
  "language": "en",
  "competitors": "HubSpot CRM, Pipedrive, Salesforce Essentials, Zoho CRM",
  "budget_range": "$3,000 - $6,000/month",
  "campaign_duration": "90 days"
}
```

---

## 🎯 Step 4: Generate the Complete Campaign

### Using cURL:

```bash
curl -X POST http://localhost:8080/api/marketing/strategy \
  -H "Content-Type: application/json" \
  -d @real-crm-campaign.json \
  -o campaign-result.json
```

### Using PowerShell:

```powershell
$body = Get-Content real-crm-campaign.json -Raw
Invoke-RestMethod -Uri "http://localhost:8080/api/marketing/strategy" `
  -Method Post `
  -ContentType "application/json" `
  -Body $body `
  -OutFile "campaign-result.json"
```

### Using Postman/Insomnia:

1. Create POST request to `http://localhost:8080/api/marketing/strategy`
2. Headers: `Content-Type: application/json`
3. Body: Paste the JSON above
4. Send ▶️

---

## 📊 Step 5: View the Results

### View in Terminal:

```bash
cat campaign-result.json
```

### View the generated file:

The system automatically saves to: `./outputs/strategy_XXXXXXXX_YYYYMMDD_HHMMSS.json`

**Example result structure**:

```json
{
  "request_id": "a1b2c3d4",
  "status": "success",
  "data": {
    "ads": {
      "google_ads": {
        "headlines": [
          {"text": "CloudCRM - Automate Your Sales", "length": "30"},
          {"text": "Smart CRM for SMBs", "length": "20"},
          {"text": "Increase Conversions by 25%", "length": "25"}
        ],
        "descriptions": [...]
      },
      "meta_ads": {...},
      "linkedin_ads": {...},
      "qa_score": {
        "overall_score": 88,
        "clarity": 90,
        "relevance": 85,
        "brand_alignment": 90,
        "call_to_action_strength": 87
      }
    },
    "crm_sequence": {
      "sequence_name": "CloudCRM Nurture Campaign",
      "emails": [
        {
          "sequence": 1,
          "subject": "Welcome to CloudCRM",
          "body": "Hi {First Name},...",
          "cta_text": "Get Started",
          "cta_link": "https://example.com/getting-started"
        }
        // ... 4 additional emails
      ],
      "timing": {...}
    },
    "seo_strategy": {
      "keyword_strategy": {
        "primary_keywords": [
          {"keyword": "crm for small business", "monthly_volume": 5000, "competition": "high"},
          {"keyword": "cloud crm software", "monthly_volume": 2000, "competition": "medium"}
        ],
        "secondary_keywords": [...],
        "long_tail_keywords": [...]
      },
      "content_plan": {...},
      "technical_seo": {...}
    }
  },
  "output_path": "./outputs/strategy_a1b2c3d4_20260114_153045.json",
  "timestamp": "2026-01-14T15:30:47",
  "message": "full GTM strategy generation completed successfully",
  "execution_time_ms": 2345
}
```

---

## 🎨 Examples of Other Real Campaigns

### 1. Sustainable Fashion E-commerce

```json
{
  "product": "EcoStyle - Sustainable fashion marketplace featuring certified eco-friendly brands, exclusive vintage pieces, and conscious production. Offers complete supply chain traceability, verified sustainability seal, clothing recycling rewards program, and P2P resale marketplace",
  "audience": "Women aged 25-45, classes A and B, residing in capitals and major urban centers, professionally active (entrepreneurs, executives, professionals), concerned with sustainability, conscious consumption, quality over quantity, and who value exclusivity and purpose in purchases",
  "brand_voice": "Inspiring, authentic and empowering. Friendly yet sophisticated tone, feminine without being infantilized. Strong focus on brand and artisan storytelling, ethical values, positive social/environmental impact, and building an engaged community. Clean and editorial visual style",
  "goals": [
    "Increase GMV (gross merchandise volume) by 40% next quarter",
    "Reduce cart abandonment rate from 70% to 45% with strategic remarketing",
    "Build engaged community of 10,000 qualified Instagram followers",
    "Position EcoStyle as top 3 reference in sustainable fashion",
    "Increase average order value from $70 to $95 with upsells and bundles"
  ],
  "language": "en",
  "competitors": "Everlane, Reformation, ThredUp, Poshmark",
  "budget_range": "$4,000 - $8,000/month",
  "campaign_duration": "120 days"
}
```

### 2. Boutique Fitness Studio

```json
{
  "product": "FitLux Studio - Premium boutique gym with personalized classes in small groups (max 12 students), high-intensity functional training, integrated nutrition program with in-house nutritionist, exclusive AI-powered progress tracking app, luxury locker room with amenities, and VIP member community. Specialized in smart weight loss and executive fitness",
  "audience": "Men and women aged 30-55, classes A and B, residing in upscale neighborhoods, professionally successful (executives, entrepreneurs, professionals) seeking exclusivity, fully personalized attention, privacy, and fast sustainable results. Willing to invest 3-5x more than traditional gyms for quality and differentiation",
  "brand_voice": "Motivational, exclusive and sophisticated. Energetic but not aggressive or militaristic tone. Focus on holistic transformation (body+mind), executive wellness, high performance, VIP community, and aspirational lifestyle. Inspires without pressuring, educates without being pedantic",
  "goals": [
    "Fill 80% of total capacity reaching 200 active members",
    "Increase average ticket by 35% migrating clients to premium plans and personal training",
    "Reduce monthly churn rate from 15% to 8% with loyalty program",
    "Generate waiting list for prime time slots (6am-9am and 6pm-9pm)",
    "Establish NPS above 85 and generate 40% of leads via referral"
  ],
  "language": "en",
  "competitors": "Equinox, Barry's Bootcamp, SoulCycle, Local boutique studios",
  "budget_range": "$2,000 - $4,000/month",
  "campaign_duration": "60 days"
}
```

### 3. School Management SaaS

```json
{
  "product": "EduCloud - Complete SaaS school management platform with integrated modules for automated online enrollment, multichannel parent communication (app, SMS, email), digital class diary with AI for performance analysis, financial management with automated billing, gamified student portal, digital library, and advanced management reports. Cloud-based, mobile-first with premium 24/7 support",
  "audience": "Directors, pedagogical coordinators and school owners of private elementary and middle schools (capacity 100-1000 students), located in capitals and mid-sized cities. Decision-makers seeking operational efficiency, administrative cost reduction, technological modernization, improved parent communication, and competitive differentiation in the educational market",
  "brand_voice": "Professional, reliable and educational. Consultative and long-term strategic partner tone, never just a vendor. Focus on educational digital transformation, pedagogical team time savings, operational cost reduction, GDPR compliance, and excellence in family experience. Uses data and proven success cases",
  "goals": [
    "Convert 50 schools (200-500 students each) to annual subscription in 6 months",
    "Increase MRR (Monthly Recurring Revenue) by $30,000",
    "Reduce B2B sales cycle from 90 to 45 days with automation and nurturing",
    "Become top 3 in awareness and consideration in school management segment",
    "Achieve 92% annual retention rate through proactive customer success"
  ],
  "language": "en",
  "competitors": "Blackbaud, PowerSchool, Schoology, Canvas LMS",
  "budget_range": "$5,000 - $10,000/month",
  "campaign_duration": "180 days"
}
```

---

## 🔍 Step 6: Use Swagger UI (Visual Interface)

1. Access: **http://localhost:8080/swagger-ui.html**
2. Expand desired endpoint (e.g., `POST /api/marketing/strategy`)
3. Click "Try it out"
4. Paste your JSON in the text area
5. Click "Execute"
6. View the result in the response

---

## 📦 Individual Operations

### Generate Only Ads:

```bash
curl -X POST http://localhost:8080/api/marketing/ads \
  -H "Content-Type: application/json" \
  -d @real-crm-campaign.json
```

### Generate Only CRM Sequence:

```bash
curl -X POST http://localhost:8080/api/marketing/crm-sequences \
  -H "Content-Type: application/json" \
  -d @real-crm-campaign.json
```

### Generate Only SEO Strategy:

```bash
curl -X POST http://localhost:8080/api/marketing/seo-plan \
  -H "Content-Type: application/json" \
  -d @real-crm-campaign.json
```

---

## 📈 Metrics and Monitoring

### View Logs in Real-time:

```bash
tail -f logs/mcp-marketing-suite.log
```

### Check Application Health:

```bash
curl http://localhost:8080/api/health
```

### View Generated Files:

```bash
ls -la outputs/
```

---

## 💡 Tips for Best Results

### 1. **Be Specific About the Product**
❌ "CRM Software"
✅ "CloudCRM - Cloud-based CRM system with integrated AI, analytics dashboard, pipeline management, and WhatsApp/Email integration"

### 2. **Detail the Audience**
❌ "Small businesses"
✅ "SMBs (10-100 employees) in retail, services, and technology sectors looking to digitize sales"

### 3. **Clear Brand Voice**
❌ "Professional"
✅ "Professional yet accessible, inspiring, consultative, ROI-focused. Avoids technical jargon"

### 4. **Measurable Goals**
❌ "Increase sales"
✅ "Generate 500 qualified leads in 90 days, increase conversion by 25%, reduce CAC by 30%"

### 5. **Include Context**
- Competitors: Helps differentiate
- Budget: Calibrates expectations
- Duration: Defines urgency and scope

---

## 🐛 Troubleshooting

### Error: "Connection refused"
```
Solution: Check if the application is running (mvn spring-boot:run)
```

### Error: "Invalid request: Product name is required"
```
Solution: Verify all required fields are filled:
- product
- audience
- brand_voice
- goals (array with at least 1 item)
```

### Warning: "Dummy AI model called"
```
This is normal! It means you're running without an OpenAI API key.
The system will use intelligent templates instead of generative AI.
```

### Empty outputs
```
Solution: Check the ./outputs/ folder - files are saved there
```

---

## 🎯 Next Steps After Generating the Campaign

1. **Review Content**: Adjust texts as needed
2. **Implement Ads**: Use the texts in Google Ads, Meta Ads, LinkedIn
3. **Configure CRM**: Implement the email sequence in your ESP
4. **Execute SEO**: Follow the content plan and keywords
5. **Measure Results**: Track the metrics defined in the goals

---

## 📞 Support

- Logs: `./logs/mcp-marketing-suite.log`
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- API Docs: `http://localhost:8080/api-docs`
- Outputs: `./outputs/`

---

## 📚 Additional Documentation

- **[Quick Start](QUICKSTART.md)** - Get running in 5 minutes
- **[Configuration](CONFIGURATION.md)** - Setup and environment variables
- **[API Reference](API.md)** - Complete API documentation
- **[Architecture](ARCHITECTURE.md)** - System design and patterns
- **[Troubleshooting](TROUBLESHOOTING.md)** - Common issues and solutions

---

**Ready! You can now generate complete and professional marketing campaigns with real data! 🚀**

