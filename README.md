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

### Health Check
```
GET /health
```

### Marketing Operations

#### Generate Ads
```
POST /api/marketing/ads
Content-Type: application/json

{
  "product": "SaaS Analytics Platform",
  "audience": "B2B Data Scientists",
  "brand_voice": "Professional, Data-Driven",
  "goals": ["Increase Trial Signups", "Brand Awareness"]
}
```

#### Generate CRM Sequences
```
POST /api/marketing/crm-sequences
```

#### Generate SEO Plan
```
POST /api/marketing/seo-plan
```

#### Generate Full Strategy
```
POST /api/marketing/strategy
```

## Project Structure
```
src/
├── main/
│   ├── java/
│   │   └── com/mcp/marketing/
│   │       ├── McpMarketingApplication.java
│   │       ├── config/          # Configuration classes
│   │       ├── controller/      # REST controllers
│   │       ├── service/         # Business logic
│   │       ├── agent/           # AI agents
│   │       ├── mcp/             # MCP server implementation
│   │       ├── tool/            # Marketing tools
│   │       ├── model/           # Domain models
│   │       └── observability/   # Tracing and logging
│   └── resources/
│       ├── application.yml
│       └── logback-spring.xml
└── test/
    └── java/
        └── com/mcp/marketing/
```

## Configuration

Spring Boot automatically reads environment variables and applies them to `application.yml`. 

### Available Environment Variables

| Variable | Default | Description |
|----------|---------|-------------|
| `OPENAI_API_KEY` | - | OpenAI API key (required for AI features) |
| `LLM_MODEL` | `gpt-4` | LLM model to use |
| `ENABLE_AI_AGENTS` | `true` | Enable/disable AI agent orchestration |
| `OUTPUT_DIR` | `./outputs` | Directory for generated artifacts |

### Configuration File

The `application.yml` uses Spring Boot's standard property placeholder syntax:

```yaml
mcp:
  marketing:
    llm:
      provider: openai
      api-key: ${OPENAI_API_KEY:}  # Reads from environment variable
      model: ${LLM_MODEL:gpt-4}    # With default fallback
      temperature: 0.7
      max-tokens: 2000
    output-directory: ${OUTPUT_DIR:./outputs}
    enable-ai-agents: ${ENABLE_AI_AGENTS:true}
```

### Development Configuration

For local development, you can:

1. **Use IDE environment variables** (IntelliJ IDEA, Eclipse, VS Code)
2. **Override in application-dev.yml**:
```yaml
mcp:
  marketing:
    llm:
      api-key: "sk-your-dev-key"
```
3. **Pass as command-line arguments**:
```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--mcp.marketing.llm.api-key=sk-..."
```
      provider: openai
      api-key: ${OPENAI_API_KEY}
      model: gpt-4
    output-directory: ./outputs
```

## Contributing

Contributions are welcome! Please:
1. Fork the repository
2. Create a feature branch
3. Submit a pull request with tests

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Community

- **Issues**: Report bugs or request features via GitHub Issues
- **Discussions**: Share ideas and ask questions in GitHub Discussions
- **Pull Requests**: All contributions are appreciated!

## Roadmap

- [ ] Integration with real CRM systems (HubSpot, Salesforce)
- [ ] Multi-language support beyond EN/PT
- [ ] Advanced analytics dashboard
- [ ] Plugin system for custom tools
- [ ] Workflow versioning and rollback
- [ ] Team collaboration features

## Support

For questions or support, please open an issue or reach out via discussions.

