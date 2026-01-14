package com.mcp.marketing.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * MCP Resource representing audience information
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AudienceResource {
    private String segment;
    private String demographics;
    private String psychographics;
    private String painPoints;
    private String behaviors;
    private String channels;
}

