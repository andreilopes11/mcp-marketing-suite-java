# Documentation Index

_Last updated: January 19, 2026_

This index summarizes every active documentation asset and when to use it. All files are in English and synchronized with the current codebase.

---

## 1. Quick Navigation

| Goal | Start Here |
|------|------------|
| Run the REST API or MCP server | `README.md` (root) |
| Copy/paste curl scripts | `examples/README.md` |
| Understand the architecture | `docs/ARCHITECTURE.md` |
| Configure logging/management/SpringDoc | `docs/COMPLETE_CONFIGURATION.md` |
| Launch MCP quickly | `docs/MCP_QUICK_START.md` |
| Deep dive MCP internals | `docs/MCP_SERVER_COMPLETE.md` |

---

## 2. Document Summaries

| File | Description |
|------|-------------|
| `README.md` | Main guide: setup, REST usage, MCP usage, configuration, examples |
| `docs/ARCHITECTURE.md` | Architectural principles, layered diagram, request sequences |
| `docs/COMPLETE_CONFIGURATION.md` | Property reference, environment overrides, observability |
| `docs/MCP_QUICK_START.md` | Step-by-step MCP server startup and sample calls |
| `docs/MCP_SERVER_COMPLETE.md` | Internal design of the MCP server, tools, and resources |

---

## 3. Workflow Examples

1. **Need to deploy:** read `README.md` → `COMPLETE_CONFIGURATION.md`.
2. **Need to embed into another API:** read `README.md` (REST usage) → `ARCHITECTURE.md` (sequence flow).
3. **Need to integrate with Claude:** read `MCP_QUICK_START.md` → `MCP_SERVER_COMPLETE.md`.
4. **Need to extend the platform:** read `ARCHITECTURE.md` → `MCP_SERVER_COMPLETE.md` → source code.

---

Keep this index up to date whenever a new document is added or renamed so contributors always know where to look.
