# Quick Start Guide

Get up and running with MCP Marketing Suite in 5 minutes!

## Prerequisites

- Java 17 or higher
- Maven 3.8+
- Git
- (Optional) OpenAI API key for AI-enhanced features

## Installation

### Step 1: Clone the Repository

```bash
git clone https://github.com/yourusername/mcp-marketing-suite-java.git
cd mcp-marketing-suite-java
```

### Step 2: Configure Environment (Optional)

For AI-enhanced features, set your OpenAI API key as an environment variable:

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

> **Note:** You can skip this step and run in deterministic mode (no AI) by setting `ENABLE_AI_AGENTS=false`
> 
> For more configuration options, see the [Configuration Guide](CONFIGURATION.md)

### Step 3: Build the Project

```bash
mvn clean install
```

### Step 4: Run the Application

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## Verify Installation

### Check Health

```bash
curl http://localhost:8080/health
```

Expected response:
```json
{
  "status": "UP",
  "timestamp": "2026-01-13T10:30:00",
  "service": "MCP Marketing Suite",
  "version": "0.1.0-SNAPSHOT"
}
```

## Your First Request

### Generate Ads

Create a file `my-request.json`:

```json
{
  "product": "SaaS Analytics Platform",
  "audience": "B2B Data Scientists",
  "brand_voice": "Professional, Data-Driven",
  "goals": ["Increase Trial Signups", "Brand Awareness"]
}
```

Send the request:

```bash
curl -X POST http://localhost:8080/api/marketing/ads \
  -H "Content-Type: application/json" \
  -d @my-request.json
```

Or use the provided example:

```bash
curl -X POST http://localhost:8080/api/marketing/ads \
  -H "Content-Type: application/json" \
  -d @examples/ads-request.json
```

### View Output

Check the `outputs/` directory for generated files:

```bash
ls -la outputs/
```

You'll find JSON files with:
- Ad creatives for Google, Meta, LinkedIn
- QA scores
- Recommendations

## Explore More

### Generate CRM Sequences

```bash
curl -X POST http://localhost:8080/api/marketing/crm-sequences \
  -H "Content-Type: application/json" \
  -d @examples/crm-request.json
```

### Generate SEO Strategy

```bash
curl -X POST http://localhost:8080/api/marketing/seo-plan \
  -H "Content-Type: application/json" \
  -d @examples/seo-request.json
```

### Generate Full GTM Strategy

```bash
curl -X POST http://localhost:8080/api/marketing/strategy \
  -H "Content-Type: application/json" \
  -d '{
    "product": "My Product",
    "audience": "My Target Audience",
    "brand_voice": "Professional",
    "goals": ["Goal 1", "Goal 2"]
  }'
```

## Using Docker

### Build and Run with Docker

```bash
# Build the JAR first
mvn clean package -DskipTests

# Build Docker image
docker build -t mcp-marketing-suite .

# Run container
docker run -p 8080:8080 \
  -e OPENAI_API_KEY=your-api-key \
  -v $(pwd)/outputs:/app/outputs \
  mcp-marketing-suite
```

### Or use Docker Compose

```bash
# Set your API key in .env file
echo "OPENAI_API_KEY=your-api-key" > .env

# Build and start
docker-compose up --build
```

## API Documentation

Once running, visit:
- Swagger UI: http://localhost:8080/swagger-ui.html
- API Docs: http://localhost:8080/api-docs

## Testing

Run all tests:

```bash
mvn test
```

Run specific test:

```bash
mvn test -Dtest=MarketingControllerTest
```

## Common Issues

### Issue: Port 8080 already in use

**Solution**: Change the port in `application.yml` or set environment variable:

```bash
SERVER_PORT=9090 mvn spring-boot:run
```

### Issue: OpenAI API key not working

**Solution**: Verify your API key and ensure it's set correctly:

```bash
echo $OPENAI_API_KEY
```

If AI features aren't needed, disable them:

```bash
ENABLE_AI_AGENTS=false mvn spring-boot:run
```

### Issue: Output directory permission error

**Solution**: Create and give permissions:

```bash
mkdir -p outputs
chmod 755 outputs
```

## Next Steps

1. **Read the Documentation**
   - [API Documentation](API.md)
   - [Architecture Overview](ARCHITECTURE.md)
   - [Contributing Guide](CONTRIBUTING.md)

2. **Customize Resources**
   - Edit `McpResourceProvider.java` to add your own product/audience data

3. **Add New Tools**
   - Create custom marketing tools following the pattern in `tool/` package

4. **Integrate with Your Stack**
   - Use the REST API from your applications
   - Deploy to your infrastructure

## Support

- GitHub Issues: Report bugs and request features
- Discussions: Ask questions and share ideas
- Documentation: Comprehensive guides and examples

## Useful Commands

```bash
# Clean build
mvn clean

# Run with specific profile
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Package without tests
mvn package -DskipTests

# Run in debug mode
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"

# View logs
tail -f logs/mcp-marketing-suite.log
```

Happy Marketing! 🚀

