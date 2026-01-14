# Quick Start Guide

Get MCP Marketing Suite running in 5 minutes.

## Prerequisites

- Java 17+
- Maven 3.8+
- (Optional) OpenAI API key

## Installation

### 1. Clone Repository

```bash
git clone https://github.com/yourusername/mcp-marketing-suite-java.git
cd mcp-marketing-suite-java
```

### 2. Set API Key (Optional)

```bash
# Linux/Mac
export OPENAI_API_KEY="sk-your-key"

# Windows CMD
set OPENAI_API_KEY=sk-your-key

# Windows PowerShell
$env:OPENAI_API_KEY="sk-your-key"
```

> Skip this to run without AI: `export ENABLE_AI_AGENTS=false`

### 3. Build & Run

```bash
mvn clean install
mvn spring-boot:run
```

Application starts at: `http://localhost:8080`

---

## Verify Installation

```bash
curl http://localhost:8080/health
```

Response:
```json
{
  "status": "UP",
  "service": "MCP Marketing Suite"
}
```

---

## Your First Request

### Generate Ads

```bash
curl -X POST http://localhost:8080/api/marketing/ads \
  -H "Content-Type: application/json" \
  -d '{
    "product": "SaaS Analytics Platform",
    "audience": "B2B Data Scientists",
    "brand_voice": "Professional, Data-Driven",
    "goals": ["Increase Signups", "Brand Awareness"]
  }'
```

### View Output

```bash
ls outputs/
# ads_<timestamp>.json
```

---

## More Operations

### CRM Sequences

```bash
curl -X POST http://localhost:8080/api/marketing/crm-sequences \
  -H "Content-Type: application/json" \
  -d @examples/crm-request.json
```

### SEO Strategy

```bash
curl -X POST http://localhost:8080/api/marketing/seo-plan \
  -H "Content-Type: application/json" \
  -d @examples/seo-request.json
```

### Full GTM Strategy

```bash
curl -X POST http://localhost:8080/api/marketing/strategy \
  -H "Content-Type: application/json" \
  -d '{
    "product": "My Product",
    "audience": "Target Audience",
    "brand_voice": "Professional",
    "goals": ["Goal 1", "Goal 2"]
  }'
```

---

## Docker Deployment

```bash
# Build
mvn clean package -DskipTests

# Run
docker-compose up
```

---

## API Documentation

Interactive API docs: `http://localhost:8080/swagger-ui.html`

---

## Troubleshooting

### "API key not configured"

```bash
# Check if set
echo $OPENAI_API_KEY

# Set it
export OPENAI_API_KEY="sk-..."
```

### Build fails

```bash
# Clean rebuild
mvn clean install -U
```

### Port already in use

Change port in `application.yml`:
```yaml
server:
  port: 8081
```

---

## Next Steps

- **[Configuration Guide](CONFIGURATION.md)** - Advanced configuration
- **[API Reference](API.md)** - Complete API documentation
- **[Architecture](ARCHITECTURE.md)** - System design

---

For detailed configuration options, see **[Configuration Guide](CONFIGURATION.md)**.

