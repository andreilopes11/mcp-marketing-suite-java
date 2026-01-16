package com.mcp.marketing.infra.storage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcp.marketing.config.AppConfiguration;
import com.mcp.marketing.domain.ports.StoragePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Filesystem implementation of StoragePort
 * <p>
 * Saves JSON artifacts to ./outputs directory with standardized naming:
 * <artifactType>_<requestId>_<yyyyMMdd_HHmmss>.json
 * <p>
 * Saves the complete StandardResponse envelope for full audit trail
 */
@Component
public class FileSystemStorage implements StoragePort {

    private static final Logger logger = LoggerFactory.getLogger(FileSystemStorage.class);
    private static final DateTimeFormatter FILENAME_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    private final AppConfiguration appConfig;
    private final ObjectMapper objectMapper;

    public FileSystemStorage(AppConfiguration appConfig, ObjectMapper objectMapper) {
        this.appConfig = appConfig;
        this.objectMapper = objectMapper;
        ensureOutputDirectory();
    }

    /**
     * Ensure output directory exists, create if necessary
     */
    private void ensureOutputDirectory() {
        if (!appConfig.getOutputs().isEnabled()) {
            logger.info("Output storage is disabled");
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
            throw new RuntimeException("Failed to initialize output directory", e);
        }
    }

    @Override
    public String saveJson(String artifactType, String requestId, Object payload) {
        if (!appConfig.getOutputs().isEnabled()) {
            logger.warn("Output storage disabled, skipping save for request_id={}", requestId);
            return null;
        }

        try {
            // Generate filename: <artifactType>_<requestId>_<yyyyMMdd_HHmmss>.json
            String timestamp = LocalDateTime.now().format(FILENAME_DATE_FORMAT);
            String filename = String.format("%s_%s_%s.json", artifactType, requestId, timestamp);

            Path outputDir = Paths.get(appConfig.getOutputs().getDirectory());
            Path outputPath = outputDir.resolve(filename);

            // Save complete envelope with pretty-print for audit
            objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValue(outputPath.toFile(), payload);

            File savedFile = outputPath.toFile();
            logger.info("Artifact saved: {} (type={}, request_id={}, size={} bytes)",
                    outputPath.toAbsolutePath(),
                    artifactType,
                    requestId,
                    savedFile.length());

            return outputPath.toAbsolutePath().toString();

        } catch (IOException e) {
            logger.error("Failed to save artifact: type={}, request_id={}", artifactType, requestId, e);
            throw new RuntimeException("Failed to save artifact to filesystem", e);
        }
    }
}
