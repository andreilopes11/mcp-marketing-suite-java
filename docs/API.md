# API Documentation

## Base URL
```
http://localhost:8080
```

## Authentication
Currently, no authentication is required. This will be added in future versions.

## Endpoints

### Health Check

#### GET /health
Returns the health status of the service.

**Response:**
```json
{
  "status": "UP",
  "timestamp": "2026-01-13T10:30:00",
  "service": "MCP Marketing Suite",
  "version": "0.1.0-SNAPSHOT"
}
```

---

### Generate Ads

#### POST /api/marketing/ads
Generate advertising creatives for multiple platforms.

**Request Body:**
```json
{
  "product": "SaaS Analytics Platform",
  "audience": "B2B Data Scientists",
  "brand_voice": "Professional, Data-Driven",
  "goals": ["Increase Trial Signups", "Brand Awareness"],
  "language": "en",
  "competitors": "Tableau, Power BI",
  "budget_range": "$5000-$10000",
  "campaign_duration": "3 months"
}
```

**Response:**
```json
{
  "request_id": "abc12345",
  "status": "success",
  "data": {
    "google_ads": { ... },
    "meta_ads": { ... },
    "linkedin_ads": { ... },
    "qa_score": { ... },
    "recommendations": [ ... ]
  },
  "output_path": "./outputs/ads_abc12345_20260113_103000.json",
  "timestamp": "2026-01-13T10:30:00",
  "message": "Ads generated successfully",
  "execution_time_ms": 1234
}
```

---

### Generate CRM Sequences

#### POST /api/marketing/crm-sequences
Generate automated email nurture sequences.

**Request Body:**
```json
{
  "product": "AI-Powered CRM Suite",
  "audience": "Sales Managers",
  "brand_voice": "Friendly, Professional",
  "goals": [
    "Generate Qualified Leads",
    "Increase Customer Engagement"
  ],
  "language": "en",
  "competitors": "HubSpot, Salesforce",
  "budget_range": "$3k-$6k",
  "campaign_duration": "6 weeks"
}
```

**Response:**
```json
{
  "request_id": "def67890",
  "status": "success",
  "data": {
    "sequence_name": "SaaS Analytics Platform Nurture Campaign",
    "target_audience": "B2B Data Scientists",
    "emails": [ ... ],
    "timing": { ... },
    "success_metrics": { ... }
  },
  "output_path": "./outputs/crm_sequence_def67890_20260113_103000.json",
  "timestamp": "2026-01-13T10:30:00",
  "message": "CRM sequence generated successfully",
  "execution_time_ms": 2345
}
```

---

### Generate SEO Strategy

#### POST /api/marketing/seo-plan
Generate comprehensive SEO strategy with keywords and tactics.

**Request Body:**
```json
{
  "product": "Cloud Project Management Tool",
  "audience": "IT Project Managers",
  "brand_voice": "Authoritative, Innovative",
  "goals": [
    "Organic Traffic Growth",
    "Lead Generation",
    "Thought Leadership"
  ],
  "language": "en",
  "competitors": "Jira, Asana, Monday.com",
  "budget_range": "$4k-$8k",
  "campaign_duration": "Quarterly"
}
```

**Response:**
```json
{
  "request_id": "ghi12345",
  "status": "success",
  "data": {
    "strategy_overview": { ... },
    "keyword_strategy": { ... },
    "content_plan": { ... },
    "technical_seo": { ... },
    "link_building": { ... },
    "performance_tracking": { ... }
  },
  "output_path": "./outputs/seo_strategy_ghi12345_20260113_103000.json",
  "timestamp": "2026-01-13T10:30:00",
  "message": "SEO strategy generated successfully",
  "execution_time_ms": 1567
}
```

---

### Generate Full GTM Strategy

#### POST /api/marketing/strategy
Generate complete go-to-market strategy including ads, CRM, and SEO.

**Request Body:**
```json
{
  "product": "AI Growth Suite",
  "audience": "SaaS Growth Teams",
  "brand_voice": "Professional",
  "goals": [
    "Launch EU expansion",
    "Increase SQLs 25%",
    "Improve retention"
  ],
  "language": "en",
  "competitors": "GrowthPilot, ScaleOps",
  "budget_range": "$50k-$75k",
  "campaign_duration": "Q1 2026"
}
```

**Response:**
```json
{
  "request_id": "jkl67890",
  "status": "success",
  "data": {
    "ads": { ... },
    "crm_sequence": { ... },
    "seo_strategy": { ... }
  },
  "timestamp": "2026-01-13T10:30:00",
  "message": "Full GTM strategy generated successfully",
  "execution_time_ms": 4567
}
```

---

## Error Responses

All endpoints return standard error responses:

```json
{
  "request_id": "xyz12345",
  "status": "error",
  "timestamp": "2026-01-13T10:30:00",
  "message": "Error description here",
  "execution_time_ms": 100
}
```

## Rate Limiting
Currently not implemented. Will be added in future versions.

## Best Practices

1. **Always review outputs**: All generated content should be reviewed by humans before publication
2. **Provide detailed context**: More specific inputs lead to better outputs
3. **Save outputs**: All outputs are automatically saved to the `outputs/` directory
4. **Monitor execution time**: Use the `execution_time_ms` field to track performance
5. **Use request IDs**: Track requests using the `request_id` for debugging and tracing

