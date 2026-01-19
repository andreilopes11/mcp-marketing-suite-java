package com.mcp.marketing.mcp.server;

import com.mcp.marketing.domain.ports.StoragePort;
import com.mcp.marketing.domain.service.OrchestratorService;
import com.mcp.marketing.domain.service.ValidationService;
import com.mcp.marketing.mcp.resources.AudienceResource;
import com.mcp.marketing.mcp.resources.BrandResource;
import com.mcp.marketing.mcp.resources.CompetitorsResource;
import com.mcp.marketing.mcp.resources.ProductResource;
import com.mcp.marketing.mcp.tools.AdsGenerationTool;
import com.mcp.marketing.mcp.tools.CrmSequencesTool;
import com.mcp.marketing.mcp.tools.SeoPlanTool;
import com.mcp.marketing.mcp.tools.StrategyTool;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * MCP Marketing Server
 * <p>
 * Exposes marketing tools via Model Context Protocol (MCP).
 * Provides programmatic access to marketing content generation.
 */
@Component
public class McpMarketingServer {

    private static final Logger logger = LoggerFactory.getLogger(McpMarketingServer.class);

    private final OrchestratorService orchestratorService;
    private final ValidationService validationService;
    private final StoragePort storagePort;

    @Getter
    @Value("${mcp.sdk.server.name}")
    private String serverName;

    @Getter
    @Value("${mcp.sdk.server.version}")
    private String serverVersion;

    @Getter
    @Value("${mcp.sdk.tools.enabled}")
    private boolean toolsEnabled;

    @Getter
    @Value("${mcp.sdk.resources.enabled}")
    private boolean resourcesEnabled;

    // Getters for tools
    // MCP Tools
    @Getter
    private AdsGenerationTool adsTool;
    @Getter
    private SeoPlanTool seoTool;
    @Getter
    private CrmSequencesTool crmTool;
    @Getter
    private StrategyTool strategyTool;

    // Getters for resources
    // MCP Resources
    @Getter
    private ProductResource productResource;
    @Getter
    private AudienceResource audienceResource;
    @Getter
    private BrandResource brandResource;
    @Getter
    private CompetitorsResource competitorsResource;

    public McpMarketingServer(OrchestratorService orchestratorService,
                              ValidationService validationService,
                              StoragePort storagePort) {
        this.orchestratorService = orchestratorService;
        this.validationService = validationService;
        this.storagePort = storagePort;
    }

    @PostConstruct
    public void initialize() {
        logger.info("Initializing MCP Marketing Server: name={} version={} tools={} resources={}",
                serverName, serverVersion, toolsEnabled, resourcesEnabled);

        try {
            // Initialize Tools
            if (toolsEnabled) {
                initializeTools();
            }

            // Initialize Resources
            if (resourcesEnabled) {
                initializeResources();
            }

            logger.info("MCP Marketing Server initialized successfully");

        } catch (Exception e) {
            logger.error("Failed to initialize MCP Marketing Server", e);
            throw new RuntimeException("MCP Server initialization failed", e);
        }
    }

    /**
     * Initialize MCP Tools
     */
    private void initializeTools() {
        logger.debug("Initializing MCP tools...");

        adsTool = new AdsGenerationTool(orchestratorService, validationService, storagePort);
        seoTool = new SeoPlanTool(orchestratorService, validationService, storagePort);
        crmTool = new CrmSequencesTool(orchestratorService, validationService, storagePort);
        strategyTool = new StrategyTool(orchestratorService, validationService, storagePort);

        logger.info("Initialized 4 MCP tools: ads, seo-plan, crm-sequences, strategy");
    }

    /**
     * Initialize MCP Resources
     */
    private void initializeResources() {
        logger.debug("Initializing MCP resources...");

        productResource = new ProductResource();
        audienceResource = new AudienceResource();
        brandResource = new BrandResource();
        competitorsResource = new CompetitorsResource();

        logger.info("Initialized 4 MCP resources: product, audience, brand, competitors");
    }
}
