package com.mcp.marketing.service;

import com.mcp.marketing.config.MarketingProperties;
import com.mcp.marketing.mcp.McpResourceProvider;
import com.mcp.marketing.model.*;
import com.mcp.marketing.observability.ObservabilityService;
import com.mcp.marketing.tool.AdGeneratorTool;
import com.mcp.marketing.tool.CrmSequenceTool;
import com.mcp.marketing.tool.SeoStrategyTool;
import dev.langchain4j.model.chat.ChatLanguageModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Main orchestration service for marketing operations
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MarketingService {

    private final AdGeneratorTool adGeneratorTool;
    private final CrmSequenceTool crmSequenceTool;
    private final SeoStrategyTool seoStrategyTool;
    private final McpResourceProvider resourceProvider;
    private final ObservabilityService observability;
    private final MarketingProperties properties;
    private final ChatLanguageModel chatModel;

    public MarketingResponse generateAds(MarketingRequest request) {
        String requestId = observability.generateRequestId();
        observability.setRequestId(requestId);
        long startTime = System.currentTimeMillis();

        try {
            log.info("Processing ad generation request: {}", request);

            // Fetch MCP resources
            ProductResource product = resourceProvider.getProduct(request.getProduct());
            AudienceResource audience = resourceProvider.getAudience(request.getAudience());
            BrandResource brand = resourceProvider.getBrand(request.getBrandVoice());

            // Generate ads using tools
            Map<String, Object> ads = adGeneratorTool.generateAds(
                request.getProduct(),
                request.getAudience(),
                request.getBrandVoice(),
                request.getGoals()
            );

            // Enhance with AI if enabled
            if (properties.isEnableAiAgents() && chatModel != null) {
                ads = enhanceWithAI(ads, request);
            }

            long executionTime = System.currentTimeMillis() - startTime;

            return MarketingResponse.builder()
                .requestId(requestId)
                .status("success")
                .data(ads)
                .outputPath((String) ads.get("output_file"))
                .timestamp(LocalDateTime.now())
                .message("Ads generated successfully")
                .executionTimeMs(executionTime)
                .build();

        } catch (Exception e) {
            log.error("Error generating ads", e);
            long executionTime = System.currentTimeMillis() - startTime;

            return MarketingResponse.builder()
                .requestId(requestId)
                .status("error")
                .timestamp(LocalDateTime.now())
                .message("Failed to generate ads: " + e.getMessage())
                .executionTimeMs(executionTime)
                .build();
        } finally {
            observability.clearRequestId();
        }
    }

    public MarketingResponse generateCrmSequence(MarketingRequest request) {
        String requestId = observability.generateRequestId();
        observability.setRequestId(requestId);
        long startTime = System.currentTimeMillis();

        try {
            log.info("Processing CRM sequence generation request: {}", request);

            // Fetch MCP resources
            ProductResource product = resourceProvider.getProduct(request.getProduct());
            AudienceResource audience = resourceProvider.getAudience(request.getAudience());

            // Generate CRM sequence
            Map<String, Object> sequence = crmSequenceTool.generateCrmSequence(
                request.getProduct(),
                request.getAudience(),
                request.getBrandVoice(),
                request.getGoals()
            );

            long executionTime = System.currentTimeMillis() - startTime;

            return MarketingResponse.builder()
                .requestId(requestId)
                .status("success")
                .data(sequence)
                .outputPath((String) sequence.get("output_file"))
                .timestamp(LocalDateTime.now())
                .message("CRM sequence generated successfully")
                .executionTimeMs(executionTime)
                .build();

        } catch (Exception e) {
            log.error("Error generating CRM sequence", e);
            long executionTime = System.currentTimeMillis() - startTime;

            return MarketingResponse.builder()
                .requestId(requestId)
                .status("error")
                .timestamp(LocalDateTime.now())
                .message("Failed to generate CRM sequence: " + e.getMessage())
                .executionTimeMs(executionTime)
                .build();
        } finally {
            observability.clearRequestId();
        }
    }

    public MarketingResponse generateSeoStrategy(MarketingRequest request) {
        String requestId = observability.generateRequestId();
        observability.setRequestId(requestId);
        long startTime = System.currentTimeMillis();

        try {
            log.info("Processing SEO strategy generation request: {}", request);

            // Fetch MCP resources
            ProductResource product = resourceProvider.getProduct(request.getProduct());
            AudienceResource audience = resourceProvider.getAudience(request.getAudience());
            CompetitorResource competitors = resourceProvider.getCompetitors(request.getCompetitors());

            // Generate SEO strategy
            Map<String, Object> strategy = seoStrategyTool.generateSeoStrategy(
                request.getProduct(),
                request.getAudience(),
                request.getBrandVoice(),
                request.getGoals()
            );

            long executionTime = System.currentTimeMillis() - startTime;

            return MarketingResponse.builder()
                .requestId(requestId)
                .status("success")
                .data(strategy)
                .outputPath((String) strategy.get("output_file"))
                .timestamp(LocalDateTime.now())
                .message("SEO strategy generated successfully")
                .executionTimeMs(executionTime)
                .build();

        } catch (Exception e) {
            log.error("Error generating SEO strategy", e);
            long executionTime = System.currentTimeMillis() - startTime;

            return MarketingResponse.builder()
                .requestId(requestId)
                .status("error")
                .timestamp(LocalDateTime.now())
                .message("Failed to generate SEO strategy: " + e.getMessage())
                .executionTimeMs(executionTime)
                .build();
        } finally {
            observability.clearRequestId();
        }
    }

    public MarketingResponse generateFullStrategy(MarketingRequest request) {
        String requestId = observability.generateRequestId();
        observability.setRequestId(requestId);
        long startTime = System.currentTimeMillis();

        try {
            log.info("Processing full GTM strategy generation request: {}", request);

            Map<String, Object> fullStrategy = new HashMap<>();

            // Generate all components
            fullStrategy.put("ads", adGeneratorTool.generateAds(
                request.getProduct(),
                request.getAudience(),
                request.getBrandVoice(),
                request.getGoals()
            ));

            fullStrategy.put("crm_sequence", crmSequenceTool.generateCrmSequence(
                request.getProduct(),
                request.getAudience(),
                request.getBrandVoice(),
                request.getGoals()
            ));

            fullStrategy.put("seo_strategy", seoStrategyTool.generateSeoStrategy(
                request.getProduct(),
                request.getAudience(),
                request.getBrandVoice(),
                request.getGoals()
            ));

            long executionTime = System.currentTimeMillis() - startTime;

            return MarketingResponse.builder()
                .requestId(requestId)
                .status("success")
                .data(fullStrategy)
                .timestamp(LocalDateTime.now())
                .message("Full GTM strategy generated successfully")
                .executionTimeMs(executionTime)
                .build();

        } catch (Exception e) {
            log.error("Error generating full strategy", e);
            long executionTime = System.currentTimeMillis() - startTime;

            return MarketingResponse.builder()
                .requestId(requestId)
                .status("error")
                .timestamp(LocalDateTime.now())
                .message("Failed to generate full strategy: " + e.getMessage())
                .executionTimeMs(executionTime)
                .build();
        } finally {
            observability.clearRequestId();
        }
    }

    private Map<String, Object> enhanceWithAI(Map<String, Object> ads, MarketingRequest request) {
        try {
            log.info("Enhancing ads with AI");
            // In a real implementation, use AI to refine and optimize the ads
            // For now, just return the original ads
            return ads;
        } catch (Exception e) {
            log.warn("Failed to enhance with AI, returning original ads", e);
            return ads;
        }
    }
}

