package com.mcp.marketing.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Request payload for marketing operations
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarketingRequest {

    @NotBlank(message = "Product description is required")
    private String product;

    @NotBlank(message = "Audience description is required")
    private String audience;

    @JsonProperty("brand_voice")
    @NotBlank(message = "Brand voice is required")
    private String brandVoice;

    @NotEmpty(message = "At least one goal is required")
    private List<String> goals;

    private String language; // Optional: en, pt, es

    private String competitors; // Optional

    @JsonProperty("budget_range")
    private String budgetRange; // Optional

    @JsonProperty("campaign_duration")
    private String campaignDuration; // Optional
}

