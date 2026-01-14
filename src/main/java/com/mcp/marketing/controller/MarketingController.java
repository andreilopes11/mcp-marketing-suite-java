package com.mcp.marketing.controller;

import com.mcp.marketing.model.MarketingRequest;
import com.mcp.marketing.model.MarketingResponse;
import com.mcp.marketing.service.MarketingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST API endpoints for marketing operations
 */
@Slf4j
@RestController
@RequestMapping("/api/marketing")
@RequiredArgsConstructor
@Tag(name = "Marketing", description = "Marketing content generation endpoints")
public class MarketingController {

    private final MarketingService marketingService;

    @PostMapping("/ads")
    @Operation(summary = "Generate ad creatives",
               description = "Generate ads for Google, Meta, and LinkedIn platforms")
    public ResponseEntity<MarketingResponse> generateAds(
        @Valid @RequestBody MarketingRequest request
    ) {
        log.info("Received ad generation request");
        MarketingResponse response = marketingService.generateAds(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/crm-sequences")
    @Operation(summary = "Generate CRM email sequences",
               description = "Generate automated email nurture sequences")
    public ResponseEntity<MarketingResponse> generateCrmSequences(
        @Valid @RequestBody MarketingRequest request
    ) {
        log.info("Received CRM sequence generation request");
        MarketingResponse response = marketingService.generateCrmSequence(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/seo-plan")
    @Operation(summary = "Generate SEO strategy",
               description = "Generate comprehensive SEO plan with keywords and tactics")
    public ResponseEntity<MarketingResponse> generateSeoStrategy(
        @Valid @RequestBody MarketingRequest request
    ) {
        log.info("Received SEO strategy generation request");
        MarketingResponse response = marketingService.generateSeoStrategy(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/strategy")
    @Operation(summary = "Generate full GTM strategy",
               description = "Generate complete go-to-market strategy including ads, CRM, and SEO")
    public ResponseEntity<MarketingResponse> generateFullStrategy(
        @Valid @RequestBody MarketingRequest request
    ) {
        log.info("Received full strategy generation request");
        MarketingResponse response = marketingService.generateFullStrategy(request);
        return ResponseEntity.ok(response);
    }
}

