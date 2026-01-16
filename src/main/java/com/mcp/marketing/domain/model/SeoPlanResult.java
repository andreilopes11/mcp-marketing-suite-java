package com.mcp.marketing.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * Result model for SEO Plan generation
 * <p>
 * Contains comprehensive SEO strategy including keywords, content plan, and optimization
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeoPlanResult {

    /**
     * Primary keywords
     */
    private List<String> primaryKeywords;

    /**
     * Secondary/long-tail keywords
     */
    private List<String> secondaryKeywords;

    /**
     * Content strategy
     */
    private ContentStrategy contentStrategy;

    /**
     * On-page optimization recommendations
     */
    private OnPageOptimization onPageOptimization;

    /**
     * Off-page optimization recommendations
     */
    private OffPageOptimization offPageOptimization;

    /**
     * Technical SEO recommendations
     */
    private List<String> technicalSeo;

    /**
     * Quality assurance score (0-100)
     */
    private Integer qaScore;

    /**
     * Recommendations for improvement
     */
    private List<String> recommendations;

    /**
     * Additional metadata
     */
    private Map<String, Object> metadata;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ContentStrategy {
        private List<String> blogTopics;
        private List<String> pillarPages;
        private String contentCalendar;
        private Integer monthlyArticles;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OnPageOptimization {
        private String titleTemplate;
        private String metaDescriptionTemplate;
        private List<String> headerStructure;
        private List<String> internalLinking;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OffPageOptimization {
        private List<String> backlinkStrategy;
        private List<String> socialMediaPlan;
        private List<String> prOpportunities;
    }
}
