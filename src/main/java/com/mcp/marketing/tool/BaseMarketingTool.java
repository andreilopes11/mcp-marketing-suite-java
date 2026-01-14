package com.mcp.marketing.tool;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcp.marketing.config.MarketingProperties;
import com.mcp.marketing.observability.ObservabilityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Base class for marketing tools - eliminates code duplication
 */
@Slf4j
@RequiredArgsConstructor
public abstract class BaseMarketingTool {

    protected final MarketingProperties properties;
    protected final ObservabilityService observability;
    protected final ObjectMapper objectMapper;

    /**
     * Save output to file - shared implementation
     */
    protected String saveToFile(Object data, String type) {
        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String requestId = observability.getCurrentRequestId();
            String filename = String.format("%s_%s_%s.json", type, requestId, timestamp);

            Path outputDir = Paths.get(properties.getOutputDirectory());
            Files.createDirectories(outputDir);

            Path filePath = outputDir.resolve(filename);
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(filePath.toFile(), data);

            log.info("Saved {} output to: {}", type, filePath);
            return filePath.toString();
        } catch (IOException e) {
            log.error("Failed to save output file for type: {}", type, e);
            return null;
        }
    }
}

