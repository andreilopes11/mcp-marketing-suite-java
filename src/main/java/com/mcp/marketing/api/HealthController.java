package com.mcp.marketing.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Health check endpoint
 * Minimal REST API adapter - MCP Server is the core
 */
@RestController
public class HealthController {

    private static final Logger logger = LoggerFactory.getLogger(HealthController.class);
    private final Instant startTime = Instant.now();

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        String requestId = UUID.randomUUID().toString();

        logger.info("Health check requested, request_id={}", requestId);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "UP");
        response.put("service", "mcp-marketing-suite");
        response.put("version", "0.1.0");
        response.put("request_id", requestId);
        response.put("timestamp", Instant.now().toString());
        response.put("uptime_seconds", Instant.now().getEpochSecond() - startTime.getEpochSecond());

        return ResponseEntity.ok(response);
    }
}
