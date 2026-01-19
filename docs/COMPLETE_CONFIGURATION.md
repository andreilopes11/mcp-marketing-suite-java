# Complete Configuration Guide

_Last updated: January 19, 2026_

This document describes every configuration knob exposed by the MCP Marketing Suite so you can tune logging, management endpoints, Swagger/OpenAPI, and output persistence for your environment.

---

## 1. Configuration Files

| File | Purpose |
|------|---------|
| `src/main/resources/application.yml` | Baseline Spring Boot configuration (port, outputs, actuator, springdoc) |
| `src/main/resources/logback-spring.xml` | JSON logging with MDC `request_id` |
| `src/main/resources/banner.txt` | Startup banner referencing application metadata |

All properties can be overridden with environment variables or `-D` flags. Spring’s relaxed binding applies (e.g., `APP_OUTPUTS_DIRECTORY` overrides `app.outputs.directory`).

---

## 2. Application Properties (`application.yml`)

```yaml
spring:
  application:
    name: mcp-marketing-suite

server:
  port: 8080

app:
  version: 0.1.0
  outputs:
    directory: ./outputs
    enabled: true

logging:
  level:
    root: INFO
    com.mcp.marketing: DEBUG

management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus
  endpoint:
    health:
      probes:
        enabled: true

springdoc:
  api-docs:
    path: /v3/api-docs
  swagger-ui:
    path: /swagger-ui.html
```

### Environment Overrides

| Purpose | Property | Example Override |
|---------|----------|------------------|
| API port | `server.port` | `SERVER_PORT=9090` |
| Disable outputs | `app.outputs.enabled` | `APP_OUTPUTS_ENABLED=false` |
| Change outputs dir | `app.outputs.directory` | `APP_OUTPUTS_DIRECTORY=/var/marketing-outputs` |
| Reduce logging noise | `logging.level.com.mcp.marketing` | `LOGGING_LEVEL_COM_MCP_MARKETING=INFO` |
| Hide metrics | `management.endpoints.web.exposure.include` | `MANAGEMENT_ENDPOINTS_WEB_EXPOSURE_INCLUDE=health,info` |
| Serve Swagger elsewhere | `springdoc.swagger-ui.path` | `SPRINGDOC_SWAGGER_UI_PATH=/docs` |

---

## 3. Logging & MDC

- `RequestContextFilter` sets `request_id` in MDC at the beginning of every HTTP request and clears it afterward.
- `logback-spring.xml` formats every log line as JSON with consistent fields (`timestamp`, `level`, `thread`, `logger`, `request_id`, `message`).
- Structure:

```xml
<pattern>{"timestamp":"%d{ISO8601}","level":"%p","request_id":"%X{request_id}","logger":"%logger","message":%msg}%n</pattern>
```

**Recommended overrides**:
- `LOGGING_FILE_NAME` – Persist logs to disk: `LOGGING_FILE_NAME=/var/log/mcp/mcp.log`
- `LOGGING_LEVEL_ROOT=DEBUG` – Useful during development

---

## 4. Management & Observability

Enabled endpoints:
- `/health` – Lightweight custom endpoint returning uptime, service name, version, and `request_id`
- `/actuator/health` – Full health tree (with liveness/readiness)
- `/actuator/info` – Application metadata (name, version)
- `/actuator/metrics` – Micrometer metrics catalogue
- `/actuator/prometheus` – Prometheus scrape format

Expose fewer endpoints by trimming `management.endpoints.web.exposure.include`. When deploying behind Kubernetes or ECS, point your liveness/readiness probes to `/health` or `/actuator/health/liveness`.

---

## 5. SpringDoc / Swagger UI

- OpenAPI spec: `/v3/api-docs`
- Swagger UI: `/swagger-ui.html`

To serve behind a proxy path, override:

```bash
SPRINGDOC_API_DOCS_PATH=/internal/api-docs
SPRINGDOC_SWAGGER_UI_PATH=/internal/swagger
```

If you want to disable Swagger entirely (e.g., production), set `SPRINGDOC_SWAGGER_UI_ENABLED=false`.

---

## 6. Outputs & StoragePort

- Enable/disable persistence globally via `app.outputs.enabled`.
- Change directory via `app.outputs.directory` (relative or absolute path).
- `FileSystemStorage` writes one file per request: `outputs/<artifact>/<request_id>.json`.
- To plug a new backend (S3, database), implement `StoragePort` and register it as a Spring bean; no configuration changes required beyond removing the filesystem bean.

Filesystem permissions: ensure the configured directory exists and is writable by the application user.

---

## 7. Banner Configuration

`src/main/resources/banner.txt` uses Spring placeholders so the banner always reflects your configuration:

```
  __  __   ____   ____
 |  \/  | / ___| / ___|     ${spring.application.name}
 | |\/| | \___ \| |         ${app.version}
 | |  | |  ___) | |___      Deterministic Marketing Suite
 |_|  |_| |____/ \____|     Execution Mode: ${app.execution-mode:DETERMINISTIC}
```

Supply custom metadata with:

```bash
SPRING_APPLICATION_NAME=marketing-suite-prod
APP_VERSION=1.2.3
APP_EXECUTION_MODE=DETERMINISTIC
```

---

## 8. Secrets & Profiles

- Use Spring profiles for environment-specific overrides (e.g., `application-prod.yml`).
- Inject credentials via environment variables or a secrets manager and reference them with `${}` syntax inside `application.yml`.
- When adding new properties, document the override name in this file so deployers know how to configure them.

---

## 9. Verification Checklist

1. Start the app: `mvn spring-boot:run`
2. Confirm banner prints the expected name/version.
3. Hit `curl http://localhost:8080/health` and verify `request_id` + uptime.
4. Trigger an endpoint and check `./outputs` for a new file and logs for the structured entry.
5. Visit `/swagger-ui.html` to confirm documentation is live.

Following these steps ensures the integration behaves consistently across environments while remaining fully observable.
