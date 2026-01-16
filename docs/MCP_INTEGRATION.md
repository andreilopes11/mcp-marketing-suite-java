# MCP Java SDK Integration Guide

## Overview

This guide documents the integration of the official [MCP Java SDK](https://github.com/modelcontextprotocol/java-sdk)
into the MCP Marketing Suite. The integration enables protocol-compliant communication with any MCP client (Claude
Desktop, Cursor, VS Code extensions, etc.).

## What Changed

### Architecture

The application now exposes a **dual-interface architecture**:

1. **REST API** (`/api/marketing/*`) - Traditional HTTP endpoints for direct API access
2. **MCP Protocol** (`/mcp/*`) - Standard MCP endpoints for AI assistant integration

Both interfaces use the same underlying services, ensuring consistent behavior.

### Dependencies Added

```xml
<!-- MCP SDK BOM -->
<dependency>
    <groupId>io.modelcontextprotocol.sdk</groupId>
    <artifactId>mcp-bom</artifactId>
    <version>0.16.0</version>
    <type>pom</type>
    <scope>import</scope>
</dependency>

<!-- MCP Core -->
<dependency>
    <groupId>io.modelcontextprotocol.sdk</groupId>
    <artifactId>mcp-core</artifactId>
</dependency>

<!-- MCP JSON Jackson -->
<dependency>
    <groupId>io.modelcontextprotocol.sdk</groupId>
    <artifactId>mcp-json-jackson2</artifactId>
</dependency>

<!-- MCP Spring WebMVC Transport -->
<dependency>
    <groupId>io.modelcontextprotocol.sdk</groupId>
    <artifactId>mcp-spring-webmvc</artifactId>
</dependency>

<!-- Reactor Core (for MCP async) -->
<dependency>
    <groupId>io.projectreactor</groupId>
    <artifactId>reactor-core</artifactId>
</dependency>
```

### New Components

#### 1. McpSdkConfiguration

- Configures MCP server beans
- Loads server properties from `application.yml`
- Manages server lifecycle

#### 2. McpResourceHandler

- Exposes marketing resources via MCP protocol
- Converts internal models to MCP-compatible maps
- Supports: `product`, `audience`, `brand`, `competitors`

#### 3. McpToolHandler

- Exposes marketing tools via MCP protocol
- Wraps existing tool implementations
- Supports: `generateAds`, `generateCrmSequence`, `generateSeoStrategy`, `generateFullStrategy`

#### 4. McpServerController

- REST controller for MCP endpoints
- Handles resource queries and tool invocations
- Provides SSE endpoint for streaming

#### 5. McpClientService (Optional)

- Internal MCP client for dogfooding
- Allows services to consume resources via MCP protocol
- Fallback to direct provider access

## Configuration

### application.yml

```yaml
mcp:
  sdk:
    enabled: true  # Enable/disable MCP SDK integration
    server:
      name: mcp-marketing-suite-server
      version: 0.1.0
      endpoint: /mcp
      transport: sse
    resources:
      enabled: true  # Enable resource exposure
    tools:
      enabled: true  # Enable tool exposure
```

### Environment Variables

- `MCP_SDK_ENABLED` - Enable/disable MCP integration (default: true)

## MCP Protocol Endpoints

### Server Info

```
GET /mcp/info
```

Returns server metadata and capabilities.

### List Resources

```
GET /mcp/resources
```

Lists all available resources (product, audience, brand, competitors).

### Get Resource

```
GET /mcp/resources/{type}/{id}
```

Fetches a specific resource.

Examples:

- `/mcp/resources/product/saas-platform`
- `/mcp/resources/audience/b2b-tech`
- `/mcp/resources/brand/professional`
- `/mcp/resources/competitors/saas-market`

### List Tools

```
GET /mcp/tools
```

Lists all available marketing tools with schemas.

### Execute Tool

```
POST /mcp/tools/{toolName}
Content-Type: application/json

{
  "product": "SaaS Platform",
  "audience": "B2B Tech Companies",
  "goals": "Lead Generation"
}
```

Available tools:

- `generateAds` - Generate multi-platform ads
- `generateCrmSequence` - Generate email sequences
- `generateSeoStrategy` - Generate SEO plan
- `generateFullStrategy` - Generate complete GTM strategy

### Health Check

```
GET /mcp/health
```

MCP server health status.

### SSE Endpoint

```
GET /mcp/sse
```

Server-Sent Events endpoint for streaming responses.

## Benefits

### 1. Protocol Compliance

- Full adherence to official MCP specification
- Automatic compatibility with all MCP clients
- Future-proof as the protocol evolves

### 2. Broader Ecosystem Integration

- Works with Claude Desktop out-of-the-box
- Compatible with Cursor IDE
- Integrates with VS Code MCP extensions
- Accessible to any MCP-compliant tool

### 3. Standardized Communication

- Consistent request/response schemas
- Built-in error handling
- Standard authentication hooks (future)

### 4. Better Developer Experience

- No custom protocol implementation needed
- Comprehensive SDK documentation
- Active community support

### 5. Enhanced Observability

- SDK provides built-in tracing via Reactor Context
- SLF4J integration for logging
- Easy integration with OpenTelemetry

### 6. Extensibility

- Simple to add new resources
- Easy to expose new tools
- Plugin-based architecture

## How to Use

### From Claude Desktop

1. Configure Claude Desktop to connect to the server:

```json
{
  "mcpServers": {
    "marketing-suite": {
      "url": "http://localhost:8080/mcp/sse"
    }
  }
}
```

2. Ask Claude:

> "Using the marketing-suite MCP server, generate ads for a SaaS platform targeting B2B tech companies focused on lead
> generation."

### From Cursor IDE

1. Install MCP extension in Cursor
2. Configure server connection
3. Use marketing tools directly in your coding workflow

### From REST API (No Change)

The traditional REST API continues to work exactly as before:

```bash
curl -X POST http://localhost:8080/api/marketing/ads \
  -H "Content-Type: application/json" \
  -d '{
    "product": "SaaS Platform",
    "audience": "B2B Tech",
    "goals": "Lead Generation"
  }'
```

## Testing

### Unit Tests

Standard Spring Boot tests continue to work.

### Integration Tests

New MCP integration tests validate protocol compliance:

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"mcp.sdk.enabled=true"})
class McpServerIntegrationTest {
    // Tests for MCP endpoints
}
```

Run tests:

```bash
mvn test
```

### Manual Testing with MCP CLI

```bash
# Install MCP CLI
npm install -g @modelcontextprotocol/cli

# Connect to server
npx @modelcontextprotocol/cli connect http://localhost:8080/mcp

# List resources
resources.list

# Get resource
resources.get product saas-platform

# List tools
tools.list

# Execute tool
tools.call generateAds '{"product":"SaaS","audience":"B2B","goals":"Leads"}'
```

## Migration Path

### Phase 1: Coexistence (Current)

- MCP SDK runs alongside traditional REST API
- Both interfaces available
- Feature flag controls MCP enablement

### Phase 2: Validation

- Run integration tests
- Test with real MCP clients
- Monitor metrics and logs

### Phase 3: Adoption

- Encourage MCP usage
- Document MCP workflows
- Maintain REST API for backward compatibility

### Phase 4: Optimization

- Fine-tune MCP performance
- Add advanced features (streaming, batching)
- Implement authentication/authorization

## Troubleshooting

### MCP Endpoints Not Available

Check configuration:

```yaml
mcp:
  sdk:
    enabled: true
```

Check logs:

```
Initializing MCP Server: mcp-marketing-suite-server v0.1.0
```

### Resource Not Found

Verify resource provider has the requested resource:

```bash
curl http://localhost:8080/mcp/resources
```

### Tool Execution Fails

Check request parameters match schema:

```bash
curl http://localhost:8080/mcp/tools
```

## Next Steps

### Immediate

- [x] Add MCP SDK dependencies
- [x] Create MCP configuration
- [x] Implement resource handlers
- [x] Implement tool handlers
- [x] Add MCP controller
- [x] Write integration tests
- [x] Update documentation

### Short Term

- [ ] Implement full SSE streaming
- [ ] Add authentication/authorization
- [ ] Add rate limiting
- [ ] Implement tool result caching
- [ ] Add Prometheus metrics for MCP endpoints

### Long Term

- [ ] Support additional transports (WebSocket, stdio)
- [ ] Implement advanced MCP features (prompts, sampling)
- [ ] Create MCP client SDKs for other languages
- [ ] Build MCP marketplace integration

## References

- [MCP Java SDK Documentation](https://modelcontextprotocol.io/sdk/java/mcp-overview)
- [MCP Specification](https://modelcontextprotocol.org/docs/concepts/architecture)
- [Spring AI MCP Integration](https://docs.spring.ai/spring-ai/reference/api/mcp/mcp-overview.html)
- [Model Context Protocol GitHub](https://github.com/modelcontextprotocol)
