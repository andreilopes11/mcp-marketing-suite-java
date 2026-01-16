package com.mcp.marketing.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * Result model for Ads generation
 * <p>
 * Contains ads for multiple platforms with QA scoring and recommendations
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdsResult {

    /**
     * Google Ads content
     */
    private GoogleAd googleAds;

    /**
     * Meta (Facebook/Instagram) Ads content
     */
    private MetaAd metaAds;

    /**
     * LinkedIn Ads content
     */
    private LinkedInAd linkedinAds;

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
    public static class GoogleAd {
        private String headline1;
        private String headline2;
        private String headline3;
        private String description1;
        private String description2;
        private String displayUrl;
        private List<String> keywords;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MetaAd {
        private String primaryText;
        private String headline;
        private String description;
        private String callToAction;
        private List<String> targeting;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LinkedInAd {
        private String introText;
        private String headline;
        private String description;
        private String callToAction;
        private List<String> targetAudience;
    }
}
