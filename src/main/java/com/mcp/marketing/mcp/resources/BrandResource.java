package com.mcp.marketing.mcp.resources;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * MCP Resource: Brand Information
 * <p>
 * Provides mock brand voice and identity information
 */
public class BrandResource {

    private static final Map<String, Map<String, Object>> MOCK_BRANDS = new LinkedHashMap<>();

    static {
        // Brand 1: Professional & Trustworthy
        Map<String, Object> professional = new LinkedHashMap<>();
        professional.put("id", "brand-001");
        professional.put("name", "Professional & Trustworthy");
        professional.put("tone", "Professional, authoritative, yet approachable");
        professional.put("voice", List.of(
                "Clear and direct",
                "Fact-based",
                "Solution-oriented",
                "Respectful"
        ));
        professional.put("doNots", List.of(
                "Avoid jargon",
                "Don't be overly casual",
                "No emojis in professional contexts"
        ));
        professional.put("examples", List.of(
                "We help businesses grow through proven strategies.",
                "Our platform delivers measurable results."
        ));
        MOCK_BRANDS.put("brand-001", professional);

        // Brand 2: Innovative & Tech-Savvy
        Map<String, Object> innovative = new LinkedHashMap<>();
        innovative.put("id", "brand-002");
        innovative.put("name", "Innovative & Tech-Savvy");
        innovative.put("tone", "Modern, cutting-edge, enthusiastic");
        innovative.put("voice", List.of(
                "Forward-thinking",
                "Tech-focused",
                "Energetic",
                "Data-driven"
        ));
        innovative.put("doNots", List.of(
                "Don't sound stuffy",
                "Avoid outdated references",
                "Don't oversimplify technical concepts"
        ));
        innovative.put("examples", List.of(
                "Transform your workflow with AI-powered automation.",
                "Built for the future of work."
        ));
        MOCK_BRANDS.put("brand-002", innovative);

        // Brand 3: Friendly & Approachable
        Map<String, Object> friendly = new LinkedHashMap<>();
        friendly.put("id", "brand-003");
        friendly.put("name", "Friendly & Approachable");
        friendly.put("tone", "Warm, conversational, supportive");
        friendly.put("voice", List.of(
                "Conversational",
                "Empathetic",
                "Encouraging",
                "Human-centered"
        ));
        friendly.put("doNots", List.of(
                "Don't be too formal",
                "Avoid corporate speak",
                "Don't sound robotic"
        ));
        friendly.put("examples", List.of(
                "We're here to help you succeed, every step of the way.",
                "Let's make marketing simple together."
        ));
        MOCK_BRANDS.put("brand-003", friendly);
    }

    public Map<String, Object> read(String uri) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("uri", uri);
        response.put("mimeType", "application/json");

        String brandId = extractBrandId(uri);

        if (brandId == null || brandId.equals("list")) {
            response.put("content", Map.of(
                    "brands", MOCK_BRANDS.values(),
                    "count", MOCK_BRANDS.size()
            ));
        } else if (MOCK_BRANDS.containsKey(brandId)) {
            response.put("content", MOCK_BRANDS.get(brandId));
        } else {
            response.put("error", "Brand not found: " + brandId);
            response.put("availableBrands", MOCK_BRANDS.keySet());
        }

        return response;
    }

    private String extractBrandId(String uri) {
        if (uri == null) {
            return "list";
        }
        String[] parts = uri.split("/");
        if (parts.length > 0) {
            return parts[parts.length - 1];
        }
        return "list";
    }
}
