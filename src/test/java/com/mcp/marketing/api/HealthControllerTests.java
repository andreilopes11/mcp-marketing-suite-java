package com.mcp.marketing.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcp.marketing.api.exception.GlobalExceptionHandler;
import com.mcp.marketing.api.controller.MarketingController;
import com.mcp.marketing.api.util.RequestIdResolver;
import com.mcp.marketing.domain.model.AdsResult;
import com.mcp.marketing.domain.ports.StoragePort;
import com.mcp.marketing.domain.service.OrchestratorService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests for health endpoint
 */
@WebMvcTest(MarketingController.class)
@Import({RequestIdResolver.class, GlobalExceptionHandler.class})
@TestPropertySource(properties = {
        "spring.application.name=test-suite",
        "app.version=0.0-test"
})
class HealthControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrchestratorService orchestratorService;

    @MockBean
    private StoragePort storagePort;

    @Test
    void healthEndpointReturnsOk() throws Exception {
        mockMvc.perform(get("/health").header("X-Request-Id", "test-request-123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.service").value("test-suite"))
                .andExpect(jsonPath("$.version").value("0.0-test"))
                .andExpect(jsonPath("$.request_id").value("test-request-123"))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.uptime_seconds").isNumber())
                .andExpect(jsonPath("$.execution_time_ms").isNumber());
    }

    @Test
    void adsEndpointPersistsAndReturnsEnvelope() throws Exception {
        AdsResult sampleResult = AdsResult.builder()
                .qaScore(90)
                .recommendations(java.util.List.of("Add proof"))
                .build();
        when(orchestratorService.generateAds(any())).thenReturn(sampleResult);
        when(storagePort.saveJson(eq("ads"), eq("test-request-id"), any())).thenReturn("/tmp/ads_123.json");

        String payload = objectMapper.writeValueAsString(java.util.Map.of(
                "product", "SaaS",
                "audience", "B2B",
                "brandVoice", "Professional",
                "goals", "Leads",
                "language", "en"
        ));

        mockMvc.perform(post("/api/marketing/ads")
                        .header("X-Request-Id", "test-request-id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.requestId").value("test-request-id"))
                .andExpect(jsonPath("$.data.artifact_type").value("ads"))
                .andExpect(jsonPath("$.data.execution_time_ms").isNumber())
                .andExpect(jsonPath("$.data.output_path").value("/tmp/ads_123.json"))
                .andExpect(jsonPath("$.data.result.qaScore").value(90));

        ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(Object.class);
        verify(storagePort).saveJson(eq("ads"), eq("test-request-id"), captor.capture());
        assertThat(captor.getValue()).isNotNull();
    }
}
