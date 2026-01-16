package com.mcp.marketing.mcp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mcp.marketing.domain.ports.StoragePort;
import com.mcp.marketing.domain.service.OrchestratorService;
import com.mcp.marketing.domain.service.ValidationService;
import com.mcp.marketing.infra.storage.FileSystemStorage;
import com.mcp.marketing.mcp.server.McpMarketingServer;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Standalone MCP Server Demo
 * <p>
 * Demonstrates MCP tools and resources usage without starting the full Spring Boot application.
 * Can be run as a main class for quick testing.
 */
public class McpServerDemo {

    private static final ObjectMapper mapper = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT);

    public static void main(String[] args) throws Exception {
        System.out.println("=".repeat(80));
        System.out.println("MCP Marketing Server - Demo");
        System.out.println("=".repeat(80));
        System.out.println();

        // Bootstrap Spring context to get beans
        ApplicationContext context = SpringApplication.run(com.mcp.marketing.Application.class, args);

        OrchestratorService orchestratorService = context.getBean(OrchestratorService.class);
        ValidationService validationService = context.getBean(ValidationService.class);
        StoragePort storagePort = context.getBean(StoragePort.class);

        // Initialize MCP Server
        McpMarketingServer mcpServer = new McpMarketingServer(
                orchestratorService,
                validationService,
                storagePort
        );
        mcpServer.initialize();

        System.out.println("✓ MCP Server initialized successfully");
        System.out.println("  - Server: " + mcpServer.getServerName());
        System.out.println("  - Version: " + mcpServer.getServerVersion());
        System.out.println("  - Tools enabled: " + mcpServer.isToolsEnabled());
        System.out.println("  - Resources enabled: " + mcpServer.isResourcesEnabled());
        System.out.println();

        // Demo 1: List Products Resource
        demonstrateProductResource(mcpServer);

        // Demo 2: List Audiences Resource
        demonstrateAudienceResource(mcpServer);

        // Demo 3: Get Specific Brand
        demonstrateBrandResource(mcpServer);

        // Demo 4: Generate Ads
        demonstrateAdsGeneration(mcpServer);

        // Demo 5: Generate SEO Plan
        demonstrateSeoPlanGeneration(mcpServer);

        System.out.println("=".repeat(80));
        System.out.println("Demo completed successfully!");
        System.out.println("=".repeat(80));

        // Shutdown
        SpringApplication.exit(context, () -> 0);
    }

    private static void demonstrateProductResource(McpMarketingServer mcpServer) throws Exception {
        System.out.println("-".repeat(80));
        System.out.println("DEMO 1: Product Resource - List all products");
        System.out.println("-".repeat(80));

        Map<String, Object> result = mcpServer.getProductResource().read("product/list");

        System.out.println("Result:");
        System.out.println(mapper.writeValueAsString(result));
        System.out.println();
    }

    private static void demonstrateAudienceResource(McpMarketingServer mcpServer) throws Exception {
        System.out.println("-".repeat(80));
        System.out.println("DEMO 2: Audience Resource - Get specific audience");
        System.out.println("-".repeat(80));

        Map<String, Object> result = mcpServer.getAudienceResource().read("audience/aud-001");

        System.out.println("Result:");
        System.out.println(mapper.writeValueAsString(result));
        System.out.println();
    }

    private static void demonstrateBrandResource(McpMarketingServer mcpServer) throws Exception {
        System.out.println("-".repeat(80));
        System.out.println("DEMO 3: Brand Resource - Get brand voice guidelines");
        System.out.println("-".repeat(80));

        Map<String, Object> result = mcpServer.getBrandResource().read("brand/brand-001");

        System.out.println("Result:");
        System.out.println(mapper.writeValueAsString(result));
        System.out.println();
    }

    private static void demonstrateAdsGeneration(McpMarketingServer mcpServer) throws Exception {
        System.out.println("-".repeat(80));
        System.out.println("DEMO 4: Ads Generation Tool");
        System.out.println("-".repeat(80));

        Map<String, Object> input = new LinkedHashMap<>();
        input.put("product", "Cloud CRM Platform");
        input.put("audience", "Small Business Owners");
        input.put("brandVoice", "Professional and Approachable");
        input.put("goals", "Generate 100 qualified leads per month");
        input.put("language", "en");
        input.put("platforms", List.of("google", "meta", "linkedin"));
        input.put("budget", "5000");
        input.put("duration", "3 months");

        System.out.println("Input:");
        System.out.println(mapper.writeValueAsString(input));
        System.out.println();

        Map<String, Object> result = mcpServer.getAdsTool().execute(input);

        System.out.println("Result:");
        System.out.println(mapper.writeValueAsString(result));
        System.out.println();
    }

    private static void demonstrateSeoPlanGeneration(McpMarketingServer mcpServer) throws Exception {
        System.out.println("-".repeat(80));
        System.out.println("DEMO 5: SEO Plan Generation Tool");
        System.out.println("-".repeat(80));

        Map<String, Object> input = new LinkedHashMap<>();
        input.put("product", "E-commerce Platform");
        input.put("audience", "Online Retailers");
        input.put("brandVoice", "Trustworthy and Efficient");
        input.put("goals", "Increase organic traffic by 200%");
        input.put("language", "en");
        input.put("domain", "ecommerce-platform.com");
        input.put("keywords", List.of("e-commerce", "online store", "sell online"));
        input.put("monthlyBudget", 5000);

        System.out.println("Input:");
        System.out.println(mapper.writeValueAsString(input));
        System.out.println();

        Map<String, Object> result = mcpServer.getSeoTool().execute(input);

        System.out.println("Result (summary):");
        @SuppressWarnings("unchecked")
        Map<String, Object> data = (Map<String, Object>) result.get("data");
        System.out.println("  - Artifact Type: " + data.get("artifact_type"));
        System.out.println("  - Execution Time: " + data.get("execution_time_ms") + " ms");
        System.out.println("  - Output Path: " + data.get("output_path"));
        System.out.println("  - Success: " + result.get("success"));
        System.out.println();
    }
}
