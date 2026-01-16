package com.mcp.marketing.handler;

import com.mcp.marketing.model.AudienceResource;
import com.mcp.marketing.model.BrandResource;
import com.mcp.marketing.model.CompetitorResource;
import com.mcp.marketing.model.ProductResource;
import com.mcp.marketing.resource.McpResourceProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * MCP Resource Handler
 * Exposes marketing resources (product, audience, brand, competitors) via MCP protocol
 */
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "mcp.sdk.resources", name = "enabled", havingValue = "true", matchIfMissing = true)
public class McpResourceHandler {

    private final McpResourceProvider resourceProvider;

    /**
     * Get product resource by name
     */
    public Map<String, Object> getProductResource(String productName) {
        log.debug("MCP: Fetching product resource: {}", productName);
        ProductResource product = resourceProvider.getProduct(productName);
        return convertProductToMap(product);
    }

    /**
     * Get audience resource by segment
     */
    public Map<String, Object> getAudienceResource(String audienceSegment) {
        log.debug("MCP: Fetching audience resource: {}", audienceSegment);
        AudienceResource audience = resourceProvider.getAudience(audienceSegment);
        return convertAudienceToMap(audience);
    }

    /**
     * Get brand resource by voice
     */
    public Map<String, Object> getBrandResource(String brandVoice) {
        log.debug("MCP: Fetching brand resource: {}", brandVoice);
        BrandResource brand = resourceProvider.getBrand(brandVoice);
        return convertBrandToMap(brand);
    }

    /**
     * Get competitor resource by context
     */
    public Map<String, Object> getCompetitorResource(String context) {
        log.debug("MCP: Fetching competitor resource: {}", context);
        CompetitorResource competitors = resourceProvider.getCompetitors(context);
        return convertCompetitorToMap(competitors);
    }

    /**
     * List all available resources
     */
    public Map<String, String> listResources() {
        Map<String, String> resources = new HashMap<>();
        resources.put("product", "Product information and specifications");
        resources.put("audience", "Target audience demographics and behaviors");
        resources.put("brand", "Brand voice, tone, and messaging guidelines");
        resources.put("competitors", "Competitive landscape and positioning");
        return resources;
    }

    // Conversion methods
    private Map<String, Object> convertProductToMap(ProductResource product) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", product.getName());
        map.put("description", product.getDescription());
        map.put("category", product.getCategory());
        map.put("valueProposition", product.getValueProposition());
        map.put("features", product.getFeatures());
        map.put("pricing", product.getPricing());
        return map;
    }

    private Map<String, Object> convertAudienceToMap(AudienceResource audience) {
        Map<String, Object> map = new HashMap<>();
        map.put("segment", audience.getSegment());
        map.put("demographics", audience.getDemographics());
        map.put("psychographics", audience.getPsychographics());
        map.put("painPoints", audience.getPainPoints());
        map.put("behaviors", audience.getBehaviors());
        map.put("channels", audience.getChannels());
        return map;
    }

    private Map<String, Object> convertBrandToMap(BrandResource brand) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", brand.getName());
        map.put("voice", brand.getVoice());
        map.put("tone", brand.getTone());
        map.put("personality", brand.getPersonality());
        map.put("values", brand.getValues());
        map.put("guidelines", brand.getGuidelines());
        return map;
    }

    private Map<String, Object> convertCompetitorToMap(CompetitorResource competitors) {
        Map<String, Object> map = new HashMap<>();
        map.put("competitors", competitors.getCompetitors());
        return map;
    }
}
