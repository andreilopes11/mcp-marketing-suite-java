package com.mcp.marketing.domain.model;

/**
 * Execution mode for marketing content generation
 * <p>
 * Defines how content should be generated:
 * - DETERMINISTIC: Uses rule-based templates (no AI, fully deterministic)
 * - AI_ENABLED: Uses LLM for intelligent content generation (future)
 */
public enum ExecutionMode {

    /**
     * Deterministic mode - rule-based templates
     * Default mode, no external AI dependencies
     */
    DETERMINISTIC,

    /**
     * AI-enabled mode - uses LLM for content generation
     * Not implemented yet, prepared for future enhancement
     */
    AI_ENABLED

}
