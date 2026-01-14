package com.mcp.marketing.tool;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcp.marketing.config.MarketingProperties;
import com.mcp.marketing.observability.ObservabilityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Tool for generating CRM email sequences
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CrmSequenceTool {

    private final MarketingProperties properties;
    private final ObservabilityService observability;
    private final ObjectMapper objectMapper;

    public Map<String, Object> generateCrmSequence(
        String product,
        String audience,
        String brandVoice,
        List<String> goals
    ) {
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
        List<String> goals
    ) {
        List<Map<String, Object>> emails = new ArrayList<>();

        // Email 1: Welcome
        emails.add(createEmail(
            1,
            "Welcome to " + product,
            String.format("Hi {First Name},\n\nWelcome to %s! We're excited to have you join our community of %s.\n\n" +
                "Here's what you can expect:\n" +
                "• Exclusive insights and tips\n" +
                "• Early access to new features\n" +
                "• Dedicated support from our team\n\n" +
                "Let's get started on your journey to %s!\n\nBest regards,\nThe %s Team",
                product, audience, goals.get(0), product),
            "Get Started",
            "https://example.com/getting-started"
        ));

        // Email 2: Education
        emails.add(createEmail(
            2,
            "How " + product + " Helps " + audience,
            String.format("Hi {First Name},\n\nWe wanted to share how other %s are using %s to achieve amazing results.\n\n" +
                "Case Study: Company X increased their %s by 150%% in just 3 months.\n\n" +
                "Key strategies they used:\n" +
                "1. Strategy A\n" +
                "2. Strategy B\n" +
                "3. Strategy C\n\n" +
                "Want to replicate their success? Let's talk!\n\nBest regards,\nThe %s Team",
                audience, product, goals.get(0), product),
            "Learn More",
            "https://example.com/case-studies"
        ));

        // Email 3: Social Proof
        emails.add(createEmail(
            3,
            "Join 10,000+ " + audience + " Using " + product,
            String.format("Hi {First Name},\n\nDid you know that over 10,000 %s trust %s?\n\n" +
                "Here's what they're saying:\n" +
                "⭐⭐⭐⭐⭐ \"Game-changer for our team!\" - Sarah J.\n" +
                "⭐⭐⭐⭐⭐ \"Best investment we've made\" - Mike T.\n" +
                "⭐⭐⭐⭐⭐ \"Results exceeded expectations\" - Lisa K.\n\n" +
                "Ready to experience the difference?\n\nBest regards,\nThe %s Team",
                audience, product, product),
            "Start Free Trial",
            "https://example.com/trial"
        ));

        // Email 4: Offer
        emails.add(createEmail(
            4,
            "Special Offer: 20%% Off for " + audience,
            String.format("Hi {First Name},\n\nAs a valued member of our %s community, we have a special offer just for you.\n\n" +
                "Get 20%% off your first 3 months when you upgrade today!\n\n" +
                "This exclusive offer includes:\n" +
                "✓ Full access to all features\n" +
                "✓ Priority support\n" +
                "✓ Custom onboarding session\n" +
                "✓ 30-day money-back guarantee\n\n" +
                "Offer expires in 48 hours. Don't miss out!\n\nBest regards,\nThe %s Team",
                audience, product),
            "Claim Offer",
            "https://example.com/special-offer"
        ));

        // Email 5: Re-engagement
        emails.add(createEmail(
            5,
            "We Miss You! Here's What You've Been Missing",
            String.format("Hi {First Name},\n\nWe noticed you haven't been active lately, and we wanted to check in.\n\n" +
                "Here's what's new at %s:\n" +
                "• New feature X launched\n" +
                "• Updated %s resources\n" +
                "• Community events and webinars\n\n" +
                "We'd love to help you achieve your goals of %s.\n\n" +
                "Can we schedule a quick call to discuss your needs?\n\nBest regards,\nThe %s Team",
                product, audience, String.join(", ", goals), product),
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
        String ctaLink
    ) {
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

    private String saveToFile(Map<String, Object> data, String type) {
        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String requestId = observability.getCurrentRequestId();
            String filename = String.format("%s_%s_%s.json", type, requestId, timestamp);

            Path outputDir = Paths.get(properties.getOutputDirectory());
            Files.createDirectories(outputDir);

            Path filePath = outputDir.resolve(filename);
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(filePath.toFile(), data);

            log.info("Saved {} output to: {}", type, filePath);
            return filePath.toString();
        } catch (IOException e) {
            log.error("Failed to save output file", e);
            return null;
        }
    }
}

