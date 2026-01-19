package com.mcp.marketing.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Domain model representing a saved output artifact
 * <p>
 * Carries metadata about persisted artifacts for tracking and retrieval
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OutputArtifact {

    /**
     * Type of artifact (ads, seo, crm, strategy)
     */
    private String artifactType;

    /**
     * Unique request identifier
     */
    private String requestId;

    /**
     * Full path where artifact was saved
     */
    private String outputPath;

    /**
     * Timestamp when artifact was created
     */
    private Instant timestamp;

    /**
     * File size in bytes
     */
    private Long sizeBytes;

    /**
     * Create artifact metadata
     */
    public static OutputArtifact of(String artifactType, String requestId, String outputPath) {
        return OutputArtifact.builder()
                .artifactType(artifactType)
                .requestId(requestId)
                .outputPath(outputPath)
                .timestamp(Instant.now())
                .build();
    }

    /**
     * Create artifact metadata with size
     */
    public static OutputArtifact of(String artifactType, String requestId, String outputPath, Long sizeBytes) {
        return OutputArtifact.builder()
                .artifactType(artifactType)
                .requestId(requestId)
                .outputPath(outputPath)
                .timestamp(Instant.now())
                .sizeBytes(sizeBytes)
                .build();
    }
}
