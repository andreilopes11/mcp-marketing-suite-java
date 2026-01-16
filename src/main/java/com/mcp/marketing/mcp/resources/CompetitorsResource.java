package com.mcp.marketing.mcp.resources;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * MCP Resource: Competitors Information
 * <p>
 * Provides mock competitor analysis data
 */
public class CompetitorsResource {

    private static final Map<String, Map<String, Object>> MOCK_COMPETITORS = new LinkedHashMap<>();

    static {
        // Competitor 1: Salesforce
        Map<String, Object> salesforce = new LinkedHashMap<>();
        salesforce.put("id", "comp-001");
        salesforce.put("name", "Salesforce");
        salesforce.put("category", "CRM");
        salesforce.put("marketShare", "23%");
        salesforce.put("strengths", List.of(
                "Brand recognition",
                "Comprehensive features",
                "Enterprise-ready",
                "Large ecosystem"
        ));
        salesforce.put("weaknesses", List.of(
                "High cost",
                "Complex setup",
                "Steep learning curve"
        ));
        salesforce.put("pricing", "$25-$300+ per user/month");
        salesforce.put("targetAudience", "Enterprise & Mid-Market");
        MOCK_COMPETITORS.put("comp-001", salesforce);

        // Competitor 2: HubSpot
        Map<String, Object> hubspot = new LinkedHashMap<>();
        hubspot.put("id", "comp-002");
        hubspot.put("name", "HubSpot");
        hubspot.put("category", "Marketing/CRM");
        hubspot.put("marketShare", "15%");
        hubspot.put("strengths", List.of(
                "User-friendly",
                "Free tier available",
                "All-in-one platform",
                "Great content/education"
        ));
        hubspot.put("weaknesses", List.of(
                "Expensive at scale",
                "Limited customization",
                "Can be overwhelming"
        ));
        hubspot.put("pricing", "$45-$3,200+ per month");
        hubspot.put("targetAudience", "SMBs & Mid-Market");
        MOCK_COMPETITORS.put("comp-002", hubspot);

        // Competitor 3: Mailchimp
        Map<String, Object> mailchimp = new LinkedHashMap<>();
        mailchimp.put("id", "comp-003");
        mailchimp.put("name", "Mailchimp");
        mailchimp.put("category", "Email Marketing");
        mailchimp.put("marketShare", "18%");
        mailchimp.put("strengths", List.of(
                "Easy to use",
                "Free plan",
                "Good templates",
                "Wide integrations"
        ));
        mailchimp.put("weaknesses", List.of(
                "Limited automation",
                "Basic CRM features",
                "Email deliverability issues"
        ));
        mailchimp.put("pricing", "$0-$350+ per month");
        mailchimp.put("targetAudience", "Small Businesses & Startups");
        MOCK_COMPETITORS.put("comp-003", mailchimp);

        // Competitor 4: Shopify
        Map<String, Object> shopify = new LinkedHashMap<>();
        shopify.put("id", "comp-004");
        shopify.put("name", "Shopify");
        shopify.put("category", "E-commerce");
        shopify.put("marketShare", "32%");
        shopify.put("strengths", List.of(
                "Market leader",
                "Easy setup",
                "Large app store",
                "Excellent support"
        ));
        shopify.put("weaknesses", List.of(
                "Transaction fees",
                "Limited customization without apps",
                "Costs add up with apps"
        ));
        shopify.put("pricing", "$29-$299+ per month");
        shopify.put("targetAudience", "E-commerce Businesses (all sizes)");
        MOCK_COMPETITORS.put("comp-004", shopify);
    }

    public Map<String, Object> read(String uri) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("uri", uri);
        response.put("mimeType", "application/json");

        String competitorId = extractCompetitorId(uri);

        if (competitorId == null || competitorId.equals("list")) {
            response.put("content", Map.of(
                    "competitors", MOCK_COMPETITORS.values(),
                    "count", MOCK_COMPETITORS.size(),
                    "categories", List.of("CRM", "Marketing/CRM", "Email Marketing", "E-commerce")
            ));
        } else if (MOCK_COMPETITORS.containsKey(competitorId)) {
            response.put("content", MOCK_COMPETITORS.get(competitorId));
        } else {
            response.put("error", "Competitor not found: " + competitorId);
            response.put("availableCompetitors", MOCK_COMPETITORS.keySet());
        }

        return response;
    }

    private String extractCompetitorId(String uri) {
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
