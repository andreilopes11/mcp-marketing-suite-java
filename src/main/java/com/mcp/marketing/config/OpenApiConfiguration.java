package com.mcp.marketing.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * OpenAPI/Swagger Configuration
 * <p>
 * Configures API documentation with dynamic information from application.yml
 */
@Configuration
public class OpenApiConfiguration {

    @Value("${info.app.name}")
    private String appName;

    @Value("${info.app.description}")
    private String appDescription;

    @Value("${info.app.version}")
    private String appVersion;

    @Value("${server.port}")
    private String serverPort;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title(appName + " API")
                        .version(appVersion)
                        .description(appDescription + "\n\n" +
                                "## Features\n" +
                                "- **Deterministic Content Generation**: Generate marketing content without AI\n" +
                                "- **Multi-Platform Ads**: Google, Meta, LinkedIn ads generation\n" +
                                "- **SEO Strategy**: Complete SEO planning and optimization\n" +
                                "- **CRM Sequences**: Email nurturing sequences\n" +
                                "- **Integrated Strategy**: Comprehensive marketing strategy\n" +
                                "- **Request Correlation**: Every request tracked with request_id\n" +
                                "- **File Persistence**: All outputs saved to ./outputs directory\n\n" +
                                "## Authentication\n" +
                                "No authentication required for this version (MVP).\n\n" +
                                "## Request Headers\n" +
                                "- `X-Request-Id`: Optional. Provide your own request ID for tracking\n" +
                                "- `Content-Type`: application/json\n\n" +
                                "## Response Format\n" +
                                "All responses follow the StandardResponse envelope:\n" +
                                "```json\n" +
                                "{\n" +
                                "  \"requestId\": \"uuid\",\n" +
                                "  \"timestamp\": \"ISO-8601\",\n" +
                                "  \"status\": 200,\n" +
                                "  \"success\": true,\n" +
                                "  \"data\": { ... }\n" +
                                "}\n" +
                                "```")
                        .contact(new Contact()
                                .name("MCP Marketing Suite Team")
                                .email("support@mcp-marketing.com")
                                .url("https://github.com/your-org/mcp-marketing-suite-java"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:" + serverPort)
                                .description("Local Development Server"),
                        new Server()
                                .url("https://api.mcp-marketing.com")
                                .description("Production Server (when deployed)")
                ));
    }
}
