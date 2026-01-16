package com.mcp.marketing.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

/**
 * Error response DTO for validation and exception handling
 * <p>
 * Provides detailed error information with field-level validation messages
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    /**
     * Unique request identifier for tracing
     */
    private String requestId;

    /**
     * Error timestamp (ISO-8601)
     */
    private String timestamp;

    /**
     * HTTP status code
     */
    private Integer status;

    /**
     * Error type/code
     */
    private String error;

    /**
     * Human-readable error message
     */
    private String message;

    /**
     * Request path that generated the error
     */
    private String path;

    /**
     * Field-level validation errors
     */
    private List<FieldError> fieldErrors;

    /**
     * Field validation error details
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FieldError {
        private String field;
        private String rejectedValue;
        private String message;
    }

    /**
     * Create error response for validation failures
     */
    public static ErrorResponse validation(String requestId, String path, List<FieldError> fieldErrors) {
        return ErrorResponse.builder()
                .requestId(requestId)
                .timestamp(Instant.now().toString())
                .status(400)
                .error("VALIDATION_ERROR")
                .message("Validation failed for one or more fields")
                .path(path)
                .fieldErrors(fieldErrors)
                .build();
    }

    /**
     * Create generic error response
     */
    public static ErrorResponse of(String requestId, Integer status, String error, String message, String path) {
        return ErrorResponse.builder()
                .requestId(requestId)
                .timestamp(Instant.now().toString())
                .status(status)
                .error(error)
                .message(message)
                .path(path)
                .build();
    }
}
