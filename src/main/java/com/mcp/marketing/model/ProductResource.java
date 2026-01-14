package com.mcp.marketing.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * MCP Resource representing product information
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResource {
    private String name;
    private String description;
    private String category;
    private String valueProposition;
    private String features;
    private String pricing;
}

