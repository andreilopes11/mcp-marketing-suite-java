package com.mcp.marketing.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Standard response wrapper for all API responses
 * <p>
 * Provides consistent structure with request_id tracking, timestamp, and data payload
 *
 * @param <T> Type of the response data
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StandardResponse<T> {

    /**
     * Unique request identifier for tracing
     */
    private String requestId;

    /**
     * Response timestamp (ISO-8601)
     */
    private String timestamp;

    /**
     * HTTP status code
     */
    private Integer status;

    /**
     * Success flag
     */
    private Boolean success;

    /**
     * Optional message
     */
    private String message;

    /**
     * Response data payload
     */
    private T data;

    /**
     * Create successful response
     */
    public static <T> StandardResponse<T> success(String requestId, T data) {
        return StandardResponse.<T>builder()
                .requestId(requestId)
                .timestamp(Instant.now().toString())
                .status(200)
                .success(true)
                .data(data)
                .build();
    }

    /**
     * Create successful response with message
     */
    public static <T> StandardResponse<T> success(String requestId, String message, T data) {
        return StandardResponse.<T>builder()
                .requestId(requestId)
                .timestamp(Instant.now().toString())
                .status(200)
                .success(true)
                .message(message)
                .data(data)
                .build();
    }

    /**
     * Create error response
     */
    public static <T> StandardResponse<T> error(String requestId, Integer status, String message) {
        return StandardResponse.<T>builder()
                .requestId(requestId)
                .timestamp(Instant.now().toString())
                .status(status)
                .success(false)
                .message(message)
                .build();
    }
}
