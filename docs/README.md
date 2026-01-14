# Documentation

Complete documentation for MCP Marketing Suite.

## Getting Started

- **[Quick Start](QUICKSTART.md)** - Get running in 5 minutes
- **[Configuration](CONFIGURATION.md)** - Setup and environment variables
- **[API Reference](API.md)** - Complete API documentation

## Architecture

- **[Architecture](ARCHITECTURE.md)** - System design and patterns
- **[Project Summary](PROJECT_SUMMARY.md)** - What's been built

## Contributing

- **[Contributing Guide](CONTRIBUTING.md)** - How to contribute

## Quick Links

### Configuration
```bash
# Set API key
export OPENAI_API_KEY="sk-your-key"

# Run
mvn spring-boot:run
```

### API Endpoints
- `POST /api/marketing/ads` - Generate ads
- `POST /api/marketing/crm-sequences` - Generate CRM sequences
- `POST /api/marketing/seo-plan` - Generate SEO strategy
- `POST /api/marketing/strategy` - Generate full strategy

### Common Questions

**Q: Do I need a .env file?**  
A: No. Use environment variables directly (Spring Boot native).

**Q: How do I set my API key?**  
A: See [Configuration Guide](CONFIGURATION.md)

**Q: Can I run without an API key?**  
A: Yes. Set `ENABLE_AI_AGENTS=false`

---

For more details, see the individual guides above.

