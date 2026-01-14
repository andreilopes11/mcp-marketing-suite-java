package com.mcp.marketing.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Response payload for marketing operations
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarketingResponse {

    @JsonProperty("request_id")
    private String requestId;

    private String status; // success, error, partial

    private Map<String, Object> data;

    @JsonProperty("output_path")
    private String outputPath;

    private LocalDateTime timestamp;

    private String message;

    @JsonProperty("execution_time_ms")
    private Long executionTimeMs;
}

