# MCP Marketing Suite (Java)

Open-source marketing copilot built on top of MCP, Spring Boot, and Langchain4j to orchestrate end-to-end workflows that output ads, CRM sequences, SEO plans, and strategy decks. The goal is to share my MCP stack so the community can collaborate, learn, and evolve the solution together.

## Overview
- **Purpose**: accelerate marketing deliverables with governance, traceability, and simple integration into existing pipelines.
- **Operating model**: structured inputs arrive via the MCP server or HTTP API; the orchestrator queries contextual resources, runs deterministic tools, and stores outputs inside `outputs/`.
- **Project status**: Open alpha. Contributions are welcome—new tools, data providers, or orchestration tweaks.

## Key Capabilities
- Coordinated generation of strategy, paid media, CRM, and SEO assets from a single context payload.
- MCP server with mocked resources to simulate product, audience, brand, and competitors.
- REST API endpoints for synchronous trigger and retrieval of artifacts.
- Lightweight observability (JSON logs + basic tracing) so every request is inspectable.
- AI agent orchestration for multi-step marketing workflows.

## Priority Use Cases
- **Agencies and in-house squads**: bootstrapping multi-channel campaign kits for clients or internal launches.
- **SaaS growth teams**: standardizing briefs while iterating on hypotheses and messaging.
- **Community / education**: showcasing MCP + AI orchestration best practices in workshops, bootcamps, or hackathons.
- **MCP builders**: customizing tools with real inputs and contributing new public resources.

## Operating Scenarios
1. **Ads-first execution**: send product, tone, and goals to receive ready-to-use JSON for Google, Meta, and LinkedIn plus QA scoring.
2. **Full GTM playbook**: activate every channel to get strategy, SEO plan, and CRM sequences for launch squads.
3. **Deterministic mode**: run only the built-in tools (no AI agents) for air-gapped or credential-less environments.

## Technical Limitations
- Short-lived memory per request; there is no stateful history between executions.
- Resources are mocked in memory. External connectors must be coded manually.
- No automatic LLM provider fallback if AI execution fails.
- Observability is limited to logs and lightweight tracing; no prebuilt dashboards.

## Usage Limitations
- Outputs are prototypes; human review is mandatory before publishing anything.
- Tools are tuned for Portuguese or English. Other languages are not optimized.
- No native versioning for artifacts; move the final bundle to your canonical storage.

## Architecture
- **MCP Server**: resources `product`, `audience`, `brand`, `competitors` plus validated tools.
- **AI Orchestration**: specialized agents coordinated by a short-term-memory orchestrator.
- **Spring Boot REST API**: exposes REST endpoints (`/api/marketing/*`, `/health`).
- **Observability**: JSON logs with `request_id` correlation and spans via tracing utilities.

## Tech Stack
- **Java 17+**
- **Spring Boot 3.2+**
- **Langchain4j** for AI orchestration
- **Maven** for dependency management
- **SLF4J + Logback** for logging
- **OpenAPI/Swagger** for API documentation

## Getting Started

### Quick Start

For a step-by-step guide to get up and running in 5 minutes, see the **[Quick Start Guide](docs/QUICKSTART.md)**.

For detailed configuration options, see **[Configuration Guide](docs/CONFIGURATION.md)**.

### Prerequisites
- Java 17 or higher
- Maven 3.8+
- OpenAI API key (optional, for AI-enhanced features)

### Basic Installation

1. Clone the repository:
```bash
git clone https://github.com/yourusername/mcp-marketing-suite-java.git
cd mcp-marketing-suite-java
```

2. Configure environment variables (optional for AI features):

**Linux/Mac/Git Bash:**
```bash
export OPENAI_API_KEY="your-api-key-here"
export LLM_MODEL="gpt-4"
```

**Windows CMD:**
```cmd
set OPENAI_API_KEY=your-api-key-here
set LLM_MODEL=gpt-4
```

**Windows PowerShell:**
```powershell
$env:OPENAI_API_KEY="your-api-key-here"
$env:LLM_MODEL="gpt-4"
```

**Or edit `application.yml` directly for development**

3. Verify your configuration (optional):
```bash
./check-config.sh
```

4. Build and run:
```bash
mvn clean install
mvn spring-boot:run
```

The API will be available at `http://localhost:8080`

### Docker Deployment

```bash
mvn clean package -DskipTests
docker-compose up --build
```

### API Documentation
Once running, visit:
- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **API Docs**: See [API Documentation](docs/API.md)

## API Endpoints

For complete API documentation, see [API Reference](docs/API.md) or visit the Swagger UI at `http://localhost:8080/swagger-ui.html`

### Quick Examples

**Health Check**
```bash
curl http://localhost:8080/health
```

**Generate Ads**
```bash
curl -X POST http://localhost:8080/api/marketing/ads \
  -H "Content-Type: application/json" \
  -d '{
    "product": "SaaS Analytics Platform",
    "audience": "B2B Data Scientists",
    "brand_voice": "Professional, Data-Driven",
    "goals": ["Increase Trial Signups"]
  }'
```

**Other Endpoints**
- `POST /api/marketing/crm-sequences` - Generate CRM sequences
- `POST /api/marketing/seo-plan` - Generate SEO strategy
- `POST /api/marketing/strategy` - Generate full GTM strategy

## Project Structure
```
src/main/java/com/mcp/marketing/
├── McpMarketingApplication.java    # Main application
├── config/                         # Configuration
├── controller/                     # REST endpoints
├── service/                        # Business logic
├── tool/                          # Marketing tools
├── mcp/                           # MCP resources
├── model/                         # Domain models
└── observability/                 # Logging & tracing
```

For detailed architecture, see [Architecture Guide](docs/ARCHITECTURE.md).

## Configuration

### Environment Variables

| Variable | Default | Description |
|----------|---------|-------------|
| `OPENAI_API_KEY` | - | OpenAI API key (required for AI) |
| `LLM_MODEL` | `gpt-4` | LLM model identifier |
| `ENABLE_AI_AGENTS` | `true` | Enable/disable AI orchestration |
| `OUTPUT_DIR` | `./outputs` | Output directory for artifacts |

Spring Boot automatically reads environment variables. See [Configuration Guide](docs/CONFIGURATION.md) for detailed setup.

## Contributing

Contributions are welcome! See [Contributing Guide](docs/CONTRIBUTING.md) for details.

## Documentation

- [Quick Start Guide](docs/QUICKSTART.md) - Get started in 5 minutes
- [Configuration Guide](docs/CONFIGURATION.md) - Setup and environment variables
- [API Reference](docs/API.md) - Complete API documentation
- [Architecture Guide](docs/ARCHITECTURE.md) - System design and patterns
- [Project Summary](docs/PROJECT_SUMMARY.md) - What's been built

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.


