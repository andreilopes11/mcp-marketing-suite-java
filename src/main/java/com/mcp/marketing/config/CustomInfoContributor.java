package com.mcp.marketing.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Custom Info Contributor for /actuator/info endpoint
 * <p>
 * Adds custom application information to the actuator info endpoint
 */
@Component
public class CustomInfoContributor implements InfoContributor {

    @Value("${info.app.name}")
    private String appName;

    @Value("${info.app.version}")
    private String appVersion;

    @Value("${info.app.description}")
    private String appDescription;

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${server.port}")
    private String serverPort;

    @Value("${app.banner.mode}")
    private String mode;

    @Value("${app.outputs.directory}")
    private String outputsDirectory;

    @Value("${app.outputs.enabled}")
    private boolean outputsEnabled;

    @Value("${mcp.sdk.server.name}")
    private String mcpServerName;

    @Value("${mcp.sdk.server.version}")
    private String mcpServerVersion;

    @Value("${mcp.sdk.server.endpoint}")
    private String mcpEndpoint;

    @Value("${mcp.sdk.tools.enabled}")
    private boolean mcpToolsEnabled;

    @Value("${mcp.sdk.resources.enabled}")
    private boolean mcpResourcesEnabled;

    @Override
    public void contribute(Info.Builder builder) {
        Map<String, Object> customInfo = new LinkedHashMap<>();

        // Application Info
        Map<String, Object> application = new LinkedHashMap<>();
        application.put("name", appName);
        application.put("version", appVersion);
        application.put("description", appDescription);
        application.put("springName", applicationName);
        application.put("mode", mode);
        application.put("serverPort", serverPort);
        application.put("startedAt", Instant.now().toString());
        customInfo.put("application", application);

        // Outputs Configuration
        Map<String, Object> outputs = new LinkedHashMap<>();
        outputs.put("directory", outputsDirectory);
        outputs.put("enabled", outputsEnabled);
        customInfo.put("outputs", outputs);

        // MCP Server Info
        Map<String, Object> mcpServer = new LinkedHashMap<>();
        mcpServer.put("name", mcpServerName);
        mcpServer.put("version", mcpServerVersion);
        mcpServer.put("endpoint", mcpEndpoint);
        mcpServer.put("toolsEnabled", mcpToolsEnabled);
        mcpServer.put("resourcesEnabled", mcpResourcesEnabled);
        customInfo.put("mcpServer", mcpServer);

        // Features
        Map<String, Boolean> features = new LinkedHashMap<>();
        features.put("deterministicGeneration", true);
        features.put("adsGeneration", true);
        features.put("seoPlanning", true);
        features.put("crmSequences", true);
        features.put("strategyGeneration", true);
        features.put("requestCorrelation", true);
        features.put("structuredLogging", true);
        features.put("filePersistence", outputsEnabled);
        features.put("mcpProtocol", mcpToolsEnabled || mcpResourcesEnabled);
        customInfo.put("features", features);

        // Endpoints
        Map<String, String> endpoints = new LinkedHashMap<>();
        endpoints.put("health", "/health");
        endpoints.put("ads", "/api/marketing/ads");
        endpoints.put("seoPlan", "/api/marketing/seo-plan");
        endpoints.put("crmSequences", "/api/marketing/crm-sequences");
        endpoints.put("strategy", "/api/marketing/strategy");
        endpoints.put("swagger", "/swagger-ui.html");
        endpoints.put("apiDocs", "/api-docs");
        endpoints.put("actuator", "/actuator");
        endpoints.put("mcpServer", mcpEndpoint);
        customInfo.put("endpoints", endpoints);

        builder.withDetail("mcp-marketing-suite", customInfo);
    }
}
