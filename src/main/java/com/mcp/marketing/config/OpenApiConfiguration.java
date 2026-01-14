package com.mcp.marketing.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * OpenAPI/Swagger configuration
 */
@Configuration
public class OpenApiConfiguration {

    private final String title;
    private final String description;
    private final String version;
    private final String serverUrl;

    public OpenApiConfiguration(
            @Value("${mcp.openapi.title:MCP Marketing Suite API}") String title,
            @Value("${mcp.openapi.description:Marketing content automation endpoints}") String description,
            @Value("${mcp.openapi.version:v1}") String version,
            @Value("${mcp.openapi.server-url:http://localhost:8080}") String serverUrl) {
        this.title = title;
        this.description = description;
        this.version = version;
        this.serverUrl = serverUrl;
    }

    @Bean
    public OpenAPI marketingOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title(title)
                        .description(description)
                        .version(version))
                .servers(List.of(new Server().url(serverUrl)));
    }
}
