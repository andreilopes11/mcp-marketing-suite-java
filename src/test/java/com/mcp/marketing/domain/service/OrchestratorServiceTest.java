package com.mcp.marketing.domain.service;

import com.mcp.marketing.domain.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for OrchestratorService
 * <p>
 * Tests deterministic generation of marketing content
 */
class OrchestratorServiceTest {

    private OrchestratorService orchestratorService;
    private ValidationService validationService;

    @BeforeEach
    void setUp() {
        validationService = new ValidationService();
        orchestratorService = new OrchestratorService(validationService);
    }

    @Test
    void testGenerateAds_HappyPath_ReturnsCompleteAdsResult() {
        // Given
        MarketingContext context = MarketingContext.builder()
                .requestId("test-001")
                .product("Cloud CRM Platform")
                .audience("Small Business Owners")
                .brandVoice("Professional and Approachable")
                .goals("Generate 100 qualified leads per month")
                .language("en-US")
                .executionMode(ExecutionMode.DETERMINISTIC)
                .build();

        // When
        AdsResult result = orchestratorService.generateAds(context);

        // Then
        assertNotNull(result, "AdsResult should not be null");

        // Verify Google Ads
        assertNotNull(result.getGoogleAds(), "Google ads should be present");
        assertThat(result.getGoogleAds().getHeadline1()).contains("Cloud CRM Platform");
        assertThat(result.getGoogleAds().getHeadline2()).contains("Goal:");
        assertThat(result.getGoogleAds().getHeadline3()).contains("Tone:");
        assertNotNull(result.getGoogleAds().getKeywords(), "Keywords should be present");
        assertFalse(result.getGoogleAds().getKeywords().isEmpty(), "Keywords should not be empty");

        // Verify Meta Ads
        assertNotNull(result.getMetaAds(), "Meta ads should be present");
        assertThat(result.getMetaAds().getHeadline()).contains("Cloud CRM Platform");
        assertThat(result.getMetaAds().getCallToAction()).isNotBlank();

        // Verify LinkedIn Ads
        assertNotNull(result.getLinkedinAds(), "LinkedIn ads should be present");
        assertThat(result.getLinkedinAds().getHeadline()).contains("Cloud CRM Platform");

        // Verify QA Score
        assertNotNull(result.getQaScore(), "QA score should be present");
        assertTrue(result.getQaScore() >= 0 && result.getQaScore() <= 100,
                "QA score should be between 0 and 100");

        // Verify Recommendations
        assertNotNull(result.getRecommendations(), "Recommendations should be present");
        assertFalse(result.getRecommendations().isEmpty(), "Recommendations should not be empty");

        // Verify Metadata
        assertNotNull(result.getMetadata(), "Metadata should be present");
        assertEquals("ads", result.getMetadata().get("artifact"));
        assertEquals("test-001", result.getMetadata().get("requestId"));
        assertEquals("DETERMINISTIC", result.getMetadata().get("executionMode"));
    }

    @Test
    void testGenerateAds_WithFullContext_HigherQAScore() {
        // Given - complete context with budget and duration
        MarketingContext context = MarketingContext.builder()
                .requestId("test-002")
                .product("Marketing Automation Tool")
                .audience("Marketing Managers")
                .brandVoice("Innovative and Data-Driven")
                .goals("Increase ROI by 50%")
                .language("en-US")
                .budget("10000")
                .duration("6 months")
                .platforms(List.of("google", "meta", "linkedin"))
                .build();

        // When
        AdsResult result = orchestratorService.generateAds(context);

        // Then
        assertTrue(result.getQaScore() > 70, "QA score should be higher with complete context");
        assertThat(result.getRecommendations()).contains("Complete context for paid media execution");
    }

    @Test
    void testGenerateSeoPlan_HappyPath_ReturnsCompleteSEOPlan() {
        // Given
        MarketingContext context = MarketingContext.builder()
                .requestId("test-003")
                .product("E-commerce Platform")
                .audience("Online Retailers")
                .brandVoice("Trustworthy and Efficient")
                .goals("Increase organic traffic by 200%")
                .language("en-US")
                .domain("ecommerce-platform.com")
                .monthlyBudget(5000)
                .build();

        // When
        SeoPlanResult result = orchestratorService.generateSeoPlan(context);

        // Then
        assertNotNull(result, "SEO plan result should not be null");

        // Verify Keywords
        assertNotNull(result.getPrimaryKeywords(), "Primary keywords should be present");
        assertFalse(result.getPrimaryKeywords().isEmpty(), "Primary keywords should not be empty");
        assertNotNull(result.getSecondaryKeywords(), "Secondary keywords should be present");

        // Verify Content Strategy
        assertNotNull(result.getContentStrategy(), "Content strategy should be present");
        assertNotNull(result.getContentStrategy().getBlogTopics());
        assertFalse(result.getContentStrategy().getBlogTopics().isEmpty());
        assertTrue(result.getContentStrategy().getMonthlyArticles() > 0);

        // Verify On-Page Optimization
        assertNotNull(result.getOnPageOptimization(), "On-page optimization should be present");
        assertThat(result.getOnPageOptimization().getTitleTemplate()).contains("E-commerce Platform");

        // Verify Off-Page Optimization
        assertNotNull(result.getOffPageOptimization(), "Off-page optimization should be present");
        assertNotNull(result.getOffPageOptimization().getBacklinkStrategy());

        // Verify Technical SEO
        assertNotNull(result.getTechnicalSeo(), "Technical SEO should be present");
        assertFalse(result.getTechnicalSeo().isEmpty());

        // Verify QA Score
        assertTrue(result.getQaScore() >= 0 && result.getQaScore() <= 100);

        // Verify Recommendations
        assertNotNull(result.getRecommendations());

        // Verify Metadata
        assertEquals("seo-plan", result.getMetadata().get("artifact"));
    }

    @Test
    void testGenerateCrmSequences_HappyPath_ReturnsCompleteSequence() {
        // Given
        MarketingContext context = MarketingContext.builder()
                .requestId("test-004")
                .product("SaaS Analytics Tool")
                .audience("Data Analysts")
                .brandVoice("Technical and Insightful")
                .goals("Convert free trial users to paid")
                .language("en-US")
                .sequenceLength(4)
                .conversionGoal("20% trial-to-paid conversion")
                .channels(List.of("email", "in-app"))
                .build();

        // When
        CrmSequencesResult result = orchestratorService.generateCrmSequences(context);

        // Then
        assertNotNull(result, "CRM sequences result should not be null");

        // Verify Sequence Name
        assertNotNull(result.getSequenceName(), "Sequence name should be present");
        assertThat(result.getSequenceName()).contains("Flow");

        // Verify Email Steps
        assertNotNull(result.getEmails(), "Email steps should be present");
        assertEquals(4, result.getEmails().size(), "Should have 4 email steps");

        // Verify each email step has required fields
        for (CrmSequencesResult.EmailStep email : result.getEmails()) {
            assertNotNull(email.getDayNumber(), "Day number should be present");
            assertNotNull(email.getSubject(), "Subject should be present");
            assertNotNull(email.getPreviewText(), "Preview text should be present");
            assertNotNull(email.getBody(), "Body should be present");
            assertNotNull(email.getCallToAction(), "Call to action should be present");
            assertNotNull(email.getGoal(), "Goal should be present");
        }

        // Verify Timing Strategy
        assertNotNull(result.getTiming(), "Timing strategy should be present");
        assertEquals(12, result.getTiming().getTotalDays(), "Total days should be 12 (4 emails * 3 days)");
        assertEquals(4, result.getTiming().getNumberOfTouchpoints());
        assertNotNull(result.getTiming().getCadence());
        assertNotNull(result.getTiming().getBestSendTimes());

        // Verify Success Metrics
        assertNotNull(result.getSuccessMetrics(), "Success metrics should be present");
        assertNotNull(result.getSuccessMetrics().getOpenRateTarget());
        assertNotNull(result.getSuccessMetrics().getClickRateTarget());
        assertNotNull(result.getSuccessMetrics().getConversionRateTarget());
        assertThat(result.getSuccessMetrics().getConversionRateTarget()).contains("20%");

        // Verify QA Score
        assertTrue(result.getQaScore() >= 0 && result.getQaScore() <= 100);

        // Verify Recommendations
        assertNotNull(result.getRecommendations());

        // Verify Metadata
        assertEquals("crm-sequences", result.getMetadata().get("artifact"));
    }

    @Test
    void testGenerateStrategy_HappyPath_AggregatesAllResults() {
        // Given
        MarketingContext context = MarketingContext.builder()
                .requestId("test-005")
                .product("Project Management Software")
                .audience("Team Leads and Project Managers")
                .brandVoice("Collaborative and Organized")
                .goals("Acquire 1000 new customers")
                .language("en-US")
                .marketSegment("Tech Startups")
                .timeframe("Q1 2026")
                .budget("50000")
                .monthlyBudget(10000)
                .conversionGoal("5% visitor-to-customer")
                .build();

        // When
        StrategyResult result = orchestratorService.generateStrategy(context);

        // Then
        assertNotNull(result, "Strategy result should not be null");

        // Verify Executive Summary
        assertNotNull(result.getExecutiveSummary(), "Executive summary should be present");
        assertThat(result.getExecutiveSummary()).contains("Project Management Software");

        // Verify All Sub-Strategies are present
        assertNotNull(result.getAdsStrategy(), "Ads strategy should be present");
        assertNotNull(result.getSeoStrategy(), "SEO strategy should be present");
        assertNotNull(result.getCrmStrategy(), "CRM strategy should be present");

        // Verify QA Score (average of all strategies)
        assertNotNull(result.getQaScore());
        assertTrue(result.getQaScore() >= 0 && result.getQaScore() <= 100);

        // Verify Recommendations are aggregated
        assertNotNull(result.getRecommendations(), "Recommendations should be present");
        assertThat(result.getRecommendations()).contains("Keep a consistent tone of voice across every channel");

        // Verify Budget Allocation
        assertNotNull(result.getBudgetAllocation(), "Budget allocation should be present");
        assertTrue(result.getBudgetAllocation().containsKey("paid_media"));
        assertTrue(result.getBudgetAllocation().containsKey("seo"));
        assertTrue(result.getBudgetAllocation().containsKey("crm"));

        // Verify Timeline
        assertNotNull(result.getTimeline(), "Timeline should be present");
        assertTrue(result.getTimeline().containsKey("phase_1"));
        assertTrue(result.getTimeline().containsKey("phase_2"));
        assertTrue(result.getTimeline().containsKey("phase_3"));

        // Verify KPIs
        assertNotNull(result.getKpis(), "KPIs should be present");
        assertTrue(result.getKpis().containsKey("lead_generation"));
        assertTrue(result.getKpis().containsKey("conversion_rate"));
        assertTrue(result.getKpis().containsKey("brand_reach"));

        // Verify Metadata
        assertNotNull(result.getMetadata());
        assertEquals("strategy", result.getMetadata().get("artifact"));
        assertEquals("DETERMINISTIC", result.getMetadata().get("executionMode"));
    }

    @Test
    void testGenerateAds_InvalidContext_ThrowsException() {
        // Given - missing required field (audience)
        MarketingContext context = MarketingContext.builder()
                .requestId("test-006")
                .product("Test Product")
                .brandVoice("Professional")
                .goals("Test Goals")
                .language("en-US")
                // Missing audience
                .build();

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            orchestratorService.generateAds(context);
        }, "Should throw exception for invalid context");
    }

    @Test
    void testQAScore_IsDeterministic() {
        // Given - same context
        MarketingContext context = MarketingContext.builder()
                .requestId("test-007")
                .product("Test Product")
                .audience("Test Audience")
                .brandVoice("Test Voice")
                .goals("Test Goals")
                .language("en-US")
                .budget("5000")
                .build();

        // When - generate multiple times
        AdsResult result1 = orchestratorService.generateAds(context);
        AdsResult result2 = orchestratorService.generateAds(context);

        // Then - should produce same QA score
        assertEquals(result1.getQaScore(), result2.getQaScore(),
                "QA score should be deterministic for same context");
    }

    @Test
    void testGenerateAds_WithCustomKeywords_UsesProvidedKeywords() {
        // Given
        List<String> customKeywords = List.of("crm software", "customer management", "sales automation");
        MarketingContext context = MarketingContext.builder()
                .requestId("test-008")
                .product("CRM Tool")
                .audience("Sales Teams")
                .brandVoice("Professional")
                .goals("Increase sales")
                .language("en-US")
                .keywords(customKeywords)
                .build();

        // When
        AdsResult result = orchestratorService.generateAds(context);

        // Then
        assertEquals(customKeywords, result.getGoogleAds().getKeywords(),
                "Should use provided keywords instead of generated ones");
    }

    @Test
    void testGenerateCrmSequences_DefaultLength_Creates3Emails() {
        // Given - no sequence length specified
        MarketingContext context = MarketingContext.builder()
                .requestId("test-009")
                .product("Test Product")
                .audience("Test Audience")
                .brandVoice("Test Voice")
                .goals("Test Goals")
                .language("en-US")
                .build();

        // When
        CrmSequencesResult result = orchestratorService.generateCrmSequences(context);

        // Then
        assertEquals(3, result.getEmails().size(),
                "Should create 3 emails by default");
        assertEquals(9, result.getTiming().getTotalDays(),
                "Total days should be 9 (3 emails * 3 days)");
    }

    @Test
    void testGenerateSeoPlan_WithDomain_UsesProvidedDomain() {
        // Given
        MarketingContext context = MarketingContext.builder()
                .requestId("test-010")
                .product("Test Product")
                .audience("Test Audience")
                .brandVoice("Test Voice")
                .goals("Test Goals")
                .language("en-US")
                .domain("example.com")
                .build();

        // When
        SeoPlanResult result = orchestratorService.generateSeoPlan(context);

        // Then
        assertThat(result.getRecommendations())
                .noneMatch(r -> r.contains("Provide the domain"));
    }
}
