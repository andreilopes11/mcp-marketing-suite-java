package com.mcp.marketing.mcp.resources;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * MCP Resource: Audience Information
 * <p>
 * Provides mock audience/persona information
 */
public class AudienceResource {

    private static final Map<String, Map<String, Object>> MOCK_AUDIENCES = new LinkedHashMap<>();

    static {
        // Audience 1: Small Business Owners
        Map<String, Object> smallBiz = new LinkedHashMap<>();
        smallBiz.put("id", "aud-001");
        smallBiz.put("name", "Small Business Owners");
        smallBiz.put("size", "5-50 employees");
        smallBiz.put("demographics", Map.of(
                "ageRange", "30-55",
                "location", "Urban/Suburban",
                "income", "$50k-$150k annual revenue"
        ));
        smallBiz.put("painPoints", List.of(
                "Limited resources",
                "Need to automate processes",
                "Budget constraints",
                "Time management"
        ));
        smallBiz.put("goals", List.of(
                "Increase efficiency",
                "Scale operations",
                "Improve customer relationships"
        ));
        smallBiz.put("channels", List.of("LinkedIn", "Google Search", "Industry Forums"));
        MOCK_AUDIENCES.put("aud-001", smallBiz);

        // Audience 2: Marketing Managers
        Map<String, Object> marketers = new LinkedHashMap<>();
        marketers.put("id", "aud-002");
        marketers.put("name", "Marketing Managers");
        marketers.put("size", "Mid to Large Companies");
        marketers.put("demographics", Map.of(
                "ageRange", "28-45",
                "location", "Global",
                "role", "Marketing/Growth"
        ));
        marketers.put("painPoints", List.of(
                "Proving ROI",
                "Managing multiple campaigns",
                "Data-driven decisions",
                "Team collaboration"
        ));
        marketers.put("goals", List.of(
                "Increase conversions",
                "Optimize campaigns",
                "Measure performance"
        ));
        marketers.put("channels", List.of("LinkedIn", "Twitter", "Marketing Blogs", "Webinars"));
        MOCK_AUDIENCES.put("aud-002", marketers);

        // Audience 3: E-commerce Entrepreneurs
        Map<String, Object> ecom = new LinkedHashMap<>();
        ecom.put("id", "aud-003");
        ecom.put("name", "E-commerce Entrepreneurs");
        ecom.put("size", "Solo to Small Teams");
        ecom.put("demographics", Map.of(
                "ageRange", "25-40",
                "location", "Global",
                "experience", "1-5 years in e-commerce"
        ));
        ecom.put("painPoints", List.of(
                "Managing inventory",
                "Payment processing",
                "Shipping logistics",
                "Customer acquisition cost"
        ));
        ecom.put("goals", List.of(
                "Increase sales",
                "Reduce operational complexity",
                "Expand to new markets"
        ));
        ecom.put("channels", List.of("Instagram", "Facebook", "E-commerce Forums", "YouTube"));
        MOCK_AUDIENCES.put("aud-003", ecom);
    }

    public Map<String, Object> read(String uri) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("uri", uri);
        response.put("mimeType", "application/json");

        String audienceId = extractAudienceId(uri);

        if (audienceId == null || audienceId.equals("list")) {
            response.put("content", Map.of(
                    "audiences", MOCK_AUDIENCES.values(),
                    "count", MOCK_AUDIENCES.size()
            ));
        } else if (MOCK_AUDIENCES.containsKey(audienceId)) {
            response.put("content", MOCK_AUDIENCES.get(audienceId));
        } else {
            response.put("error", "Audience not found: " + audienceId);
            response.put("availableAudiences", MOCK_AUDIENCES.keySet());
        }

        return response;
    }

    private String extractAudienceId(String uri) {
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
