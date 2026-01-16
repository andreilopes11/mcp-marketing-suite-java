package com.mcp.marketing.api.util;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Utility for resolving request IDs from HTTP headers or generating new ones
 * <p>
 * Follows the pattern:
 * - If X-Request-Id header exists, use it
 * - Otherwise, generate a new UUID
 * <p>
 * This enables request tracing across distributed systems
 */
@Component
public class RequestIdResolver {

    private static final Logger logger = LoggerFactory.getLogger(RequestIdResolver.class);
    private static final String REQUEST_ID_HEADER = "X-Request-Id";

    /**
     * Resolve request ID from HTTP request
     *
     * @param request HTTP request
     * @return request ID from header or newly generated UUID
     */
    public String resolve(HttpServletRequest request) {
        String requestId = request.getHeader(REQUEST_ID_HEADER);

        if (requestId != null && !requestId.isBlank()) {
            logger.debug("Using existing request_id from header: {}", requestId);
            return requestId;
        }

        requestId = UUID.randomUUID().toString();
        logger.debug("Generated new request_id: {}", requestId);
        return requestId;
    }

    /**
     * Generate a new request ID (UUID)
     *
     * @return newly generated UUID string
     */
    public String generate() {
        String requestId = UUID.randomUUID().toString();
        logger.debug("Generated request_id: {}", requestId);
        return requestId;
    }

    /**
     * Validate if a string is a valid request ID format
     *
     * @param requestId request ID to validate
     * @return true if valid UUID format
     */
    public boolean isValid(String requestId) {
        if (requestId == null || requestId.isBlank()) {
            return false;
        }

        try {
            UUID.fromString(requestId);
            return true;
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid request_id format: {}", requestId);
            return false;
        }
    }
}
