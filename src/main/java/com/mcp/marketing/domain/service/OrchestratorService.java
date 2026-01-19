package com.mcp.marketing.domain.service;

import com.mcp.marketing.domain.model.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Deterministic orchestrator responsible for building marketing payloads.
 */
@Service
public class OrchestratorService {

    private static final List<String> DEFAULT_PLATFORMS = List.of("google", "meta", "linkedin");

    private final ValidationService validationService;

    public OrchestratorService(ValidationService validationService) {
        this.validationService = validationService;
    }

    public AdsResult generateAds(MarketingContext context) {
        ensureValidContext(context);
        return buildAds(context);
    }

    public SeoPlanResult generateSeoPlan(MarketingContext context) {
        ensureValidContext(context);
        return buildSeoPlan(context);
    }

    public CrmSequencesResult generateCrmSequences(MarketingContext context) {
        ensureValidContext(context);
        return buildCrmSequences(context);
    }

    public StrategyResult generateStrategy(MarketingContext context) {
        ensureValidContext(context);

        AdsResult adsResult = buildAds(context);
        SeoPlanResult seoPlanResult = buildSeoPlan(context);
        CrmSequencesResult crmSequencesResult = buildCrmSequences(context);

        int qaScore = (adsResult.getQaScore() + seoPlanResult.getQaScore() + crmSequencesResult.getQaScore()) / 3;

        List<String> combinedRecommendations = new ArrayList<>();
        combinedRecommendations.add("Keep a consistent tone of voice across every channel");
        combinedRecommendations.addAll(adsResult.getRecommendations());
        combinedRecommendations.addAll(seoPlanResult.getRecommendations());
        combinedRecommendations.addAll(crmSequencesResult.getRecommendations());
        List<String> uniqueRecommendations = combinedRecommendations.stream()
                .distinct()
                .collect(Collectors.toList());

        Map<String, String> budgetAllocation = new LinkedHashMap<>();
        budgetAllocation.put("paid_media", hasText(context.getBudget())
                ? context.getBudget()
                : "Provide the estimated paid media budget");
        budgetAllocation.put("seo", context.getMonthlyBudget() != null
                ? context.getMonthlyBudget() + " focused on evergreen content"
                : "Reserve a monthly SEO budget");
        budgetAllocation.put("crm", hasText(context.getConversionGoal())
                ? "Allocate time to nurture until " + context.getConversionGoal()
                : "Define a conversion goal for follow-up workflows");

        Map<String, String> timeline = new LinkedHashMap<>();
        String timeframe = hasText(context.getTimeframe()) ? context.getTimeframe() : "Quarterly cycle";
        timeline.put("phase_1", "Discovery and alignment (" + timeframe + ")");
        timeline.put("phase_2", "Integrated media, content, and CRM execution");
        timeline.put("phase_3", "Optimization driven by metrics");

        Map<String, String> kpis = new LinkedHashMap<>();
        kpis.put("lead_generation", "+25% qualified leads");
        kpis.put("conversion_rate", hasText(context.getConversionGoal())
                ? "Reach " + context.getConversionGoal()
                : "Define a clear conversion target");
        kpis.put("brand_reach", "Grow presence in " + safeString(context.getMarketSegment(), "priority segments"));

        Map<String, Object> metadata = Map.of(
                "artifact", "strategy",
                "requestId", safeString(context.getRequestId(), "n/a"),
                "executionMode", context.getExecutionMode().name());

        return StrategyResult.builder()
                .executiveSummary(buildExecutiveSummary(context))
                .adsStrategy(adsResult)
                .seoStrategy(seoPlanResult)
                .crmStrategy(crmSequencesResult)
                .qaScore(qaScore)
                .recommendations(uniqueRecommendations)
                .budgetAllocation(budgetAllocation)
                .timeline(timeline)
                .kpis(kpis)
                .metadata(metadata)
                .build();
    }

    private String buildExecutiveSummary(MarketingContext context) {
        return "Integrated plan for " + context.getProduct()
                + " to achieve " + context.getGoals()
                + " with " + context.getAudience();
    }

    private AdsResult buildAds(MarketingContext context) {
        List<String> keywordSource = defaultKeywords(context);
        List<String> activePlatforms = context.getPlatforms() == null || context.getPlatforms().isEmpty()
                ? DEFAULT_PLATFORMS
                : context.getPlatforms();

        AdsResult.GoogleAd googleAd = AdsResult.GoogleAd.builder()
                .headline1(context.getProduct() + " for " + context.getAudience())
                .headline2("Goal: " + context.getGoals())
                .headline3("Tone: " + context.getBrandVoice())
                .description1("Explain how " + context.getProduct() + " solves the pains of " + context.getAudience())
                .description2("Direct and trustworthy call-to-action")
                .displayUrl(buildDisplayUrl(context))
                .keywords(keywordSource)
                .build();

        AdsResult.MetaAd metaAd = AdsResult.MetaAd.builder()
                .primaryText("Show how " + context.getProduct() + " transforms " + context.getAudience())
                .headline(context.getProduct() + " focused on " + context.getGoals())
                .description("Reinforce tangible benefits in " + context.getLanguage())
                .callToAction("Learn more")
                .targeting(List.of(
                        "Interests: " + safeString(context.getMarketSegment(), "core segment"),
                        "Behaviors aligned with " + context.getGoals()))
                .build();

        AdsResult.LinkedInAd linkedInAd = AdsResult.LinkedInAd.builder()
                .introText("Connect with decision makers pursuing " + context.getGoals())
                .headline(context.getProduct() + " for " + context.getAudience())
                .description("Highlight proof points and concrete metrics")
                .callToAction("Talk to our team")
                .targetAudience(List.of(
                        safeString(context.getAudience(), "Professionals"),
                        "Segment: " + safeString(context.getMarketSegment(), "General")))
                .build();

        int qaScore = 70 + qualityBonus(
                hasText(context.getBudget()),
                hasText(context.getDuration()),
                !activePlatforms.isEmpty());

        List<String> recommendations = new ArrayList<>();
        if (!hasText(context.getBudget())) {
            recommendations.add("Provide the expected budget to calibrate paid media");
        }
        if (!hasText(context.getDuration())) {
            recommendations.add("Specify the campaign duration for better pacing");
        }
        if (activePlatforms.size() < DEFAULT_PLATFORMS.size()) {
            recommendations.add("Consider activating every channel for maximum coverage");
        }
        if (recommendations.isEmpty()) {
            recommendations.add("Complete context for paid media execution");
        }

        Map<String, Object> metadata = Map.of(
                "artifact", "ads",
                "requestId", safeString(context.getRequestId(), "n/a"),
                "executionMode", context.getExecutionMode().name(),
                "platforms", new ArrayList<>(activePlatforms));

        return AdsResult.builder()
                .googleAds(googleAd)
                .metaAds(metaAd)
                .linkedinAds(linkedInAd)
                .qaScore(Math.min(qaScore, 95))
                .recommendations(recommendations)
                .metadata(metadata)
                .build();
    }

    private SeoPlanResult buildSeoPlan(MarketingContext context) {
        List<String> primaryKeywords = defaultKeywords(context);
        List<String> secondaryKeywords = primaryKeywords.stream()
                .map(keyword -> keyword + " tips")
                .collect(Collectors.toList());

        SeoPlanResult.ContentStrategy contentStrategy = SeoPlanResult.ContentStrategy.builder()
                .blogTopics(List.of(
                        "How " + context.getProduct() + " helps " + context.getAudience(),
                        "Checklist to reach " + context.getGoals()))
                .pillarPages(List.of(
                        context.getProduct() + " for " + safeString(context.getMarketSegment(), "the market")))
                .contentCalendar(hasText(context.getTimeframe()) ? context.getTimeframe() : "Monthly sprint")
                .monthlyArticles(context.getMonthlyBudget() != null && context.getMonthlyBudget() > 0 ? 4 : 2)
                .build();

        SeoPlanResult.OnPageOptimization onPageOptimization = SeoPlanResult.OnPageOptimization.builder()
                .titleTemplate(context.getProduct() + " | " + context.getGoals())
                .metaDescriptionTemplate("Discover how " + context.getProduct() + " serves " + context.getAudience())
                .headerStructure(List.of("H1: " + context.getProduct(), "H2: Problem", "H2: Solution"))
                .internalLinking(List.of("Link to case studies", "Link to contact"))
                .build();

        SeoPlanResult.OffPageOptimization offPageOptimization = SeoPlanResult.OffPageOptimization.builder()
                .backlinkStrategy(List.of("Guest posts on industry portals", "Partnerships with B2B influencers"))
                .socialMediaPlan(List.of("Weekly clips highlighting " + context.getGoals()))
                .prOpportunities(List.of("Launch proprietary research"))
                .build();

        int qaScore = 68 + qualityBonus(
                hasText(context.getDomain()),
                !primaryKeywords.isEmpty(),
                context.getMonthlyBudget() != null && context.getMonthlyBudget() > 0);

        List<String> recommendations = new ArrayList<>();
        if (!hasText(context.getDomain())) {
            recommendations.add("Provide the domain to prioritize pages");
        }
        if (primaryKeywords.isEmpty()) {
            recommendations.add("Define at least three primary keywords");
        }
        if (context.getMonthlyBudget() == null || context.getMonthlyBudget() <= 0) {
            recommendations.add("Allocate a monthly budget for content production");
        }
        if (recommendations.isEmpty()) {
            recommendations.add("SEO plan is complete and ready to execute");
        }

        Map<String, Object> metadata = Map.of(
                "artifact", "seo-plan",
                "requestId", safeString(context.getRequestId(), "n/a"),
                "executionMode", context.getExecutionMode().name());

        return SeoPlanResult.builder()
                .primaryKeywords(primaryKeywords)
                .secondaryKeywords(secondaryKeywords)
                .contentStrategy(contentStrategy)
                .onPageOptimization(onPageOptimization)
                .offPageOptimization(offPageOptimization)
                .technicalSeo(List.of(
                        "Run performance audit",
                        "Implement schema for " + context.getProduct(),
                        "Monitor Core Web Vitals"))
                .qaScore(Math.min(qaScore, 95))
                .recommendations(recommendations)
                .metadata(metadata)
                .build();
    }

    private CrmSequencesResult buildCrmSequences(MarketingContext context) {
        int steps = context.getSequenceLength() != null && context.getSequenceLength() > 0
                ? Math.min(context.getSequenceLength(), 5)
                : 3;

        List<String> stages = List.of("Discovery", "Diagnosis", "Proof", "Offer", "Follow-up");
        List<CrmSequencesResult.EmailStep> emails = new ArrayList<>();
        for (int i = 0; i < steps; i++) {
            String stage = stages.get(i);
            emails.add(CrmSequencesResult.EmailStep.builder()
                    .dayNumber((i * 3) + 1)
                    .subject(stage + ": " + context.getProduct())
                    .previewText("Connect " + context.getAudience() + " with the " + stage.toLowerCase() + " stage")
                    .body("Explain " + context.getProduct() + " and tie it to the goal " + context.getGoals())
                    .callToAction("Reply to keep moving")
                    .goal(stage)
                    .build());
        }

        CrmSequencesResult.TimingStrategy timing = CrmSequencesResult.TimingStrategy.builder()
                .totalDays(steps * 3)
                .numberOfTouchpoints(steps)
                .cadence("Every 3 days")
                .bestSendTimes(List.of("09:00", "14:00"))
                .build();

        CrmSequencesResult.SuccessMetrics successMetrics = CrmSequencesResult.SuccessMetrics.builder()
                .openRateTarget("35%")
                .clickRateTarget("12%")
                .conversionRateTarget(hasText(context.getConversionGoal())
                        ? context.getConversionGoal()
                        : "Set a conversion target")
                .trackingPoints(List.of("Reply rate", "Meeting quality"))
                .build();

        int qaScore = 69 + qualityBonus(
                hasText(context.getConversionGoal()),
                context.getChannels() != null && !context.getChannels().isEmpty(),
                context.getSequenceLength() != null && context.getSequenceLength() >= 3);

        List<String> recommendations = new ArrayList<>();
        if (!hasText(context.getConversionGoal())) {
            recommendations.add("Define a conversion goal to measure funnel success");
        }
        if (context.getChannels() == null || context.getChannels().isEmpty()) {
            recommendations.add("Select the lead's preferred channels");
        }
        if (context.getSequenceLength() == null || context.getSequenceLength() < 3) {
            recommendations.add("Keep at least three touchpoints for a full nurture");
        }
        if (recommendations.isEmpty()) {
            recommendations.add("Sequence is complete and aligned with the funnel");
        }

        Map<String, Object> metadata = Map.of(
                "artifact", "crm-sequences",
                "requestId", safeString(context.getRequestId(), "n/a"),
                "executionMode", context.getExecutionMode().name());

        return CrmSequencesResult.builder()
                .sequenceName("Flow " + safeString(context.getGoals(), "Goal"))
                .emails(emails)
                .timing(timing)
                .successMetrics(successMetrics)
                .qaScore(Math.min(qaScore, 95))
                .recommendations(recommendations)
                .metadata(metadata)
                .build();
    }

    private String buildDisplayUrl(MarketingContext context) {
        if (hasText(context.getDomain())) {
            return context.getDomain();
        }
        return context.getProduct().toLowerCase().replaceAll("\\s+", "") + ".com";
    }

    private List<String> defaultKeywords(MarketingContext context) {
        if (context.getKeywords() != null && !context.getKeywords().isEmpty()) {
            return context.getKeywords();
        }
        String base = context.getProduct().toLowerCase().replaceAll("[^a-z0-9]", "-");
        return List.of(base + "-solution", base + "-offer", base + "-case-studies");
    }

    private void ensureValidContext(MarketingContext context) {
        List<String> errors = validationService.validateContext(context);
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException("Invalid context: " + String.join(", ", errors));
        }
    }

    private int qualityBonus(boolean... flags) {
        int bonus = 0;
        for (boolean flag : flags) {
            if (flag) {
                bonus += 8;
            }
        }
        return bonus;
    }

    private String safeString(String value, String fallback) {
        return hasText(value) ? value : fallback;
    }

    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }
}
