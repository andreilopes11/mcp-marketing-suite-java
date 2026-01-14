package com.mcp.marketing.service;

import com.mcp.marketing.config.MarketingProperties;
import com.mcp.marketing.model.MarketingRequest;
import com.mcp.marketing.model.MarketingResponse;
import com.mcp.marketing.observability.ObservabilityService;
import com.mcp.marketing.resource.McpResourceProvider;
import com.mcp.marketing.tool.AdGeneratorTool;
import com.mcp.marketing.tool.CrmSequenceTool;
import com.mcp.marketing.tool.SeoStrategyTool;
import dev.langchain4j.model.chat.ChatLanguageModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

/**
 * Main orchestration service for marketing operations
 * Optimized for performance with DRY principles and async support
 */
@Slf4j
@Service
public class MarketingService {

    private final AdGeneratorTool adGeneratorTool;
    private final CrmSequenceTool crmSequenceTool;
    private final SeoStrategyTool seoStrategyTool;
    private final McpResourceProvider resourceProvider;
    private final ObservabilityService observability;
    private final MarketingProperties properties;
    private final ChatLanguageModel chatModel;
    private final Executor taskExecutor;

    /**
     * Constructor with explicit @Qualifier for taskExecutor
     * This is the correct way to use @Qualifier with dependency injection
     */
    public MarketingService(
            AdGeneratorTool adGeneratorTool,
            CrmSequenceTool crmSequenceTool,
            SeoStrategyTool seoStrategyTool,
            McpResourceProvider resourceProvider,
            ObservabilityService observability,
            MarketingProperties properties,
            ChatLanguageModel chatModel,
            @Qualifier("marketingTaskExecutor") Executor taskExecutor) {
        this.adGeneratorTool = adGeneratorTool;
        this.crmSequenceTool = crmSequenceTool;
        this.seoStrategyTool = seoStrategyTool;
        this.resourceProvider = resourceProvider;
        this.observability = observability;
        this.properties = properties;
        this.chatModel = chatModel;
        this.taskExecutor = taskExecutor;
    }

    /**
     * Generate ads for multiple platforms
     * Optimized: reuses extracted logic for consistency
     */
    public MarketingResponse generateAds(MarketingRequest request) {
        validateRequest(request);
        return executeWithObservability("Ads generation", request, () -> generateAdsData(request));
    }

    /**
     * Generate CRM email sequence
     * Optimized: reuses extracted logic for consistency
     */
    public MarketingResponse generateCrmSequence(MarketingRequest request) {
        validateRequest(request);
        return executeWithObservability("CRM sequence generation", request, () -> generateCrmData(request));
    }

    /**
     * Generate SEO strategy
     * Optimized: reuses extracted logic for consistency
     */
    public MarketingResponse generateSeoStrategy(MarketingRequest request) {
        validateRequest(request);
        return executeWithObservability("SEO strategy generation", request, () -> generateSeoData(request));
    }

    /**
     * Generate full GTM strategy with all components
     * Optimized: reuses existing service methods with parallel execution
     */
    public MarketingResponse generateFullStrategy(MarketingRequest request) {
        validateRequest(request);

        String requestId = observability.generateRequestId();
        observability.setRequestId(requestId);
        long startTime = System.currentTimeMillis();

        try {
            log.info("Processing full GTM strategy generation request: {}", request);

            // Parallel execution with configured executor for better performance
            Executor executor = taskExecutor != null ? taskExecutor : Runnable::run;

            // Execute all components in parallel by reusing existing service logic
            CompletableFuture<Map<String, Object>> adsFuture = CompletableFuture.supplyAsync(() ->
                    executeComponentWithRequestId(requestId, "Ads generation", () -> generateAdsData(request)), executor);

            CompletableFuture<Map<String, Object>> crmFuture = CompletableFuture.supplyAsync(() ->
                    executeComponentWithRequestId(requestId, "CRM sequence generation", () -> generateCrmData(request)), executor);

            CompletableFuture<Map<String, Object>> seoFuture = CompletableFuture.supplyAsync(() ->
                    executeComponentWithRequestId(requestId, "SEO strategy generation", () -> generateSeoData(request)), executor);

            // Wait for all tasks to complete
            CompletableFuture.allOf(adsFuture, crmFuture, seoFuture).join();

            // Collect results
            Map<String, Object> fullStrategy = new HashMap<>();
            fullStrategy.put("ads_strategy", adsFuture.join());
            fullStrategy.put("crm_strategy", crmFuture.join());
            fullStrategy.put("seo_strategy", seoFuture.join());

            // Add metadata
            fullStrategy.put("generated_at", LocalDateTime.now().toString());
            fullStrategy.put("request_context", Map.of(
                    "product", request.getProduct(),
                    "audience", request.getAudience(),
                    "goals", request.getGoals()
            ));

            long executionTime = System.currentTimeMillis() - startTime;
            log.info("Full GTM strategy generated successfully for product: {}, audience: {}, goals: {}",
                    request.getProduct(), request.getAudience(), request.getGoals());

            return buildSuccessResponse(requestId, fullStrategy, "full GTM strategy generation", executionTime);

        } catch (Exception e) {
            log.error("Error in full GTM strategy generation: {}", e.getMessage(), e);
            long executionTime = System.currentTimeMillis() - startTime;
            return buildErrorResponse(requestId, "full GTM strategy generation", e.getMessage(), executionTime);

        } finally {
            observability.clearRequestId();
        }
    }

    /**
     * Execute a component with proper request ID context in async threads
     */
    private Map<String, Object> executeComponentWithRequestId(
            String requestId,
            String componentName,
            Supplier<Map<String, Object>> supplier) {
        observability.setRequestId(requestId);
        try {
            log.info("Generating {} as part of full strategy", componentName);
            return supplier.get();
        } catch (Exception e) {
            log.error("Error generating {} in full strategy", componentName, e);
            return Map.of("error", "Failed to generate " + componentName + ": " + e.getMessage());
        } finally {
            observability.clearRequestId();
        }
    }

    /**
     * Generate ads data - extracted logic from generateAds() for reuse
     */
    private Map<String, Object> generateAdsData(MarketingRequest request) {
        // Pre-load resources asynchronously (cached, non-blocking)
        CompletableFuture<Void> resourcesFuture = taskExecutor != null ? CompletableFuture.runAsync(() -> {
            resourceProvider.getProduct(request.getProduct());
            resourceProvider.getAudience(request.getAudience());
            resourceProvider.getBrand(request.getBrandVoice());
        }, taskExecutor) : CompletableFuture.completedFuture(null);

        // Generate ads
        Map<String, Object> ads = adGeneratorTool.generateAds(
                request.getProduct(),
                request.getAudience(),
                request.getBrandVoice(),
                request.getGoals()
        );

        // Wait for resources to be cached (for future requests)
        resourcesFuture.join();

        // Enhance with AI if enabled
        return maybeEnhanceWithAI(ads, request);
    }

    /**
     * Generate CRM data - extracted logic from generateCrmSequence() for reuse
     */
    private Map<String, Object> generateCrmData(MarketingRequest request) {
        // Pre-load resources (cached)
        resourceProvider.getProduct(request.getProduct());
        resourceProvider.getAudience(request.getAudience());

        log.info("Generating CRM sequence for product: {}, audience: {}, Brand Voice: {}, goals: {}",
                request.getProduct(), request.getAudience(), request.getBrandVoice(), request.getGoals());
        return crmSequenceTool.generateCrmSequence(
                request.getProduct(),
                request.getAudience(),
                request.getBrandVoice(),
                request.getGoals()
        );
    }

    /**
     * Generate SEO data - extracted logic from generateSeoStrategy() for reuse
     */
    private Map<String, Object> generateSeoData(MarketingRequest request) {
        // Pre-load all relevant resources
        resourceProvider.getProduct(request.getProduct());
        resourceProvider.getAudience(request.getAudience());
        if (request.getCompetitors() != null && !request.getCompetitors().isEmpty()) {
            resourceProvider.getCompetitors(request.getCompetitors());
        }

        log.info("Generating SEO strategy for product: {}, audience: {}, Brand Voice: {}, goals: {}",
                request.getProduct(), request.getAudience(), request.getBrandVoice(), request.getGoals());
        return seoStrategyTool.generateSeoStrategy(
                request.getProduct(),
                request.getAudience(),
                request.getBrandVoice(),
                request.getGoals()
        );
    }

    /**
     * Template method to handle observability, timing, and error handling
     * Eliminates code duplication across all service methods
     */
    private MarketingResponse executeWithObservability(
            String operationType,
            MarketingRequest request,
            Supplier<Map<String, Object>> operation) {

        String requestId = observability.generateRequestId();
        observability.setRequestId(requestId);
        long startTime = System.currentTimeMillis();

        try {
            log.info("Processing {} request: {}", operationType, request);

            Map<String, Object> data = operation.get();
            long executionTime = System.currentTimeMillis() - startTime;
            return buildSuccessResponse(requestId, data, operationType, executionTime);

        } catch (Exception e) {
            log.error("Error in {}: {}", operationType, e.getMessage(), e);
            long executionTime = System.currentTimeMillis() - startTime;

            return buildErrorResponse(requestId, operationType, e.getMessage(), executionTime);

        } finally {
            observability.clearRequestId();
        }
    }

    /**
     * Build success response - extracted for reuse
     */
    private MarketingResponse buildSuccessResponse(
            String requestId,
            Map<String, Object> data,
            String operationType,
            long executionTime) {

        log.info("{} successful for requestId: {}", operationType, requestId);
        return MarketingResponse.builder()
                .requestId(requestId)
                .status("success")
                .data(data)
                .outputPath((String) data.get("output_file"))
                .timestamp(LocalDateTime.now())
                .message(operationType + " completed successfully")
                .executionTimeMs(executionTime)
                .build();
    }

    /**
     * Build error response - extracted for reuse
     */
    private MarketingResponse buildErrorResponse(
            String requestId,
            String operationType,
            String errorMessage,
            long executionTime) {

        log.info("{} failed for requestId: {} with error: {}", operationType, requestId, errorMessage);
        return MarketingResponse.builder()
                .requestId(requestId)
                .status("error")
                .timestamp(LocalDateTime.now())
                .message("Failed " + operationType + ": " + errorMessage)
                .executionTimeMs(executionTime)
                .build();
    }

    /**
     * Conditionally enhance ads with AI - only if enabled and available
     */
    private Map<String, Object> maybeEnhanceWithAI(Map<String, Object> ads, MarketingRequest request) {
        if (!properties.isEnableAiAgents() || chatModel == null) {
            return ads;
        }

        try {
            log.info("Enhancing ads with AI for request: {}", request.getProduct());
            return enhanceWithAI(ads, request);
        } catch (Exception e) {
            log.warn("AI enhancement failed, using original ads: {}", e.getMessage());
            return ads;
        }
    }

    /**
     * AI enhancement logic - placeholder for future implementation
     *
     * @param ads     The ads to enhance
     * @param request The original marketing request for context
     * @return Enhanced ads or original ads if enhancement not implemented
     */
    private Map<String, Object> enhanceWithAI(Map<String, Object> ads, MarketingRequest request) {
        // TODO: Implement AI-based enhancement using chatModel and request context
        // Future implementation will use request.getBrandVoice(), request.getGoals(), etc.
        log.info("AI enhancement placeholder - returning original ads for product: {}",
                request.getProduct());
        return ads;
    }

    /**
     * Validate marketing request
     *
     * @throws IllegalArgumentException if validation fails
     */
    private void validateRequest(MarketingRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Marketing request cannot be null");
        }

        if (request.getProduct() == null || request.getProduct().trim().isEmpty()) {
            throw new IllegalArgumentException("Product name is required and cannot be empty");
        }

        if (request.getAudience() == null || request.getAudience().trim().isEmpty()) {
            throw new IllegalArgumentException("Target audience is required and cannot be empty");
        }

        if (request.getGoals() == null || request.getGoals().isEmpty()) {
            throw new IllegalArgumentException("At least one marketing goal is required");
        }

        // Validate goals are not empty strings
        if (request.getGoals().stream().anyMatch(g -> g == null || g.trim().isEmpty())) {
            throw new IllegalArgumentException("Marketing goals cannot contain empty values");
        }

        log.info("Request validation passed for product: {}", request.getProduct());
    }
}
