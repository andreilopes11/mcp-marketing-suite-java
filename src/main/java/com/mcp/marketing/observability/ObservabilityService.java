package com.mcp.marketing.observability;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import java.util.function.Supplier;

/**
 * Observability utilities for tracing and logging
 */
@Slf4j
@Component
public class ObservabilityService {

    public String generateRequestId() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    public void setRequestId(String requestId) {
        MDC.put("request_id", requestId);
    }

    public void clearRequestId() {
        MDC.remove("request_id");
    }

    public String getCurrentRequestId() {
        return MDC.get("request_id");
    }

    public <T> T traceOperation(String operationName, Supplier<T> operation) {
        Instant start = Instant.now();
        String requestId = getCurrentRequestId();

        log.info("Starting operation: {} [request_id={}]", operationName, requestId);

        try {
            T result = operation.get();
            long duration = Duration.between(start, Instant.now()).toMillis();
            log.info("Completed operation: {} in {}ms [request_id={}]",
                operationName, duration, requestId);
            return result;
        } catch (Exception e) {
            long duration = Duration.between(start, Instant.now()).toMillis();
            log.error("Failed operation: {} after {}ms [request_id={}]",
                operationName, duration, requestId, e);
            throw e;
        }
    }

    public void traceOperationVoid(String operationName, Runnable operation) {
        Instant start = Instant.now();
        String requestId = getCurrentRequestId();

        log.info("Starting operation: {} [request_id={}]", operationName, requestId);

        try {
            operation.run();
            long duration = Duration.between(start, Instant.now()).toMillis();
            log.info("Completed operation: {} in {}ms [request_id={}]",
                operationName, duration, requestId);
        } catch (Exception e) {
            long duration = Duration.between(start, Instant.now()).toMillis();
            log.error("Failed operation: {} after {}ms [request_id={}]",
                operationName, duration, requestId, e);
            throw e;
        }
    }

    public void logMetric(String metricName, Object value) {
        String requestId = getCurrentRequestId();
        log.info("METRIC: {} = {} [request_id={}]", metricName, value, requestId);
    }
}

