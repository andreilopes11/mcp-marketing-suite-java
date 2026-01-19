package com.mcp.marketing.api.controller;

import com.mcp.marketing.api.context.RequestContextAttributes;
import com.mcp.marketing.api.dto.*;
import com.mcp.marketing.api.util.RequestIdResolver;
import com.mcp.marketing.domain.model.MarketingContext;
import com.mcp.marketing.domain.ports.StoragePort;
import com.mcp.marketing.domain.service.OrchestratorService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.lang.management.ManagementFactory;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

/**
 * REST controller responsible for deterministic marketing payload generation.
 */
@RestController
public class MarketingController {

    private static final Logger logger = LoggerFactory.getLogger(MarketingController.class);

    private final OrchestratorService orchestratorService;
    private final StoragePort storagePort;
    private final RequestIdResolver requestIdResolver;
    private final String serviceName;
    private final String serviceVersion;

    public MarketingController(OrchestratorService orchestratorService,
                               StoragePort storagePort,
                               RequestIdResolver requestIdResolver,
                               @Value("${spring.application.name:mcp-marketing-suite}") String serviceName,
                               @Value("${app.version:0.1.0}") String serviceVersion) {
        this.orchestratorService = orchestratorService;
        this.storagePort = storagePort;
        this.requestIdResolver = requestIdResolver;
        this.serviceName = serviceName;
        this.serviceVersion = serviceVersion;
    }

    @PostMapping("/api/marketing/ads")
    public ResponseEntity<StandardResponse<Map<String, Object>>> generateAds(@Valid @RequestBody AdsRequest request,
                                                                             HttpServletRequest httpRequest) {
        return processRequest(httpRequest, "ads", requestId -> {
            MarketingContext context = baseBuilder(requestId, request.getProduct(), request.getAudience(),
                    request.getBrandVoice(), request.getGoals(), request.getLanguage())
                    .platforms(request.getPlatforms())
                    .budget(request.getBudget())
                    .duration(request.getDuration())
                    .build();
            return orchestratorService.generateAds(context);
        });
    }

    @PostMapping("/api/marketing/seo-plan")
    public ResponseEntity<StandardResponse<Map<String, Object>>> generateSeoPlan(@Valid @RequestBody SeoPlanRequest request,
                                                                                 HttpServletRequest httpRequest) {
        return processRequest(httpRequest, "seo-plan", requestId -> {
            MarketingContext context = baseBuilder(requestId, request.getProduct(), request.getAudience(),
                    request.getBrandVoice(), request.getGoals(), request.getLanguage())
                    .keywords(request.getKeywords())
                    .domain(request.getDomain())
                    .monthlyBudget(request.getMonthlyBudget())
                    .build();
            return orchestratorService.generateSeoPlan(context);
        });
    }

    @PostMapping("/api/marketing/crm-sequences")
    public ResponseEntity<StandardResponse<Map<String, Object>>> generateCrmSequences(@Valid @RequestBody CrmSequencesRequest request,
                                                                                      HttpServletRequest httpRequest) {
        return processRequest(httpRequest, "crm-sequences", requestId -> {
            MarketingContext context = baseBuilder(requestId, request.getProduct(), request.getAudience(),
                    request.getBrandVoice(), request.getGoals(), request.getLanguage())
                    .sequenceLength(request.getSequenceLength())
                    .channels(request.getChannels())
                    .conversionGoal(request.getConversionGoal())
                    .build();
            return orchestratorService.generateCrmSequences(context);
        });
    }

    @PostMapping("/api/marketing/strategy")
    public ResponseEntity<StandardResponse<Map<String, Object>>> generateStrategy(@Valid @RequestBody StrategyRequest request,
                                                                                  HttpServletRequest httpRequest) {
        return processRequest(httpRequest, "strategy", requestId -> {
            MarketingContext context = baseBuilder(requestId, request.getProduct(), request.getAudience(),
                    request.getBrandVoice(), request.getGoals(), request.getLanguage())
                    .marketSegment(request.getMarketSegment())
                    .competitorAnalysis(request.getCompetitorAnalysis())
                    .channels(request.getChannels())
                    .timeframe(request.getTimeframe())
                    .build();
            return orchestratorService.generateStrategy(context);
        });
    }

    @GetMapping("/health")
    public Map<String, Object> health(HttpServletRequest request) {
        String requestId = resolveRequestId(request);
        long executionTime = calculateExecutionTime(request);
        long uptimeSeconds = ManagementFactory.getRuntimeMXBean().getUptime() / 1000;

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("status", "UP");
        payload.put("service", serviceName);
        payload.put("version", serviceVersion);
        payload.put("timestamp", Instant.now().toString());
        payload.put("request_id", requestId);
        payload.put("execution_time_ms", executionTime);
        payload.put("uptime_seconds", uptimeSeconds);
        return payload;
    }

    private MarketingContext.MarketingContextBuilder baseBuilder(String requestId,
                                                                 String product,
                                                                 String audience,
                                                                 String brandVoice,
                                                                 String goals,
                                                                 String language) {
        return MarketingContext.builder()
                .requestId(requestId)
                .product(product)
                .audience(audience)
                .brandVoice(brandVoice)
                .goals(goals)
                .language(normalizeLanguage(language));
    }

    private String normalizeLanguage(String language) {
        if (!StringUtils.hasText(language)) {
            return "en-US";
        }
        String normalized = language.trim().toLowerCase(Locale.ROOT);
        return switch (normalized) {
            case "pt-br" -> "pt-BR";
            case "en-us" -> "en-US";
            case "es-es" -> "es-ES";
            default -> language.trim();
        };
    }

    private <T> ResponseEntity<StandardResponse<Map<String, Object>>> processRequest(HttpServletRequest servletRequest,
                                                                                     String artifactType,
                                                                                     Function<String, T> handler) {
        String requestId = resolveRequestId(servletRequest);
        long startTime = getOrInitStartTime(servletRequest);

        T result = handler.apply(requestId);
        long executionTime = System.currentTimeMillis() - startTime;

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("artifact_type", artifactType);
        data.put("execution_time_ms", executionTime);
        data.put("result", result);

        StandardResponse<Map<String, Object>> response = StandardResponse.success(requestId, data);
        String outputPath = storagePort.saveJson(artifactType, requestId, response);
        if (StringUtils.hasText(outputPath)) {
            data.put("output_path", outputPath);
        }

        logger.info("request processed artifact={} status=success execution_time_ms={} output_path={}", artifactType, executionTime, StringUtils.hasText(outputPath) ? outputPath : "n/a");

        return ResponseEntity.ok(response);
    }

    private String resolveRequestId(HttpServletRequest request) {
        Object existing = request.getAttribute(RequestContextAttributes.REQUEST_ID);
        if (existing instanceof String existingId && StringUtils.hasText(existingId)) {
            return existingId;
        }
        String headerId = requestIdResolver.resolve(request);
        request.setAttribute(RequestContextAttributes.REQUEST_ID, headerId);
        return headerId;
    }

    private long getOrInitStartTime(HttpServletRequest request) {
        Object value = request.getAttribute(RequestContextAttributes.START_TIME);
        if (value instanceof Long start) {
            return start;
        }
        long now = System.currentTimeMillis();
        request.setAttribute(RequestContextAttributes.START_TIME, now);
        return now;
    }

    private long calculateExecutionTime(HttpServletRequest request) {
        long start = getOrInitStartTime(request);
        return Math.max(0, System.currentTimeMillis() - start);
    }
}
