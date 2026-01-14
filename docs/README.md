# Documentation Index

Welcome to the MCP Marketing Suite documentation!

## 📚 Getting Started

Start here if you're new to the project:

1. **[Quick Start Guide](QUICKSTART.md)** - Get up and running in 5 minutes
2. **[Configuration Guide](CONFIGURATION.md)** - Detailed configuration options
3. **[API Documentation](API.md)** - Complete API reference with examples

## 🔧 Configuration

Everything about configuring the application:

- **[Configuration Guide](CONFIGURATION.md)** - Complete configuration reference
- **[Configuration Patterns](CONFIGURATION_PATTERNS.md)** - Java vs Node.js/Python patterns explained
- **[Environment Variables Quick Reference](ENV_VARS_QUICK_REF.md)** - Quick lookup for all env vars
- **[Configuration Changelog](CHANGELOG_CONFIG.md)** - Recent changes and migration guide

## 🏗️ Architecture & Development

Deep dive into the system:

- **[Architecture](ARCHITECTURE.md)** - System architecture and design patterns
- **[Project Summary](PROJECT_SUMMARY.md)** - Complete overview of what's been built
- **[Contributing Guide](CONTRIBUTING.md)** - How to contribute to the project

## 📖 API Reference

Working with the API:

- **[API Documentation](API.md)** - Complete API reference
- **[Examples](../examples/)** - Request/response examples
  - `ads-request.json` - Ad generation
  - `crm-request.json` - CRM sequences
  - `seo-request.json` - SEO strategy

## 🚀 Deployment

Getting to production:

- **[Docker Configuration](../container/)** - Docker and docker-compose setup
- **[Configuration Guide - Production Section](CONFIGURATION.md#production)** - Production configuration

## ❓ Common Questions

### Configuration

**Q: Do I need a .env file?**
A: No! This is a Java Spring Boot project. Use environment variables directly. See [Configuration Patterns](CONFIGURATION_PATTERNS.md) for why.

**Q: How do I set my API key?**
A: See the [Quick Reference](ENV_VARS_QUICK_REF.md) for your platform-specific commands.

**Q: Can I run without an API key?**
A: Yes! Set `ENABLE_AI_AGENTS=false` for deterministic mode.

### Development

**Q: How do I contribute?**
A: Check the [Contributing Guide](CONTRIBUTING.md).

**Q: Where are the tests?**
A: Unit tests are in `src/test/java/`. Run with `mvn test`.

**Q: How do I debug?**
A: Enable debug logging in `application-dev.yml` and use your IDE's debugger.

## 📝 Document Updates

Last updated: 2026-01-13

Recent changes:
- Removed `.env` pattern in favor of Spring Boot native configuration
- Added comprehensive configuration guides
- Added Java vs Node.js/Python comparison
- Updated all documentation to reflect Spring Boot best practices

## 🔗 External Resources

- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Langchain4j Documentation](https://docs.langchain4j.dev/)
- [OpenAPI Specification](https://swagger.io/specification/)
- [MCP Protocol](https://modelcontextprotocol.io/)

---

**Need help?** Open an issue on GitHub or check the [Quick Start Guide](QUICKSTART.md).

