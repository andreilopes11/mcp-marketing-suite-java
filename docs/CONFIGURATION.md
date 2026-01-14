# Configuration Guide

This guide explains how to configure the MCP Marketing Suite Java application using Spring Boot's standard configuration mechanisms.

## Overview

Unlike Node.js/Python projects that use `.env` files, Spring Boot applications use:
- **application.yml** - Main configuration file
- **Environment Variables** - For sensitive data and deployment-specific values
- **Spring Profiles** - For environment-specific configurations

## Environment Variables

### Core Configuration

| Variable | Required | Default | Description |
|----------|----------|---------|-------------|
| `OPENAI_API_KEY` | No* | - | OpenAI API key for AI-enhanced features |
| `LLM_MODEL` | No | `gpt-4` | LLM model identifier |
| `LLM_TEMPERATURE` | No | `0.7` | LLM temperature (0.0-1.0) |
| `LLM_MAX_TOKENS` | No | `2000` | Maximum tokens per request |
| `ENABLE_AI_AGENTS` | No | `true` | Enable/disable AI agent orchestration |
| `OUTPUT_DIR` | No | `./outputs` | Directory for generated artifacts |

\* Required only if `ENABLE_AI_AGENTS=true`

### How Spring Boot Reads Environment Variables

Spring Boot automatically resolves `${VARIABLE_NAME:default}` placeholders in `application.yml`:

```yaml
mcp:
  marketing:
    llm:
      api-key: ${OPENAI_API_KEY:}  # Reads from env, empty if not set
      model: ${LLM_MODEL:gpt-4}    # Reads from env, defaults to gpt-4
```

## Configuration Methods

### 1. Environment Variables (Recommended for Production)

**Linux/Mac/Git Bash:**
```bash
export OPENAI_API_KEY="sk-your-api-key"
export LLM_MODEL="gpt-4"
export ENABLE_AI_AGENTS="true"
mvn spring-boot:run
```

**Windows CMD:**
```cmd
set OPENAI_API_KEY=sk-your-api-key
set LLM_MODEL=gpt-4
set ENABLE_AI_AGENTS=true
mvn spring-boot:run
```

**Windows PowerShell:**
```powershell
$env:OPENAI_API_KEY="sk-your-api-key"
$env:LLM_MODEL="gpt-4"
$env:ENABLE_AI_AGENTS="true"
mvn spring-boot:run
```

### 2. IDE Configuration (Recommended for Development)

#### IntelliJ IDEA
1. Open Run/Debug Configurations
2. Select your Spring Boot application
3. Add environment variables in "Environment Variables" field:
   ```
   OPENAI_API_KEY=sk-your-api-key;LLM_MODEL=gpt-4
   ```

#### Eclipse
1. Run > Run Configurations
2. Select your application
3. Environment tab > Add variables

#### VS Code
Add to `.vscode/launch.json`:
```json
{
  "configurations": [
    {
      "type": "java",
      "name": "MCP Marketing Suite",
      "request": "launch",
      "mainClass": "com.mcp.marketing.McpMarketingApplication",
      "env": {
        "OPENAI_API_KEY": "sk-your-api-key",
        "LLM_MODEL": "gpt-4"
      }
    }
  ]
}
```

### 3. Command Line Arguments

```bash
mvn spring-boot:run -Dspring-boot.run.arguments="\
  --mcp.marketing.llm.api-key=sk-your-api-key \
  --mcp.marketing.llm.model=gpt-4 \
  --mcp.marketing.enable-ai-agents=true"
```

Or when running the JAR:
```bash
java -jar target/mcp-marketing-suite-*.jar \
  --mcp.marketing.llm.api-key=sk-your-api-key \
  --mcp.marketing.llm.model=gpt-4
```

### 4. Profile-Specific Configuration (Development)

Create `src/main/resources/application-dev.yml`:
```yaml
mcp:
  marketing:
    llm:
      api-key: "sk-your-dev-key"
      model: "gpt-3.5-turbo"  # Cheaper for testing
    enable-ai-agents: true
    output-directory: "./dev-outputs"

logging:
  level:
    com.mcp.marketing: TRACE
```

Run with profile:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### 5. Docker Configuration

**docker-compose.yml:**
```yaml
services:
  mcp-marketing:
    image: mcp-marketing-suite:latest
    environment:
      - OPENAI_API_KEY=${OPENAI_API_KEY}
      - LLM_MODEL=gpt-4
      - ENABLE_AI_AGENTS=true
      - OUTPUT_DIR=/app/outputs
    volumes:
      - ./outputs:/app/outputs
```

Run with:
```bash
export OPENAI_API_KEY="sk-your-api-key"
docker-compose up
```

## Security Best Practices

### ❌ Never Do This
```yaml
# Bad: Hardcoded API keys in application.yml
mcp:
  marketing:
    llm:
      api-key: "sk-hardcoded-key-12345"  # NEVER!
```

### ✅ Always Do This
```yaml
# Good: Use environment variables
mcp:
  marketing:
    llm:
      api-key: ${OPENAI_API_KEY:}
```

### Additional Security Tips

1. **Never commit secrets** to version control
2. **Use CI/CD secrets** for deployment (GitHub Secrets, AWS Secrets Manager, etc.)
3. **Rotate API keys** regularly
4. **Use different keys** for dev/staging/production
5. **Restrict API key permissions** when possible

## Running Without AI (Deterministic Mode)

If you don't have an API key or want to run in air-gapped environments:

```bash
export ENABLE_AI_AGENTS=false
mvn spring-boot:run
```

The application will run with deterministic tools only (no LLM calls).

## Verifying Configuration

### Check Environment Variables
```bash
# Linux/Mac/Git Bash
echo $OPENAI_API_KEY

# Windows CMD
echo %OPENAI_API_KEY%

# Windows PowerShell
echo $env:OPENAI_API_KEY
```

### Check Spring Boot Configuration
Once running, check the logs:
```
Initializing OpenAI Chat Model: gpt-4
```

Or if API key is missing:
```
OpenAI API key not configured. AI agents will not be available.
```

### Health Check Endpoint
```bash
curl http://localhost:8080/health
```

## Troubleshooting

### "API key not configured"
- Verify environment variable is set: `echo $OPENAI_API_KEY`
- Check Spring Boot logs for property resolution
- Ensure no typos in variable name

### "Invalid API key"
- Verify key starts with `sk-`
- Check key is active in OpenAI dashboard
- Try using the key in a direct API call

### Configuration Not Applied
- Check Spring profile is correct
- Verify property prefix matches: `mcp.marketing.*`
- Review application startup logs for property binding errors

## Reference

- [Spring Boot Externalized Configuration](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.external-config)
- [Spring Boot Configuration Properties](https://docs.spring.io/spring-boot/docs/current/reference/html/configuration-metadata.html)
- [Spring Profiles](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.profiles)

