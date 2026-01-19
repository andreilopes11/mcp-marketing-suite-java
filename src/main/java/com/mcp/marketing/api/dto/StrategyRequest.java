package com.mcp.marketing.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * Request DTO for Marketing Strategy generation
 * <p>
 * Generates comprehensive marketing strategy including positioning, channels, and tactics
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class StrategyRequest extends BaseRequest {

    private String marketSegment;
    private String competitorAnalysis;
    private List<String> channels;
    private String timeframe; // e.g., "Q1 2026", "6 months"

}
