package com.mcp.marketing.handler;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for MCP Server endpoints
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
        "mcp.sdk.enabled=true",
        "mcp.sdk.resources.enabled=true",
        "mcp.sdk.tools.enabled=true"})
class McpServerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String getBaseUrl() {
        return "http://localhost:" + port + "/mcp";
    }

    @Test
    void testServerInfo() {
        ResponseEntity<Map> response = restTemplate.getForEntity(
                getBaseUrl() + "/info",
                Map.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("mcp-marketing-suite-server", response.getBody().get("name"));
        assertEquals("0.1.0", response.getBody().get("version"));
    }

    @Test
    void testListResources() {
        ResponseEntity<Map> response = restTemplate.getForEntity(
                getBaseUrl() + "/resources",
                Map.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().containsKey("resources"));
    }

    @Test
    void testGetProductResource() {
        ResponseEntity<Map> response = restTemplate.getForEntity(
                getBaseUrl() + "/resources/product/saas-product",
                Map.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().containsKey("name"));
    }

    @Test
    void testGetAudienceResource() {
        ResponseEntity<Map> response = restTemplate.getForEntity(
                getBaseUrl() + "/resources/audience/b2b-tech",
                Map.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().containsKey("segment"));
    }

    @Test
    void testListTools() {
        ResponseEntity<Map> response = restTemplate.getForEntity(
                getBaseUrl() + "/tools",
                Map.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().containsKey("tools"));
    }

    @Test
    void testExecuteEchoTool() {
        Map<String, Object> params = new HashMap<>();
        params.put("message", "Hello MCP");

        ResponseEntity<Map> response = restTemplate.postForEntity(
                getBaseUrl() + "/tools/echo",
                params,
                Map.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().containsKey("requestId"));
        assertEquals("success", response.getBody().get("status"));
    }

    @Test
    void testExecuteGenerateMarketingContentTool() {
        Map<String, Object> params = new HashMap<>();
        params.put("product", "SaaS Platform");
        params.put("audience", "B2B Tech Companies");

        ResponseEntity<Map> response = restTemplate.postForEntity(
                getBaseUrl() + "/tools/generateMarketingContent",
                params,
                Map.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().containsKey("requestId"));
        assertEquals("success", response.getBody().get("status"));
    }

    @Test
    void testMcpHealth() {
        ResponseEntity<Map> response = restTemplate.getForEntity(
                getBaseUrl() + "/health",
                Map.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("healthy", response.getBody().get("status"));
    }
}
