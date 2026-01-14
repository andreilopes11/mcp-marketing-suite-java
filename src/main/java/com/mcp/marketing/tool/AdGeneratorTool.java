package com.mcp.marketing.tool;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcp.marketing.config.MarketingProperties;
import com.mcp.marketing.observability.ObservabilityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Tool for generating advertising creative and copy
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AdGeneratorTool {

    private final MarketingProperties properties;
    private final ObservabilityService observability;
    private final ObjectMapper objectMapper;

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

        List<Map<String, String>> descriptions = Arrays.asList(
                Map.of("text", "Achieve " + String.join(", ", goals) + " with our proven solution.", "length", "90"),
                Map.of("text", "Join thousands of satisfied customers. Start your free trial today.", "length", "90")
        );

        googleAds.put("headlines", headlines);
        googleAds.put("descriptions", descriptions);
        googleAds.put("final_url", "https://example.com/landing-page");
        googleAds.put("display_path", "example.com/product");

        return googleAds;
    }

    private Map<String, Object> generateMetaAds(
            String product,
            String audience,
            String brandVoice,
            List<String> goals) {

        Map<String, Object> metaAds = new HashMap<>();

        metaAds.put("primary_text", String.format(
                "Attention %s! 🚀 Discover how %s can help you achieve %s. " +
                        "Join the revolution today!",
                audience, product, goals.get(0)
        ));

        metaAds.put("headline", product + " - Your Success Partner");
        metaAds.put("description", "Transform your results. Start free trial now.");
        metaAds.put("call_to_action", "Learn More");
        metaAds.put("link", "https://example.com/meta-landing");

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
        linkedInAds.put("description", "Trusted by industry leaders. Get started today.");
        linkedInAds.put("call_to_action", "Request Demo");

        return linkedInAds;
    }

    private Map<String, Object> calculateQAScore(Map<String, Object> ads) {
        Map<String, Object> qaScore = new HashMap<>();
        qaScore.put("overall_score", 85);
        qaScore.put("clarity", 90);
        qaScore.put("relevance", 85);
        qaScore.put("brand_alignment", 80);
        qaScore.put("call_to_action_strength", 85);
        return qaScore;
    }

    private List<String> generateRecommendations(Map<String, Object> ads) {
        return Arrays.asList(
                "Test multiple headline variations to optimize CTR",
                "Consider adding emotional triggers to increase engagement",
                "A/B test different CTAs to improve conversion rates",
                "Ensure landing page messaging aligns with ad copy"
        );
    }

    private String saveToFile(Map<String, Object> data, String type) {
        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String requestId = observability.getCurrentRequestId();
            String filename = String.format("%s_%s_%s.json", type, requestId, timestamp);

            Path outputDir = Paths.get(properties.getOutputDirectory());
            Files.createDirectories(outputDir);

            Path filePath = outputDir.resolve(filename);
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(filePath.toFile(), data);

            log.info("Saved {} output to: {}", type, filePath);
            return filePath.toString();
        } catch (IOException e) {
            log.error("Failed to save output file", e);
            return null;
        }
    }
}

