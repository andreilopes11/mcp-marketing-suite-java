package com.mcp.marketing.domain.service;

import com.mcp.marketing.domain.model.MarketingContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Validation service for marketing context and requests
 * <p>
 * Centralizes validation logic to be used by both REST API and MCP handlers
 */
@Service
public class ValidationService {

    /**
     * Validate marketing context has all required fields
     *
     * @param context Marketing context to validate
     * @return List of validation error messages (empty if valid)
     */
    public List<String> validateContext(MarketingContext context) {
        List<String> errors = new ArrayList<>();

        if (context == null) {
            errors.add("Context cannot be null");
            return errors;
        }

        if (isBlank(context.getProduct())) {
            errors.add("product is required");
        }

        if (isBlank(context.getAudience())) {
            errors.add("audience is required");
        }

        if (isBlank(context.getBrandVoice())) {
            errors.add("brand_voice is required");
        }

        if (isBlank(context.getGoals())) {
            errors.add("goals is required");
        }

        if (isBlank(context.getLanguage())) {
            errors.add("language is required");
        } else if (!isValidLanguage(context.getLanguage())) {
            errors.add("language must be 'pt-BR' or 'en-US' or 'es-ES'");
        }

        return errors;
    }

    /**
     * Check if context is valid
     *
     * @param context Marketing context to validate
     * @return true if valid, false otherwise
     */
    public boolean isValid(MarketingContext context) {
        return validateContext(context).isEmpty();
    }

    /**
     * Validate language is one of the supported values
     *
     * @param language Language code
     * @return true if valid
     */
    private boolean isValidLanguage(String language) {
        if (language == null) {
            return false;
        }
        String normalized = language.trim().toLowerCase();
        return normalized.equals("pt-br") || normalized.equals("en-us") || normalized.equals("es-es");
    }

    /**
     * Check if string is blank (null or whitespace only)
     *
     * @param value String to check
     * @return true if blank
     */
    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
