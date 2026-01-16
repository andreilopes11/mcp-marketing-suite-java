package com.mcp.marketing.api.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for DTO validation
 * <p>
 * Tests Bean Validation constraints on request DTOs
 */
class DtoValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testAdsRequest_AllFieldsValid_NoViolations() {
        // Given
        AdsRequest request = AdsRequest.builder()
                .product("SaaS Platform")
                .audience("B2B Tech Companies")
                .brandVoice("Professional")
                .goals("Lead Generation")
                .language("pt-BR")
                .build();

        // When
        Set<ConstraintViolation<AdsRequest>> violations = validator.validate(request);

        // Then
        assertTrue(violations.isEmpty(), "Should have no validation violations");
    }

    @Test
    void testAdsRequest_MissingProduct_HasViolation() {
        // Given
        AdsRequest request = AdsRequest.builder()
                .audience("B2B Tech Companies")
                .brandVoice("Professional")
                .goals("Lead Generation")
                .language("pt-BR")
                .build();

        // When
        Set<ConstraintViolation<AdsRequest>> violations = validator.validate(request);

        // Then
        assertFalse(violations.isEmpty(), "Should have validation violations");
        assertEquals(1, violations.size(), "Should have exactly 1 violation");

        ConstraintViolation<AdsRequest> violation = violations.iterator().next();
        assertEquals("product", violation.getPropertyPath().toString());
        assertEquals("product is required", violation.getMessage());
    }

    @Test
    void testAdsRequest_InvalidLanguage_HasViolation() {
        // Given
        AdsRequest request = AdsRequest.builder()
                .product("SaaS Platform")
                .audience("B2B Tech Companies")
                .brandVoice("Professional")
                .goals("Lead Generation")
                .language("fr") // Invalid language
                .build();

        // When
        Set<ConstraintViolation<AdsRequest>> violations = validator.validate(request);

        // Then
        assertFalse(violations.isEmpty(), "Should have validation violations");
        assertEquals(1, violations.size(), "Should have exactly 1 violation");

        ConstraintViolation<AdsRequest> violation = violations.iterator().next();
        assertEquals("language", violation.getPropertyPath().toString());
        assertEquals("language must be 'pt-BR' or 'en'", violation.getMessage());
    }

    @Test
    void testAdsRequest_LanguagePtBr_Valid() {
        // Given
        AdsRequest request = AdsRequest.builder()
                .product("SaaS Platform")
                .audience("B2B Tech Companies")
                .brandVoice("Professional")
                .goals("Lead Generation")
                .language("pt-BR")
                .build();

        // When
        Set<ConstraintViolation<AdsRequest>> violations = validator.validate(request);

        // Then
        assertTrue(violations.isEmpty(), "pt-BR should be valid");
    }

    @Test
    void testAdsRequest_LanguageEn_Valid() {
        // Given
        AdsRequest request = AdsRequest.builder()
                .product("SaaS Platform")
                .audience("B2B Tech Companies")
                .brandVoice("Professional")
                .goals("Lead Generation")
                .language("en")
                .build();

        // When
        Set<ConstraintViolation<AdsRequest>> violations = validator.validate(request);

        // Then
        assertTrue(violations.isEmpty(), "en should be valid");
    }

    @Test
    void testSeoPlanRequest_MissingRequiredFields_HasMultipleViolations() {
        // Given
        SeoPlanRequest request = SeoPlanRequest.builder()
                .product("SaaS Platform")
                // Missing audience, brandVoice, goals, language
                .build();

        // When
        Set<ConstraintViolation<SeoPlanRequest>> violations = validator.validate(request);

        // Then
        assertFalse(violations.isEmpty(), "Should have validation violations");
        assertEquals(4, violations.size(), "Should have 4 violations (audience, brandVoice, goals, language)");
    }

    @Test
    void testSeoPlanRequest_AllFieldsValid_NoViolations() {
        // Given
        SeoPlanRequest request = SeoPlanRequest.builder()
                .product("SaaS Platform")
                .audience("B2B Tech Companies")
                .brandVoice("Professional")
                .goals("Organic Traffic Growth")
                .language("en")
                .build();

        // When
        Set<ConstraintViolation<SeoPlanRequest>> violations = validator.validate(request);

        // Then
        assertTrue(violations.isEmpty(), "Should have no validation violations");
    }

    @Test
    void testCrmSequencesRequest_AllFieldsValid_NoViolations() {
        // Given
        CrmSequencesRequest request = CrmSequencesRequest.builder()
                .product("SaaS Platform")
                .audience("B2B Tech Companies")
                .brandVoice("Professional")
                .goals("Lead Nurturing")
                .language("pt-BR")
                .build();

        // When
        Set<ConstraintViolation<CrmSequencesRequest>> violations = validator.validate(request);

        // Then
        assertTrue(violations.isEmpty(), "Should have no validation violations");
    }

    @Test
    void testStrategyRequest_AllFieldsValid_NoViolations() {
        // Given
        StrategyRequest request = StrategyRequest.builder()
                .product("SaaS Platform")
                .audience("B2B Tech Companies")
                .brandVoice("Professional")
                .goals("Market Expansion")
                .language("en")
                .build();

        // When
        Set<ConstraintViolation<StrategyRequest>> violations = validator.validate(request);

        // Then
        assertTrue(violations.isEmpty(), "Should have no validation violations");
    }

    @Test
    void testStrategyRequest_BlankLanguage_HasViolation() {
        // Given
        StrategyRequest request = StrategyRequest.builder()
                .product("SaaS Platform")
                .audience("B2B Tech Companies")
                .brandVoice("Professional")
                .goals("Market Expansion")
                .language("") // Blank language
                .build();

        // When
        Set<ConstraintViolation<StrategyRequest>> violations = validator.validate(request);

        // Then
        assertFalse(violations.isEmpty(), "Should have validation violations");
        assertTrue(violations.stream()
                        .anyMatch(v -> v.getPropertyPath().toString().equals("language")),
                "Should have violation on language field");
    }

    @Test
    void testAllRequiredFields_Present_AcrossAllDtos() {
        // Test that all DTOs have the same required fields pattern

        // AdsRequest
        AdsRequest adsRequest = AdsRequest.builder()
                .product("Product")
                .audience("Audience")
                .brandVoice("Brand")
                .goals("Goals")
                .language("en")
                .build();
        assertTrue(validator.validate(adsRequest).isEmpty());

        // SeoPlanRequest
        SeoPlanRequest seoRequest = SeoPlanRequest.builder()
                .product("Product")
                .audience("Audience")
                .brandVoice("Brand")
                .goals("Goals")
                .language("pt-BR")
                .build();
        assertTrue(validator.validate(seoRequest).isEmpty());

        // CrmSequencesRequest
        CrmSequencesRequest crmRequest = CrmSequencesRequest.builder()
                .product("Product")
                .audience("Audience")
                .brandVoice("Brand")
                .goals("Goals")
                .language("en")
                .build();
        assertTrue(validator.validate(crmRequest).isEmpty());

        // StrategyRequest
        StrategyRequest strategyRequest = StrategyRequest.builder()
                .product("Product")
                .audience("Audience")
                .brandVoice("Brand")
                .goals("Goals")
                .language("pt-BR")
                .build();
        assertTrue(validator.validate(strategyRequest).isEmpty());
    }
}
