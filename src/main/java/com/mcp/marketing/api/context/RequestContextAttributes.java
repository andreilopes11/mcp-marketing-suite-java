package com.mcp.marketing.api.context;

/**
 * Utility constants used to store contextual information in the current HTTP request.
 */
public final class RequestContextAttributes {

    private RequestContextAttributes() {
    }

    public static final String REQUEST_ID = "mcp.request_id";
    public static final String START_TIME = "mcp.request_start_time";
}
