package com.mcp.marketing.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcp.marketing.config.MarketingProperties;
import com.mcp.marketing.observability.ObservabilityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * Service for saving marketing outputs to organized directory structure
 * Automatically creates subdirectories for each content type (ads, crm, seo, strategy)
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OutputService {

    private final MarketingProperties properties;
    private final ObservabilityService observability;
    private final ObjectMapper objectMapper;

    /**
     * Save output to file in organized subdirectory structure
     *
     * @param data        The data to save
     * @param contentType The type of content (ads, crm-sequences, seo-plan, strategy)
     * @return The absolute path of the saved file, or null if save failed
     */
    public String saveToFile(Object data, String contentType) {
        try {
            // Determine subdirectory based on content type
            String subdirectory = determineSubdirectory(contentType);

            // Create full output path with subdirectory
            Path outputPath = Paths.get(properties.getOutputDirectory(), subdirectory);
            Files.createDirectories(outputPath);

            // Generate filename with timestamp and request ID
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String requestId = observability.getCurrentRequestId();
            String filename = String.format("%s_%s_%s.json", contentType.toLowerCase(), requestId, timestamp);

            // Save file
            Path filePath = outputPath.resolve(filename);
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(filePath.toFile(), data);

            log.info("Saved {} output to: {}", contentType, filePath.toAbsolutePath());
            return filePath.toAbsolutePath().toString();

        } catch (IOException e) {
            log.error("Failed to save output file for type: {}", contentType, e);
            return null;
        }
    }

    /**
     * Save output as Map (for backward compatibility with tools)
     *
     * @param data        The map data to save
     * @param contentType The type of content
     * @return The absolute path of the saved file, or null if save failed
     */
    public String saveMapToFile(Map<String, Object> data, String contentType) {
        return saveToFile(data, contentType);
    }

    /**
     * Determine the subdirectory based on content type
     * Maps content types to their respective folders
     *
     * @param contentType The type of content being saved
     * @return The subdirectory name
     */
    private String determineSubdirectory(String contentType) {
        String normalized = contentType.toLowerCase().replace("_", "-");

        return switch (normalized) {
            case "ads", "ad-generation" -> "ads";
            case "crm-sequences", "crm-sequence", "crm" -> "crm";
            case "seo-plan", "seo-strategy", "seo" -> "seo";
            default -> {
                log.warn("Unknown content type '{}', saving to 'others' folder", contentType);
                yield "others";
            }
        };
    }

    /**
     * Get the full output path for a specific content type
     *
     * @param contentType The content type
     * @return The full path to the subdirectory
     */
    public Path getOutputPath(String contentType) {
        String subdirectory = determineSubdirectory(contentType);
        return Paths.get(properties.getOutputDirectory(), subdirectory);
    }

    /**
     * Ensure output directory structure exists
     * Creates all necessary subdirectories
     */
    public void initializeOutputDirectories() {
        String[] directories = {"ads", "crm", "seo", "others"};

        for (String dir : directories) {
            try {
                Path path = Paths.get(properties.getOutputDirectory(), dir);
                Files.createDirectories(path);
                log.debug("Initialized output directory: {}", path.toAbsolutePath());
            } catch (IOException e) {
                log.error("Failed to create output directory: {}", dir, e);
            }
        }

        log.info("Output directory structure initialized at: {}", properties.getOutputDirectory());
    }
}

