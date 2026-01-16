# Architecture Overview

## System Architecture (With MCP Java SDK Integration)

```
┌─────────────────────────────────────────────────────────────┐
│                    Client Applications                       │
│     (HTTP/REST, MCP Clients, Claude Desktop, Cursor)        │
└─────────────────────┬──────────────────┬────────────────────┘
                      │                  │
                      │                  ▼
                      │      ┌─────────────────────────────┐
                      │      │   MCP Server Controller     │
                      │      │   (MCP Protocol Endpoints)  │
                      │      └────────┬────────────────────┘
                      │               │
                      │               ▼
                      │      ┌─────────────────────────────┐
                      │      │   MCP Resource Handler      │
                      │      │   MCP Tool Handler          │
                      │      └────────┬────────────────────┘
                      │               │
                      ▼               ▼
┌─────────────────────────────────────────────────────────────┐
│                   Spring Boot REST API                       │
│                   (MarketingController)                      │
└─────────────────────────┬───────────────────────────────────┘
                          │
                          ▼
┌─────────────────────────────────────────────────────────────┐
│                   MarketingService                           │
│              (Orchestration & Coordination)                  │
└────────┬────────────────┬────────────────┬──────────────────┘
         │                │                │
         ▼                ▼                ▼
┌────────────────┐ ┌────────────┐ ┌──────────────────┐
│ AdGeneratorTool│ │CrmSequence │ │ SeoStrategyTool  │
└────────┬───────┘ └─────┬──────┘ └────────┬─────────┘
         │               │                  │
         └───────────────┴──────────────────┘
                         │
                         ▼
         ┌───────────────────────────────┐
         │    McpResourceProvider        │
         │  (Product, Audience, Brand,   │
         │       Competitors)            │
         └───────────────┬───────────────┘
                         │
         ┌───────────────┴───────────────┐
         ▼                               ▼
┌────────────────┐            ┌──────────────────┐
│  LLM Provider  │            │ ObservabilityService │
│  (Langchain4j) │            │  (Logging, Tracing)  │
└────────────────┘            └──────────────────┘
         │
         ▼
┌─────────────────────────────────────────────────┐
│            Output Storage (JSON Files)           │
└─────────────────────────────────────────────────┘
```

## Component Responsibilities

### 1. Controllers Layer

- **MarketingController**: Handles HTTP/REST requests, validates input, returns responses
- **McpServerController**: Exposes MCP protocol endpoints for resources and tools
- **HealthController**: Provides health check endpoint

### 2. MCP SDK Layer (NEW)

- **McpSdkConfiguration**: Configures official MCP Java SDK beans and server options
- **McpResourceHandler**: Exposes marketing resources via MCP protocol (product, audience, brand, competitors)
- **McpToolHandler**: Exposes marketing tools via MCP protocol (ads, CRM, SEO generators)
- **McpClientService**: Optional client to consume MCP resources internally (dogfooding)

### 3. Service Layer

- **MarketingService**: Orchestrates tool execution, manages resources, handles AI enhancement
- **OutputService**: Manages file output and directory structure

### 4. Tool Layer

- **AdGeneratorTool**: Generates ads for Google, Meta, LinkedIn
- **CrmSequenceTool**: Creates email nurture sequences
- **SeoStrategyTool**: Develops SEO plans with keywords and tactics
- **BaseMarketingTool**: Base class for common tool functionality

### 5. Resource Layer

- **McpResourceProvider**: Provides contextual resources (product, audience, brand, competitors)
- **FileResourceLoader**: Loads resources from file system
- **JsonResourceLoader**: Parses JSON resource files

### 6. Configuration Layer

- **MarketingProperties**: Application configuration
- **LlmConfiguration**: AI/LLM integration setup
- **McpSdkConfiguration**: MCP SDK server and client configuration
- **CacheConfiguration**: Caffeine cache setup for performance
- **AsyncConfiguration**: Async executor configuration

### 7. Observability Layer

- **ObservabilityService**: Request tracking, logging, tracing, metrics

## Data Flow

### Example 1: MCP Protocol Request (NEW)

1. **MCP Client** (Claude Desktop, Cursor, etc.) connects to `/mcp` endpoint
2. **McpServerController** receives MCP protocol request
3. **McpResourceHandler** or **McpToolHandler**:
    - For resources: fetches from McpResourceProvider
    - For tools: invokes MarketingService methods
4. **MarketingService**:
    - Generates request ID
    - Fetches MCP resources (product, audience, brand)
    - Calls appropriate tool (AdGeneratorTool, CrmSequenceTool, etc.)
5. **Tool**:
    - Generates content
    - Calculates QA scores
    - Saves output to file
    - Returns structured data
6. **McpServerController** formats response according to MCP protocol

### Example 2: REST API Request (Traditional)

1. **Client** sends POST request to `/api/marketing/ads`
2. **MarketingController** validates request, forwards to service
3. **MarketingService**:
    - Generates request ID
    - Fetches MCP resources (product, audience, brand)
    - Calls AdGeneratorTool
4. **AdGeneratorTool**:
    - Generates ads for each platform
    - Calculates QA scores
    - Saves output to file
    - Returns structured data
5. **MarketingService** (optional):
    - Enhances with AI if enabled
    - Tracks execution time
6. **MarketingController** returns response to client

## Key Design Patterns

### 1. Strategy Pattern

Tools implement specific marketing strategies (ads, CRM, SEO)

### 2. Builder Pattern

Used for creating complex domain models (MarketingRequest, MarketingResponse)

### 3. Dependency Injection

Spring Boot manages all component lifecycles and dependencies

### 4. Template Method

ObservabilityService provides tracing templates for operations

### 5. Repository Pattern

McpResourceProvider abstracts resource access

## Configuration Management

Configuration hierarchy:

1. `application.yml` - Default configuration
2. Environment variables - Override defaults
3. `.env` file - Local development (not committed)

## Extensibility Points

### Adding New Tools

1. Create tool class in `tool` package
2. Inject dependencies
3. Implement tool logic
4. Register in MarketingService
5. Add controller endpoint

### Adding New Resources

1. Create resource model
2. Add to McpResourceProvider
3. Use in tools

### Custom AI Providers

Implement custom ChatLanguageModel and configure in LlmConfiguration

## Error Handling

- Validation errors: HTTP 400 with details
- Service errors: Logged and returned as HTTP 200 with error status
- Uncaught exceptions: Global exception handler

## Observability

### Logging

- Request ID in MDC for correlation
- Structured JSON logs
- Operation tracing with duration

### Metrics

- Execution time per operation
- Success/failure rates
- Resource usage

### Tracing

- Request lifecycle tracking
- Tool execution spans
- LLM call tracing

## Security Considerations

Current implementation:

- No authentication (open API)
- No rate limiting
- Input validation only

Future enhancements needed:

- API key authentication
- OAuth2/JWT support
- Rate limiting
- Input sanitization
- Output encryption
- Audit logging

## Scalability

Current limitations:

- Single instance deployment
- In-memory resource storage
- Synchronous processing
- No caching

Future improvements:

- Horizontal scaling with load balancer
- Distributed caching (Redis)
- Async processing with message queues
- Database persistence
- CDN for static outputs

## Performance

### Bottlenecks

- LLM API calls (when enabled)
- File I/O for output storage
- JSON serialization

### Optimization Strategies

- Response caching
- Parallel tool execution
- Connection pooling
- Batch operations

## Testing Strategy

- **Unit Tests**: Individual components
- **Integration Tests**: API endpoints
- **Contract Tests**: API contracts
- **Load Tests**: Performance under load

## Deployment Options

1. **Standalone JAR**: `java -jar mcp-marketing-suite.jar`
2. **Docker Container**: Containerized deployment
3. **Kubernetes**: Cloud-native orchestration
4. **Cloud Services**: AWS/GCP/Azure managed services

