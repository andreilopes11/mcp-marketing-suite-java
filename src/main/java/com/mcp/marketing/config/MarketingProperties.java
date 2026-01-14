package com.mcp.marketing.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuration properties for MCP Marketing Suite
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "mcp.marketing")
public class MarketingProperties {

    private LlmConfig llm = new LlmConfig();
    private String outputDirectory = "./outputs";
    private boolean enableAiAgents = true;
    private List<String> supportedLanguages = List.of("en", "pt");

    @Data
    public static class LlmConfig {
        private String provider = "openai";
        private String apiKey;
        private String model = "gpt-4";
        private double temperature = 0.7;
        private int maxTokens = 2000;
    }
}

