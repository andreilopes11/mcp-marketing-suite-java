package com.mcp.marketing.mcp.tools;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcp.marketing.api.dto.StandardResponse;
import com.mcp.marketing.domain.model.AdsResult;
import com.mcp.marketing.domain.model.ExecutionMode;
import com.mcp.marketing.domain.model.MarketingContext;
import com.mcp.marketing.domain.ports.StoragePort;
import com.mcp.marketing.domain.service.OrchestratorService;
import com.mcp.marketing.domain.service.ValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * MCP Tool for Ads Generation
 * <p>
 * Generates multi-platform advertising content (Google, Meta, LinkedIn)
 */
public class AdsGenerationTool {

    private static final Logger logger = LoggerFactory.getLogger(AdsGenerationTool.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final OrchestratorService orchestratorService;
    private final ValidationService validationService;
    private final StoragePort storagePort;

    public AdsGenerationTool(OrchestratorService orchestratorService,
                             ValidationService validationService,
                             StoragePort storagePort) {
        this.orchestratorService = orchestratorService;
        this.validationService = validationService;
        this.storagePort = storagePort;
    }

    /**
     * Execute the ads generation tool
     */
    public Map<String, Object> execute(Map<String, Object> input) {
        String requestId = UUID.randomUUID().toString();
        long startTime = System.currentTimeMillis();

        logger.info("MCP Tool 'ads' called with request_id={}", requestId);

        try {
            // Parse input
            String product = getRequiredString(input, "product");
            String audience = getRequiredString(input, "audience");
            String brandVoice = getRequiredString(input, "brandVoice");
            String goals = getRequiredString(input, "goals");
            String language = getRequiredString(input, "language");

            // Optional parameters
            List<String> platforms = getOptionalList(input, "platforms");
            String budget = getOptionalString(input, "budget");
            String duration = getOptionalString(input, "duration");

            // Build context
            MarketingContext context = MarketingContext.builder()
                    .requestId(requestId)
                    .product(product)
                    .audience(audience)
                    .brandVoice(brandVoice)
                    .goals(goals)
                    .language(language)
                    .executionMode(ExecutionMode.DETERMINISTIC)
                    .platforms(platforms)
                    .budget(budget)
                    .duration(duration)
                    .build();

            // Validate
            List<String> errors = validationService.validateContext(context);
            if (!errors.isEmpty()) {
                return buildErrorResponse(requestId, "VALIDATION_ERROR",
                        "Validation failed: " + String.join(", ", errors), startTime);
            }

            // Generate ads
            AdsResult result = orchestratorService.generateAds(context);
            long executionTime = System.currentTimeMillis() - startTime;

            // Build response
            Map<String, Object> data = new LinkedHashMap<>();
            data.put("artifact_type", "ads");
            data.put("execution_time_ms", executionTime);
            data.put("result", result);

            StandardResponse<Map<String, Object>> response = StandardResponse.success(requestId, data);

            // Persist
            String outputPath = storagePort.saveJson("ads", requestId, response);
            if (outputPath != null) {
                data.put("output_path", outputPath);
            }

            logger.info("MCP Tool 'ads' completed successfully: request_id={} execution_time_ms={}",
                    requestId, executionTime);

            return toMap(response);

        } catch (IllegalArgumentException e) {
            logger.warn("MCP Tool 'ads' validation error: request_id={} error={}", requestId, e.getMessage());
            return buildErrorResponse(requestId, "INVALID_INPUT", e.getMessage(), startTime);
        } catch (Exception e) {
            logger.error("MCP Tool 'ads' failed: request_id={}", requestId, e);
            return buildErrorResponse(requestId, "INTERNAL_ERROR", e.getMessage(), startTime);
        }
    }

    private Map<String, Object> buildErrorResponse(String requestId, String errorCode,
                                                   String message, long startTime) {
        long executionTime = System.currentTimeMillis() - startTime;
        Map<String, Object> error = new LinkedHashMap<>();
        error.put("requestId", requestId);
        error.put("timestamp", Instant.now().toString());
        error.put("status", 400);
        error.put("success", false);
        error.put("error", errorCode);
        error.put("message", message);
        error.put("executionTimeMs", executionTime);
        return error;
    }

    private String getRequiredString(Map<String, Object> input, String key) {
        Object value = input.get(key);
        if (value == null || value.toString().trim().isEmpty()) {
            throw new IllegalArgumentException(key + " is required");
        }
        return value.toString();
    }

    private String getOptionalString(Map<String, Object> input, String key) {
        Object value = input.get(key);
        return value != null ? value.toString() : null;
    }

    @SuppressWarnings("unchecked")
    private List<String> getOptionalList(Map<String, Object> input, String key) {
        Object value = input.get(key);
        if (value instanceof List) {
            return (List<String>) value;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> toMap(Object obj) {
        return objectMapper.convertValue(obj, Map.class);
    }
}
