package com.mcp.marketing.infra;

import com.mcp.marketing.config.AppConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Infrastructure component for writing outputs to filesystem
 * All executions save output to ./outputs with request_id tracking
 */
@Component
public class OutputWriter {

    private static final Logger logger = LoggerFactory.getLogger(OutputWriter.class);
    private final AppConfiguration appConfig;

    public OutputWriter(AppConfiguration appConfig) {
        this.appConfig = appConfig;
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
}
