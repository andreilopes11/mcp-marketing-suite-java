# Quick Start Guide - MCP Marketing Suite

## ⚡ TL;DR - Get Running in 2 Minutes

```bash
# 1. Check Java version (must be 23)
java -version

# 2. Clone and enter directory
git clone <repo-url>
cd mcp-marketing-suite-java

# 3. Run tests (optional but recommended)
mvn clean test

# 4. Run the application
mvn spring-boot:run

# 5. Test it's working
curl http://localhost:8080/health
```

Visit: http://localhost:8080/swagger-ui.html

---

## 📋 Prerequisites Check

### Java 23
```bash
java -version
# ✅ Should output: java version "23.x.x"
# ❌ If not, install Java 23 from:
#    - https://www.oracle.com/java/technologies/downloads/#java23
#    - https://jdk.java.net/23/
```

### Maven
```bash
mvn -version
# ✅ Should output: Apache Maven 3.8.x or higher
# ❌ If not, install from: https://maven.apache.org/download.cgi
```

---

## 🚀 Running the Application

### Option 1: Maven (Recommended)
```bash
mvn spring-boot:run
```

**Advantages:**
- Handles all dependencies
- No Java version conflicts
- Hot reload during development

### Option 2: Pre-built JAR
```bash
# First, build the project
mvn clean package -DskipTests

# Then run (requires Java 23 in PATH)
java -jar target/mcp-marketing-suite-0.1.0-SNAPSHOT.jar
```

**Note:** If you get `UnsupportedClassVersionError`, your `java` command is using an older version. Use Option 1 instead.

### Option 3: Docker (Coming Soon)
```bash
docker-compose up
```

---

## 🧪 Testing the Application

### 1. Health Check
```bash
curl http://localhost:8080/health
```

**Expected Response:**
```json
{
  "service": "MCP Marketing Suite",
  "version": "0.1.0-SNAPSHOT",
  "status": "UP",
  "timestamp": "2026-01-14T10:00:00"
}
```

### 2. Generate Ads (Example)
```bash
curl -X POST http://localhost:8080/api/marketing/ads \
  -H "Content-Type: application/json" \
  -d '{
    "product": "AI-Powered Analytics Platform",
    "audience": "B2B Data Scientists",
    "brandVoice": "Professional",
    "goals": ["Increase Trial Signups", "Boost Brand Awareness"]
  }'
```

### 3. Swagger UI
Visit: http://localhost:8080/swagger-ui.html

Try all endpoints interactively!

---

## 🔧 Configuration

### Without OpenAI API Key (Default)
The application runs with a dummy AI model that returns placeholder responses.

**Good for:**
- Testing the application structure
- Development
- CI/CD pipelines

### With OpenAI API Key (Full Features)

**Linux/Mac/Git Bash:**
```bash
export OPENAI_API_KEY="sk-your-key-here"
export LLM_MODEL="gpt-4"
mvn spring-boot:run
```

**Windows CMD:**
```cmd
set OPENAI_API_KEY=sk-your-key-here
set LLM_MODEL=gpt-4
mvn spring-boot:run
```

**Windows PowerShell:**
```powershell
$env:OPENAI_API_KEY="sk-your-key-here"
$env:LLM_MODEL="gpt-4"
mvn spring-boot:run
```

**Via application.yml:**
```yaml
mcp:
  marketing:
    llm:
      api-key: "sk-your-key-here"
      model: "gpt-4"
```

---

## 🎯 All Available Endpoints

### Health & Documentation
- **Health Check:** `GET /health`
- **Swagger UI:** `GET /swagger-ui.html`
- **API Docs (JSON):** `GET /api-docs`
- **Actuator:** `GET /actuator/health`

### Marketing Tools
- **Generate Ads:** `POST /api/marketing/ads`
- **Generate CRM Sequences:** `POST /api/marketing/crm-sequences`
- **Generate SEO Plan:** `POST /api/marketing/seo-plan`
- **Generate Strategy:** `POST /api/marketing/strategy`

---

## 🏗️ Project Structure

```
mcp-marketing-suite-java/
├── src/main/java/com/mcp/marketing/
│   ├── Application.java                # Main entry point
│   ├── config/                         # Configuration classes
│   ├── controller/                     # REST controllers
│   ├── service/                        # Business logic
│   ├── tool/                           # Marketing tools
│   ├── model/                          # Data models
│   ├── mcp/                            # MCP resources
│   └── observability/                  # Logging & tracing
├── src/main/resources/
│   ├── application.yml                 # Main configuration
│   ├── application-dev.yml             # Dev profile
│   ├── banner.txt                      # Startup banner
│   └── logback-spring.xml              # Logging config
├── src/test/                           # Unit & integration tests
├── docs/                               # Documentation
├── examples/                           # Example requests
└── pom.xml                             # Maven configuration
```

---

## 🧩 Running Tests

### Run All Tests
```bash
mvn clean test
```

**Expected Output:**
```
Tests run: 4, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

### Run Specific Test
```bash
mvn test -Dtest=MarketingServiceTest
```

### Skip Tests (for quick builds)
```bash
mvn clean install -DskipTests
```

---

## 🐛 Common Issues

### Issue: "java: release version 23 not supported"
**Solution:** Your Maven is using a different Java version.
```bash
# Check what Maven is using
mvn -version

# Set JAVA_HOME
export JAVA_HOME=/path/to/jdk-23
export PATH=$JAVA_HOME/bin:$PATH
```

### Issue: "Port 8080 already in use"
**Solution 1 - Stop the other process:**
```bash
# Windows
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# Linux/Mac
lsof -ti:8080 | xargs kill -9
```

**Solution 2 - Use different port:**
```bash
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=9090
```

### Issue: Tests fail with ByteBuddy errors
**Solution:** Already fixed! Update your code:
```bash
git pull origin main
mvn clean install
```

### Issue: UnsupportedClassVersionError
**Solution:** You're running with Java < 23. Use Maven instead:
```bash
mvn spring-boot:run
```

For more issues, see [TROUBLESHOOTING.md](TROUBLESHOOTING.md)

---

## 📚 Next Steps

1. ✅ Application is running
2. 📖 Read [API Documentation](API.md)
3. 🔧 Check [Configuration Guide](CONFIGURATION.md)
4. 🏗️ Read [Architecture Overview](ARCHITECTURE.md)
5. 🚀 See [Improvement Suggestions](IMPROVEMENTS.md)
6. 🤝 Read [Contributing Guide](CONTRIBUTING.md)

---

## 🆘 Getting Help

1. **Documentation:** Check `docs/` folder
2. **Examples:** See `examples/` folder
3. **Logs:** Check `logs/mcp-marketing-suite.log`
4. **Issues:** Open a GitHub issue
5. **Community:** Join our discussions

---

## ✅ Success Checklist

- [ ] Java 23 installed and in PATH
- [ ] Maven 3.8+ installed
- [ ] Project cloned
- [ ] Tests passing (`mvn clean test`)
- [ ] Application running (`mvn spring-boot:run`)
- [ ] Health check returns 200 OK
- [ ] Swagger UI accessible
- [ ] Can generate ads via API

**All checked?** 🎉 You're ready to use MCP Marketing Suite!

---

## 📊 Current Status

| Component | Status | Notes |
|-----------|--------|-------|
| Java 23 Support | ✅ | Fully configured |
| Spring Boot 3.3.0 | ✅ | Latest compatible version |
| All Tests | ✅ | 4/4 passing |
| ByteBuddy | ✅ | v1.15.10 (Java 23 support) |
| Dummy LLM Model | ✅ | Works without API key |
| OpenAI Integration | ✅ | Optional, configurable |
| REST API | ✅ | All endpoints working |
| Swagger UI | ✅ | Interactive documentation |
| Health Checks | ✅ | Actuator enabled |
| Logging | ✅ | Request ID correlation |

**Build:** [![BUILD SUCCESS](https://img.shields.io/badge/build-passing-brightgreen)]()
**Tests:** [![4/4 passing](https://img.shields.io/badge/tests-4%2F4%20passing-brightgreen)]()
**Java:** [![Java 23](https://img.shields.io/badge/Java-23-orange)]()

