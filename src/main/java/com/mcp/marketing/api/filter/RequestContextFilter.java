package com.mcp.marketing.api.filter;

import com.mcp.marketing.api.context.RequestContextAttributes;
import com.mcp.marketing.api.util.RequestIdResolver;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filter that initializes contextual information for each HTTP request.
 */
@Component("marketingRequestContextFilter")
public class RequestContextFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(RequestContextFilter.class);

    private final RequestIdResolver requestIdResolver;

    public RequestContextFilter(RequestIdResolver requestIdResolver) {
        this.requestIdResolver = requestIdResolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        long startTime = System.currentTimeMillis();
        request.setAttribute(RequestContextAttributes.START_TIME, startTime);

        String requestId = requestIdResolver.resolve(request);
        request.setAttribute(RequestContextAttributes.REQUEST_ID, requestId);
        response.setHeader("X-Request-Id", requestId);
        MDC.put("request_id", requestId);

        logger.debug("Initialized request context: request_id={} path={}", requestId, request.getRequestURI());

        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove("request_id");
        }
    }
}
