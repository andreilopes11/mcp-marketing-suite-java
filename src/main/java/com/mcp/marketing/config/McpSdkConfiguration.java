package com.mcp.marketing.config;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MCP SDK Configuration
 * Wires the official MCP Java SDK for protocol-compliant server and client operations
 */
@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "mcp.sdk", name = "enabled", havingValue = "true", matchIfMissing = true)
public class McpSdkConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "mcp.sdk.server")
    public McpServerProperties mcpServerProperties() {
        return new McpServerProperties();
    }

    /**
     * MCP Server Properties
     */
    @Setter
    @Getter
    public static class McpServerProperties {
        private String name = "mcp-marketing-suite-server";
        private String version = "0.1.0";
        private String endpoint = "/mcp";
        private String transport = "sse";
    }
}
