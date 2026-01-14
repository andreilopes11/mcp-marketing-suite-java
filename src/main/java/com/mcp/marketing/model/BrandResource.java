package com.mcp.marketing.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * MCP Resource representing brand information
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BrandResource {
    private String name;
    private String voice;
    private String tone;
    private String values;
    private String personality;
    private String guidelines;
}

