package com.mcp.marketing.controller;

import com.mcp.marketing.model.MarketingRequest;
import com.mcp.marketing.model.MarketingResponse;
import com.mcp.marketing.service.MarketingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

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
    @Operation(summary = "Generate ad creatives", description = "Generate ads for Google, Meta, and LinkedIn platforms")
    public ResponseEntity<MarketingResponse> generateAds(@Valid @RequestBody MarketingRequest request) {
        log.info("Received ad generation request");
        try {
            MarketingResponse response = marketingService.generateAds(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error("Invalid request: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(MarketingResponse.builder()
                            .status("error")
                            .message("Invalid request: " + e.getMessage())
                            .timestamp(LocalDateTime.now())
                            .build());
        } catch (Exception e) {
            log.error("Error generating ads", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(MarketingResponse.builder()
                            .status("error")
                            .message("Internal server error: " + e.getMessage())
                            .timestamp(LocalDateTime.now())
                            .build());
        }
    }

    @PostMapping("/crm-sequences")
    @Operation(summary = "Generate CRM email sequences", description = "Generate automated email nurture sequences")
    public ResponseEntity<MarketingResponse> generateCrmSequences(@Valid @RequestBody MarketingRequest request) {
        log.info("Received CRM sequence generation request");
        try {
            MarketingResponse response = marketingService.generateCrmSequence(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error("Invalid request: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(MarketingResponse.builder()
                            .status("error")
                            .message("Invalid request: " + e.getMessage())
                            .timestamp(LocalDateTime.now())
                            .build());
        } catch (Exception e) {
            log.error("Error generating CRM sequences", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(MarketingResponse.builder()
                            .status("error")
                            .message("Internal server error: " + e.getMessage())
                            .timestamp(LocalDateTime.now())
                            .build());
        }
    }

    @PostMapping("/seo-plan")
    @Operation(summary = "Generate SEO strategy", description = "Generate comprehensive SEO plan with keywords and tactics")
    public ResponseEntity<MarketingResponse> generateSeoStrategy(@Valid @RequestBody MarketingRequest request) {
        log.info("Received SEO strategy generation request");
        try {
            MarketingResponse response = marketingService.generateSeoStrategy(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error("Invalid request: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(MarketingResponse.builder()
                            .status("error")
                            .message("Invalid request: " + e.getMessage())
                            .timestamp(LocalDateTime.now())
                            .build());
        } catch (Exception e) {
            log.error("Error generating SEO strategy", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(MarketingResponse.builder()
                            .status("error")
                            .message("Internal server error: " + e.getMessage())
                            .timestamp(LocalDateTime.now())
                            .build());
        }
    }

    @PostMapping("/strategy")
    @Operation(summary = "Generate full GTM strategy", description = "Generate complete go-to-market strategy including ads, CRM, and SEO")
    public ResponseEntity<MarketingResponse> generateFullStrategy(@Valid @RequestBody MarketingRequest request) {
        log.info("Received full strategy generation request");
        try {
            MarketingResponse response = marketingService.generateFullStrategy(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error("Invalid request: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(MarketingResponse.builder()
                            .status("error")
                            .message("Invalid request: " + e.getMessage())
                            .timestamp(LocalDateTime.now())
                            .build());
        } catch (Exception e) {
            log.error("Error generating full strategy", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(MarketingResponse.builder()
                            .status("error")
                            .message("Internal server error: " + e.getMessage())
                            .timestamp(LocalDateTime.now())
                            .build());
        }
    }
}
