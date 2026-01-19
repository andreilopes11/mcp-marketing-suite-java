package com.mcp.marketing.domain.service;

import com.mcp.marketing.domain.model.ExecutionMode;
import com.mcp.marketing.domain.model.MarketingContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ValidationService
 */
class ValidationServiceTest {

    private ValidationService validationService;

    @BeforeEach
    void setUp() {
        validationService = new ValidationService();
    }

    @Test
    void testValidateContext_AllFieldsValid_ReturnsNoErrors() {
        // Given
        MarketingContext context = MarketingContext.builder()
                .requestId("test-001")
                .product("Cloud CRM")
                .audience("Small Business")
                .brandVoice("Professional")
                .goals("Generate leads")
                .language("en")
                .executionMode(ExecutionMode.DETERMINISTIC)
                .build();

        // When
        List<String> errors = validationService.validateContext(context);

        // Then
        assertTrue(errors.isEmpty(), "Should have no validation errors");
        assertTrue(validationService.isValid(context), "Context should be valid");
    }

    @Test
    void testValidateContext_NullContext_ReturnsError() {
        // When
        List<String> errors = validationService.validateContext(null);

        // Then
        assertFalse(errors.isEmpty(), "Should have validation errors");
        assertThat(errors).contains("Context cannot be null");
        assertFalse(validationService.isValid(null), "Null context should be invalid");
    }

    @Test
    void testValidateContext_MissingProduct_ReturnsError() {
        // Given - missing product
        MarketingContext context = MarketingContext.builder()
                .audience("Test Audience")
                .brandVoice("Test Voice")
                .goals("Test Goals")
                .language("en")
                .build();

        // When
        List<String> errors = validationService.validateContext(context);

        // Then
        assertFalse(errors.isEmpty(), "Should have validation errors");
        assertThat(errors).contains("product is required");
        assertFalse(validationService.isValid(context));
    }

    @Test
    void testValidateContext_MissingAudience_ReturnsError() {
        // Given
        MarketingContext context = MarketingContext.builder()
                .product("Test Product")
                .brandVoice("Test Voice")
                .goals("Test Goals")
                .language("en")
                .build();

        // When
        List<String> errors = validationService.validateContext(context);

        // Then
        assertThat(errors).contains("audience is required");
    }

    @Test
    void testValidateContext_MissingBrandVoice_ReturnsError() {
        // Given
        MarketingContext context = MarketingContext.builder()
                .product("Test Product")
                .audience("Test Audience")
                .goals("Test Goals")
                .language("en")
                .build();

        // When
        List<String> errors = validationService.validateContext(context);

        // Then
        assertThat(errors).contains("brand_voice is required");
    }

    @Test
    void testValidateContext_MissingGoals_ReturnsError() {
        // Given
        MarketingContext context = MarketingContext.builder()
                .product("Test Product")
                .audience("Test Audience")
                .brandVoice("Test Voice")
                .language("en")
                .build();

        // When
        List<String> errors = validationService.validateContext(context);

        // Then
        assertThat(errors).contains("goals is required");
    }

    @Test
    void testValidateContext_MissingLanguage_ReturnsError() {
        // Given
        MarketingContext context = MarketingContext.builder()
                .product("Test Product")
                .audience("Test Audience")
                .brandVoice("Test Voice")
                .goals("Test Goals")
                .build();

        // When
        List<String> errors = validationService.validateContext(context);

        // Then
        assertThat(errors).contains("language is required");
    }

    @Test
    void testValidateContext_InvalidLanguage_ReturnsError() {
        // Given - unsupported language
        MarketingContext context = MarketingContext.builder()
                .product("Test Product")
                .audience("Test Audience")
                .brandVoice("Test Voice")
                .goals("Test Goals")
                .language("fr")
                .build();

        // When
        List<String> errors = validationService.validateContext(context);

        // Then
        assertThat(errors).contains("language must be 'pt-BR' or 'en-US' or 'es-ES'");
    }

    @Test
    void testValidateContext_LanguageEn_Valid() {
        // Given
        MarketingContext context = MarketingContext.builder()
                .product("Test Product")
                .audience("Test Audience")
                .brandVoice("Test Voice")
                .goals("Test Goals")
                .language("en")
                .build();

        // When
        List<String> errors = validationService.validateContext(context);

        // Then
        assertTrue(errors.isEmpty(), "Language 'en' should be valid");
    }

    @Test
    void testValidateContext_LanguagePtBR_Valid() {
        // Given
        MarketingContext context = MarketingContext.builder()
                .product("Test Product")
                .audience("Test Audience")
                .brandVoice("Test Voice")
                .goals("Test Goals")
                .language("pt-BR")
                .build();

        // When
        List<String> errors = validationService.validateContext(context);

        // Then
        assertTrue(errors.isEmpty(), "Language 'pt-BR' should be valid");
    }

    @Test
    void testValidateContext_MultipleErrors_ReturnsAllErrors() {
        // Given - missing multiple required fields
        MarketingContext context = MarketingContext.builder()
                .product("Test Product")
                // Missing audience, brandVoice, goals, language
                .build();

        // When
        List<String> errors = validationService.validateContext(context);

        // Then
        assertEquals(4, errors.size(), "Should return all validation errors");
        assertThat(errors).contains(
                "audience is required",
                "brand_voice is required",
                "goals is required",
                "language is required"
        );
    }

    @Test
    void testValidateContext_EmptyStrings_TreatedAsBlank() {
        // Given - empty strings for required fields
        MarketingContext context = MarketingContext.builder()
                .product("")
                .audience("  ")
                .brandVoice("\t")
                .goals("\n")
                .language("")
                .build();

        // When
        List<String> errors = validationService.validateContext(context);

        // Then
        assertEquals(5, errors.size(), "Empty/whitespace strings should fail validation");
    }

    @Test
    void testValidateContext_OptionalFieldsMissing_NoErrors() {
        // Given - only required fields, optional fields missing
        MarketingContext context = MarketingContext.builder()
                .product("Test Product")
                .audience("Test Audience")
                .brandVoice("Test Voice")
                .goals("Test Goals")
                .language("en")
                // Optional fields not set: budget, platforms, domain, etc.
                .build();

        // When
        List<String> errors = validationService.validateContext(context);

        // Then
        assertTrue(errors.isEmpty(), "Optional fields should not cause validation errors");
    }

    @Test
    void testIsValid_ValidContext_ReturnsTrue() {
        // Given
        MarketingContext context = MarketingContext.builder()
                .product("Test Product")
                .audience("Test Audience")
                .brandVoice("Test Voice")
                .goals("Test Goals")
                .language("en")
                .build();

        // When
        boolean isValid = validationService.isValid(context);

        // Then
        assertTrue(isValid, "Valid context should return true");
    }

    @Test
    void testIsValid_InvalidContext_ReturnsFalse() {
        // Given - missing required field
        MarketingContext context = MarketingContext.builder()
                .product("Test Product")
                .language("en")
                .build();

        // When
        boolean isValid = validationService.isValid(context);

        // Then
        assertFalse(isValid, "Invalid context should return false");
    }

    @Test
    void testValidateContext_WithExecutionMode_Valid() {
        // Given
        MarketingContext context = MarketingContext.builder()
                .product("Test Product")
                .audience("Test Audience")
                .brandVoice("Test Voice")
                .goals("Test Goals")
                .language("en")
                .executionMode(ExecutionMode.AI_ENABLED)
                .build();

        // When
        List<String> errors = validationService.validateContext(context);

        // Then
        assertTrue(errors.isEmpty(), "Execution mode should not affect validation");
    }
}
