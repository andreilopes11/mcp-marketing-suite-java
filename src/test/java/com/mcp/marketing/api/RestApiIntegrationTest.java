package com.mcp.marketing.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcp.marketing.api.dto.AdsRequest;
import com.mcp.marketing.api.dto.CrmSequencesRequest;
import com.mcp.marketing.api.dto.SeoPlanRequest;
import com.mcp.marketing.api.dto.StrategyRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration test validating complete REST API flow with file persistence
 */
@SpringBootTest
@AutoConfigureMockMvc
class RestApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void cleanOutputs() {
        cleanOutputsDirectory();
    }

    @Test
    void testAdsEndpointCreatesOutputFile() throws Exception {
        // Clean outputs directory before test
        cleanOutputsDirectory();

        // Given
        AdsRequest request = createAdsRequest();

        // When & Then
        mockMvc.perform(post("/api/marketing/ads")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Request-Id", "integration-test-001")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.requestId").value("integration-test-001"))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.artifact_type").value("ads"))
                .andExpect(jsonPath("$.data.execution_time_ms").isNumber())
                .andExpect(jsonPath("$.data.output_path").exists())
                .andExpect(jsonPath("$.data.result.qaScore").isNumber())
                .andExpect(jsonPath("$.data.result.recommendations").isArray())
                .andExpect(jsonPath("$.data.result.googleAds").exists())
                .andExpect(jsonPath("$.data.result.metaAds").exists())
                .andExpect(jsonPath("$.data.result.linkedinAds").exists());

        // Verify file was created
        Path outputsPath = Paths.get("./outputs");
        if (Files.exists(outputsPath)) {
            try (Stream<Path> files = Files.list(outputsPath)) {
                long count = files.filter(p -> p.toString().contains("ads_integration-test-001")).count();
                assert count > 0 : "Expected at least one output file to be created";
            }
        }
    }

    @Test
    void testValidationErrorResponse() throws Exception {
        // Given - missing required fields
        String invalidRequest = "{\"product\": \"Test\"}";

        // When & Then
        mockMvc.perform(post("/api/marketing/ads")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.requestId").exists())
                .andExpect(jsonPath("$.executionTimeMs").isNumber())
                .andExpect(jsonPath("$.fieldErrors").isArray());
    }

    @Test
    void testSeoPlanEndpointProcessesKeywords() throws Exception {
        SeoPlanRequest request = createSeoPlanRequest();

        mockMvc.perform(post("/api/marketing/seo-plan")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Request-Id", "seo-plan-001")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.artifact_type").value("seo-plan"))
                .andExpect(jsonPath("$.data.result.primaryKeywords").isArray())
                .andExpect(jsonPath("$.data.result.contentStrategy").exists());
    }

    @Test
    void testCrmSequencesEndpointReturnsTouchpoints() throws Exception {
        CrmSequencesRequest request = createCrmRequest();

        mockMvc.perform(post("/api/marketing/crm-sequences")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.artifact_type").value("crm-sequences"))
                .andExpect(jsonPath("$.data.result.qaScore").isNumber())
                .andExpect(jsonPath("$.data.result.emails").isArray());
    }

    @Test
    void testStrategyEndpointAggregatesArtifacts() throws Exception {
        StrategyRequest request = createStrategyRequest();

        mockMvc.perform(post("/api/marketing/strategy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.artifact_type").value("strategy"))
                .andExpect(jsonPath("$.data.result.executiveSummary").exists())
                .andExpect(jsonPath("$.data.result.qaScore").isNumber());
    }

    @Test
    void testHealthEndpointReturnsMetadata() throws Exception {
        mockMvc.perform(get("/health").header("X-Request-Id", "health-check"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.service").exists())
                .andExpect(jsonPath("$.request_id").value("health-check"))
                .andExpect(jsonPath("$.execution_time_ms").isNumber())
                .andExpect(jsonPath("$.uptime_seconds").isNumber());
    }

    private void cleanOutputsDirectory() {
        try {
            Path outputsPath = Paths.get("./outputs");
            if (Files.exists(outputsPath)) {
                try (Stream<Path> paths = Files.walk(outputsPath)) {
                    paths.sorted(Comparator.reverseOrder())
                            .map(Path::toFile)
                            .filter(f -> !f.getPath().equals(outputsPath.toString()))
                            .forEach(File::delete);
                }
            }
        } catch (Exception e) {
            // Ignore cleanup errors
        }
    }

    private SeoPlanRequest createSeoPlanRequest() {
        return SeoPlanRequest.builder()
                .product("SaaS Platform")
                .audience("B2B Tech Companies")
                .brandVoice("Professional")
                .goals("Increase organic traffic")
                .language("pt-BR")
                .keywords(List.of("crm", "automation"))
                .domain("example.com")
                .monthlyBudget(5000)
                .build();
    }

    private CrmSequencesRequest createCrmRequest() {
        return CrmSequencesRequest.builder()
                .product("SaaS Platform")
                .audience("Revenue Teams")
                .brandVoice("Analytical")
                .goals("Convert trials")
                .language("pt-BR")
                .sequenceLength(4)
                .channels(List.of("email", "sms"))
                .conversionGoal("Booked demo")
                .build();
    }

    private StrategyRequest createStrategyRequest() {
        return StrategyRequest.builder()
                .product("SaaS Platform")
                .audience("Global Enterprises")
                .brandVoice("Confident")
                .goals("Expand reach")
                .language("pt-BR")
                .marketSegment("Enterprise SaaS")
                .competitorAnalysis("Crowded space")
                .channels(List.of("events", "linkedin"))
                .timeframe("FY2026")
                .build();
    }

    private AdsRequest createAdsRequest() {
        AdsRequest request = new AdsRequest();
        request.setProduct("SaaS Platform");
        request.setAudience("B2B Tech Companies");
        request.setBrandVoice("Professional");
        request.setGoals("Lead Generation");
        request.setLanguage("pt-BR");
        return request;
    }
}
