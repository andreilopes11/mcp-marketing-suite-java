# MCP Server – Complete Guide

_Last updated: January 19, 2026_

This document covers the MCP server implementation in detail: how it is bootstrapped, how tools/resources interact with the domain layer, and how to extend or troubleshoot it.

---

## 1. Overview

- **Entry point**: `com.mcp.marketing.mcp.server.McpMarketingServer`
- **Transport**: STDIO (the official MCP Java SDK will support additional transports in the future)
- **Tools**: `ads`, `seo-plan`, `crm-sequences`, `strategy`
- **Resources**: `product`, `audience`, `brand`, `competitors`
- **Shared services**: `ValidationService`, `OrchestratorService`, `StoragePort`

The MCP server reuses the same domain logic as the REST API; any bug fix or enhancement automatically benefits both integration paths.

---

## 2. Bootstrapping

```java
public class McpMarketingServer {
    private final ValidationService validationService;
    private final OrchestratorService orchestratorService;
    private final StoragePort storagePort;

    public void start() {
        McpTransport transport = McpTransport.stdio();
        transport.registerTool(new AdsTool(validationService, orchestratorService, storagePort));
        // ... register other tools/resources
        transport.run();
    }
}
```

Steps:
1. Instantiate Spring application context or manual wiring (current implementation relies on Spring Boot to provide the beans).
2. Create the MCP transport (STDIO) and register tool/resource implementations.
3. Call `run()` to start the blocking event loop.

---

## 3. Tool Lifecycle

Each tool extends `AbstractMarketingTool` (utility class) and implements:

- `String getName()` – Tool identifier (matches REST endpoint name)
- `Map<String, Object> call(Map<String, Object> input)` – Main execution

Execution flow inside a tool:

1. **Parse Input** – Convert the incoming JSON to a request DTO or directly to `MarketingContext`. Mandatory fields, such as `product` or `language`, are extracted.
2. **Validate** – Call `validationService.validateContext(context)`. If errors exist, throw `IllegalArgumentException` with a joined message.
3. **Generate Result** – Call the appropriate `OrchestratorService.generateX(context)` method.
4. **Persist** – Build `StandardResponse` and call `storagePort.saveJson(toolName, requestId, response)`.
5. **Return** – Respond with the same envelope consumed by REST clients.

The shared `MarketingContextBuilder` ensures `language` normalization and `ExecutionMode` defaults.

---

## 4. Tools and Their Builders

| Tool | Builder method | Notes |
|------|----------------|-------|
| `ads` | `OrchestratorService.generateAds()` | Produces Google/Meta/LinkedIn structures + `qa_score` |
| `seo-plan` | `generateSeoPlan()` | Provides keywords, on/off-page tactics, technical SEO |
| `crm-sequences` | `generateCrmSequences()` | Builds touchpoints, cadence, success metrics |
| `strategy` | `generateStrategy()` | Calls the three builders above and aggregates output |

Every tool adds metadata such as `requestId`, `executionMode`, and `artifact` to the result payload, mirroring REST behavior.

---

## 5. Resources

Resources expose reference data to MCP clients so they can enrich prompts before calling tools.

| Resource | Implementation | Payload |
|----------|----------------|---------|
| `product` | `ProductResource` | List of example products and positioning |
| `audience` | `AudienceResource` | Personas with pain points |
| `brand` | `BrandResource` | Brand voice guidelines |
| `competitors` | `CompetitorsResource` | Competitor summaries |

Resources are in-memory and deterministic. They can be replaced with dynamic providers (e.g., database queries) without touching the tools.

---

## 6. Validation & Error Handling

- Invalid inputs throw `IllegalArgumentException` with the same message format as the REST API (`language must be 'pt-BR' or 'en-US' or 'es-ES'`, etc.).
- Runtime errors bubble up to the MCP transport, which wraps them in an MCP error response. Stack traces remain in logs only.
- Always include `request_id` in error payloads so clients can correlate responses with logs and saved artifacts (failed runs do not persist files by default).

---

## 7. Persistence & Outputs

`StoragePort` abstracts persistence. Current flow:

1. Build `StandardResponse` after tool execution.
2. Persist via `FileSystemStorage` to `./outputs/<tool>/<request_id>.json`.
3. Return the same response to the caller.

Disable persistence by setting `APP_OUTPUTS_ENABLED=false` or swap the `StoragePort` bean for another implementation (S3, database, etc.).

---

## 8. Observability

- MCP tools log start/end events with `request_id`, tool name, status, execution time, and `output_path`.
- Because the server runs inside the Spring Boot app, the same Logback configuration and Actuator endpoints apply. Use `/health` to verify the overall application even when only using MCP tools.

---

## 9. Extending the MCP Server

1. **Add a new artifact**:
   - Define DTO/result models (domain layer).
   - Add a builder method to `OrchestratorService`.
   - Create a REST endpoint (optional but recommended) and a matching MCP tool.
2. **Add a resource**:
   - Implement `ResourceHandler` (or the equivalent interface from the MCP Java SDK).
   - Register it in `McpMarketingServer`.
3. **Change transport**:
   - Swap `McpTransport.stdio()` for the desired transport once the SDK exposes additional options (WebSockets, HTTP, etc.).

Keep all new logic deterministic and covered by unit tests.

---

## 10. Testing Strategy

- **Unit tests** – Validate each domain builder (`OrchestratorServiceTest`, etc.).
- **MCP smoke tests** – `McpServerSmokeTest` ensures the server can start and respond to a tool call.
- **Integration tests** – Use `McpServerDemo` or custom harness to perform end-to-end STDIO exchanges.

When changing tool schemas, update both REST DTOs and MCP tool builders, then rerun the smoke tests.

---

## 11. Troubleshooting Reference

| Issue | Resolution |
|-------|------------|
| Server exits immediately | Check for stack trace in logs; missing STDIO transport typically indicates the SDK dependency was shaded incorrectly. Rebuild with `mvn clean package`. |
| Client hangs waiting for response | Ensure requests include all required fields; validation exceptions must be handled on the client side. Logs should reveal the missing field. |
| Files not written under `./outputs` | Confirm `APP_OUTPUTS_ENABLED=true` and the process has write permissions. |
| Missing request id in logs | The MCP tool must call `RequestContextAttributes.set` before logging. In this project, helper utilities set it automatically, but custom tools must do the same. |

---

By following these guidelines, you can safely integrate MCP clients, extend the available tools, or replace infrastructure components without compromising deterministic behavior.
