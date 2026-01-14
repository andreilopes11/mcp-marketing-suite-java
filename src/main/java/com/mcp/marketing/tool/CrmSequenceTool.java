package com.mcp.marketing.tool;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcp.marketing.config.MarketingProperties;
import com.mcp.marketing.observability.ObservabilityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Tool for generating CRM email sequences
 * Optimized: extends BaseMarketingTool to eliminate code duplication
 */
@Slf4j
@Component
public class CrmSequenceTool extends BaseMarketingTool {

    public CrmSequenceTool(
            MarketingProperties properties,
            ObservabilityService observability,
            ObjectMapper objectMapper) {
        super(properties, observability, objectMapper);
    }

    public Map<String, Object> generateCrmSequence(
            String product,
            String audience,
            String brandVoice,
            List<String> goals) {

        return observability.traceOperation("generate_crm_sequence", () -> {
            Map<String, Object> result = new HashMap<>();

            result.put("sequence_name", product + " Nurture Campaign");
            result.put("target_audience", audience);
            result.put("emails", generateEmailSequence(product, audience, brandVoice, goals));
            result.put("timing", generateTiming());
            result.put("success_metrics", generateSuccessMetrics());

            // Save to file
            String filePath = saveToFile(result, "crm_sequence");
            result.put("output_file", filePath);

            observability.logMetric("crm_emails_generated", 5);
            return result;
        });
    }

    private List<Map<String, Object>> generateEmailSequence(
            String product,
            String audience,
            String brandVoice,
            List<String> goals) {

        List<Map<String, Object>> emails = new ArrayList<>();

        // Adapt greeting and tone based on brand voice
        String greeting = getGreetingForBrandVoice(brandVoice);
        String closingTone = getClosingForBrandVoice(brandVoice);

        // Email 1: Welcome
        emails.add(createEmail(
                1,
                "Welcome to " + product,
                String.format("%s {First Name},\n\nWelcome to %s! We're excited to have you join our community of %s.\n\n" +
                                "Here's what you can expect:\n" +
                                "• Exclusive insights and tips\n" +
                                "• Early access to new features\n" +
                                "• Dedicated support from our team\n\n" +
                                "Let's get started on your journey to %s!\n\n%s,\nThe %s Team",
                        greeting, product, audience, goals.get(0), closingTone, product),
                "Get Started",
                "https://example.com/getting-started"
        ));

        // Email 2: Education
        emails.add(createEmail(
                2,
                "How " + product + " Helps " + audience,
                String.format("%s {First Name},\n\nWe wanted to share how other %s are using %s to achieve amazing results.\n\n" +
                                "Case Study: Company X increased their %s by 150%% in just 3 months.\n\n" +
                                "Key strategies they used:\n" +
                                "1. Strategy A\n" +
                                "2. Strategy B\n" +
                                "3. Strategy C\n\n" +
                                "Want to replicate their success? Let's talk!\n\n%s,\nThe %s Team",
                        greeting, audience, product, goals.get(0), closingTone, product),
                "Learn More",
                "https://example.com/case-studies"
        ));

        // Email 3: Social Proof
        emails.add(createEmail(
                3,
                "Join 10,000+ " + audience + " Using " + product,
                String.format("%s {First Name},\n\nDid you know that over 10,000 %s trust %s?\n\n" +
                                "Here's what they're saying:\n" +
                                "⭐⭐⭐⭐⭐ \"Game-changer for our team!\" - Sarah J.\n" +
                                "⭐⭐⭐⭐⭐ \"Best investment we've made\" - Mike T.\n" +
                                "⭐⭐⭐⭐⭐ \"Results exceeded expectations\" - Lisa K.\n\n" +
                                "Ready to experience the difference?\n\n%s,\nThe %s Team",
                        greeting, audience, product, closingTone, product),
                "Start Free Trial",
                "https://example.com/trial"
        ));

        // Email 4: Offer
        emails.add(createEmail(
                4,
                "Special Offer: 20%% Off for " + audience,
                String.format("%s {First Name},\n\nAs a valued member of our %s community, we have a special offer just for you.\n\n" +
                                "Get 20%% off your first 3 months when you upgrade today!\n\n" +
                                "This exclusive offer includes:\n" +
                                "✓ Full access to all features\n" +
                                "✓ Priority support\n" +
                                "✓ Custom onboarding session\n" +
                                "✓ 30-day money-back guarantee\n\n" +
                                "Offer expires in 48 hours. Don't miss out!\n\n%s,\nThe %s Team",
                        greeting, audience, closingTone, product),
                "Claim Offer",
                "https://example.com/special-offer"
        ));

        // Email 5: Re-engagement
        emails.add(createEmail(
                5,
                "We Miss You! Here's What You've Been Missing",
                String.format("%s {First Name},\n\nWe noticed you haven't been active lately, and we wanted to check in.\n\n" +
                                "Here's what's new at %s:\n" +
                                "• New feature X launched\n" +
                                "• Updated %s resources\n" +
                                "• Community events and webinars\n\n" +
                                "We'd love to help you achieve your goals of %s.\n\n" +
                                "Can we schedule a quick call to discuss your needs?\n\n%s,\nThe %s Team",
                        greeting, product, audience, String.join(", ", goals), closingTone, product),
                "Reconnect",
                "https://example.com/schedule-call"
        ));

        return emails;
    }

    private Map<String, Object> createEmail(
            int sequence,
            String subject,
            String body,
            String ctaText,
            String ctaLink) {

        Map<String, Object> email = new HashMap<>();
        email.put("sequence_number", sequence);
        email.put("subject_line", subject);
        email.put("body", body);
        email.put("cta_text", ctaText);
        email.put("cta_link", ctaLink);
        email.put("personalization_tokens", Arrays.asList("First Name", "Company", "Industry"));
        return email;
    }

    private Map<String, Object> generateTiming() {
        Map<String, Object> timing = new HashMap<>();
        timing.put("email_1", "Immediate (upon signup)");
        timing.put("email_2", "Day 2");
        timing.put("email_3", "Day 5");
        timing.put("email_4", "Day 7");
        timing.put("email_5", "Day 14 (if inactive)");
        return timing;
    }

    private Map<String, Object> generateSuccessMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("target_open_rate", "25-30%");
        metrics.put("target_click_rate", "3-5%");
        metrics.put("target_conversion_rate", "2-3%");
        metrics.put("unsubscribe_threshold", "<0.5%");
        return metrics;
    }

    private String getGreetingForBrandVoice(String brandVoice) {
        if (brandVoice == null || brandVoice.isEmpty()) {
            return "Hi";
        }

        String voice = brandVoice.toLowerCase();
        if (voice.contains("formal") || voice.contains("professional")) {
            return "Hello";
        } else if (voice.contains("casual") || voice.contains("friendly")) {
            return "Hey";
        }
        return "Hi";
    }

    private String getClosingForBrandVoice(String brandVoice) {
        if (brandVoice == null || brandVoice.isEmpty()) {
            return "Best regards";
        }

        String voice = brandVoice.toLowerCase();
        if (voice.contains("formal")) {
            return "Sincerely";
        } else if (voice.contains("casual") || voice.contains("friendly")) {
            return "Cheers";
        } else if (voice.contains("professional")) {
            return "Best regards";
        }
        return "Best regards";
    }
}
