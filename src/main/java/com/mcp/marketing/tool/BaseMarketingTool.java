package com.mcp.marketing.tool;

import com.mcp.marketing.config.MarketingProperties;
import com.mcp.marketing.observability.ObservabilityService;
import com.mcp.marketing.service.OutputService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Base class for marketing tools - eliminates code duplication
 */
@Slf4j
@RequiredArgsConstructor
public abstract class BaseMarketingTool {

    protected final MarketingProperties properties;
    protected final ObservabilityService observability;
    protected final OutputService outputService;

    /**
     * Save output to file using OutputService
     * Files are automatically organized into subdirectories by type
     *
     * @param data The data to save
     * @param type The content type (ads, crm-sequences, seo-plan, strategy)
     * @return The absolute path of the saved file, or null if save failed
     */
    protected String saveToFile(Object data, String type) {
        return outputService.saveToFile(data, type);
    }
}

