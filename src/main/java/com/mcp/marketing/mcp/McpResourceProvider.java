package com.mcp.marketing.mcp;

import com.mcp.marketing.model.AudienceResource;
import com.mcp.marketing.model.BrandResource;
import com.mcp.marketing.model.CompetitorResource;
import com.mcp.marketing.model.ProductResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * MCP Resource Provider - simulates contextual resources
 * In production, these would connect to real data sources
 */
@Slf4j
@Component
public class McpResourceProvider {

    private final Map<String, ProductResource> productCache = new HashMap<>();
    private final Map<String, AudienceResource> audienceCache = new HashMap<>();
    private final Map<String, BrandResource> brandCache = new HashMap<>();
    private final Map<String, CompetitorResource> competitorCache = new HashMap<>();

    public McpResourceProvider() {
        initializeMockData();
    }

    public ProductResource getProduct(String productName) {
        log.debug("Fetching product resource: {}", productName);
        return productCache.getOrDefault(
            productName.toLowerCase(),
            createGenericProduct(productName)
        );
    }

    public AudienceResource getAudience(String audienceSegment) {
        log.debug("Fetching audience resource: {}", audienceSegment);
        return audienceCache.getOrDefault(
            audienceSegment.toLowerCase(),
            createGenericAudience(audienceSegment)
        );
    }

    public BrandResource getBrand(String brandVoice) {
        log.debug("Fetching brand resource: {}", brandVoice);
        return brandCache.getOrDefault(
            brandVoice.toLowerCase(),
            createGenericBrand(brandVoice)
        );
    }

    public CompetitorResource getCompetitors(String context) {
        log.debug("Fetching competitor resource: {}", context);
        return competitorCache.getOrDefault(
            context.toLowerCase(),
            createGenericCompetitors()
        );
    }

    private void initializeMockData() {
        // Mock Product Data
        productCache.put("saas analytics platform", ProductResource.builder()
            .name("SaaS Analytics Platform")
            .description("Enterprise-grade analytics platform for SaaS companies")
            .category("Business Intelligence")
            .valueProposition("Turn data into actionable insights with AI-powered analytics")
            .features("Real-time dashboards, Predictive analytics, Custom reports, API integrations")
            .pricing("Starting at $99/month")
            .build());

        // Mock Audience Data
        audienceCache.put("b2b data scientists", AudienceResource.builder()
            .segment("B2B Data Scientists")
            .demographics("Age 28-45, Master's/PhD degree, $80K-$150K salary")
            .psychographics("Tech-savvy, data-driven decision makers, value efficiency")
            .painPoints("Complex data pipelines, Limited visualization tools, Integration challenges")
            .behaviors("Active on LinkedIn, Read technical blogs, Attend conferences")
            .channels("LinkedIn, Twitter, Dev.to, Medium, Email")
            .build());

        // Mock Brand Data
        brandCache.put("professional, data-driven", BrandResource.builder()
            .name("Professional Data-Driven Brand")
            .voice("Professional, authoritative, educational")
            .tone("Confident, clear, solution-oriented")
            .values("Innovation, Accuracy, Transparency, Excellence")
            .personality("Expert advisor, trusted partner, innovative thinker")
            .guidelines("Use data to support claims, avoid jargon, focus on outcomes")
            .build());

        // Mock Competitor Data
        competitorCache.put("default", CompetitorResource.builder()
            .competitors(Arrays.asList(
                CompetitorResource.Competitor.builder()
                    .name("Tableau")
                    .positioning("Visual analytics leader")
                    .strengths("Powerful visualizations, Large community")
                    .weaknesses("Steep learning curve, Expensive")
                    .marketShare("15%")
                    .build(),
                CompetitorResource.Competitor.builder()
                    .name("Power BI")
                    .positioning("Microsoft ecosystem integration")
                    .strengths("Microsoft integration, Cost-effective")
                    .weaknesses("Limited customization")
                    .marketShare("20%")
                    .build()
            ))
            .build());
    }

    private ProductResource createGenericProduct(String name) {
        return ProductResource.builder()
            .name(name)
            .description("A product in the " + name + " category")
            .category("General")
            .valueProposition("Delivers value to customers")
            .features("Key features and capabilities")
            .pricing("Competitive pricing")
            .build();
    }

    private AudienceResource createGenericAudience(String segment) {
        return AudienceResource.builder()
            .segment(segment)
            .demographics("Target demographic information")
            .psychographics("Attitudes, values, and lifestyle")
            .painPoints("Key challenges and pain points")
            .behaviors("Typical behaviors and preferences")
            .channels("Preferred communication channels")
            .build();
    }

    private BrandResource createGenericBrand(String voice) {
        return BrandResource.builder()
            .name("Brand")
            .voice(voice)
            .tone("Professional and engaging")
            .values("Quality, Innovation, Customer Focus")
            .personality("Trustworthy and reliable")
            .guidelines("Consistent messaging and quality standards")
            .build();
    }

    private CompetitorResource createGenericCompetitors() {
        return CompetitorResource.builder()
            .competitors(Arrays.asList(
                CompetitorResource.Competitor.builder()
                    .name("Competitor A")
                    .positioning("Market leader")
                    .strengths("Brand recognition")
                    .weaknesses("Higher pricing")
                    .marketShare("Unknown")
                    .build()
            ))
            .build();
    }
}

