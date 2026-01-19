package com.mcp.marketing.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Result model for comprehensive Marketing Strategy
 * <p>
 * Aggregates ads, SEO, and CRM strategies into a unified marketing plan
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StrategyResult {

    /**
     * Executive summary
     */
    private String executiveSummary;

    /**
     * Ads strategy
     */
    private AdsResult adsStrategy;

    /**
     * SEO strategy
     */
    private SeoPlanResult seoStrategy;

    /**
     * CRM strategy
     */
    private CrmSequencesResult crmStrategy;

    /**
     * Overall quality assurance score (0-100)
     */
    private Integer qaScore;

    /**
     * Integrated recommendations
     */
    private java.util.List<String> recommendations;

    /**
     * Budget allocation recommendations
     */
    private Map<String, String> budgetAllocation;

    /**
     * Timeline and milestones
     */
    private Map<String, String> timeline;

    /**
     * KPIs and success metrics
     */
    private Map<String, String> kpis;

    /**
     * Additional metadata
     */
    private Map<String, Object> metadata;
}
