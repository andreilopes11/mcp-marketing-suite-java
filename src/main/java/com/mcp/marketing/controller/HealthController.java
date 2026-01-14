package com.mcp.marketing.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Health check endpoint
 */
@Slf4j
@RestController
public class HealthController {

    private BuildProperties buildProperties;

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("timestamp", LocalDateTime.now());

        if (buildProperties != null) {
            health.put("service", buildProperties.getName());
            health.put("version", buildProperties.getVersion());
            health.put("artifact", buildProperties.getArtifact());
            health.put("group", buildProperties.getGroup());
            health.put("buildTime", buildProperties.getTime());

            log.info("Health check accessed for service: {}, version: {}, artifact: {}, group: {}, buildTime: {}",
                    buildProperties.getName(),
                    buildProperties.getVersion(),
                    buildProperties.getArtifact(),
                    buildProperties.getGroup(),
                    buildProperties.getTime()
            );
        } else {
            health.put("service", "MCP Marketing Suite");
            health.put("version", "0.1.0-SNAPSHOT");
        }

        return ResponseEntity.ok(health);
    }
}

