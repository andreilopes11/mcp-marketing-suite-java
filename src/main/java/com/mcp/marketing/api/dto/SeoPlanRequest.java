package com.mcp.marketing.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Request DTO for SEO Plan generation
 * <p>
 * Generates comprehensive SEO strategy including keywords, content plan, and optimization
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeoPlanRequest {

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

    private List<String> keywords;
    private String domain;
    private Integer monthlyBudget;
}
