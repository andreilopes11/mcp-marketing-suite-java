# Configuration Guide

Complete guide for configuring MCP Marketing Suite using Spring Boot conventions.

## Quick Start

### Set Environment Variables

```bash
# Linux/Mac/Git Bash
export OPENAI_API_KEY="sk-your-api-key"
export LLM_MODEL="gpt-4"

# Windows CMD
set OPENAI_API_KEY=sk-your-api-key
set LLM_MODEL=gpt-4

# Windows PowerShell
$env:OPENAI_API_KEY="sk-your-api-key"
$env:LLM_MODEL="gpt-4"
```

### Run Application

```bash
mvn spring-boot:run
```

---

## Environment Variables

| Variable | Required | Default | Description |
|----------|----------|---------|-------------|
| `OPENAI_API_KEY` | No* | - | OpenAI API key |
| `LLM_MODEL` | No | `gpt-4` | Model identifier |
| `ENABLE_AI_AGENTS` | No | `true` | Enable/disable AI |
| `OUTPUT_DIR` | No | `./outputs` | Output directory |

\* Required only if `ENABLE_AI_AGENTS=true`

---

## Configuration Methods

### 1. IDE Configuration (Development)

**IntelliJ IDEA:**
- Run → Edit Configurations → Environment Variables
- Add: `OPENAI_API_KEY=sk-...;LLM_MODEL=gpt-4`

**VS Code:**
```json
{
  "configurations": [{
    "env": {
      "OPENAI_API_KEY": "sk-...",
      "LLM_MODEL": "gpt-4"
    }
  }]
}
```

### 2. Profile Configuration

Create `application-dev.yml`:
```yaml
mcp:
  marketing:
    llm:
      api-key: ${OPENAI_API_KEY:}
      model: gpt-3.5-turbo  # Cheaper for dev
```

Run with profile:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### 3. Command Line

```bash
java -jar target/mcp-marketing-suite-*.jar \
  --mcp.marketing.llm.api-key=sk-... \
  --mcp.marketing.llm.model=gpt-4
```

### 4. Docker

```yaml
# docker-compose.yml
services:
  app:
    environment:
      - OPENAI_API_KEY=${OPENAI_API_KEY}
      - LLM_MODEL=gpt-4
```

### 5. Kubernetes

```yaml
env:
  - name: OPENAI_API_KEY
    valueFrom:
      secretKeyRef:
        name: api-keys
        key: openai
```

---

## Spring Boot vs Node.js/Python

### Why No `.env` File?

Spring Boot reads environment variables **natively** - no library needed.

**Node.js/Python Pattern (NOT used here):**
```
.env file → dotenv library → process.env
```

**Spring Boot Pattern (USED here):**
```
Environment Variables → application.yml → @ConfigurationProperties
```

### Comparison

| Feature | .env (Node/Python) | Spring Boot |
|---------|-------------------|-------------|
| Native Support | ❌ Needs library | ✅ Built-in |
| Type Safety | ❌ Strings only | ✅ Typed classes |
| Validation | ❌ Manual | ✅ @Validated |
| Profiles | ❌ Multiple files | ✅ application-{profile}.yml |
| Cloud Ready | ⚠️ Manual | ✅ Auto-configured |

---

## Configuration File

`application.yml` uses placeholders:

```yaml
mcp:
  marketing:
    llm:
      api-key: ${OPENAI_API_KEY:}      # From environment
      model: ${LLM_MODEL:gpt-4}        # With default
      temperature: 0.7                  # Fixed value
    enable-ai-agents: ${ENABLE_AI_AGENTS:true}
```

---

## Security Best Practices

### ✅ Do This
- Use environment variables for secrets
- Different keys for dev/staging/prod
- Rotate API keys regularly
- Use cloud secret managers (AWS Secrets Manager, Azure Key Vault)

### ❌ Never Do This
```yaml
# Bad: Hardcoded secrets
mcp:
  marketing:
    llm:
      api-key: "sk-hardcoded-key"  # NEVER!
```

---

## Troubleshooting

### "API key not configured"

Check if variable is set:
```bash
echo $OPENAI_API_KEY      # Linux/Mac
echo %OPENAI_API_KEY%     # Windows CMD
echo $env:OPENAI_API_KEY  # PowerShell
```

If not set:
```bash
export OPENAI_API_KEY="sk-..."  # Linux/Mac
set OPENAI_API_KEY=sk-...       # Windows CMD
$env:OPENAI_API_KEY="sk-..."    # PowerShell
```

### Variable not recognized

Restart terminal or run in same session:
```bash
export OPENAI_API_KEY="sk-..." && mvn spring-boot:run
```

### Run without API key

Disable AI agents:
```bash
export ENABLE_AI_AGENTS=false
mvn spring-boot:run
```

---

## Validation Script

Use the provided script to validate configuration:

```bash
./check-config.sh
```

Output:
```
🔍 MCP Marketing Suite - Configuration Validation
==================================================
✓ OPENAI_API_KEY: SET (sk-proj...L4wA)
✓ Java: INSTALLED (java version "17.0.x")
✓ Maven: INSTALLED (Apache Maven 3.9.x)
✓ application.yml: FOUND
==================================================
✓ Configuration is valid!
```

---

## Migration from .env

If migrating from Node.js/Python project:

**Before:**
```bash
# .env
OPENAI_API_KEY=sk-abc123
```

**After:**
```bash
# System environment
export OPENAI_API_KEY="sk-abc123"
```

The `application.yml` automatically reads from environment variables - no `.env` file needed!

---

## Reference

- [Spring Boot Externalized Configuration](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.external-config)
- [Spring Boot Configuration Properties](https://docs.spring.io/spring-boot/docs/current/reference/html/configuration-metadata.html)

