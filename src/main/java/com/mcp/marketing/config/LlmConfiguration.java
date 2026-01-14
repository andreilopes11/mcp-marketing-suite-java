package com.mcp.marketing.config;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.output.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * Configuration for AI/LLM integration using Langchain4j
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class LlmConfiguration {

    private final MarketingProperties properties;

    @Bean
    @ConditionalOnProperty(
            prefix = "mcp.marketing",
            name = "enable-ai-agents",
            havingValue = "true",
            matchIfMissing = true
    )
    public ChatLanguageModel chatLanguageModel() {
        String apiKey = properties.getLlm().getApiKey();

        if (apiKey == null || apiKey.isEmpty()) {
            log.warn("OpenAI API key not configured. Using dummy model. AI features will return placeholder responses.");

            // Return a dummy implementation when API key is not configured
            return userMessage -> {
                log.warn("Dummy AI model called. Configure OPENAI_API_KEY for real AI responses.");
                return new Response<>(new AiMessage("AI response placeholder. Configure OpenAI API key to enable AI features."));
            };
        }

        log.info("Initializing OpenAI Chat Model: {}", properties.getLlm().getModel());

        return OpenAiChatModel.builder()
                .apiKey(apiKey)
                .modelName(properties.getLlm().getModel())
                .temperature(properties.getLlm().getTemperature())
                .maxTokens(properties.getLlm().getMaxTokens())
                .timeout(Duration.ofSeconds(60))
                .logRequests(true)
                .logResponses(true)
                .build();
    }
}

