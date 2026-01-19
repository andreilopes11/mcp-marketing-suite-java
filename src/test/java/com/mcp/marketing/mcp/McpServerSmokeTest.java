package com.mcp.marketing.mcp;

import com.mcp.marketing.api.dto.StandardResponse;
import com.mcp.marketing.domain.model.AdsResult;
import com.mcp.marketing.domain.model.CrmSequencesResult;
import com.mcp.marketing.domain.model.SeoPlanResult;
import com.mcp.marketing.domain.model.StrategyResult;
import com.mcp.marketing.domain.ports.StoragePort;
import com.mcp.marketing.domain.service.OrchestratorService;
import com.mcp.marketing.domain.service.ValidationService;
import com.mcp.marketing.mcp.server.McpMarketingServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for MCP server tools/resources.
 */
@ExtendWith(MockitoExtension.class)
class McpServerSmokeTest {

    @Mock
    private OrchestratorService orchestratorService;

    @Mock
    private ValidationService validationService;

    @Mock
    private StoragePort storagePort;

    private McpMarketingServer server;

    @BeforeEach
    void setUp() {
        server = buildServer(true, true);
    }

    @Test
    void initializesToolsAndResources() {
        assertEquals("mcp-marketing-suite-server", server.getServerName());
        assertTrue(server.isToolsEnabled());
        assertTrue(server.isResourcesEnabled());
        assertNotNull(server.getAdsTool());
        assertNotNull(server.getSeoTool());
        assertNotNull(server.getCrmTool());
        assertNotNull(server.getStrategyTool());
        assertNotNull(server.getProductResource());
        assertNotNull(server.getAudienceResource());
        assertNotNull(server.getBrandResource());
        assertNotNull(server.getCompetitorsResource());
    }

    @Test
    void adsToolReturnsSuccessPayload() {
        when(validationService.validateContext(any())).thenReturn(List.of());
        when(orchestratorService.generateAds(any())).thenReturn(sampleAdsResult());
        when(storagePort.saveJson(eq("ads"), anyString(), any(StandardResponse.class))).thenReturn("/tmp/ads.json");

        Map<String, Object> result = server.getAdsTool().execute(validAdsInput());

        assertTrue((Boolean) result.get("success"));
        assertEquals(200, result.get("status"));
        assertNotNull(result.get("requestId"));
        @SuppressWarnings("unchecked")
        Map<String, Object> data = (Map<String, Object>) result.get("data");
        assertThat(data).containsEntry("artifact_type", "ads").containsKey("execution_time_ms");
        assertThat(data).containsEntry("output_path", "/tmp/ads.json");
        verify(storagePort).saveJson(eq("ads"), anyString(), any(StandardResponse.class));
    }

    @Test
    void seoToolReturnsValidationError() {
        when(validationService.validateContext(any())).thenReturn(List.of("language missing"));

        Map<String, Object> result = server.getSeoTool().execute(validSeoInput());

        assertFalse((Boolean) result.get("success"));
        assertEquals("VALIDATION_ERROR", result.get("error"));
        assertTrue(((String) result.get("message")).contains("language"));
        verify(storagePort, never()).saveJson(anyString(), anyString(), any());
    }

    @Test
    void crmToolRejectsMissingField() {
        Map<String, Object> result = server.getCrmTool().execute(Map.of("product", "CRM"));

        assertFalse((Boolean) result.get("success"));
        assertEquals("INVALID_INPUT", result.get("error"));
        assertTrue(((String) result.get("message")).contains("audience"));
    }

    @Test
    void strategyToolHandlesInternalError() {
        when(validationService.validateContext(any())).thenReturn(List.of());
        when(orchestratorService.generateStrategy(any())).thenThrow(new IllegalStateException("boom"));

        Map<String, Object> result = server.getStrategyTool().execute(validStrategyInput());

        assertFalse((Boolean) result.get("success"));
        assertEquals("INTERNAL_ERROR", result.get("error"));
        assertEquals(400, result.get("status"));
    }

    @Test
    void strategyToolPersistsSuccessfulResponse() {
        when(validationService.validateContext(any())).thenReturn(List.of());
        when(orchestratorService.generateStrategy(any())).thenReturn(sampleStrategyResult());
        when(storagePort.saveJson(eq("strategy"), anyString(), any(StandardResponse.class))).thenReturn("/tmp/strategy.json");

        Map<String, Object> result = server.getStrategyTool().execute(validStrategyInput());

        assertTrue((Boolean) result.get("success"));
        @SuppressWarnings("unchecked")
        Map<String, Object> data = (Map<String, Object>) result.get("data");
        assertEquals("strategy", data.get("artifact_type"));
        assertEquals("/tmp/strategy.json", data.get("output_path"));
        verify(storagePort).saveJson(eq("strategy"), anyString(), any(StandardResponse.class));
    }

    @Test
    void crmToolSuccessPersistsEnvelope() {
        when(validationService.validateContext(any())).thenReturn(List.of());
        when(orchestratorService.generateCrmSequences(any())).thenReturn(sampleCrmResult());
        when(storagePort.saveJson(eq("crm-sequences"), anyString(), any(StandardResponse.class))).thenReturn("/tmp/crm.json");

        Map<String, Object> result = server.getCrmTool().execute(validCrmInput());

        assertTrue((Boolean) result.get("success"));
        ArgumentCaptor<StandardResponse> captor = ArgumentCaptor.forClass(StandardResponse.class);
        verify(storagePort).saveJson(eq("crm-sequences"), anyString(), captor.capture());
        assertEquals("/tmp/crm.json", ((Map<?, ?>) result.get("data")).get("output_path"));
        assertEquals("crm-sequences", ((Map<?, ?>) result.get("data")).get("artifact_type"));
        assertTrue(captor.getValue().getSuccess());
    }

    @Test
    void seoToolCastsOptionalInputs() {
        when(validationService.validateContext(any())).thenReturn(List.of());
        when(orchestratorService.generateSeoPlan(any())).thenReturn(sampleSeoPlan());
        when(storagePort.saveJson(eq("seo-plan"), anyString(), any(StandardResponse.class))).thenReturn(null);

        Map<String, Object> result = server.getSeoTool().execute(validSeoInput());

        assertTrue((Boolean) result.get("success"));
        Map<?, ?> data = (Map<?, ?>) result.get("data");
        assertNull(data.get("output_path"));
        assertEquals("seo-plan", data.get("artifact_type"));
    }

    @Test
    void toolsDisabledSkipsInitialization() {
        McpMarketingServer withoutTools = buildServer(false, true);
        assertFalse(withoutTools.isToolsEnabled());
        assertNull(withoutTools.getAdsTool());
        assertNotNull(withoutTools.getProductResource());
    }

    @Test
    void resourcesDisabledSkipsInitialization() {
        McpMarketingServer withoutResources = buildServer(true, false);
        assertFalse(withoutResources.isResourcesEnabled());
        assertNotNull(withoutResources.getAdsTool());
        assertNull(withoutResources.getProductResource());
        assertNull(withoutResources.getAudienceResource());
    }

    @Test
    void productResourceHandlesSpecificAndMissingIds() {
        Map<String, Object> listResponse = server.getProductResource().read("product/list");
        assertEquals("application/json", listResponse.get("mimeType"));
        Map<?, ?> listContent = (Map<?, ?>) listResponse.get("content");
        assertTrue((Integer) listContent.get("count") >= 3);

        Map<String, Object> itemResponse = server.getProductResource().read("product/crm-001");
        assertEquals("crm-001", ((Map<?, ?>) itemResponse.get("content")).get("id"));

        Map<String, Object> missingResponse = server.getProductResource().read("product/missing");
        assertEquals("Product not found: missing", missingResponse.get("error"));
        assertNotNull(missingResponse.get("availableProducts"));
    }

    @Test
    void audienceResourceHandlesNullUriAsList() {
        Map<String, Object> response = server.getAudienceResource().read(null);
        Map<?, ?> content = (Map<?, ?>) response.get("content");
        assertEquals(server.getAudienceResource().read("audience/list").get("content"), content);
    }

    @Test
    void brandResourceReturnsItemById() {
        Map<String, Object> response = server.getBrandResource().read("brand/brand-002");
        assertEquals("brand-002", ((Map<?, ?>) response.get("content")).get("id"));
    }

    @Test
    void competitorsResourceListsCategories() {
        Map<String, Object> list = server.getCompetitorsResource().read("competitors/list");
        Map<?, ?> content = (Map<?, ?>) list.get("content");
        assertTrue(((List<?>) content.get("categories")).contains("CRM"));

        Map<String, Object> missing = server.getCompetitorsResource().read("competitors/unknown");
        assertEquals("Competitor not found: unknown", missing.get("error"));
    }

    private McpMarketingServer buildServer(boolean toolsEnabled, boolean resourcesEnabled) {
        McpMarketingServer instance = new McpMarketingServer(orchestratorService, validationService, storagePort);
        ReflectionTestUtils.setField(instance, "serverName", "mcp-marketing-suite-server");
        ReflectionTestUtils.setField(instance, "serverVersion", "0.1.0-test");
        ReflectionTestUtils.setField(instance, "toolsEnabled", toolsEnabled);
        ReflectionTestUtils.setField(instance, "resourcesEnabled", resourcesEnabled);
        instance.initialize();
        return instance;
    }

    private Map<String, Object> validAdsInput() {
        Map<String, Object> input = new LinkedHashMap<>();
        input.put("product", "Cloud CRM Platform");
        input.put("audience", "SMB Owners");
        input.put("brandVoice", "Professional");
        input.put("goals", "Acquire leads");
        input.put("language", "pt-BR");
        input.put("platforms", List.of("google", "meta"));
        input.put("budget", "5000");
        input.put("duration", "Q1");
        return input;
    }

    private Map<String, Object> validSeoInput() {
        Map<String, Object> input = new LinkedHashMap<>();
        input.put("product", "E-commerce Platform");
        input.put("audience", "Retailers");
        input.put("brandVoice", "Trustworthy");
        input.put("goals", "Increase traffic");
        input.put("language", "en-US");
        input.put("keywords", List.of("seo", "commerce"));
        input.put("domain", "example.com");
        input.put("monthlyBudget", 10000);
        return input;
    }

    private Map<String, Object> validCrmInput() {
        Map<String, Object> input = new LinkedHashMap<>();
        input.put("product", "Analytics Suite");
        input.put("audience", "Growth Teams");
        input.put("brandVoice", "Analytical");
        input.put("goals", "Convert trials");
        input.put("language", "es-ES");
        input.put("sequenceLength", 5);
        input.put("channels", List.of("email", "sms"));
        input.put("conversionGoal", "Demos");
        return input;
    }

    private Map<String, Object> validStrategyInput() {
        Map<String, Object> input = new LinkedHashMap<>();
        input.put("product", "Project Management Tool");
        input.put("audience", "Team Leads");
        input.put("brandVoice", "Collaborative");
        input.put("goals", "Acquire 1000 customers");
        input.put("language", "pt-BR");
        input.put("marketSegment", "SaaS");
        input.put("competitorAnalysis", "Strong incumbents");
        input.put("channels", List.of("linkedin", "events"));
        input.put("timeframe", "H1 2026");
        return input;
    }

    private AdsResult sampleAdsResult() {
        return AdsResult.builder().qaScore(95).recommendations(List.of("Add CTA")).build();
    }

    private SeoPlanResult sampleSeoPlan() {
        return SeoPlanResult.builder().qaScore(88).primaryKeywords(List.of("crm"))
                .contentStrategy(SeoPlanResult.ContentStrategy.builder().monthlyArticles(4).build()).build();
    }

    private CrmSequencesResult sampleCrmResult() {
        return CrmSequencesResult.builder()
                .qaScore(90)
                .emails(List.of(CrmSequencesResult.EmailStep.builder().dayNumber(1).subject("Hello").build()))
                .build();
    }

    private StrategyResult sampleStrategyResult() {
        return StrategyResult.builder()
                .qaScore(92)
                .executiveSummary("Plan")
                .build();
    }
}
