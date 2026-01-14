package com.mcp.marketing.resource.loader;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * JSON resource loader with caching support
 * Parses JSON files and converts to Maps
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JsonResourceLoader {

    private final FileResourceLoader fileLoader;
    private final ObjectMapper objectMapper;

    /**
     * Load and parse JSON file with caching
     */
    @Cacheable(value = "jsonResources", key = "#fileName")
    public Map<String, Object> loadJsonAsMap(String fileName) {
        try {
            String content = fileLoader.loadFile(fileName);
            if (content == null || content.isEmpty()) {
                log.warn("Empty or null content for file: {}", fileName);
                return Map.of();
            }

            @SuppressWarnings("unchecked")
            Map<String, Object> result = objectMapper.readValue(content, Map.class);
            log.info("Successfully loaded JSON from: {}", fileName);
            return result;
        } catch (Exception e) {
            log.error("Error parsing JSON file: {}", fileName, e);
            return Map.of();
        }
    }

    /**
     * Load and parse JSON file to specific type
     */
    @Cacheable(value = "jsonResources", key = "#fileName + '_' + #clazz.simpleName")
    public <T> T loadJson(String fileName, Class<T> clazz) {
        try {
            String content = fileLoader.loadFile(fileName);
            if (content == null || content.isEmpty()) {
                log.warn("Empty or null content for file: {}", fileName);
                return null;
            }

            T result = objectMapper.readValue(content, clazz);
            log.info("Successfully loaded JSON from: {} as {}", fileName, clazz.getSimpleName());
            return result;
        } catch (Exception e) {
            log.error("Error parsing JSON file: {} to {}", fileName, clazz.getSimpleName(), e);
            return null;
        }
    }
}

