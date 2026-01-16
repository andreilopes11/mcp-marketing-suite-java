package com.mcp.marketing.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * MCP Tool Handler - Simple tool implementations
 */
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "mcp.sdk.tools", name = "enabled", havingValue = "true", matchIfMissing = true)
public class McpToolHandler {

    /**
     * Simple echo tool for testing
     */
    public Map<String, Object> echo(Map<String, Object> params) {
        log.info("MCP Tool: echo invoked with params: {}", params);
        Map<String, Object> result = new HashMap<>();
        result.put("requestId", UUID.randomUUID().toString());
        result.put("status", "success");
        result.put("message", "Echo: " + params);
        result.put("timestamp", LocalDateTime.now());
        return result;
    }

    /**
     * Generate simple marketing content
     */
    public Map<String, Object> generateMarketingContent(Map<String, Object> params) {
        log.info("MCP Tool: generateMarketingContent invoked");

        String product = (String) params.getOrDefault("product", "Unknown Product");
        String audience = (String) params.getOrDefault("audience", "General Audience");

        Map<String, Object> result = new HashMap<>();
        result.put("requestId", UUID.randomUUID().toString());
        result.put("status", "success");
        result.put("product", product);
        result.put("audience", audience);
        result.put("content", Map.of(
                "headline", "Transform Your Business with " + product,
                "description", "Perfect solution for " + audience,
                "callToAction", "Get Started Today"
        ));
        result.put("timestamp", LocalDateTime.now());

        return result;
    }

    /**
     * List all available tools
     */
    public List<Map<String, String>> listTools() {
        return List.of(
                Map.of(
                        "name", "echo",
                        "description", "Simple echo tool for testing",
                        "inputSchema", "{\"message\": \"string\"}"
                ),
                Map.of(
                        "name", "generateMarketingContent",
                        "description", "Generate simple marketing content",
                        "inputSchema", "{\"product\": \"string\", \"audience\": \"string\"}"
                )
        );
    }
}
