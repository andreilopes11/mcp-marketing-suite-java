# MCP Marketing Suite - Referência Rápida

## 🚀 Comandos Essenciais

### Inicialização
```bash
mvn spring-boot:run
```

### Testes
```bash
mvn clean test                      # Rodar todos os testes
mvn test -Dtest=ClassNameTest      # Rodar teste específico
mvn clean install -DskipTests       # Build sem testes
```

### Build
```bash
mvn clean package                   # Gerar JAR
mvn clean install                   # Instalar localmente
```

---

## 📡 Endpoints

### Saúde
- `GET /health` - Status da aplicação
- `GET /actuator/health` - Health check detalhado

### Documentação
- `GET /swagger-ui.html` - Interface interativa
- `GET /api-docs` - Especificação OpenAPI JSON

### Marketing Tools
- `POST /api/marketing/ads` - Gerar anúncios
- `POST /api/marketing/crm-sequences` - Gerar sequências CRM
- `POST /api/marketing/seo-plan` - Gerar plano SEO
- `POST /api/marketing/strategy` - Gerar estratégia completa

---

## 🧪 Exemplos de Uso

### Health Check
```bash
curl http://localhost:8080/health
```

### Gerar Ads
```bash
curl -X POST http://localhost:8080/api/marketing/ads \
  -H "Content-Type: application/json" \
  -d '{
    "product": "Analytics Platform",
    "audience": "Data Scientists",
    "brandVoice": "Professional",
    "goals": ["Increase Trials"]
  }'
```

### Com jq (formatado)
```bash
curl -s http://localhost:8080/health | jq .
```

---

## 🔧 Configuração

### Variáveis de Ambiente
```bash
# Linux/Mac
export OPENAI_API_KEY="sk-..."
export LLM_MODEL="gpt-4"

# Windows CMD
set OPENAI_API_KEY=sk-...

# Windows PowerShell
$env:OPENAI_API_KEY="sk-..."
```

### Porta Customizada
```bash
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=9090
```

### Profile de Desenvolvimento
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

---

## 🐛 Troubleshooting Rápido

### Problema: Java version mismatch
```bash
# Verificar versão
java -version

# Configurar JAVA_HOME
export JAVA_HOME=/path/to/jdk-23
export PATH=$JAVA_HOME/bin:$PATH
```

### Problema: Porta em uso
```bash
# Windows - encontrar PID
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# Linux/Mac
lsof -ti:8080 | xargs kill -9
```

### Problema: Dependências corrompidas
```bash
mvn clean install -U
```

### Problema: Cache do Maven
```bash
mvn dependency:purge-local-repository
```

---

## 📁 Estrutura de Diretórios

```
mcp-marketing-suite-java/
├── src/main/java/           # Código fonte
├── src/main/resources/      # Configurações
├── src/test/java/           # Testes
├── docs/                    # Documentação
├── examples/                # Exemplos JSON
├── logs/                    # Logs da aplicação
├── outputs/                 # Saídas geradas
├── target/                  # Build artifacts
└── pom.xml                  # Maven config
```

---

## 📊 Status de Build

### Verificar compilação
```bash
mvn clean compile
```

### Verificar dependências
```bash
mvn dependency:tree
```

### Verificar plugins
```bash
mvn help:effective-pom
```

---

## 🔍 Logs

### Visualizar logs
```bash
tail -f logs/mcp-marketing-suite.log
```

### Buscar por request ID
```bash
grep "test-123" logs/mcp-marketing-suite.log
```

### Últimas 50 linhas
```bash
tail -n 50 logs/mcp-marketing-suite.log
```

---

## 🎯 Verificações Pré-execução

```bash
# Checklist rápido
java -version              # ✅ Deve ser 23.x.x
mvn -version              # ✅ Deve ser 3.8+
mvn clean test            # ✅ Deve passar 4/4
mvn spring-boot:run       # ✅ Deve iniciar sem erros
curl http://localhost:8080/health  # ✅ Deve retornar 200
```

---

## 💡 Dicas Úteis

### Hot Reload (Dev)
```bash
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005"
```

### Debug Remoto
```bash
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5005"
```

### Aumentar Memória
```bash
export MAVEN_OPTS="-Xmx2g -Xms512m"
mvn spring-boot:run
```

### Skip Compilation
```bash
mvn spring-boot:run -Dmaven.test.skip=true
```

---

## 📚 Links Rápidos

| Recurso | Link |
|---------|------|
| Swagger UI | http://localhost:8080/swagger-ui.html |
| API Docs | http://localhost:8080/api-docs |
| Health | http://localhost:8080/health |
| Actuator | http://localhost:8080/actuator |

---

## 🆘 Ajuda Rápida

| Situação | Documento |
|----------|-----------|
| Primeira vez? | `docs/GETTING_STARTED.md` |
| Erro ao executar? | `docs/TROUBLESHOOTING.md` |
| Quer melhorar? | `docs/IMPROVEMENTS.md` |
| Configurar? | `docs/CONFIGURATION.md` |
| API Reference? | `docs/API.md` |
| Arquitetura? | `docs/ARCHITECTURE.md` |

---

## ✅ Cheat Sheet Mínimo

```bash
# Executar
mvn spring-boot:run

# Testar
curl http://localhost:8080/health

# Ver docs
open http://localhost:8080/swagger-ui.html

# Parar
Ctrl+C
```

---

**Última atualização:** 14/01/2026  
**Versão:** 1.0

