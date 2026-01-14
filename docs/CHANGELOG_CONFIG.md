# Mudanças de Configuração - Removido .env

## Resumo

O arquivo `.env.example` foi **removido** do projeto porque é uma convenção de **Node.js/Python**, não de **Java Spring Boot**.

## O Que Foi Alterado

### 🗑️ Removido
- `.env.example` - Não é necessário em projetos Spring Boot

### ✅ Criado
- `docs/CONFIGURATION.md` - Guia completo de configuração
- `docs/CONFIGURATION_PATTERNS.md` - Comparação entre padrões Java vs Node.js/Python
- `src/main/resources/application-dev.yml` - Perfil de desenvolvimento

### 📝 Atualizado
- `README.md` - Instruções corretas para Spring Boot
- `docs/QUICKSTART.md` - Removidas referências ao .env
- `.gitignore` - Comentários atualizados

## Como Configurar Agora

### Desenvolvimento Local

**Opção 1: Variáveis de Ambiente (Recomendado)**
```bash
# Linux/Mac/Git Bash
export OPENAI_API_KEY="sk-your-key"
mvn spring-boot:run

# Windows CMD
set OPENAI_API_KEY=sk-your-key
mvn spring-boot:run

# Windows PowerShell
$env:OPENAI_API_KEY="sk-your-key"
mvn spring-boot:run
```

**Opção 2: IDE (IntelliJ IDEA, Eclipse, VS Code)**
- Adicione variáveis de ambiente na configuração de execução

**Opção 3: Perfil de Desenvolvimento**
```bash
# Edite application-dev.yml e execute:
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### Produção

```bash
# Docker Compose
export OPENAI_API_KEY="sk-prod-key"
docker-compose up

# Kubernetes
# Use Secrets e ConfigMaps

# Cloud Platforms
# Use gerenciadores de secrets (AWS Secrets Manager, Azure Key Vault, etc.)
```

## Por Que Esta Mudança?

### ❌ Problemas com .env em Java
1. **Não é nativo** - Requer bibliotecas externas
2. **Confuso** - Mistura convenções de diferentes ecossistemas
3. **Desnecessário** - Spring Boot já lê variáveis de ambiente
4. **Menos seguro** - Pode encorajar hardcoding de secrets

### ✅ Vantagens do Padrão Spring Boot
1. **Nativo** - Funciona sem bibliotecas adicionais
2. **Type-safe** - Validação em tempo de compilação
3. **Flexível** - Múltiplas fontes de configuração
4. **Profiles** - Diferentes configs por ambiente
5. **Cloud-ready** - Integração nativa com plataformas cloud

## Estrutura de Configuração

```
src/main/resources/
├── application.yml              # Configuração base (comitada)
│   └── Usa ${ENV_VAR:default}  # Placeholders para env vars
│
└── application-dev.yml          # Perfil de desenvolvimento (comitada)
    └── Configurações de dev     # Ainda usa env vars

# Secrets NÃO vão no código
# Defina no sistema operacional ou IDE
```

## Exemplo: application.yml

```yaml
mcp:
  marketing:
    llm:
      api-key: ${OPENAI_API_KEY:}      # Lê do ambiente
      model: ${LLM_MODEL:gpt-4}        # Com valor padrão
      temperature: 0.7                  # Valor fixo
    enable-ai-agents: ${ENABLE_AI_AGENTS:true}
```

## Migrando de Outro Projeto

Se você vem de um projeto Node.js/Python com `.env`:

### Antes (.env)
```bash
OPENAI_API_KEY=sk-abc123
LLM_MODEL=gpt-4
```

### Depois (Spring Boot)

**1. Adicione placeholders no application.yml:**
```yaml
llm:
  api-key: ${OPENAI_API_KEY:}
  model: ${LLM_MODEL:gpt-4}
```

**2. Configure a variável de ambiente:**
```bash
export OPENAI_API_KEY="sk-abc123"
```

**3. Execute normalmente:**
```bash
mvn spring-boot:run
```

## Checklist de Segurança

- ✅ `application.yml` comitado (sem secrets)
- ✅ Secrets apenas em variáveis de ambiente
- ✅ `.gitignore` configurado
- ✅ Documentação atualizada
- ❌ Nunca comitar API keys
- ❌ Nunca hardcoded secrets em YAML
- ❌ Nunca usar .env em Java (use se necessário, mas não é recomendado)

## Documentação

- 📖 [Configuration Guide](CONFIGURATION.md) - Guia detalhado
- 📖 [Configuration Patterns](CONFIGURATION_PATTERNS.md) - Comparação de padrões
- 📖 [Quick Start](QUICKSTART.md) - Início rápido
- 📖 [README.md](../README.md) - Documentação principal

## Perguntas Frequentes

**P: Por que não usar .env como no Node.js?**
R: Porque Spring Boot já suporta variáveis de ambiente nativamente, sem bibliotecas externas.

**P: Posso ainda usar .env se quiser?**
R: Tecnicamente sim (com bibliotecas), mas vai contra as convenções Java e não é recomendado.

**P: Como faço para diferentes ambientes?**
R: Use Spring Profiles: `application-dev.yml`, `application-prod.yml`, etc.

**P: E se eu trabalho com desenvolvedores de Node.js?**
R: Mostre este documento para explicar que cada linguagem tem suas convenções.

**P: Onde guardo secrets em produção?**
R: Use gerenciadores de secrets do cloud provider ou variáveis de ambiente do container.

## Conclusão

Esta mudança alinha o projeto com as **melhores práticas da comunidade Java/Spring Boot**. 

O arquivo `.env` é uma convenção excelente para Node.js e Python, mas **não é necessário** e pode causar confusão em projetos Java.

---

**Alterado em:** 2026-01-13
**Motivo:** Alinhamento com padrões Spring Boot
**Impacto:** Nenhum na funcionalidade, apenas na forma de configurar

