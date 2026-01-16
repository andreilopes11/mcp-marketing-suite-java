package com.mcp.marketing.api.util;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * Unit tests for RequestIdResolver
 */
class RequestIdResolverTest {

    private RequestIdResolver resolver;

    @Mock
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        resolver = new RequestIdResolver();
    }

    @Test
    void testResolve_WithExistingHeader_ReturnsHeaderValue() {
        // Given
        String existingRequestId = "existing-request-id-123";
        when(request.getHeader("X-Request-Id")).thenReturn(existingRequestId);

        // When
        String resolvedId = resolver.resolve(request);

        // Then
        assertEquals(existingRequestId, resolvedId, "Should return existing request ID from header");
    }

    @Test
    void testResolve_WithoutHeader_GeneratesNewUuid() {
        // Given
        when(request.getHeader("X-Request-Id")).thenReturn(null);

        // When
        String resolvedId = resolver.resolve(request);

        // Then
        assertNotNull(resolvedId, "Should generate a new request ID");
        assertTrue(resolver.isValid(resolvedId), "Generated ID should be valid UUID format");
    }

    @Test
    void testResolve_WithBlankHeader_GeneratesNewUuid() {
        // Given
        when(request.getHeader("X-Request-Id")).thenReturn("   ");

        // When
        String resolvedId = resolver.resolve(request);

        // Then
        assertNotNull(resolvedId, "Should generate a new request ID");
        assertTrue(resolver.isValid(resolvedId), "Generated ID should be valid UUID format");
    }

    @Test
    void testGenerate_CreatesValidUuid() {
        // When
        String requestId = resolver.generate();

        // Then
        assertNotNull(requestId, "Should generate a request ID");
        assertTrue(resolver.isValid(requestId), "Generated ID should be valid UUID format");
    }

    @Test
    void testGenerate_CreatesDifferentUuids() {
        // When
        String requestId1 = resolver.generate();
        String requestId2 = resolver.generate();

        // Then
        assertNotEquals(requestId1, requestId2, "Each generate() call should produce unique UUID");
    }

    @Test
    void testIsValid_WithValidUuid_ReturnsTrue() {
        // Given
        String validUuid = "550e8400-e29b-41d4-a716-446655440000";

        // When
        boolean isValid = resolver.isValid(validUuid);

        // Then
        assertTrue(isValid, "Valid UUID should return true");
    }

    @Test
    void testIsValid_WithInvalidFormat_ReturnsFalse() {
        // Given
        String invalidUuid = "not-a-valid-uuid";

        // When
        boolean isValid = resolver.isValid(invalidUuid);

        // Then
        assertFalse(isValid, "Invalid UUID format should return false");
    }

    @Test
    void testIsValid_WithNull_ReturnsFalse() {
        // When
        boolean isValid = resolver.isValid(null);

        // Then
        assertFalse(isValid, "Null should return false");
    }

    @Test
    void testIsValid_WithBlank_ReturnsFalse() {
        // When
        boolean isValid = resolver.isValid("   ");

        // Then
        assertFalse(isValid, "Blank string should return false");
    }

    @Test
    void testIsValid_WithEmpty_ReturnsFalse() {
        // When
        boolean isValid = resolver.isValid("");

        // Then
        assertFalse(isValid, "Empty string should return false");
    }
}
