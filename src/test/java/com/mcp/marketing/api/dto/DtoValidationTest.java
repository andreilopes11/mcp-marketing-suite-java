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
        AdsRequest request = createAdsRequest();

        // When
        Set<ConstraintViolation<AdsRequest>> violations = validator.validate(request);

        // Then
        assertTrue(violations.isEmpty(), "Should have no validation violations");
    }

    @Test
    void testAdsRequest_MissingProduct_HasViolation() {
        // Given
        AdsRequest request = createAdsRequest();
        request.setProduct(null);

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
        AdsRequest request = createAdsRequest();
        request.setLanguage("fr"); // Invalid language

        // When
        Set<ConstraintViolation<AdsRequest>> violations = validator.validate(request);

        // Then
        assertFalse(violations.isEmpty(), "Should have validation violations");
        assertEquals(1, violations.size(), "Should have exactly 1 violation");

        ConstraintViolation<AdsRequest> violation = violations.iterator().next();
        assertEquals("language", violation.getPropertyPath().toString());
        assertEquals("language must be 'pt-BR' or 'en-US' or 'es-ES'", violation.getMessage());
    }

    @Test
    void testAdsRequest_LanguagePtBr_Valid() {
        // Given
        AdsRequest request = createAdsRequest();

        // When
        Set<ConstraintViolation<AdsRequest>> violations = validator.validate(request);

        // Then
        assertTrue(violations.isEmpty(), "pt-BR should be valid");
    }

    @Test
    void testAdsRequest_LanguageEnUs_Valid() {
        // Given
        AdsRequest request = createAdsRequest();
        request.setLanguage("en-US");

        // When
        Set<ConstraintViolation<AdsRequest>> violations = validator.validate(request);

        // Then
        assertTrue(violations.isEmpty(), "en-US should be valid");
    }

    @Test
    void testSeoPlanRequest_MissingRequiredFields_HasMultipleViolations() {
        // Given
        SeoPlanRequest request = new SeoPlanRequest();
        request.setProduct("SaaS Platform");
        // Missing audience, brandVoice, goals, language

        // When
        Set<ConstraintViolation<SeoPlanRequest>> violations = validator.validate(request);

        // Then
        assertFalse(violations.isEmpty(), "Should have validation violations");
        assertEquals(4, violations.size(), "Should have 4 violations (audience, brandVoice, goals, language)");
    }

    @Test
    void testSeoPlanRequest_AllFieldsValid_NoViolations() {
        // Given
        SeoPlanRequest request = createSeoPlanRequest();

        // When
        Set<ConstraintViolation<SeoPlanRequest>> violations = validator.validate(request);

        // Then
        assertTrue(violations.isEmpty(), "Should have no validation violations");
    }

    @Test
    void testCrmSequencesRequest_AllFieldsValid_NoViolations() {
        // Given
        CrmSequencesRequest request = createCrmSequencesRequest();

        // When
        Set<ConstraintViolation<CrmSequencesRequest>> violations = validator.validate(request);

        // Then
        assertTrue(violations.isEmpty(), "Should have no validation violations");
    }

    @Test
    void testStrategyRequest_AllFieldsValid_NoViolations() {
        // Given
        StrategyRequest request = createStrategyRequest();

        // When
        Set<ConstraintViolation<StrategyRequest>> violations = validator.validate(request);

        // Then
        assertTrue(violations.isEmpty(), "Should have no validation violations");
    }

    @Test
    void testStrategyRequest_BlankLanguage_HasViolation() {
        // Given
        StrategyRequest request = createStrategyRequest();
        request.setLanguage(""); // Blank language

        // When
        Set<ConstraintViolation<StrategyRequest>> violations = validator.validate(request);

        // Then
        assertFalse(violations.isEmpty(), "Should have validation violations");
        assertTrue(violations.stream()
                        .anyMatch(v -> v.getPropertyPath().toString().equals("language")),
                "Should have violation on language field");
    }

    @Test
    void testAdsRequest_MissingAllFields_HasMultipleViolations() {
        // Given
        AdsRequest request = new AdsRequest();

        // When
        Set<ConstraintViolation<AdsRequest>> violations = validator.validate(request);

        // Then
        assertFalse(violations.isEmpty(), "Should have validation violations");
        assertEquals(5, violations.size(), "Should have 5 violations (all required fields)");
    }

    @Test
    void testAdsRequest_BlankProduct_HasViolation() {
        // Given
        AdsRequest request = createAdsRequest();
        request.setProduct("");

        // When
        Set<ConstraintViolation<AdsRequest>> violations = validator.validate(request);

        // Then
        assertFalse(violations.isEmpty(), "Should have validation violations");
        assertTrue(violations.stream()
                        .anyMatch(v -> v.getPropertyPath().toString().equals("product")),
                "Should have violation on product field");
    }

    @Test
    void testCrmSequencesRequest_MissingRequiredFields_HasMultipleViolations() {
        // Given
        CrmSequencesRequest request = new CrmSequencesRequest();
        request.setProduct("SaaS Platform");
        // Missing audience, brandVoice, goals, language

        // When
        Set<ConstraintViolation<CrmSequencesRequest>> violations = validator.validate(request);

        // Then
        assertFalse(violations.isEmpty(), "Should have validation violations");
        assertEquals(4, violations.size(), "Should have 4 violations");
    }

    @Test
    void testCrmSequencesRequest_InvalidLanguage_HasViolation() {
        // Given
        CrmSequencesRequest request = createCrmSequencesRequest();
        request.setLanguage("en"); // Invalid for BaseRequest (requires pt-BR, en-US, or es-ES)

        // When
        Set<ConstraintViolation<CrmSequencesRequest>> violations = validator.validate(request);

        // Then
        assertFalse(violations.isEmpty(), "Should have validation violations");
        assertEquals(1, violations.size(), "Should have exactly 1 violation");

        ConstraintViolation<CrmSequencesRequest> violation = violations.iterator().next();
        assertEquals("language", violation.getPropertyPath().toString());
        assertEquals("language must be 'pt-BR' or 'en-US' or 'es-ES'", violation.getMessage());
    }

    @Test
    void testStrategyRequest_MissingRequiredFields_HasMultipleViolations() {
        // Given
        StrategyRequest request = new StrategyRequest();
        request.setProduct("SaaS Platform");
        // Missing audience, brandVoice, goals, language

        // When
        Set<ConstraintViolation<StrategyRequest>> violations = validator.validate(request);

        // Then
        assertFalse(violations.isEmpty(), "Should have validation violations");
        assertEquals(4, violations.size(), "Should have 4 violations");
    }

    @Test
    void testStrategyRequest_InvalidLanguage_HasViolation() {
        // Given
        StrategyRequest request = createStrategyRequest();
        request.setLanguage("en"); // Invalid for BaseRequest (requires pt-BR, en-US, or es-ES)

        // When
        Set<ConstraintViolation<StrategyRequest>> violations = validator.validate(request);

        // Then
        assertFalse(violations.isEmpty(), "Should have validation violations");
        assertEquals(1, violations.size(), "Should have exactly 1 violation");

        ConstraintViolation<StrategyRequest> violation = violations.iterator().next();
        assertEquals("language", violation.getPropertyPath().toString());
        assertEquals("language must be 'pt-BR' or 'en-US' or 'es-ES'", violation.getMessage());
    }

    @Test
    void testAdsRequest_BlankAudience_HasViolation() {
        // Given
        AdsRequest request = createAdsRequest();
        request.setAudience("");

        // When
        Set<ConstraintViolation<AdsRequest>> violations = validator.validate(request);

        // Then
        assertFalse(violations.isEmpty(), "Should have validation violations");
        assertEquals(1, violations.size(), "Should have exactly 1 violation");

        ConstraintViolation<AdsRequest> violation = violations.iterator().next();
        assertEquals("audience", violation.getPropertyPath().toString());
        assertEquals("audience is required", violation.getMessage());
    }

    @Test
    void testAdsRequest_BlankBrandVoice_HasViolation() {
        // Given
        AdsRequest request = createAdsRequest();
        request.setBrandVoice("");

        // When
        Set<ConstraintViolation<AdsRequest>> violations = validator.validate(request);

        // Then
        assertFalse(violations.isEmpty(), "Should have validation violations");
        assertEquals(1, violations.size(), "Should have exactly 1 violation");

        ConstraintViolation<AdsRequest> violation = violations.iterator().next();
        assertEquals("brandVoice", violation.getPropertyPath().toString());
        assertEquals("brand_voice is required", violation.getMessage());
    }

    @Test
    void testAdsRequest_BlankGoals_HasViolation() {
        // Given
        AdsRequest request = createAdsRequest();
        request.setGoals("");

        // When
        Set<ConstraintViolation<AdsRequest>> violations = validator.validate(request);

        // Then
        assertFalse(violations.isEmpty(), "Should have validation violations");
        assertEquals(1, violations.size(), "Should have exactly 1 violation");

        ConstraintViolation<AdsRequest> violation = violations.iterator().next();
        assertEquals("goals", violation.getPropertyPath().toString());
        assertEquals("goals is required", violation.getMessage());
    }

    @Test
    void testAdsRequest_BlankLanguage_HasViolation() {
        // Given
        AdsRequest request = createAdsRequest();
        request.setLanguage("");

        // When
        Set<ConstraintViolation<AdsRequest>> violations = validator.validate(request);

        // Then
        assertFalse(violations.isEmpty(), "Should have validation violations");
        // Should have both NotBlank and Pattern violations on language
        assertTrue(violations.size() >= 1, "Should have at least 1 violation");
        assertTrue(violations.stream()
                        .anyMatch(v -> v.getPropertyPath().toString().equals("language")),
                "Should have violation on language field");
    }

    @Test
    void testSeoPlanRequest_BlankProduct_HasViolation() {
        // Given
        SeoPlanRequest request = createSeoPlanRequest();
        request.setProduct("");

        // When
        Set<ConstraintViolation<SeoPlanRequest>> violations = validator.validate(request);

        // Then
        assertFalse(violations.isEmpty(), "Should have validation violations");
        assertEquals(1, violations.size(), "Should have exactly 1 violation");

        ConstraintViolation<SeoPlanRequest> violation = violations.iterator().next();
        assertEquals("product", violation.getPropertyPath().toString());
        assertEquals("product is required", violation.getMessage());
    }

    @Test
    void testCrmSequencesRequest_BlankAudience_HasViolation() {
        // Given
        CrmSequencesRequest request = createCrmSequencesRequest();
        request.setAudience("");

        // When
        Set<ConstraintViolation<CrmSequencesRequest>> violations = validator.validate(request);

        // Then
        assertFalse(violations.isEmpty(), "Should have validation violations");
        assertEquals(1, violations.size(), "Should have exactly 1 violation");

        ConstraintViolation<CrmSequencesRequest> violation = violations.iterator().next();
        assertEquals("audience", violation.getPropertyPath().toString());
        assertEquals("audience is required", violation.getMessage());
    }

    @Test
    void testStrategyRequest_BlankGoals_HasViolation() {
        // Given
        StrategyRequest request = createStrategyRequest();
        request.setGoals("");

        // When
        Set<ConstraintViolation<StrategyRequest>> violations = validator.validate(request);

        // Then
        assertFalse(violations.isEmpty(), "Should have validation violations");
        assertEquals(1, violations.size(), "Should have exactly 1 violation");

        ConstraintViolation<StrategyRequest> violation = violations.iterator().next();
        assertEquals("goals", violation.getPropertyPath().toString());
        assertEquals("goals is required", violation.getMessage());
    }

    @Test
    void testAllRequiredFields_Present_AcrossAllDtos() {
        // Test that all DTOs share the same required field enforcement

        // AdsRequest - supports pt-BR, en-US, es-ES
        AdsRequest adsRequest = createAdsRequest();
        adsRequest.setLanguage("en-US");
        assertTrue(validator.validate(adsRequest).isEmpty());

        // SeoPlanRequest extends BaseRequest - supports pt-BR, en-US, es-ES
        SeoPlanRequest seoRequest = createSeoPlanRequest();
        seoRequest.setLanguage("es-ES");
        assertTrue(validator.validate(seoRequest).isEmpty());

        // CrmSequencesRequest - supports pt-BR, en-US, es-ES
        CrmSequencesRequest crmRequest = createCrmSequencesRequest();
        crmRequest.setLanguage("en-US");
        assertTrue(validator.validate(crmRequest).isEmpty());

        // StrategyRequest - supports pt-BR, en-US, es-ES
        StrategyRequest strategyRequest = createStrategyRequest();
        strategyRequest.setLanguage("pt-BR");
        assertTrue(validator.validate(strategyRequest).isEmpty());
    }

    private AdsRequest createAdsRequest() {
        AdsRequest request = new AdsRequest();
        request.setProduct("SaaS Platform");
        request.setAudience("B2B Tech Companies");
        request.setBrandVoice("Professional");
        request.setGoals("Lead Generation");
        request.setLanguage("pt-BR");
        return request;
    }

    private SeoPlanRequest createSeoPlanRequest() {
        SeoPlanRequest request = new SeoPlanRequest();
        request.setProduct("SaaS Platform");
        request.setAudience("B2B Tech Companies");
        request.setBrandVoice("Professional");
        request.setGoals("Organic Traffic Growth");
        request.setLanguage("en-US");
        return request;
    }

    private CrmSequencesRequest createCrmSequencesRequest() {
        CrmSequencesRequest request = new CrmSequencesRequest();
        request.setProduct("SaaS Platform");
        request.setAudience("B2B Tech Companies");
        request.setBrandVoice("Professional");
        request.setGoals("Lead Nurturing");
        request.setLanguage("pt-BR");
        return request;
    }

    private StrategyRequest createStrategyRequest() {
        StrategyRequest request = new StrategyRequest();
        request.setProduct("SaaS Platform");
        request.setAudience("B2B Tech Companies");
        request.setBrandVoice("Professional");
        request.setGoals("Market Expansion");
        request.setLanguage("en-US");
        return request;
    }
}
