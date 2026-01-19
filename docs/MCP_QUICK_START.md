# MCP Quick Start

_Last updated: January 19, 2026_

Follow this guide to run the MCP server locally and call the marketing tools from any MCP-compliant client (Claude Desktop, VS Code extensions, custom automation, etc.).

---

## 1. Prerequisites

- Java 23
- Maven 3.8+
- Git Bash or any shell capable of running the commands below
- Optional: Claude Desktop or another MCP client capable of STDIO transport

Clone the repository and build the project (once):

```bash
git clone https://github.com/your-org/mcp-marketing-suite-java.git
cd mcp-marketing-suite-java
mvn clean install
```

---

## 2. Start the MCP Server

```bash
mvn -q package
java -cp target/mcp-marketing-suite-0.1.0-SNAPSHOT.jar \
     com.mcp.marketing.mcp.server.McpMarketingServer
```

Default behavior:
- Transport: STDIO
- Registered tools: `ads`, `seo-plan`, `crm-sequences`, `strategy`
- Mock resources: `product`, `audience`, `brand`, `competitors`
- Persistence: enabled (`./outputs`)

Keep the process running; the client will connect via STDIO pipes.

---

## 3. Manual Tool Call (for Testing)

Use the `mcp-cli` reference client or pipe JSON directly. Example using `npx mcp-cli`:

```bash
npx mcp-cli \
  --tool ads \
  --payload '{
    "product": "Cloud CRM",
    "audience": "SMBs",
    "brandVoice": "Professional",
    "goals": "100 leads/month",
    "language": "en-US"
  }'
```

Expected response (trimmed):

```json
{
  "status": "SUCCESS",
  "requestId": "req-abc123",
  "data": {
    "artifact_type": "ads",
    "execution_time_ms": 232,
    "result": { ... },
    "output_path": "./outputs/ads/req-abc123.json"
  }
}
```

---

## 4. Connecting Claude Desktop (STDIO)

1. Start the MCP server (see step 2).
2. Add the tool definition to Claude Desktop’s `claude_desktop_config.json`:

```json
{
  "mcpServers": {
    "mcp-marketing-suite": {
      "command": "java",
      "args": [
        "-cp",
        "D:/workspace/SaaS_Projects/mcp-marketing-suite-java/target/mcp-marketing-suite-0.1.0-SNAPSHOT.jar",
        "com.mcp.marketing.mcp.server.McpMarketingServer"
      ]
    }
  }
}
```

3. Restart Claude Desktop; the `mcp-marketing-suite` tools appear automatically.
4. Prompt Claude: “Use the `ads` tool to create a campaign for <context>.” Claude will call the tool without manual scripting.

---

## 5. Using Resources

Tools can fetch contextual data from resources:

| Resource | Sample Call | Description |
|----------|-------------|-------------|
| `product` | `resource/list product` | Returns built-in product context |
| `audience` | `resource/list audience` | Audience personas |
| `brand` | `resource/list brand` | Voice/style guidelines |
| `competitors` | `resource/list competitors` | Competitor snapshots |

(Exact commands depend on your MCP client. Claude exposes them in the “Resources” tab.)

---

## 6. Troubleshooting

| Symptom | Fix |
|---------|-----|
| Server stops immediately | Ensure `JAVA_HOME` points to JDK 23 and you ran `mvn -q package` successfully |
| Tool returns “language must be …” | Provide `language` as `en-US`, `pt-BR`, or `es-ES` (case-insensitive) |
| Output path missing | Verify `app.outputs.enabled=true` and the process can write to `./outputs` |
| Logs lack `request_id` | Make sure REST filter or MCP tool sets the attribute; restart after editing `RequestContextFilter` |

---

## 7. Next Steps

- Study `docs/MCP_SERVER_COMPLETE.md` to understand the server internals.
- Use `examples/payloads/*.json` to craft consistent MCP tool payloads.
- Integrate the STDIO command into your automation (e.g., Node.js child processes) for programmatic marketing workflows.
