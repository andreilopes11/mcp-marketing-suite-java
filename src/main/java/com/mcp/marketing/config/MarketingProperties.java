package com.mcp.marketing.config;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Configuration properties for MCP Marketing Suite
 */
@Data
@Configuration
@RequiredArgsConstructor
public class MarketingProperties {

    private final LlmConfig llm;

    @Value("${mcp.marketing.output-directory:./outputs}")
    private String outputDirectory;

    @Value("${mcp.marketing.enable-ai-agents:true}")
    private boolean enableAiAgents;

    @Value("#{'${mcp.marketing.supported-languages:en,pt,es}'.split(',')}")
    private List<String> supportedLanguages;

    @Data
    @Component
    public static class LlmConfig {

        @Value("${mcp.marketing.llm.provider:openai}")
        private String provider;

        @Value("${mcp.marketing.llm.api-key:}")
        private String apiKey;

        @Value("${mcp.marketing.llm.model:gpt-4}")
        private String model;

        @Value("${mcp.marketing.llm.temperature:0.2}")
        private double temperature;

        @Value("${mcp.marketing.llm.max-tokens:2000}")
        private int maxTokens;
    }
}