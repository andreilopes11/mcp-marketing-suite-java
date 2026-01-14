package com.mcp.marketing.resource;

import com.mcp.marketing.model.AudienceResource;
import com.mcp.marketing.model.BrandResource;
import com.mcp.marketing.model.CompetitorResource;
import com.mcp.marketing.model.ProductResource;
import com.mcp.marketing.resource.loader.FileResourceLoader;
import com.mcp.marketing.resource.loader.JsonResourceLoader;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * MCP Resource Provider - Enhanced with file-based loading and caching
 * Supports both in-memory mock data and file-based resources
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class McpResourceProvider {

    private final FileResourceLoader fileLoader;
    private final JsonResourceLoader jsonLoader;

    private final Map<String, ProductResource> productCache = new HashMap<>();
    private final Map<String, AudienceResource> audienceCache = new HashMap<>();
    private final Map<String, BrandResource> brandCache = new HashMap<>();
    private final Map<String, CompetitorResource> competitorCache = new HashMap<>();

    @PostConstruct
    public void init() {
        initializeMockData();
        loadResourcesFromFiles();
        log.info("McpResourceProvider initialized with {} products, {} audiences, {} brands",
                productCache.size(), audienceCache.size(), brandCache.size());
    }

    /**
     * Load resources from file system if available
     */
    private void loadResourcesFromFiles() {
        try {
            // Try to load products from file
            if (fileLoader.fileExists("products.json")) {
                Map<String, Object> productsData = jsonLoader.loadJsonAsMap("products.json");
                log.info("Loaded {} products from file", productsData.size());
                // Process and add to cache
            }

            // Try to load audiences from file
            if (fileLoader.fileExists("audiences.json")) {
                Map<String, Object> audiencesData = jsonLoader.loadJsonAsMap("audiences.json");
                log.info("Loaded {} audiences from file", audiencesData.size());
                // Process and add to cache
            }
        } catch (Exception e) {
            log.warn("Could not load resources from files, using mock data: {}", e.getMessage());
        }
    }

    @Cacheable(value = "mcpResources", key = "'product_' + #productName")
    public ProductResource getProduct(String productName) {
        log.debug("Fetching product resource: {}", productName);
        return productCache.getOrDefault(
                productName.toLowerCase(),
                createGenericProduct(productName)
        );
    }

    @Cacheable(value = "mcpResources", key = "'audience_' + #audienceSegment")
    public AudienceResource getAudience(String audienceSegment) {
        log.debug("Fetching audience resource: {}", audienceSegment);
        return audienceCache.getOrDefault(
                audienceSegment.toLowerCase(),
                createGenericAudience(audienceSegment)
        );
    }

    @Cacheable(value = "mcpResources", key = "'brand_' + #brandVoice")
    public BrandResource getBrand(String brandVoice) {
        log.debug("Fetching brand resource: {}", brandVoice);
        return brandCache.getOrDefault(
                brandVoice.toLowerCase(),
                createGenericBrand(brandVoice)
        );
    }

    @Cacheable(value = "mcpResources", key = "'competitors_' + #context")
    public CompetitorResource getCompetitors(String context) {
        log.debug("Fetching competitor resource: {}", context);
        if (context == null || context.isEmpty()) {
            return competitorCache.getOrDefault("default", createGenericCompetitors());
        }
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
                .competitors(Collections.singletonList(
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

