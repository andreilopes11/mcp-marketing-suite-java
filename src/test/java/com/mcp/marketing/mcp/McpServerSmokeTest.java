package com.mcp.marketing.mcp;

import com.mcp.marketing.domain.ports.StoragePort;
import com.mcp.marketing.domain.service.OrchestratorService;
import com.mcp.marketing.domain.service.ValidationService;
import com.mcp.marketing.mcp.server.McpMarketingServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Smoke tests for MCP Server
 */
@SpringBootTest
class McpServerSmokeTest {

    @Autowired
    private OrchestratorService orchestratorService;

    @Autowired
    private ValidationService validationService;

    @Autowired
    private StoragePort storagePort;

    private McpMarketingServer mcpServer;

    @BeforeEach
    void setUp() {
        mcpServer = new McpMarketingServer(orchestratorService, validationService, storagePort);
        mcpServer.initialize();
    }

    @Test
    void testMcpServerInitialization() {
        // Verify server is initialized
        assertNotNull(mcpServer, "MCP Server should be initialized");
        assertTrue(mcpServer.isToolsEnabled(), "Tools should be enabled");
        assertTrue(mcpServer.isResourcesEnabled(), "Resources should be enabled");
        assertEquals("mcp-marketing-suite-server", mcpServer.getServerName());
    }

    @Test
    void testAdsToolExecution() {
        // Given
        Map<String, Object> input = new LinkedHashMap<>();
        input.put("product", "Cloud CRM Platform");
        input.put("audience", "Small Business Owners");
        input.put("brandVoice", "Professional and Approachable");
        input.put("goals", "Generate 100 qualified leads per month");
        input.put("language", "en");
        input.put("platforms", List.of("google", "meta"));
        input.put("budget", "5000");

        // When
        Map<String, Object> result = mcpServer.getAdsTool().execute(input);

        // Then
        assertNotNull(result, "Result should not be null");
        assertTrue((Boolean) result.get("success"), "Tool execution should be successful");
        assertNotNull(result.get("requestId"), "Should have request ID");
        assertNotNull(result.get("data"), "Should have data");

        @SuppressWarnings("unchecked")
        Map<String, Object> data = (Map<String, Object>) result.get("data");
        assertEquals("ads", data.get("artifact_type"));
        assertNotNull(data.get("result"), "Should have result");
        assertNotNull(data.get("execution_time_ms"), "Should have execution time");
    }

    @Test
    void testSeoPlanToolExecution() {
        // Given
        Map<String, Object> input = new LinkedHashMap<>();
        input.put("product", "E-commerce Platform");
        input.put("audience", "Online Retailers");
        input.put("brandVoice", "Trustworthy");
        input.put("goals", "Increase organic traffic");
        input.put("language", "en");
        input.put("domain", "ecommerce.com");
        input.put("keywords", List.of("e-commerce", "online store"));

        // When
        Map<String, Object> result = mcpServer.getSeoTool().execute(input);

        // Then
        assertNotNull(result);
        assertTrue((Boolean) result.get("success"));

        @SuppressWarnings("unchecked")
        Map<String, Object> data = (Map<String, Object>) result.get("data");
        assertEquals("seo-plan", data.get("artifact_type"));
    }

    @Test
    void testCrmSequencesToolExecution() {
        // Given
        Map<String, Object> input = new LinkedHashMap<>();
        input.put("product", "SaaS Analytics Tool");
        input.put("audience", "Data Analysts");
        input.put("brandVoice", "Technical");
        input.put("goals", "Convert trial users");
        input.put("language", "en");
        input.put("sequenceLength", 4);

        // When
        Map<String, Object> result = mcpServer.getCrmTool().execute(input);

        // Then
        assertNotNull(result);
        assertTrue((Boolean) result.get("success"));

        @SuppressWarnings("unchecked")
        Map<String, Object> data = (Map<String, Object>) result.get("data");
        assertEquals("crm-sequences", data.get("artifact_type"));
    }

    @Test
    void testStrategyToolExecution() {
        // Given
        Map<String, Object> input = new LinkedHashMap<>();
        input.put("product", "Project Management Software");
        input.put("audience", "Team Leads");
        input.put("brandVoice", "Collaborative");
        input.put("goals", "Acquire 1000 customers");
        input.put("language", "en");
        input.put("timeframe", "Q1 2026");

        // When
        Map<String, Object> result = mcpServer.getStrategyTool().execute(input);

        // Then
        assertNotNull(result);
        assertTrue((Boolean) result.get("success"));

        @SuppressWarnings("unchecked")
        Map<String, Object> data = (Map<String, Object>) result.get("data");
        assertEquals("strategy", data.get("artifact_type"));
    }

    @Test
    void testToolValidationError() {
        // Given - missing required field
        Map<String, Object> input = new LinkedHashMap<>();
        input.put("product", "Test Product");
        // Missing audience, brandVoice, goals, language

        // When
        Map<String, Object> result = mcpServer.getAdsTool().execute(input);

        // Then
        assertNotNull(result);
        assertFalse((Boolean) result.get("success"), "Should fail validation");
        assertEquals("INVALID_INPUT", result.get("error"));
    }

    @Test
    void testProductResource() {
        // When
        Map<String, Object> result = mcpServer.getProductResource().read("product/list");

        // Then
        assertNotNull(result);
        assertEquals("application/json", result.get("mimeType"));
        assertNotNull(result.get("content"));

        @SuppressWarnings("unchecked")
        Map<String, Object> content = (Map<String, Object>) result.get("content");
        assertNotNull(content.get("products"));
        assertTrue((Integer) content.get("count") > 0);
    }

    @Test
    void testAudienceResource() {
        // When
        Map<String, Object> result = mcpServer.getAudienceResource().read("audience/list");

        // Then
        assertNotNull(result);
        assertNotNull(result.get("content"));

        @SuppressWarnings("unchecked")
        Map<String, Object> content = (Map<String, Object>) result.get("content");
        assertNotNull(content.get("audiences"));
    }

    @Test
    void testBrandResource() {
        // When
        Map<String, Object> result = mcpServer.getBrandResource().read("brand/brand-001");

        // Then
        assertNotNull(result);

        @SuppressWarnings("unchecked")
        Map<String, Object> content = (Map<String, Object>) result.get("content");
        assertNotNull(content);
        assertEquals("brand-001", content.get("id"));
    }

    @Test
    void testCompetitorsResource() {
        // When
        Map<String, Object> result = mcpServer.getCompetitorsResource().read("competitors/list");

        // Then
        assertNotNull(result);

        @SuppressWarnings("unchecked")
        Map<String, Object> content = (Map<String, Object>) result.get("content");
        assertNotNull(content.get("competitors"));
        assertThat((Integer) content.get("count")).isGreaterThan(0);
    }
}
