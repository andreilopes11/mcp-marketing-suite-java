package com.mcp.marketing.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * Result model for CRM Sequences generation
 * <p>
 * Contains email/communication sequences for lead nurturing and conversion
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrmSequencesResult {

    /**
     * Sequence name/identifier
     */
    private String sequenceName;

    /**
     * Email sequence details
     */
    private List<EmailStep> emails;

    /**
     * Timing and cadence
     */
    private TimingStrategy timing;

    /**
     * Success metrics and KPIs
     */
    private SuccessMetrics successMetrics;

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
    public static class EmailStep {
        private Integer dayNumber;
        private String subject;
        private String previewText;
        private String body;
        private String callToAction;
        private String goal;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TimingStrategy {
        private Integer totalDays;
        private Integer numberOfTouchpoints;
        private String cadence;
        private List<String> bestSendTimes;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SuccessMetrics {
        private String openRateTarget;
        private String clickRateTarget;
        private String conversionRateTarget;
        private List<String> trackingPoints;
    }
}
