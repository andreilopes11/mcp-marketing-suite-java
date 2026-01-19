package com.mcp.marketing.domain.ports;

/**
 * Port for storage operations
 * <p>
 * Defines the contract for persisting artifacts to storage systems.
 * Implementations can target filesystem, S3, databases, etc.
 */
public interface StoragePort {

    /**
     * Save a JSON artifact to storage
     *
     * @param artifactType Type of artifact (ads, seo, crm, strategy)
     * @param requestId    Unique request identifier for tracking
     * @param payload      Data to be saved (typically StandardResponse envelope)
     * @return Path where the artifact was saved
     */
    String saveJson(String artifactType, String requestId, Object payload);
}
