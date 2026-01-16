# Configuração Completa - Logging, Management e SpringDoc ✅

## 📋 Visão Geral

Configuração completa dos módulos de observabilidade, gerenciamento e documentação da API com **todos os parâmetros dinâmicos** vindos do `application.yml`.

---

## 🎯 1. LOGGING - Configuração Completa

### Estrutura Configurada

```yaml
logging:
  level:
    root: INFO                      # Nível padrão para toda a aplicação
    com.mcp.marketing: DEBUG        # Debug para nosso código
    org.springframework.web: INFO  # Info para Spring Web
    org.springframework.security: INFO
    org.hibernate: WARN             # Apenas warnings do Hibernate
    
  pattern:
    console: "%d{ISO8601} level=%-5level request_id=%X{request_id:-n/a} thread=%thread logger=%logger{36} - %msg%n%ex"
    file: "%d{ISO8601} level=%-5level request_id=%X{request_id:-n/a} thread=%thread logger=%logger{36} - %msg%n%ex"
    
  file:
    name: logs/mcp-marketing-suite.log  # Arquivo de log
    max-size: 10MB                      # Tamanho máximo por arquivo
    max-history: 30                     # Manter 30 dias de histórico
    total-size-cap: 1GB                 # Cap total de 1GB
```

### Recursos Implementados

#### ✅ Padrão Estruturado
- **Timestamp**: ISO-8601 formato (`2026-01-16T18:30:45.123Z`)
- **Level**: INFO, DEBUG, WARN, ERROR com 5 caracteres alinhados
- **Request ID**: Correlação via MDC (`%X{request_id:-n/a}`)
- **Thread**: Nome da thread para debug de concorrência
- **Logger**: Nome da classe (truncado em 36 chars)
- **Message**: Mensagem do log
- **Exception**: Stack trace completo quando presente

#### ✅ Rotação de Logs
- Arquivos limitados a 10MB cada
- Histórico de 30 dias
- Cap total de 1GB para prevenir estouro de disco
- Logs em `logs/mcp-marketing-suite.log`

#### ✅ Exemplo de Saída
```
2026-01-16T18:30:45.123Z level=INFO  request_id=abc-123 thread=http-nio-8080-exec-1 logger=c.m.m.a.c.MarketingController - request processed artifact=ads status=success execution_time_ms=250 output_path=./outputs/ads_abc-123_20260116_183045.json
```

---

## 🏥 2. MANAGEMENT - Configuração Completa

### Estrutura Configurada

```yaml
management:
  endpoints:
    web:
      base-path: /actuator              # Base path para todos os endpoints
      exposure:
        include: health,info,metrics,prometheus,env,loggers
        
  endpoint:
    health:
      show-details: always              # Mostra detalhes completos
      show-components: always           # Mostra todos os componentes
      probes:
        enabled: true                   # Kubernetes liveness/readiness
    info:
      enabled: true                     # Endpoint /actuator/info
    metrics:
      enabled: true                     # Métricas da aplicação
    prometheus:
      enabled: true                     # Exportação para Prometheus
      
  health:
    diskspace:
      enabled: true                     # Check de espaço em disco
      threshold: 10MB                   # Alerta se < 10MB
    livenessstate:
      enabled: true                     # Liveness probe
    readinessstate:
      enabled: true                     # Readiness probe
      
  info:
    env:
      enabled: true                     # Expõe variáveis de ambiente
    java:
      enabled: true                     # Expõe informações da JVM
    os:
      enabled: true                     # Expõe informações do SO
      
  metrics:
    tags:
      application: ${spring.application.name}
      environment: ${spring.profiles.active:default}
    export:
      prometheus:
        enabled: true                   # Exporta para Prometheus
```

### Endpoints Disponíveis

| Endpoint | Descrição | Exemplo |
|----------|-----------|---------|
| `/actuator/health` | Status de saúde da aplicação | `{"status":"UP"}` |
| `/actuator/info` | Informações da aplicação | Dados customizados |
| `/actuator/metrics` | Métricas da aplicação | JVM, HTTP, custom |
| `/actuator/prometheus` | Métricas formato Prometheus | Scraping |
| `/actuator/env` | Variáveis de ambiente | Configurações |
| `/actuator/loggers` | Níveis de log dinâmicos | Ajustar em runtime |

### Health Checks Configurados

#### ✅ Disk Space Check
```json
{
  "status": "UP",
  "components": {
    "diskSpace": {
      "status": "UP",
      "details": {
        "total": 500000000000,
        "free": 250000000000,
        "threshold": 10485760,
        "exists": true
      }
    }
  }
}
```

#### ✅ Liveness/Readiness Probes
```yaml
# Kubernetes deployment.yaml
livenessProbe:
  httpGet:
    path: /actuator/health/liveness
    port: 8080
  initialDelaySeconds: 30
  periodSeconds: 10

readinessProbe:
  httpGet:
    path: /actuator/health/readiness
    port: 8080
  initialDelaySeconds: 5
  periodSeconds: 5
```

### Custom Info Contributor

Criada classe `CustomInfoContributor` que adiciona informações customizadas ao `/actuator/info`:

```json
{
  "mcp-marketing-suite": {
    "application": {
      "name": "MCP Marketing Suite",
      "version": "0.1.0",
      "description": "A suite of marketing tools powered by AI.",
      "springName": "mcp-marketing-suite",
      "mode": "Deterministic Content Generation (AI-ready)",
      "serverPort": "8080",
      "startedAt": "2026-01-16T18:30:45.123Z"
    },
    "outputs": {
      "directory": "./outputs",
      "enabled": true
    },
    "mcpServer": {
      "name": "mcp-marketing-suite-server",
      "version": "0.1.0",
      "endpoint": "/mcp",
      "toolsEnabled": true,
      "resourcesEnabled": true
    },
    "features": {
      "deterministicGeneration": true,
      "adsGeneration": true,
      "seoPlanning": true,
      "crmSequences": true,
      "strategyGeneration": true,
      "requestCorrelation": true,
      "structuredLogging": true,
      "filePersistence": true,
      "mcpProtocol": true
    },
    "endpoints": {
      "health": "/health",
      "ads": "/api/marketing/ads",
      "seoPlan": "/api/marketing/seo-plan",
      "crmSequences": "/api/marketing/crm-sequences",
      "strategy": "/api/marketing/strategy",
      "swagger": "/swagger-ui.html",
      "apiDocs": "/api-docs",
      "actuator": "/actuator",
      "mcpServer": "/mcp"
    }
  }
}
```

---

## 📚 3. SPRINGDOC/SWAGGER - Configuração Completa

### Estrutura Configurada

```yaml
springdoc:
  api-docs:
    path: /api-docs                     # OpenAPI JSON spec
    enabled: true
    
  swagger-ui:
    path: /swagger-ui.html              # UI do Swagger
    enabled: true
    operationsSorter: method            # Ordena por método HTTP
    tagsSorter: alpha                   # Tags alfabéticas
    displayRequestDuration: true        # Mostra tempo de resposta
    filter: true                        # Habilita filtro de busca
    tryItOutEnabled: true               # "Try it out" habilitado
    
  show-actuator: true                   # Mostra endpoints actuator
  use-management-port: false            # Usa mesma porta
  packages-to-scan: com.mcp.marketing.api.controller
  paths-to-match: /api/**,/health       # Paths para documentar
  default-consumes-media-type: application/json
  default-produces-media-type: application/json
```

### OpenAPI Configuration Class

Criada classe `OpenApiConfiguration` com informações dinâmicas:

```java
@Bean
public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .info(new Info()
            .title(appName + " API")           // "MCP Marketing Suite API"
            .version(appVersion)                // "0.1.0"
            .description(appDescription)        // Dinâmico
            .contact(...)
            .license(...))
        .servers(List.of(
            new Server()
                .url("http://localhost:" + serverPort)  // Porta dinâmica
                .description("Local Development Server")
        ));
}
```

### Recursos da UI

#### ✅ Interface Swagger UI
- Acesse em: `http://localhost:8080/swagger-ui.html`
- Teste todos os endpoints diretamente
- Veja schemas de request/response
- Tempo de resposta exibido
- Filtro de busca por endpoint

#### ✅ OpenAPI Spec
- Acesse em: `http://localhost:8080/api-docs`
- JSON completo da especificação
- Importável em Postman, Insomnia, etc.

#### ✅ Features Habilitadas
- **Try It Out**: Teste endpoints na UI
- **Request Duration**: Veja quanto tempo levou
- **Filtering**: Busque endpoints rapidamente
- **Sorting**: Métodos e tags ordenados
- **Actuator Endpoints**: Visíveis na documentação

---

## 🎨 4. BANNER.TXT - 100% Dinâmico

### Todos os Parâmetros Dinâmicos

```text
${AnsiColor.BRIGHT_BLUE}:: ${info.app.name} ::${AnsiColor.DEFAULT}
                                  (v${info.app.version})

Description ::  ${info.app.description}
Application ::  ${spring.application.name}
Spring Boot ::  ${spring-boot.version}
Profile     ::  ${spring.profiles.active:default}
Port        ::  ${server.port}
Mode        ::  ${app.banner.mode}
Outputs Dir ::  ${app.outputs.directory}
```

### Novos Elementos no Banner

#### ✅ Seção Management
```text
Management:
  GET    ${management.endpoints.web.base-path}/health     - Health status
  GET    ${management.endpoints.web.base-path}/info       - Application info
  GET    ${management.endpoints.web.base-path}/metrics    - Metrics
  GET    ${springdoc.swagger-ui.path}  - API Documentation
```

#### ✅ Seção MCP Server
```text
MCP Server:  ${mcp.sdk.server.endpoint} | Tools: ${mcp.sdk.tools.enabled} | Resources: ${mcp.sdk.resources.enabled}
```

### Exemplo de Saída Completa

```
 __  __  ____ ____    __  __            _        _   _                 ____        _ _
|  \/  |/ ___|  _ \  |  \/  | __ _ _ __| | _____| |_(_)_ __   __ _   / ___| _   _(_) |_ ___
| |\/| | |   | |_) | | |\/| |/ _` | '__| |/ / _ \ __| | '_ \ / _` |  \___ \| | | | | __/ _ \
| |  | | |___|  __/  | |  | | (_| | |  |   <  __/ |_| | | | | (_| |   ___) | |_| | | ||  __/
|_|  |_|\____|_|     |_|  |_|\__,_|_|  |_|\_\___|\__|_|_| |_|\__, |  |____/ \__,_|_|\__\___|
                                                              |___/

:: MCP Marketing Suite ::                                  (v0.1.0)

Description ::  A suite of marketing tools powered by AI.
Application ::  mcp-marketing-suite
Spring Boot ::  3.3.0
Profile     ::  default
Port        ::  8080
Mode        ::  Deterministic Content Generation (AI-ready)
Outputs Dir ::  ./outputs

REST Endpoints:
  POST   /api/marketing/ads           - Generate multi-platform ads
  POST   /api/marketing/seo-plan      - Generate SEO strategy
  POST   /api/marketing/crm-sequences - Generate email sequences
  POST   /api/marketing/strategy      - Generate integrated strategy
  GET    /health                      - Health check

Management:
  GET    /actuator/health     - Health status
  GET    /actuator/info       - Application info
  GET    /actuator/metrics    - Metrics
  GET    /swagger-ui.html  - API Documentation

MCP Server:  /mcp | Tools: true | Resources: true
```

---

## 📊 5. Resumo de Parâmetros Dinâmicos

### De `info.app.*`

| Parâmetro | Usado Em | Descrição |
|-----------|----------|-----------|
| `${info.app.name}` | Banner, OpenAPI, Info | Nome da aplicação |
| `${info.app.version}` | Banner, OpenAPI, Info | Versão |
| `${info.app.description}` | Banner, OpenAPI, Info | Descrição |

### De `app.*`

| Parâmetro | Usado Em | Descrição |
|-----------|----------|-----------|
| `${app.banner.title}` | Banner | Título customizado |
| `${app.banner.mode}` | Banner, Info | Modo de operação |
| `${app.outputs.directory}` | Banner, Info | Diretório de saída |
| `${app.outputs.enabled}` | Info | Se outputs estão habilitados |

### De `spring.*`

| Parâmetro | Usado Em | Descrição |
|-----------|----------|-----------|
| `${spring.application.name}` | Banner, Metrics | Nome Spring |
| `${spring-boot.version}` | Banner | Versão Spring Boot |
| `${spring.profiles.active}` | Banner, Metrics | Perfil ativo |

### De `server.*`

| Parâmetro | Usado Em | Descrição |
|-----------|----------|-----------|
| `${server.port}` | Banner, OpenAPI | Porta HTTP |

### De `mcp.sdk.*`

| Parâmetro | Usado Em | Descrição |
|-----------|----------|-----------|
| `${mcp.sdk.server.name}` | Info | Nome do MCP Server |
| `${mcp.sdk.server.version}` | Info | Versão MCP |
| `${mcp.sdk.server.endpoint}` | Banner, Info | Endpoint MCP |
| `${mcp.sdk.tools.enabled}` | Banner, Info | Tools habilitados |
| `${mcp.sdk.resources.enabled}` | Banner, Info | Resources habilitados |

### De `management.*`

| Parâmetro | Usado Em | Descrição |
|-----------|----------|-----------|
| `${management.endpoints.web.base-path}` | Banner | Base path actuator |

### De `springdoc.*`

| Parâmetro | Usado Em | Descrição |
|-----------|----------|-----------|
| `${springdoc.swagger-ui.path}` | Banner | Path Swagger UI |

---

## ✅ Checklist de Implementação

### Logging ✅
- [x] Padrão estruturado com request_id
- [x] Níveis de log configurados
- [x] Rotação de arquivos
- [x] Console e file appenders
- [x] Stack traces em logs (não em responses)

### Management ✅
- [x] Health checks detalhados
- [x] Kubernetes probes
- [x] Info endpoint customizado
- [x] Métricas habilitadas
- [x] Prometheus export
- [x] Custom InfoContributor
- [x] Disk space monitoring

### SpringDoc ✅
- [x] Swagger UI habilitado
- [x] OpenAPI spec gerada
- [x] Configuração dinâmica
- [x] Try it out enabled
- [x] Request duration display
- [x] Filtering enabled
- [x] Actuator endpoints documentados

### Banner ✅
- [x] Parâmetros 100% dinâmicos
- [x] Info de `info.app.*`
- [x] Info de `app.*`
- [x] Info de `spring.*`
- [x] Info de `server.*`
- [x] Info de `mcp.sdk.*`
- [x] Info de `management.*`
- [x] Info de `springdoc.*`
- [x] Cores ANSI
- [x] Seções organizadas

---

## 🚀 Como Testar

### 1. Ver o Banner
```bash
mvn spring-boot:run
# Banner aparecerá com todos os valores dinâmicos
```

### 2. Testar Endpoints Management
```bash
# Health check
curl http://localhost:8080/actuator/health | jq

# Application info
curl http://localhost:8080/actuator/info | jq

# Métricas
curl http://localhost:8080/actuator/metrics | jq

# Prometheus
curl http://localhost:8080/actuator/prometheus
```

### 3. Testar Swagger UI
```bash
# Abrir no navegador
open http://localhost:8080/swagger-ui.html

# Ver OpenAPI spec
curl http://localhost:8080/api-docs | jq
```

### 4. Verificar Logs
```bash
# Ver logs em tempo real
tail -f logs/mcp-marketing-suite.log

# Buscar por request_id específico
grep "request_id=abc-123" logs/mcp-marketing-suite.log
```

---

## 📖 Arquivos Criados/Modificados

### Modificados ✅
- `src/main/resources/application.yml` - Configurações completas
- `src/main/resources/banner.txt` - Banner dinâmico

### Criados ✅
- `src/main/java/com/mcp/marketing/config/OpenApiConfiguration.java`
- `src/main/java/com/mcp/marketing/config/CustomInfoContributor.java`

---

## 🎉 Resultado Final

✅ **Logging**: Estruturado, com correlação, rotação automática
✅ **Management**: Health checks, métricas, info customizado
✅ **SpringDoc**: UI completa, OpenAPI spec, try it out
✅ **Banner**: 100% dinâmico com todos os parâmetros

**Tudo configurado e pronto para produção!** 🚀
