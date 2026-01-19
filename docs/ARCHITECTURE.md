# MCP Marketing Suite – Architecture Guide

_Last updated: January 19, 2026_

This document explains how the MCP Marketing Suite is structured and how REST/MCP integrations traverse the system. Use it to understand where to extend the platform or to validate that deterministic behavior is preserved end-to-end.

---

## 1. Architectural Principles

1. **Single Source of Truth** – `ValidationService` and `OrchestratorService` live in the domain layer and are reused by every adapter (REST + MCP). No logic duplication.
2. **Ports and Adapters** – Outputs are persisted through the `StoragePort` interface, currently implemented by `FileSystemStorage`. Swap the adapter to change persistence without touching business logic.
3. **Deterministic Execution** – No threads, LLMs, or external APIs. Inputs + execution mode fully determine outputs. `qa_score` is derived from context completeness.
4. **Traceability** – Every request carries a `request_id`, stored in MDC, returned to clients, and used as the file name for persisted artifacts.

---

## 2. Layered View

```
┌────────────────────────────────────────────────────────┐
│ Presentation Layer                                      │
│  • REST Controllers (Spring MVC)                        │
│  • MCP Tools (Model Context Protocol SDK)               │
└───────────────┬─────────────────────────────────────────┘
                │
┌───────────────▼─────────────────────────────────────────┐
│ Domain Layer                                            │
│  • Models: MarketingContext, AdsResult, ...             │
│  • Services: ValidationService, OrchestratorService     │
│  • Enums: ExecutionMode                                │
└───────────────┬─────────────────────────────────────────┘
                │
┌───────────────▼─────────────────────────────────────────┐
│ Infrastructure Layer                                    │
│  • StoragePort implementation (FileSystemStorage)       │
│  • Configuration (RequestIdResolver, filters, logging)  │
│  • logback + banner resources                           │
└─────────────────────────────────────────────────────────┘
```

Key characteristics:
- Both adapters call the same domain services using `MarketingContext` objects.
- Persistence and logging sit below the domain to keep the business logic pure.
- Configuration files (`application.yml`, `logback-spring.xml`, `banner.txt`) provide runtime behavior without code changes.

---

## 3. Component Responsibilities

| Component | Purpose |
|-----------|---------|
| `MarketingController` | Exposes REST endpoints, resolves request ids, times executions, saves outputs |
| `McpMarketingServer` | Registers MCP tools/resources and routes MCP calls to domain services |
| `ValidationService` | Enforces required fields and allowed language values |
| `OrchestratorService` | Generates deterministic payloads for ads, SEO, CRM sequences, and strategies |
| `StoragePort` | Persists `StandardResponse` envelopes; default implementation writes JSON files |
| `RequestContextFilter` | Adds/removes `request_id` to the logging MDC |

---

## 4. Sequence Flows

### REST API Request

1. Client sends POST `/api/marketing/<artifact>` with JSON body.
2. `MarketingController` resolves/creates a `request_id`, stores start time, and maps payload to `MarketingContext` (normalizing language).
3. `ValidationService` runs; errors bubble to `GlobalExceptionHandler` and return `ErrorResponse` with `request_id`.
4. `OrchestratorService` builds deterministic result.
5. `StoragePort.saveJson()` writes the `StandardResponse` to `./outputs/<artifact>/<request_id>.json`.
6. Response returns to the client, including `execution_time_ms` and `output_path`.

### MCP Tool Request

1. MCP client sends a `tools/call` request via STDIO.
2. `McpMarketingServer` resolves the tool, parses arguments, and builds `MarketingContext` using the same validation rules.
3. `OrchestratorService` generates the payload.
4. Result is wrapped in `StandardResponse`, persisted via `StoragePort`, and sent back to the client.

Both paths share logging, persistence, and monitoring infrastructure.

---

## 5. Data Persistence & Logging

- **Outputs** – Deterministic JSON artifacts stored per request id. Structure: `outputs/<artifact>/<request_id>.json`.
- **Logging** – `logback-spring.xml` emits JSON logs. The `RequestContextFilter` ensures every message during a request includes the same `request_id`.
- **Observability** – `/health`, `/actuator/*`, and Prometheus endpoints expose uptime, build info, and metrics.

---

## 6. Extension Points

| Extension | Recommendation |
|-----------|----------------|
| New artifact/tool | Add DTO + domain result model, extend `OrchestratorService`, expose via REST + MCP tool pointing to the same builder |
| Alternative storage | Implement `StoragePort` using S3, database, etc., and replace the Spring bean |
| Authentication | Wrap REST endpoints with Spring Security filters; MCP transport can enforce auth separately |
| Localization | Extend `ExecutionMode` or language validation and adjust copy templates |

Keeping these patterns ensures deterministic behavior and consistent integrations for every client.
