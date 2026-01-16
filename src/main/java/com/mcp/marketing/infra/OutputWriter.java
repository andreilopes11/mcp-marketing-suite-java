package com.mcp.marketing.infra;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcp.marketing.config.AppConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;

/**
 * Infrastructure component for writing outputs to filesystem
 * All executions save output to ./outputs with request_id tracking
 */
@Component
public class OutputWriter {

    private static final Logger logger = LoggerFactory.getLogger(OutputWriter.class);
    private final AppConfiguration appConfig;
    private final ObjectMapper objectMapper;

    public OutputWriter(AppConfiguration appConfig, ObjectMapper objectMapper) {
        this.appConfig = appConfig;
        this.objectMapper = objectMapper;
        ensureOutputDirectory();
    }

    private void ensureOutputDirectory() {
        if (!appConfig.getOutputs().isEnabled()) {
            logger.info("Output writing is disabled");
            return;
        }

        try {
            Path outputPath = Paths.get(appConfig.getOutputs().getDirectory());
            if (!Files.exists(outputPath)) {
                Files.createDirectories(outputPath);
                logger.info("Created output directory: {}", outputPath.toAbsolutePath());
            }
        } catch (IOException e) {
            logger.error("Failed to create output directory", e);
        }
    }

    /**
     * Write output to file with request_id tracking
     */
    public void writeOutput(String requestId, Object data, String suffix) {
        if (!appConfig.getOutputs().isEnabled()) {
            logger.debug("Output writing disabled, skipping write for request_id={}", requestId);
            return;
        }

        try {
            String timestamp = Instant.now().toString().replace(":", "-");
            String filename = String.format("%s_%s_%s.json", requestId, timestamp, suffix);
            Path outputPath = Paths.get(appConfig.getOutputs().getDirectory(), filename);

            objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValue(outputPath.toFile(), data);

            logger.info("Output written: {} (request_id={})", outputPath.toAbsolutePath(), requestId);
        } catch (IOException e) {
            logger.error("Failed to write output for request_id={}", requestId, e);
        }
    }
}
