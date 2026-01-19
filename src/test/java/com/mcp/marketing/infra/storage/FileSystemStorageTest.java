package com.mcp.marketing.infra.storage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mcp.marketing.api.dto.StandardResponse;
import com.mcp.marketing.config.AppConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.MockedStatic;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for FileSystemStorage
 * <p>
 * Validates artifact persistence, naming conventions, and file operations
 */
class FileSystemStorageTest {

    @TempDir
    Path tempDir;

    private FileSystemStorage storage;
    private ObjectMapper objectMapper;
    private AppConfiguration appConfig;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        appConfig = new AppConfiguration();
        AppConfiguration.Outputs outputs = new AppConfiguration.Outputs();
        outputs.setDirectory(tempDir.toString());
        outputs.setEnabled(true);
        appConfig.setOutputs(outputs);

        storage = new FileSystemStorage(appConfig, objectMapper);
    }

    @AfterEach
    void tearDown() throws IOException {
        // Clean up test files
        if (Files.exists(tempDir)) {
            Files.walk(tempDir)
                    .filter(Files::isRegularFile)
                    .forEach(path -> {
                        try {
                            Files.deleteIfExists(path);
                        } catch (IOException e) {
                            // Ignore cleanup errors
                        }
                    });
        }
    }

    @Test
    void testSaveJson_CreatesFileInOutputDirectory() {
        // Given
        String artifactType = "ads";
        String requestId = UUID.randomUUID().toString();
        Map<String, Object> data = new HashMap<>();
        data.put("content", "Test ad content");

        StandardResponse<Map<String, Object>> payload = StandardResponse.success(requestId, data);

        // When
        String outputPath = storage.saveJson(artifactType, requestId, payload);

        // Then
        assertNotNull(outputPath, "Output path should not be null");

        File savedFile = new File(outputPath);
        assertTrue(savedFile.exists(), "File should exist at returned path");
        assertTrue(savedFile.isFile(), "Should be a regular file");
        assertTrue(savedFile.length() > 0, "File should have content");
    }

    @Test
    void testSaveJson_FileNameMatchesStandardPattern() {
        // Given
        String artifactType = "seo";
        String requestId = "test-request-123";
        Map<String, Object> data = Map.of("keywords", "test");
        StandardResponse<Map<String, Object>> payload = StandardResponse.success(requestId, data);

        // When
        String outputPath = storage.saveJson(artifactType, requestId, payload);

        // Then
        assertNotNull(outputPath);

        String filename = Paths.get(outputPath).getFileName().toString();

        // Validate pattern: <artifactType>_<requestId>_<yyyyMMdd_HHmmss>.json
        assertTrue(filename.startsWith(artifactType + "_"),
                "Filename should start with artifact type");
        assertTrue(filename.contains(requestId),
                "Filename should contain request ID");
        assertTrue(filename.endsWith(".json"),
                "Filename should end with .json");

        // Validate timestamp format at the end: _yyyyMMdd_HHmmss.json
        // Extract last part after the request ID
        int lastUnderscoreBeforeTimestamp = filename.lastIndexOf('_', filename.length() - 16); // 16 = "_yyyyMMdd_HHmmss.json".length
        String timestampPart = filename.substring(lastUnderscoreBeforeTimestamp + 1, filename.length() - 5); // -5 removes ".json"

        assertTrue(timestampPart.matches("\\d{8}_\\d{6}"),
                "Timestamp should match yyyyMMdd_HHmmss pattern, but was: " + timestampPart);
    }

    @Test
    void testSaveJson_SavesCompleteEnvelope() throws IOException {
        // Given
        String artifactType = "crm";
        String requestId = UUID.randomUUID().toString();
        Map<String, Object> data = Map.of("sequence", "email1,email2,email3");
        StandardResponse<Map<String, Object>> payload = StandardResponse.success(requestId, "Success", data);

        // When
        String outputPath = storage.saveJson(artifactType, requestId, payload);

        // Then
        assertNotNull(outputPath);

        // Read and verify content
        File savedFile = new File(outputPath);
        StandardResponse<?> savedPayload = objectMapper.readValue(savedFile, StandardResponse.class);

        assertNotNull(savedPayload);
        assertEquals(requestId, savedPayload.getRequestId(), "Request ID should match");
        assertEquals(200, savedPayload.getStatus(), "Status should be 200");
        assertTrue(savedPayload.getSuccess(), "Success flag should be true");
        assertEquals("Success", savedPayload.getMessage(), "Message should match");
        assertNotNull(savedPayload.getTimestamp(), "Timestamp should be present");
        assertNotNull(savedPayload.getData(), "Data should be present");
    }

    @Test
    void testSaveJson_MultipleSaves_CreatesSeparateFiles() {
        // Given
        String artifactType = "strategy";
        String requestId1 = "req-1";
        String requestId2 = "req-2";
        Map<String, Object> data1 = Map.of("content", "strategy1");
        Map<String, Object> data2 = Map.of("content", "strategy2");

        // When
        String path1 = storage.saveJson(artifactType, requestId1, StandardResponse.success(requestId1, data1));

        // Sleep briefly to ensure different timestamps
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        String path2 = storage.saveJson(artifactType, requestId2, StandardResponse.success(requestId2, data2));

        // Then
        assertNotNull(path1);
        assertNotNull(path2);
        assertNotEquals(path1, path2, "Two saves should create different files");

        assertTrue(new File(path1).exists(), "First file should exist");
        assertTrue(new File(path2).exists(), "Second file should exist");
    }

    @Test
    void testSaveJson_DifferentArtifactTypes_CreatesDifferentFiles() {
        // Given
        String requestId = UUID.randomUUID().toString();
        Map<String, Object> data = Map.of("test", "data");

        // When
        String adsPath = storage.saveJson("ads", requestId, StandardResponse.success(requestId, data));
        String seoPath = storage.saveJson("seo", requestId, StandardResponse.success(requestId, data));
        String crmPath = storage.saveJson("crm", requestId, StandardResponse.success(requestId, data));
        String strategyPath = storage.saveJson("strategy", requestId, StandardResponse.success(requestId, data));

        // Then
        assertNotNull(adsPath);
        assertNotNull(seoPath);
        assertNotNull(crmPath);
        assertNotNull(strategyPath);

        assertTrue(adsPath.contains("ads_"), "Ads path should contain 'ads_'");
        assertTrue(seoPath.contains("seo_"), "SEO path should contain 'seo_'");
        assertTrue(crmPath.contains("crm_"), "CRM path should contain 'crm_'");
        assertTrue(strategyPath.contains("strategy_"), "Strategy path should contain 'strategy_'");
    }

    @Test
    void testSaveJson_WithDisabledOutputs_ReturnsNull() {
        // Given
        appConfig.getOutputs().setEnabled(false);
        FileSystemStorage disabledStorage = new FileSystemStorage(appConfig, objectMapper);

        String artifactType = "ads";
        String requestId = UUID.randomUUID().toString();
        Map<String, Object> data = Map.of("test", "data");

        // When
        String outputPath = disabledStorage.saveJson(artifactType, requestId, StandardResponse.success(requestId, data));

        // Then
        assertNull(outputPath, "Should return null when outputs are disabled");
    }

    @Test
    void testSaveJson_ReturnedPathIsAbsolute() {
        // Given
        String artifactType = "ads";
        String requestId = UUID.randomUUID().toString();
        Map<String, Object> data = Map.of("test", "data");

        // When
        String outputPath = storage.saveJson(artifactType, requestId, StandardResponse.success(requestId, data));

        // Then
        assertNotNull(outputPath);
        Path path = Paths.get(outputPath);
        assertTrue(path.isAbsolute(), "Returned path should be absolute");
    }

    @Test
    void testSaveJson_SavesPrettyPrintedJson() throws IOException {
        // Given
        String artifactType = "ads";
        String requestId = UUID.randomUUID().toString();
        Map<String, Object> data = Map.of("key", "value");
        StandardResponse<Map<String, Object>> payload = StandardResponse.success(requestId, data);

        // When
        String outputPath = storage.saveJson(artifactType, requestId, payload);

        // Then
        String content = Files.readString(Paths.get(outputPath));

        // Pretty-printed JSON should have newlines and indentation
        assertTrue(content.contains("\n"), "JSON should be pretty-printed with newlines");
        assertTrue(content.contains("  "), "JSON should have indentation");
    }

    @Test
    void testConstructor_WhenDirectoryCreationFails_ThrowsRuntimeException() {
        AppConfiguration failingConfig = new AppConfiguration();
        AppConfiguration.Outputs outputs = new AppConfiguration.Outputs();
        outputs.setDirectory("failing-dir");
        outputs.setEnabled(true);
        failingConfig.setOutputs(outputs);

        try (MockedStatic<Files> filesMock = mockStatic(Files.class)) {
            filesMock.when(() -> Files.exists(any(Path.class))).thenReturn(false);
            filesMock.when(() -> Files.createDirectories(any(Path.class))).thenThrow(new IOException("boom"));

            RuntimeException ex = assertThrows(RuntimeException.class,
                    () -> new FileSystemStorage(failingConfig, objectMapper));
            assertTrue(ex.getMessage().contains("Failed to initialize output directory"));
        }
    }

    @Test
    void testSaveJson_WhenWriteFails_ThrowsRuntimeException() throws IOException {
        ObjectMapper failingMapper = mock(ObjectMapper.class);
        ObjectWriter writer = mock(ObjectWriter.class);
        when(failingMapper.writerWithDefaultPrettyPrinter()).thenReturn(writer);
        doThrow(new IOException("boom")).when(writer).writeValue(any(File.class), any());

        FileSystemStorage failingStorage = new FileSystemStorage(appConfig, failingMapper);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> failingStorage.saveJson("ads", "req", StandardResponse.success("req", Map.of())));
        assertTrue(ex.getMessage().contains("Failed to save artifact"));
    }

    @Test
    void testConstructor_CreatesOutputDirectoryIfNotExists() throws IOException {
        // Given
        Path newTempDir = tempDir.resolve("new_outputs");
        assertFalse(Files.exists(newTempDir), "Directory should not exist yet");

        AppConfiguration newConfig = new AppConfiguration();
        AppConfiguration.Outputs outputs = new AppConfiguration.Outputs();
        outputs.setDirectory(newTempDir.toString());
        outputs.setEnabled(true);
        newConfig.setOutputs(outputs);

        // When
        new FileSystemStorage(newConfig, objectMapper);

        // Then
        assertTrue(Files.exists(newTempDir), "Directory should be created by constructor");
        assertTrue(Files.isDirectory(newTempDir), "Should be a directory");
    }
}
