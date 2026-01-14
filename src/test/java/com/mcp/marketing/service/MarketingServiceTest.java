package com.mcp.marketing.service;

import com.mcp.marketing.config.MarketingProperties;
import com.mcp.marketing.mcp.McpResourceProvider;
import com.mcp.marketing.model.MarketingRequest;
import com.mcp.marketing.model.MarketingResponse;
import com.mcp.marketing.observability.ObservabilityService;
import com.mcp.marketing.tool.AdGeneratorTool;
import com.mcp.marketing.tool.CrmSequenceTool;
import com.mcp.marketing.tool.SeoStrategyTool;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MarketingServiceTest {

    @Mock
    private AdGeneratorTool adGeneratorTool;

    @Mock
    private CrmSequenceTool crmSequenceTool;

    @Mock
    private SeoStrategyTool seoStrategyTool;

    @Mock
    private McpResourceProvider resourceProvider;

    @Mock
    private ObservabilityService observability;

    @Mock
    private MarketingProperties properties;

    @InjectMocks
    private MarketingService marketingService;

    private MarketingRequest testRequest;

    @BeforeEach
    void setUp() {
        testRequest = MarketingRequest.builder()
                .product("Test Product")
                .audience("Test Audience")
                .brandVoice("Professional")
                .goals(Arrays.asList("Goal 1", "Goal 2"))
                .build();

        when(observability.generateRequestId()).thenReturn("test-123");
    }

    @Test
    void testGenerateAds_Success() {
        // Arrange
        Map<String, Object> mockAds = new HashMap<>();
        mockAds.put("google_ads", new HashMap<>());
        mockAds.put("output_file", "test-output.json");

        when(adGeneratorTool.generateAds(anyString(), anyString(), anyString(), anyList()))
                .thenReturn(mockAds);

        // Act
        MarketingResponse response = marketingService.generateAds(testRequest);

        // Assert
        assertNotNull(response);
        assertEquals("success", response.getStatus());
        assertNotNull(response.getData());
        assertEquals("test-123", response.getRequestId());

        verify(adGeneratorTool).generateAds(
                eq("Test Product"),
                eq("Test Audience"),
                eq("Professional"),
                eq(Arrays.asList("Goal 1", "Goal 2"))
        );
    }

    @Test
    void testGenerateCrmSequence_Success() {
        // Arrange
        Map<String, Object> mockSequence = new HashMap<>();
        mockSequence.put("emails", Arrays.asList());
        mockSequence.put("output_file", "test-crm.json");

        when(crmSequenceTool.generateCrmSequence(anyString(), anyString(), anyString(), anyList()))
                .thenReturn(mockSequence);

        // Act
        MarketingResponse response = marketingService.generateCrmSequence(testRequest);

        // Assert
        assertNotNull(response);
        assertEquals("success", response.getStatus());
        assertNotNull(response.getData());
    }

    @Test
    void testGenerateSeoStrategy_Success() {
        // Arrange
        Map<String, Object> mockStrategy = new HashMap<>();
        mockStrategy.put("keywords", Arrays.asList());
        mockStrategy.put("output_file", "test-seo.json");

        when(seoStrategyTool.generateSeoStrategy(anyString(), anyString(), anyString(), anyList()))
                .thenReturn(mockStrategy);

        // Act
        MarketingResponse response = marketingService.generateSeoStrategy(testRequest);

        // Assert
        assertNotNull(response);
        assertEquals("success", response.getStatus());
        assertNotNull(response.getData());
    }
}

