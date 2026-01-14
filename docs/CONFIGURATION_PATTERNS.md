# Configuration Best Practices: Java Spring Boot vs Node.js/Python

## Why No `.env` File?

This is a **Java Spring Boot** project, not Node.js or Python. Different ecosystems have different configuration patterns.

## Configuration Patterns by Ecosystem

### ❌ Node.js/Python Pattern (NOT used here)
```
.env file → dotenv library → process.env
```

**Files:**
- `.env` (gitignored)
- `.env.example` (committed)

**Usage:**
```javascript
// Node.js
require('dotenv').config();
const apiKey = process.env.OPENAI_API_KEY;
```

```python
# Python
from dotenv import load_dotenv
load_dotenv()
api_key = os.getenv('OPENAI_API_KEY')
```

### ✅ Spring Boot Pattern (USED here)
```
Environment Variables → application.yml → @ConfigurationProperties
```

**Files:**
- `application.yml` (committed, with placeholders)
- `application-{profile}.yml` (optional, for different environments)

**Usage:**
```yaml
# application.yml
mcp:
  marketing:
    llm:
      api-key: ${OPENAI_API_KEY:}  # Reads from system environment
```

```java
// Java
@ConfigurationProperties(prefix = "mcp.marketing")
public class MarketingProperties {
    private LlmConfig llm;
    // Spring Boot automatically injects values
}
```

## Why Spring Boot Doesn't Need .env

### 1. **Native Environment Variable Support**
Spring Boot automatically reads system environment variables without any library:

```yaml
# application.yml - this works out of the box
api-key: ${OPENAI_API_KEY:default-value}
```

### 2. **Profiles for Different Environments**
Instead of multiple `.env` files, Spring uses profiles:

```bash
# Development
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Production
java -jar app.jar --spring.profiles.active=prod
```

### 3. **Type-Safe Configuration**
Spring Boot uses strongly-typed configuration classes:

```java
@ConfigurationProperties(prefix = "mcp.marketing")
@Validated
public class MarketingProperties {
    @NotBlank
    private String apiKey;  // Validated at startup
    
    @Min(0) @Max(1)
    private double temperature;
}
```

### 4. **Multiple Configuration Sources**
Spring Boot reads configuration from multiple sources (in order of precedence):
1. Command-line arguments
2. System environment variables
3. `application-{profile}.yml`
4. `application.yml`
5. Default values

## Migration Guide: From .env to Spring Boot

### Before (Node.js/Python)
```bash
# .env
OPENAI_API_KEY=sk-abc123
LLM_MODEL=gpt-4
ENABLE_AI_AGENTS=true
```

```javascript
// app.js
require('dotenv').config();
const apiKey = process.env.OPENAI_API_KEY;
```

### After (Spring Boot)
```yaml
# application.yml
mcp:
  marketing:
    llm:
      api-key: ${OPENAI_API_KEY:}
      model: ${LLM_MODEL:gpt-4}
    enable-ai-agents: ${ENABLE_AI_AGENTS:true}
```

```java
// Configuration is automatic
@Autowired
private MarketingProperties properties;

String apiKey = properties.getLlm().getApiKey();
```

## Setting Environment Variables

### Development
**Option 1: IDE Configuration** (Recommended)
- IntelliJ IDEA: Run Configuration → Environment Variables
- Eclipse: Run Configuration → Environment Tab
- VS Code: launch.json → env

**Option 2: Terminal**
```bash
export OPENAI_API_KEY="sk-abc123"
mvn spring-boot:run
```

**Option 3: Profile-specific file** (for development only)
```yaml
# application-dev.yml
mcp:
  marketing:
    llm:
      api-key: "sk-dev-key"  # Only if in .gitignore!
```

### Production

**Option 1: System Environment Variables**
```bash
export OPENAI_API_KEY="sk-prod-key"
java -jar app.jar
```

**Option 2: Docker/Kubernetes**
```yaml
# docker-compose.yml
environment:
  - OPENAI_API_KEY=${OPENAI_API_KEY}

# kubernetes-deployment.yml
env:
  - name: OPENAI_API_KEY
    valueFrom:
      secretKeyRef:
        name: api-keys
        key: openai
```

**Option 3: Cloud Platform Secrets**
- AWS: Systems Manager Parameter Store or Secrets Manager
- Azure: Key Vault
- GCP: Secret Manager
- Heroku: Config Vars

## Common Pitfalls

### ❌ Don't Mix Patterns
```yaml
# Bad: Trying to use .env in Java
api-key: ${.env.OPENAI_API_KEY}  # This won't work!
```

### ❌ Don't Hardcode Secrets
```yaml
# Bad: Secrets in application.yml
api-key: "sk-hardcoded-123"  # NEVER do this!
```

### ❌ Don't Commit Secrets
```yaml
# Bad: application-dev.yml with secrets committed to Git
api-key: "sk-real-key"  # Add to .gitignore if you do this!
```

### ✅ Use Placeholders
```yaml
# Good: Placeholders in application.yml (committed)
api-key: ${OPENAI_API_KEY:}
```

## When to Use .env in Java Projects?

### Almost Never, But...

If you **really** want `.env` support (e.g., for team familiarity), you can use:

**Option 1: spring-dotenv**
```xml
<dependency>
    <groupId>me.paulschwarz</groupId>
    <artifactId>spring-dotenv</artifactId>
    <version>4.0.0</version>
</dependency>
```

**Option 2: dotenv-java**
```xml
<dependency>
    <groupId>io.github.cdimascio</groupId>
    <artifactId>dotenv-java</artifactId>
    <version>3.0.0</version>
</dependency>
```

**But this is NOT recommended** because:
- Adds unnecessary dependency
- Goes against Spring Boot conventions
- Confuses Java developers
- Makes deployment more complex

## Summary

| Feature | .env (Node/Python) | Spring Boot Native |
|---------|-------------------|-------------------|
| Native Support | ❌ (needs library) | ✅ Built-in |
| Type Safety | ❌ Strings only | ✅ Typed classes |
| Validation | ❌ Manual | ✅ @Validated |
| Profiles | ❌ Multiple .env files | ✅ application-{profile}.yml |
| Cloud Integration | ⚠️ Manual | ✅ Auto-configured |
| IDE Support | ⚠️ Plugin needed | ✅ Native |
| Documentation | ⚠️ External | ✅ Self-documenting |

## Conclusion

**For Java Spring Boot projects:**
- ✅ Use `application.yml` with `${ENV_VAR}` placeholders
- ✅ Set environment variables at the OS/container level
- ✅ Use Spring profiles for different environments
- ❌ Don't use `.env` files (it's a Node.js/Python pattern)

**For Node.js/Python projects:**
- ✅ Use `.env` files with dotenv
- ✅ Commit `.env.example`
- ✅ Gitignore `.env`

Each ecosystem has its own best practices. Follow the conventions of the language you're using.

## References

- [Spring Boot Externalized Configuration](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.external-config)
- [12-Factor App: Config](https://12factor.net/config)
- [Spring Boot Configuration Properties](https://docs.spring.io/spring-boot/docs/current/reference/html/configuration-metadata.html)

