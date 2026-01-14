package com.mcp.marketing.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * MCP Resource representing competitor information
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompetitorResource {
    private List<Competitor> competitors;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Competitor {
        private String name;
        private String positioning;
        private String strengths;
        private String weaknesses;
        private String marketShare;
    }
}

