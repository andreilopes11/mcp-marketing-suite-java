package com.mcp.marketing.controller;

import com.mcp.marketing.handler.McpResourceHandler;
import com.mcp.marketing.handler.McpToolHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * MCP Server Controller
 * Exposes MCP protocol endpoints for resources and tools
 */
@Slf4j
@RestController
@RequestMapping("/mcp")
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "mcp.sdk", name = "enabled", havingValue = "true", matchIfMissing = true)
public class McpServerController {

    private final McpResourceHandler resourceHandler;
    private final McpToolHandler toolHandler;

    /**
     * MCP Server Info
     */
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getServerInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("name", "mcp-marketing-suite-server");
        info.put("version", "0.1.0");
        info.put("protocol", "mcp");
        info.put("capabilities", Map.of(
                "resources", true,
                "tools", true,
                "prompts", false
        ));
        return ResponseEntity.ok(info);
    }

    /**
     * List available resources
     */
    @GetMapping("/resources")
    public ResponseEntity<Map<String, Object>> listResources() {
        Map<String, Object> response = new HashMap<>();
        response.put("resources", resourceHandler.listResources());
        return ResponseEntity.ok(response);
    }

    /**
     * Get specific resource
     */
    @GetMapping("/resources/{type}/{id}")
    public ResponseEntity<Map<String, Object>> getResource(
            @PathVariable String type,
            @PathVariable String id) {

        log.info("MCP: Getting resource type={}, id={}", type, id);

        Map<String, Object> resource = switch (type.toLowerCase()) {
            case "product" -> resourceHandler.getProductResource(id);
            case "audience" -> resourceHandler.getAudienceResource(id);
            case "brand" -> resourceHandler.getBrandResource(id);
            case "competitor", "competitors" -> resourceHandler.getCompetitorResource(id);
            default -> {
                log.warn("Unknown resource type: {}", type);
                yield Map.of("error", "Unknown resource type: " + type);
            }
        };

        return ResponseEntity.ok(resource);
    }

    /**
     * List available tools
     */
    @GetMapping("/tools")
    public ResponseEntity<Map<String, Object>> listTools() {
        Map<String, Object> response = new HashMap<>();
        response.put("tools", toolHandler.listTools());
        return ResponseEntity.ok(response);
    }

    /**
     * Execute tool
     */
    @PostMapping("/tools/{toolName}")
    public ResponseEntity<Map<String, Object>> executeTool(
            @PathVariable String toolName,
            @RequestBody Map<String, Object> params) {

        log.info("MCP: Executing tool: {} with params: {}", toolName, params);

        Map<String, Object> result = switch (toolName.toLowerCase()) {
            case "echo" -> toolHandler.echo(params);
            case "generatemarketingcontent", "generate_marketing_content" ->
                    toolHandler.generateMarketingContent(params);
            default -> {
                log.warn("Unknown tool: {}", toolName);
                yield Map.of("error", "Unknown tool: " + toolName);
            }
        };

        return ResponseEntity.ok(result);
    }

    /**
     * SSE endpoint for streaming MCP responses
     */
    @GetMapping(value = "/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<String> sseEndpoint() {
        // Placeholder for SSE implementation
        // Will be enhanced with full MCP SDK SSE transport
        log.info("MCP SSE endpoint accessed");
        return ResponseEntity.ok("data: {\"type\":\"hello\",\"server\":\"mcp-marketing-suite\"}\n\n");
    }
}
