package com.mcp.marketing.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Request DTO for Marketing Strategy generation
 * <p>
 * Generates comprehensive marketing strategy including positioning, channels, and tactics
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StrategyRequest {

    @NotBlank(message = "product is required")
    private String product;

    @NotBlank(message = "audience is required")
    private String audience;

    @NotBlank(message = "brand_voice is required")
    private String brandVoice;

    @NotBlank(message = "goals is required")
    private String goals;

    @NotBlank(message = "language is required")
    @Pattern(regexp = "pt-BR|en", message = "language must be 'pt-BR' or 'en'")
    private String language;

    private String marketSegment;
    private String competitorAnalysis;
    private List<String> channels;
    private String timeframe; // e.g., "Q1 2026", "6 months"
}
