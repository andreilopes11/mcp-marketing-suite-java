package com.mcp.marketing.resource.loader;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * File-based resource loader for reading marketing data from files
 * Supports JSON, CSV, and TXT formats
 */
@Slf4j
@Component
public class FileResourceLoader {

    private static final String RESOURCES_PATH = "src/main/resources/marketing-data";

    /**
     * Load content from a file
     */
    public String loadFile(String fileName) {
        try {
            Path filePath = Paths.get(RESOURCES_PATH, fileName);
            if (!Files.exists(filePath)) {
                log.warn("File not found: {}, using fallback", fileName);
                return null;
            }
            return Files.readString(filePath);
        } catch (IOException e) {
            log.error("Error loading file: {}", fileName, e);
            return null;
        }
    }

    /**
     * Load multiple files from a directory
     */
    public List<String> loadDirectory(String dirName) {
        try {
            Path dirPath = Paths.get(RESOURCES_PATH, dirName);
            if (!Files.exists(dirPath) || !Files.isDirectory(dirPath)) {
                log.warn("Directory not found: {}", dirName);
                return List.of();
            }

            try (Stream<Path> paths = Files.walk(dirPath, 1)) {
                return paths
                        .filter(Files::isRegularFile)
                        .map(path -> {
                            try {
                                return Files.readString(path);
                            } catch (IOException e) {
                                log.error("Error reading file: {}", path, e);
                                return null;
                            }
                        })
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
            }
        } catch (IOException e) {
            log.error("Error loading directory: {}", dirName, e);
            return List.of();
        }
    }

    /**
     * Check if file exists
     */
    public boolean fileExists(String fileName) {
        Path filePath = Paths.get(RESOURCES_PATH, fileName);
        return Files.exists(filePath);
    }

    /**
     * List files in directory
     */
    public List<String> listFiles(String dirName) {
        try {
            Path dirPath = Paths.get(RESOURCES_PATH, dirName);
            if (!Files.exists(dirPath) || !Files.isDirectory(dirPath)) {
                return List.of();
            }

            try (Stream<Path> paths = Files.walk(dirPath, 1)) {
                return paths
                        .filter(Files::isRegularFile)
                        .map(path -> path.getFileName().toString())
                        .collect(Collectors.toList());
            }
        } catch (IOException e) {
            log.error("Error listing files in directory: {}", dirName, e);
            return List.of();
        }
    }
}

