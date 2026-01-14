package com.mcp.marketing.tool;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcp.marketing.config.MarketingProperties;
import com.mcp.marketing.observability.ObservabilityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Tool for generating advertising creative and copy
 * Optimized: extends BaseMarketingTool to eliminate code duplication
 */
@Slf4j
@Component
public class AdGeneratorTool extends BaseMarketingTool {

    public AdGeneratorTool(
            MarketingProperties properties,
            ObservabilityService observability,
            ObjectMapper objectMapper) {
        super(properties, observability, objectMapper);
    }

    public Map<String, Object> generateAds(
            String product,
            String audience,
            String brandVoice,
            List<String> goals) {

        return observability.traceOperation("generate_ads", () -> {
            Map<String, Object> result = new HashMap<>();

            // Generate ads for different platforms
            result.put("google_ads", generateGoogleAds(product, audience, brandVoice, goals));
            result.put("meta_ads", generateMetaAds(product, audience, brandVoice, goals));
            result.put("linkedin_ads", generateLinkedInAds(product, audience, brandVoice, goals));
            result.put("qa_score", calculateQAScore(result));
            result.put("recommendations", generateRecommendations(result));

            // Save to file
            String filePath = saveToFile(result, "ads");
            result.put("output_file", filePath);

            observability.logMetric("ads_generated", 3);
            return result;
        });
    }

    private Map<String, Object> generateGoogleAds(
            String product,
            String audience,
            String brandVoice,
            List<String> goals) {

        Map<String, Object> googleAds = new HashMap<>();

        // Responsive Search Ads
        List<Map<String, String>> headlines = Arrays.asList(
                Map.of("text", product + " - Transform Your Business", "length", "30"),
                Map.of("text", "Get Started with " + product, "length", "25"),
                Map.of("text", "Trusted by " + audience, "length", "20")
        );

        String descriptionText = "Achieve " + String.join(", ", goals) + " with our proven solution.";
        List<Map<String, String>> descriptions = Arrays.asList(
                Map.of("text", descriptionText, "length", "90"),
                Map.of("text", "Join thousands of satisfied customers. Start your free trial today.", "length", "90")
        );

        googleAds.put("headlines", headlines);
        googleAds.put("descriptions", descriptions);
        googleAds.put("final_url", "https://example.com/landing-page");
        googleAds.put("display_path", "example.com/product");
        googleAds.put("brand_voice", brandVoice);

        return googleAds;
    }

    private Map<String, Object> generateMetaAds(
            String product,
            String audience,
            String brandVoice,
            List<String> goals) {

        Map<String, Object> metaAds = new HashMap<>();

        // Adapt tone based on brand voice
        String tone = brandVoice != null && brandVoice.toLowerCase().contains("professional") ?
            "Discover" : "🚀 Discover";

        metaAds.put("primary_text", String.format(
                "Attention %s! %s how %s can help you achieve %s. " +
                        "Join the revolution today!",
                audience, tone, product, goals.get(0)
        ));

        metaAds.put("headline", product + " - Your Success Partner");
        metaAds.put("description", "Transform your results. Start free trial now.");
        metaAds.put("call_to_action", "Learn More");
        metaAds.put("link", "https://example.com/meta-landing");
        metaAds.put("brand_voice", brandVoice);

        return metaAds;
    }

    private Map<String, Object> generateLinkedInAds(
            String product,
            String audience,
            String brandVoice,
            List<String> goals) {

        Map<String, Object> linkedInAds = new HashMap<>();

        linkedInAds.put("introductory_text", String.format(
                "%s professionals are using %s to drive results. " +
                        "Discover how our solution can help you %s.",
                audience, product, String.join(" and ", goals).toLowerCase()
        ));

        linkedInAds.put("headline", "Enterprise Solution for " + audience);
        linkedInAds.put("description", String.format("Trusted by industry leaders. Get started today. %s approach.",
                brandVoice != null ? brandVoice : "Professional"));
        linkedInAds.put("call_to_action", "Request Demo");
        linkedInAds.put("brand_voice", brandVoice);

        return linkedInAds;
    }

    private Map<String, Object> calculateQAScore(Map<String, Object> ads) {
        Map<String, Object> qaScore = new HashMap<>();

        // Analyze actual ad content for scoring
        int clarity = analyzeClarity(ads);
        int relevance = analyzeRelevance(ads);
        int brandAlignment = analyzeBrandAlignment(ads);
        int ctaStrength = analyzeCtaStrength(ads);

        int overallScore = (clarity + relevance + brandAlignment + ctaStrength) / 4;

        qaScore.put("overall_score", overallScore);
        qaScore.put("clarity", clarity);
        qaScore.put("relevance", relevance);
        qaScore.put("brand_alignment", brandAlignment);
        qaScore.put("call_to_action_strength", ctaStrength);

        return qaScore;
    }

    @SuppressWarnings("unchecked")
    private int analyzeClarity(Map<String, Object> ads) {
        // Check if all platform ads have required fields
        int score = 70;
        if (ads.containsKey("google_ads") && ((Map<String, Object>) ads.get("google_ads")).containsKey("headlines")) {
            score += 10;
        }
        if (ads.containsKey("meta_ads") && ((Map<String, Object>) ads.get("meta_ads")).containsKey("primary_text")) {
            score += 10;
        }
        if (ads.containsKey("linkedin_ads") && ((Map<String, Object>) ads.get("linkedin_ads")).containsKey("introductory_text")) {
            score += 10;
        }
        return Math.min(score, 100);
    }

    @SuppressWarnings("unchecked")
    private int analyzeRelevance(Map<String, Object> ads) {
        // Check if ads have brand voice applied
        int score = 60;
        for (String key : Arrays.asList("google_ads", "meta_ads", "linkedin_ads")) {
            if (ads.containsKey(key)) {
                Map<String, Object> platformAds = (Map<String, Object>) ads.get(key);
                if (platformAds.containsKey("brand_voice") && platformAds.get("brand_voice") != null) {
                    score += 10;
                }
            }
        }
        return Math.min(score, 100);
    }

    @SuppressWarnings("unchecked")
    private int analyzeBrandAlignment(Map<String, Object> ads) {
        // Check consistency of brand voice across platforms
        int score = 70;
        String brandVoice = null;
        boolean consistent = true;

        for (String key : Arrays.asList("google_ads", "meta_ads", "linkedin_ads")) {
            if (ads.containsKey(key)) {
                Map<String, Object> platformAds = (Map<String, Object>) ads.get(key);
                String voice = (String) platformAds.get("brand_voice");
                if (brandVoice == null) {
                    brandVoice = voice;
                } else if (voice != null && !voice.equals(brandVoice)) {
                    consistent = false;
                }
            }
        }

        if (consistent && brandVoice != null) {
            score += 15;
        }
        return Math.min(score, 100);
    }

    @SuppressWarnings("unchecked")
    private int analyzeCtaStrength(Map<String, Object> ads) {
        // Check for presence and quality of CTAs
        int score = 70;

        if (ads.containsKey("meta_ads")) {
            Map<String, Object> metaAds = (Map<String, Object>) ads.get("meta_ads");
            if (metaAds.containsKey("call_to_action") && metaAds.get("call_to_action") != null) {
                score += 10;
            }
        }

        if (ads.containsKey("linkedin_ads")) {
            Map<String, Object> linkedInAds = (Map<String, Object>) ads.get("linkedin_ads");
            if (linkedInAds.containsKey("call_to_action") && linkedInAds.get("call_to_action") != null) {
                score += 10;
            }
        }

        return Math.min(score, 100);
    }

    @SuppressWarnings("unchecked")
    private List<String> generateRecommendations(Map<String, Object> ads) {
        List<String> recommendations = new ArrayList<>();

        // Base recommendations
        recommendations.add("Test multiple headline variations to optimize CTR");

        // Add specific recommendations based on QA score
        Map<String, Object> qaScore = calculateQAScore(ads);

        if ((int) qaScore.get("clarity") < 85) {
            recommendations.add("Simplify your message for better clarity and understanding");
        }

        if ((int) qaScore.get("brand_alignment") < 80) {
            recommendations.add("Ensure consistent brand voice across all ad platforms");
        }

        if ((int) qaScore.get("call_to_action_strength") < 85) {
            recommendations.add("Strengthen your CTAs with more action-oriented language");
        }

        // Check if brand voice is applied
        boolean hasBrandVoice = ads.values().stream()
            .filter(v -> v instanceof Map)
            .map(v -> (Map<String, Object>) v)
            .anyMatch(m -> m.containsKey("brand_voice") && m.get("brand_voice") != null);

        if (!hasBrandVoice) {
            recommendations.add("Apply consistent brand voice to strengthen brand identity");
        }

        // General best practices
        recommendations.add("A/B test different variations to improve conversion rates");
        recommendations.add("Ensure landing page messaging aligns with ad copy");

        return recommendations;
    }
}

