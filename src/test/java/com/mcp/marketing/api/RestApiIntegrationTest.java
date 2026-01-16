package com.mcp.marketing.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcp.marketing.api.dto.AdsRequest;
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
import java.util.stream.Stream;

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

    @Test
    void testAdsEndpointCreatesOutputFile() throws Exception {
        // Clean outputs directory before test
        cleanOutputsDirectory();

        // Given
        AdsRequest request = AdsRequest.builder()
                .product("Cloud CRM Platform")
                .audience("Small Business Owners")
                .brandVoice("Professional and Approachable")
                .goals("Generate 100 qualified leads per month")
                .language("en")
                .build();

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
}
