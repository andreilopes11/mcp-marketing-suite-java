package com.mcp.marketing.tool;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcp.marketing.config.MarketingProperties;
import com.mcp.marketing.observability.ObservabilityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Tool for generating SEO strategies and plans
 * Optimized: extends BaseMarketingTool to eliminate code duplication
 */
@Slf4j
@Component
public class SeoStrategyTool extends BaseMarketingTool {

    public SeoStrategyTool(
            MarketingProperties properties,
            ObservabilityService observability,
            ObjectMapper objectMapper) {
        super(properties, observability, objectMapper);
    }

    public Map<String, Object> generateSeoStrategy(
            String product,
            String audience,
            String brandVoice,
            List<String> goals) {

        return observability.traceOperation("generate_seo_strategy", () -> {
            Map<String, Object> result = new HashMap<>();

            result.put("strategy_overview", generateOverview(product, audience, brandVoice, goals));
            result.put("keyword_strategy", generateKeywordStrategy(product, audience));
            result.put("content_plan", generateContentPlan(product, audience, brandVoice));
            result.put("technical_seo", generateTechnicalSeo());
            result.put("link_building", generateLinkBuilding());
            result.put("performance_tracking", generatePerformanceTracking());
            result.put("brand_voice", brandVoice);

            // Save to file
            String filePath = saveToFile(result, "seo_strategy");
            result.put("output_file", filePath);

            observability.logMetric("seo_strategy_generated", 1);
            return result;
        });
    }

    private Map<String, Object> generateOverview(String product, String audience, String brandVoice, List<String> goals) {
        Map<String, Object> overview = new HashMap<>();
        overview.put("objective", String.format(
                "Increase organic visibility for %s targeting %s to achieve %s",
                product, audience, String.join(", ", goals)
        ));
        overview.put("brand_voice", brandVoice);
        overview.put("content_tone", brandVoice != null ? brandVoice : "Professional and informative");
        overview.put("timeline", "6-12 months");
        overview.put("key_focus_areas", Arrays.asList(
                "Keyword optimization",
                "Content marketing aligned with brand voice",
                "Technical SEO",
                "Link building",
                "User experience"
        ));
        return overview;
    }

    private Map<String, Object> generateKeywordStrategy(String product, String audience) {
        Map<String, Object> keywordStrategy = new HashMap<>();

        List<Map<String, Object>> primaryKeywords = Arrays.asList(
                createKeyword(product.toLowerCase(), 5000, "high", "medium"),
                createKeyword(audience.toLowerCase() + " solutions", 2000, "medium", "high"),
                createKeyword("best " + product.toLowerCase(), 3000, "high", "high")
        );

        List<Map<String, Object>> secondaryKeywords = Arrays.asList(
                createKeyword(product.toLowerCase() + " features", 1000, "medium", "low"),
                createKeyword(product.toLowerCase() + " pricing", 800, "high", "medium"),
                createKeyword(product.toLowerCase() + " reviews", 1200, "medium", "low")
        );

        List<Map<String, Object>> longTailKeywords = Arrays.asList(
                createKeyword("how to use " + product.toLowerCase(), 500, "low", "high"),
                createKeyword("best " + product.toLowerCase() + " for " + audience.toLowerCase(), 300, "low", "very high"),
                createKeyword(product.toLowerCase() + " vs competitors", 400, "medium", "medium")
        );

        keywordStrategy.put("primary_keywords", primaryKeywords);
        keywordStrategy.put("secondary_keywords", secondaryKeywords);
        keywordStrategy.put("long_tail_keywords", longTailKeywords);

        return keywordStrategy;
    }

    private Map<String, Object> createKeyword(String keyword, int volume, String competition, String intent) {
        Map<String, Object> kw = new HashMap<>();
        kw.put("keyword", keyword);
        kw.put("monthly_volume", volume);
        kw.put("competition", competition);
        kw.put("intent", intent);
        return kw;
    }

    private Map<String, Object> generateContentPlan(String product, String audience, String brandVoice) {
        Map<String, Object> contentPlan = new HashMap<>();

        String voiceGuidance = brandVoice != null ? " in a " + brandVoice + " tone" : "";

        List<Map<String, Object>> contentTypes = Arrays.asList(
                createContentType("Blog Posts", "2-3 per week", "Educational, SEO-optimized articles about " + product + voiceGuidance),
                createContentType("Product Pages", "Ongoing optimization", "Keyword-rich descriptions for " + audience + voiceGuidance),
                createContentType("Case Studies", "1 per month", "Success stories with target keywords" + voiceGuidance),
                createContentType("How-to Guides", "2 per month", "Comprehensive tutorials and guides" + voiceGuidance),
                createContentType("Video Content", "1 per week", "Embedded videos with transcripts")
        );

        contentPlan.put("content_types", contentTypes);
        contentPlan.put("brand_voice_guidelines", brandVoice != null ? brandVoice : "Professional and engaging");
        contentPlan.put("content_calendar", "Create 90-day rolling calendar");
        contentPlan.put("optimization_guidelines", Arrays.asList(
                "1,500-2,500 word count for pillar content",
                "Include target keyword in title, H1, and first paragraph",
                "Use semantic keywords throughout",
                "Add internal and external links",
                "Optimize images with alt text",
                "Maintain consistent brand voice: " + (brandVoice != null ? brandVoice : "Professional")
        ));

        return contentPlan;
    }

    private Map<String, Object> createContentType(String type, String frequency, String description) {
        Map<String, Object> content = new HashMap<>();
        content.put("type", type);
        content.put("frequency", frequency);
        content.put("description", description);
        return content;
    }

    private Map<String, Object> generateTechnicalSeo() {
        Map<String, Object> technicalSeo = new HashMap<>();

        technicalSeo.put("site_speed", Arrays.asList(
                "Optimize images (WebP format, lazy loading)",
                "Minimize CSS/JS files",
                "Enable browser caching",
                "Use CDN for static assets",
                "Target < 3 second load time"
        ));

        technicalSeo.put("mobile_optimization", Arrays.asList(
                "Responsive design",
                "Mobile-first indexing ready",
                "Touch-friendly navigation",
                "Fast mobile page speed"
        ));

        technicalSeo.put("site_architecture", Arrays.asList(
                "Clear URL structure",
                "XML sitemap",
                "Robots.txt optimization",
                "Canonical tags",
                "Structured data (Schema.org)"
        ));

        technicalSeo.put("security", Arrays.asList(
                "HTTPS enabled",
                "SSL certificate",
                "Secure payment pages"
        ));

        return technicalSeo;
    }

    private Map<String, Object> generateLinkBuilding() {
        Map<String, Object> linkBuilding = new HashMap<>();

        linkBuilding.put("strategies", Arrays.asList(
                "Guest posting on industry blogs",
                "Digital PR and press releases",
                "Resource page link building",
                "Broken link building",
                "Partnerships and collaborations"
        ));

        linkBuilding.put("targets", Arrays.asList(
                "Domain Authority 40+ sites",
                "Industry-relevant publications",
                "Complementary businesses",
                "Educational institutions (.edu)",
                "Government resources (.gov)"
        ));

        linkBuilding.put("monthly_goals", Map.of(
                "new_backlinks", "10-15 quality links",
                "referring_domains", "8-12 new domains",
                "da_average", "35+"
        ));

        return linkBuilding;
    }

    private Map<String, Object> generatePerformanceTracking() {
        Map<String, Object> tracking = new HashMap<>();

        tracking.put("kpis", Arrays.asList(
                "Organic traffic growth",
                "Keyword rankings (top 10 positions)",
                "Click-through rate (CTR)",
                "Bounce rate",
                "Time on page",
                "Conversion rate from organic",
                "Backlink profile growth"
        ));

        tracking.put("tools", Arrays.asList(
                "Google Analytics 4",
                "Google Search Console",
                "SEMrush or Ahrefs",
                "PageSpeed Insights",
                "Screaming Frog"
        ));

        tracking.put("reporting", Map.of(
                "frequency", "Monthly",
                "includes", Arrays.asList(
                        "Ranking changes",
                        "Traffic analysis",
                        "Conversion metrics",
                        "Competitor analysis",
                        "Action items"
                )
        ));

        return tracking;
    }
}

