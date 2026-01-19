# Banner Configuration - Configuração do Banner

## 📋 Visão Geral

O `banner.txt` foi configurado para usar parâmetros dinâmicos que são resolvidos em tempo de execução pelo Spring Boot, permitindo fácil personalização sem modificar o arquivo do banner.

## 🎨 Parâmetros Dinâmicos

### Parâmetros Disponíveis no Banner

| Parâmetro | Fonte | Descrição | Valor Padrão |
|-----------|-------|-----------|--------------|
| `${app.banner.title}` | `application.yml` | Título da aplicação | `MCP Marketing Suite` |
| `${mcp.sdk.server.version}` | `application.yml` | Versão da aplicação | `0.1.0` |
| `${spring.application.name}` | `application.yml` | Nome da aplicação Spring | `mcp-marketing-suite` |
| `${spring-boot.version}` | Spring Boot | Versão do Spring Boot | `3.3.0` |
| `${spring.profiles.active}` | Runtime/Config | Perfil ativo | `default` |
| `${server.port}` | `application.yml` | Porta do servidor | `8080` |
| `${app.banner.mode}` | `application.yml` | Modo de execução | `Deterministic Content Generation (AI-ready)` |

## 📝 Configuração no application.yml

```yaml
# Application Configuration
app:
  banner:
    title: MCP Marketing Suite              # Título mostrado no banner
    mode: Deterministic Content Generation (AI-ready)  # Modo de operação
  outputs:
    directory: ./outputs
    enabled: true

# MCP SDK Configuration
mcp:
  sdk:
    server:
      version: 0.1.0                        # Versão da aplicação
      name: mcp-marketing-suite-server
```

## 🎯 Como Personalizar

### 1. Alterar o Título

```yaml
app:
  banner:
    title: Minha Suite de Marketing        # Personalizado
```

### 2. Alterar a Versão

```yaml
mcp:
  sdk:
    server:
      version: 1.0.0                        # Nova versão
```

### 3. Alterar o Modo de Operação

```yaml
app:
  banner:
    mode: AI-Powered Marketing Generation   # Modo personalizado
```

### 4. Usar Perfil Específico

```bash
# Via linha de comando
mvn spring-boot:run -Dspring-boot.run.profiles=production

# Via variável de ambiente
export SPRING_PROFILES_ACTIVE=production
```

## 🌈 Cores ANSI

O banner usa cores ANSI para melhor visualização:

- **BRIGHT_BLUE**: Título principal (`:: MCP Marketing Suite ::`)
- **BRIGHT_GREEN**: Versão (`v0.1.0`)
- **CYAN**: Labels dos campos (Application, Spring Boot, Profile, etc.)
- **DEFAULT**: Valores e texto normal

### Desabilitar Cores

Para desabilitar cores em ambientes que não suportam ANSI:

```yaml
spring:
  output:
    ansi:
      enabled: never                        # Desabilita cores ANSI
```

## 📊 Exemplo de Saída

```
 __  __  ____ ____    __  __            _        _   _                 ____        _ _
|  \/  |/ ___|  _ \  |  \/  | __ _ _ __| | _____| |_(_)_ __   __ _   / ___| _   _(_) |_ ___
| |\/| | |   | |_) | | |\/| |/ _` | '__| |/ / _ \ __| | '_ \ / _` |  \___ \| | | | | __/ _ \
| |  | | |___|  __/  | |  | | (_| | |  |   <  __/ |_| | | | | (_| |   ___) | |_| | | ||  __/
|_|  |_|\____|_|     |_|  |_|\__,_|_|  |_|\_\___|\__|_|_| |_|\__, |  |____/ \__,_|_|\__\___|
                                                              |___/

:: MCP Marketing Suite ::                                  (v0.1.0)

Application ::  mcp-marketing-suite
Spring Boot ::  3.3.0
Profile     ::  default
Port        ::  8080
Mode        ::  Deterministic Content Generation (AI-ready)
```

## 🚀 Ambientes Diferentes

### Desenvolvimento (default)

```yaml
spring:
  profiles:
    active: default

server:
  port: 8080

app:
  banner:
    mode: Development Mode - Deterministic Generation
```

### Produção (production)

Criar `application-production.yml`:

```yaml
server:
  port: 9090

app:
  banner:
    title: MCP Marketing Suite - PRODUCTION
    mode: Production - AI-Enhanced Generation

mcp:
  sdk:
    server:
      version: 1.0.0
```

### Testes (test)

Criar `application-test.yml`:

```yaml
server:
  port: 0                                   # Porta aleatória para testes

app:
  banner:
    title: MCP Marketing Suite - TEST
    mode: Test Mode - Deterministic Only
```

## 🔧 Propriedades Avançadas

### Adicionar Mais Informações

Você pode adicionar novas propriedades customizadas:

```yaml
app:
  banner:
    title: MCP Marketing Suite
    mode: Deterministic Content Generation (AI-ready)
    description: AI-powered marketing content orchestration
    author: Your Company
    support-email: support@yourcompany.com
```

E usar no banner.txt:

```text
${AnsiColor.CYAN}Support${AnsiColor.DEFAULT}     ::  ${app.banner.support-email}
```

## 📦 Desabilitar o Banner

Se necessário, você pode desabilitar completamente o banner:

```yaml
spring:
  main:
    banner-mode: off                        # Desabilita o banner
```

Ou exibir apenas no log (não no console):

```yaml
spring:
  main:
    banner-mode: log                        # Banner apenas no log
```

## ✅ Verificação

Para verificar se o banner está funcionando corretamente:

```bash
# Executar a aplicação
mvn spring-boot:run

# Ou com perfil específico
mvn spring-boot:run -Dspring-boot.run.profiles=production

# Verificar nos testes
mvn test
```

## 📚 Referências

- [Spring Boot Banner Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.spring-application.banner)
- [Spring Boot Properties](https://docs.spring.io/spring-boot/docs/current/reference/html/application-properties.html)
- [ANSI Colors in Spring Boot](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.logging.console-output.color-coded)

## 🎯 Benefícios da Configuração Dinâmica

1. **Manutenção Simplificada**: Altere valores sem tocar no banner.txt
2. **Ambientes Diferentes**: Use perfis Spring para diferentes configurações
3. **Versionamento**: Versão controlada centralmente no application.yml
4. **Flexibilidade**: Adicione novos campos facilmente
5. **CI/CD Friendly**: Configure via variáveis de ambiente
6. **Profissional**: Banner consistente e informativo

---

**Status**: ✅ Configuração completa e funcional
**Última atualização**: 16/01/2026
