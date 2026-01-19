package com.mcp.marketing.mcp.resources;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * MCP Resource: Product Information
 * <p>
 * Provides mock product information for marketing context
 */
public class ProductResource {

    private static final Map<String, Map<String, Object>> MOCK_PRODUCTS = new LinkedHashMap<>();

    static {
        // Mock Product 1: CRM Platform
        Map<String, Object> crm = new LinkedHashMap<>();
        crm.put("id", "crm-001");
        crm.put("name", "Cloud CRM Platform");
        crm.put("category", "SaaS / CRM");
        crm.put("description", "All-in-one CRM solution for small and medium businesses");
        crm.put("features", List.of("Contact Management", "Sales Pipeline", "Email Integration", "Analytics Dashboard"));
        crm.put("pricing", "Starting at $29/user/month");
        crm.put("targetMarket", "Small to Medium Businesses");
        MOCK_PRODUCTS.put("crm-001", crm);

        // Mock Product 2: E-commerce Platform
        Map<String, Object> ecommerce = new LinkedHashMap<>();
        ecommerce.put("id", "ecom-001");
        ecommerce.put("name", "E-commerce Platform");
        ecommerce.put("category", "SaaS / E-commerce");
        ecommerce.put("description", "Complete e-commerce solution with payments and logistics");
        ecommerce.put("features", List.of("Online Store", "Payment Gateway", "Inventory Management", "Shipping Integration"));
        ecommerce.put("pricing", "Starting at $49/month");
        ecommerce.put("targetMarket", "Online Retailers");
        MOCK_PRODUCTS.put("ecom-001", ecommerce);

        // Mock Product 3: Marketing Automation
        Map<String, Object> marketing = new LinkedHashMap<>();
        marketing.put("id", "mkt-001");
        marketing.put("name", "Marketing Automation Tool");
        marketing.put("category", "SaaS / Marketing");
        marketing.put("description", "Automate your marketing campaigns and track ROI");
        marketing.put("features", List.of("Email Campaigns", "Social Media Scheduling", "Analytics", "A/B Testing"));
        marketing.put("pricing", "Starting at $99/month");
        marketing.put("targetMarket", "Marketing Teams");
        MOCK_PRODUCTS.put("mkt-001", marketing);
    }

    public Map<String, Object> read(String uri) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("uri", uri);
        response.put("mimeType", "application/json");

        // Extract product ID from URI (e.g., mcp://product/crm-001)
        String productId = extractProductId(uri);

        if (productId == null || productId.equals("list")) {
            // Return list of all products
            response.put("content", Map.of(
                    "products", MOCK_PRODUCTS.values(),
                    "count", MOCK_PRODUCTS.size()
            ));
        } else if (MOCK_PRODUCTS.containsKey(productId)) {
            // Return specific product
            response.put("content", MOCK_PRODUCTS.get(productId));
        } else {
            // Product not found
            response.put("error", "Product not found: " + productId);
            response.put("availableProducts", MOCK_PRODUCTS.keySet());
        }

        return response;
    }

    private String extractProductId(String uri) {
        if (uri == null) {
            return "list";
        }
        // Extract ID from URIs like "mcp://product/crm-001" or "product/crm-001"
        String[] parts = uri.split("/");
        if (parts.length > 0) {
            return parts[parts.length - 1];
        }
        return "list";
    }
}
