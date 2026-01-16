package com.mcp.marketing.resource;

import com.mcp.marketing.model.AudienceResource;
import com.mcp.marketing.model.BrandResource;
import com.mcp.marketing.model.CompetitorResource;
import com.mcp.marketing.model.ProductResource;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MCP Resource Provider - Simple in-memory data store
 */
@Slf4j
@Component
public class McpResourceProvider {

    private final Map<String, ProductResource> productCache = new HashMap<>();
    private final Map<String, AudienceResource> audienceCache = new HashMap<>();
    private final Map<String, BrandResource> brandCache = new HashMap<>();
    private final Map<String, CompetitorResource> competitorCache = new HashMap<>();

    @PostConstruct
    public void init() {
        initializeMockData();
        log.info("McpResourceProvider initialized with {} products, {} audiences, {} brands",
                productCache.size(), audienceCache.size(), brandCache.size());
    }

    public ProductResource getProduct(String productName) {
        log.info("Fetching product: {}", productName);
        return productCache.getOrDefault(productName.toLowerCase(), createGenericProduct(productName));
    }

    public AudienceResource getAudience(String audienceSegment) {
        log.info("Fetching audience: {}", audienceSegment);
        return audienceCache.getOrDefault(audienceSegment.toLowerCase(), createGenericAudience(audienceSegment));
    }

    public BrandResource getBrand(String brandVoice) {
        log.info("Fetching brand: {}", brandVoice);
        return brandCache.getOrDefault(brandVoice.toLowerCase(), createGenericBrand(brandVoice));
    }

    public CompetitorResource getCompetitors(String context) {
        log.info("Fetching competitors: {}", context);
        if (context == null || context.isEmpty()) {
            return competitorCache.getOrDefault("default", createGenericCompetitors());
        }
        return competitorCache.getOrDefault(context.toLowerCase(), createGenericCompetitors());
    }

    private void initializeMockData() {
        productCache.put("saas-platform", ProductResource.builder()
                .name("SaaS Platform").description("Enterprise cloud platform").category("Software")
                .valueProposition("Scale your business with AI").features("Analytics, Automation")
                .pricing("$99/month").build());

        audienceCache.put("b2b-tech", AudienceResource.builder()
                .segment("B2B Tech Companies").demographics("Decision makers, 30-50")
                .psychographics("Innovation-focused").painPoints("Scaling challenges")
                .behaviors("Active on LinkedIn").channels("LinkedIn, Email").build());

        brandCache.put("professional", BrandResource.builder()
                .name("Professional Brand").voice("Professional").tone("Authoritative")
                .values("Innovation, Trust").personality("Expert, Reliable")
                .guidelines("Clear communication").build());

        competitorCache.put("default", CompetitorResource.builder()
                .competitors(List.of(CompetitorResource.Competitor.builder()
                        .name("Competitor A").positioning("Market leader")
                        .strengths("Brand recognition").weaknesses("Higher pricing")
                        .marketShare("35%").build())).build());
    }

    private ProductResource createGenericProduct(String name) {
        return ProductResource.builder().name(name).description("Generic product")
                .category("General").valueProposition("Solve problems").features("Features")
                .pricing("Contact us").build();
    }

    private AudienceResource createGenericAudience(String segment) {
        return AudienceResource.builder().segment(segment).demographics("General")
                .psychographics("General").painPoints("Common issues").behaviors("Typical")
                .channels("Multiple").build();
    }

    private BrandResource createGenericBrand(String voice) {
        return BrandResource.builder().name("Generic Brand").voice(voice).tone("Professional")
                .values("Quality").personality("Trustworthy").guidelines("Standard").build();
    }

    private CompetitorResource createGenericCompetitors() {
        return CompetitorResource.builder().competitors(List.of(
                CompetitorResource.Competitor.builder().name("Generic Competitor")
                        .positioning("Market player").strengths("Various").weaknesses("Various")
                        .marketShare("Unknown").build())).build();
    }
}
