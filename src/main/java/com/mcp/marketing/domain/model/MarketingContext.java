package com.mcp.marketing.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Domain model representing the marketing context for content generation
 * <p>
 * Contains all information needed to generate marketing content across
 * different channels and formats (ads, SEO, CRM, strategy)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarketingContext {

    /**
     * Unique request identifier for tracking
     */
    private String requestId;

    /**
     * Product or service being marketed
     */
    private String product;

    /**
     * Target audience description
     */
    private String audience;

    /**
     * Brand voice and tone
     */
    private String brandVoice;

    /**
     * Marketing goals and objectives
     */
    private String goals;

    /**
     * Content language (pt-BR, en)
     */
    private String language;

    /**
     * Execution mode (DETERMINISTIC or AI_ENABLED)
     */
    @Builder.Default
    private ExecutionMode executionMode = ExecutionMode.DETERMINISTIC;

    // Additional context fields (optional)

    /**
     * Target platforms for ads (google, meta, linkedin)
     */
    private List<String> platforms;

    /**
     * Budget information
     */
    private String budget;

    /**
     * Campaign duration
     */
    private String duration;

    /**
     * SEO keywords
     */
    private List<String> keywords;

    /**
     * Domain for SEO
     */
    private String domain;

    /**
     * Monthly budget for SEO initiatives
     */
    private Integer monthlyBudget;

    /**
     * CRM sequence length
     */
    private Integer sequenceLength;

    /**
     * CRM conversion goal
     */
    private String conversionGoal;

    /**
     * Communication channels (email, sms, whatsapp)
     */
    private List<String> channels;

    /**
     * Market segment
     */
    private String marketSegment;

    /**
     * Competitor analysis
     */
    private String competitorAnalysis;

    /**
     * Campaign timeframe
     */
    private String timeframe;
}
