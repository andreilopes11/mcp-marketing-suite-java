package com.mcp.marketing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/**
 * MCP Marketing Suite - Main Application
 * <p>
 * MCP-native Marketing Platform built with Java, Spring Boot and Model Context Protocol (Java SDK)
 * for orchestrating marketing workflows in a standardized, auditable and integrable way.
 * <p>
 * Key Features:
 * - MCP Server as core orchestration layer
 * - REST API as adapter
 * - All executions generate request_id, JSON logs and save output to ./outputs
 * - Deterministic mode (can run without AI)
 */
@SpringBootApplication
@ConfigurationPropertiesScan
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

