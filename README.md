# MCP Marketing Suite (Java)

[![Java](https://img.shields.io/badge/Java-23-orange.svg)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.0-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![MCP SDK](https://img.shields.io/badge/MCP%20SDK-0.16.0-blue.svg)](https://modelcontextprotocol.io)
[![Tests](https://img.shields.io/badge/Tests-61%2F61%20passing-success.svg)]()

Deterministic marketing orchestration engine built with **Java 23**, **Spring Boot 3.3**, and the **Model Context Protocol (MCP) Java SDK**. The same domain services power both the REST API and the MCP server, so integrations receive identical payloads, the same `request_id`, and a JSON artifact persisted under `./outputs`.

---

## Quick Checklist

| Step | Command |
|------|---------|
| 1. Clone | `git clone https://github.com/your-org/mcp-marketing-suite-java.git`
| 2. Build | `cd mcp-marketing-suite-java && mvn clean install`
| 3. Run REST API | `mvn spring-boot:run`
| 4. Run MCP server (optional) | `mvn -q package && java -cp target/mcp-marketing-suite-0.1.0-SNAPSHOT.jar com.mcp.marketing.mcp.server.McpMarketingServer`
| 5. Verify | `curl http://localhost:8080/health`

Requirements: Java 23, Maven 3.8+, and a writable `./outputs` directory.

---

## REST API Consumption

### Endpoints

| Method | Path | Purpose |
|--------|------|---------|
| POST | `/api/marketing/ads` | Generates Google/Meta/LinkedIn ads |
| POST | `/api/marketing/seo-plan` | Generates a full SEO plan |
| POST | `/api/marketing/crm-sequences` | Generates CRM/email nurturing flows |
| POST | `/api/marketing/strategy` | Bundles ads + SEO + CRM into one blueprint |
| GET | `/health` | Lightweight service status |

### Request Template

All POST endpoints accept JSON adhering to the request DTO for that artifact. Shared fields:

```json
{
  "product": "Product name",
  "audience": "Who you sell to",
  "brandVoice": "Tone of voice",
  "goals": "Primary goal",
  "language": "en-US | pt-BR | es-ES",
  "...artifact specific fields...": "..."
}
```

### Response Envelope

```json
{
  "status": "SUCCESS",
  "requestId": "req-123",
  "data": {
    "artifact_type": "ads",
    "execution_time_ms": 245,
    "result": { /* deterministic payload */ },
    "output_path": "./outputs/ads/req-123.json"
  },
  "timestamp": "2026-01-19T12:40:15Z"
}
```

### Copy/Paste cURL Examples

```bash
curl -X POST http://localhost:8080/api/marketing/ads \
  -H "Content-Type: application/json" \
  -d '{
    "product": "Growth Marketing Suite",
    "audience": "SaaS startups in North America",
    "brandVoice": "Bold and data-driven",
    "goals": "Increase qualified demos by 30%",
    "language": "en-US",
    "platforms": ["google","meta","linkedin"],
    "budget": "$50K for 90 days",
    "duration": "Q1 2026"
  }'
```

```bash
curl -X POST http://localhost:8080/api/marketing/seo-plan \
  -H "Content-Type: application/json" \
  -d '{
    "product": "AI-powered Support Desk",
    "audience": "Enterprise CX directors",
    "brandVoice": "Trusted and pragmatic",
    "goals": "Own the conversational AI support keyword cluster",
    "language": "en-US",
    "keywords": ["ai support desk","conversational support platform"],
    "domain": "https://supportdesk.ai",
    "monthlyBudget": 15000
  }'
```

```bash
curl -X POST http://localhost:8080/api/marketing/crm-sequences \
  -H "Content-Type: application/json" \
  -d '{
    "product": "Workflow Automation Platform",
    "audience": "Operations leaders in logistics",
    "brandVoice": "Efficient and confident",
    "goals": "Shorten onboarding cycles",
    "language": "en-US",
    "sequenceLength": 4,
    "channels": ["email","sms"],
    "conversionGoal": "Book a 45-minute discovery call"
  }'
```

```bash
curl -X POST http://localhost:8080/api/marketing/strategy \
  -H "Content-Type: application/json" \
  -d '{
    "product": "Unified Revenue Intelligence Cloud",
    "audience": "RevOps leaders in global SaaS",
    "brandVoice": "Insightful and visionary",
    "goals": "Increase pipeline velocity",
    "language": "en-US",
    "marketSegment": "B2B SaaS 500+ employees",
    "competitorAnalysis": "Compete with Clari and Gong; emphasize predictive insights",
    "channels": ["webinars","paid media","field marketing"],
    "timeframe": "H1 2026"
  }'
```

```bash
curl http://localhost:8080/health
```

### Calling from Another API

1. Add an HTTP client to your service (Feign, WebClient, HttpClient, etc.).
2. Serialize your business payload to the DTO expected by the target endpoint.
3. Forward or generate a `request_id` header (`X-Request-Id`) for traceability.
4. Receive the `StandardResponse`, log `requestId` and persist `output_path` if needed.
5. Optionally fetch the saved artifact from `./outputs` for auditing.

---

## MCP Server Consumption

### Run the Server

```bash
mvn -q package
java -cp target/mcp-marketing-suite-0.1.0-SNAPSHOT.jar \
     com.mcp.marketing.mcp.server.McpMarketingServer
```

### Tools Registered

| Tool | Purpose |
|------|---------|
| `ads` | Same payload as REST `/ads` |
| `seo-plan` | Same payload as REST `/seo-plan` |
| `crm-sequences` | Same payload as REST `/crm-sequences` |
| `strategy` | Aggregated artifact |

### Sample MCP Call

```json
{
  "method": "tools/call",
  "params": {
    "name": "ads",
    "arguments": {
      "product": "Cloud CRM",
      "audience": "SMBs",
      "brandVoice": "Professional",
      "goals": "100 leads/month",
      "language": "en-US"
    }
  }
}
```

The response reuses `StandardResponse` and the underlying `StoragePort` persists the file the same way as the REST API. Connect the STDIO transport to Claude Desktop or any MCP-compliant client.

---

## Configuration & Observability

Key properties live in `src/main/resources/application.yml`. Override them via environment variables or `-D` flags.

```yaml
spring:
  application:
    name: mcp-marketing-suite

server:
  port: 8080

app:
  version: 0.1.0
  outputs:
    directory: ./outputs
    enabled: true

logging:
  level:
    root: INFO
    com.mcp.marketing: DEBUG

management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus

springdoc:
  api-docs:
    path: /v3/api-docs
  swagger-ui:
    path: /swagger-ui.html
```

- **Outputs**: disable persistence by setting `APP_OUTPUTS_ENABLED=false`.
- **Logging**: `logback-spring.xml` emits JSON logs with `request_id` in MDC.
- **Banner**: `banner.txt` uses `${spring.application.name}` and `${app.version}`.
- **Management**: `/actuator/health`, `/actuator/info`, `/actuator/prometheus` ready for probes.
- **SpringDoc**: Swagger UI hosted at `/swagger-ui.html`.

---

## Persistence & Logs

- Every successful request saves a JSON artifact under `./outputs/<artifact>/<request_id>.json`.
- All logs include `request_id`, endpoint/tool, status, execution time, and the saved file path.
- Stack traces are logged but never returned to clients thanks to the global exception handler.

---

## Examples & Payloads

```
examples/
├── README.md                # walkthrough
├── ads.sh                   # curl helper scripts (requires bash)
├── crm-sequences.sh
├── health-check.sh
├── seo-plan.sh
├── strategy.sh
└── payloads/
    ├── ads-request.json
    ├── crm-sequences-request.json
    ├── seo-plan-request.json
    └── strategy-request.json
```

To run:

```bash
cd examples
chmod +x ads.sh
API_URL=http://localhost:8080 ./ads.sh
```

---

## Project Structure

```
src/main/java/com/mcp/marketing/
├── api/        # Controllers, DTOs, filters, exception handling
├── domain/     # Models, ValidationService, OrchestratorService
├── infra/      # FileSystemStorage + adapters
├── mcp/        # MCP server, tools, resources
└── config/     # Spring configuration beans
```

Resources: `application.yml`, `logback-spring.xml`, `banner.txt`

---

## Testing

```bash
mvn test
```

Covers domain services, REST controllers, MCP server smoke tests, storage, and error flows (61 tests).

---

## Documentation Map

| Need | Document |
|------|----------|
| Full architecture & sequence diagrams | `docs/ARCHITECTURE.md` |
| Quick MCP bootstrapping | `docs/MCP_QUICK_START.md` |
| Deep-dive MCP internals | `docs/MCP_SERVER_COMPLETE.md` |
| Logging, management, SpringDoc tuning | `docs/COMPLETE_CONFIGURATION.md` |
| Banner configuration | `docs/BANNER_CONFIGURATION.md` |
| Documentation index & navigation | `docs/DOCUMENTATION_INDEX.md` |

All documentation is written in English and reflects the current codebase.

---

## Contributing & License

1. Fork, create a feature branch, and keep the generators deterministic.
2. Add or update tests for every change.
3. Submit a PR with context and logs if relevant.

Licensed under MIT (see `LICENSE`).

---

**Built with using Spring Boot, MCP Java SDK, and clean architecture principles.**

*Last updated: January 19, 2026*
