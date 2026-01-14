package com.mcp.marketing.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI/Swagger configuration
 */
@Configuration
public class OpenApiConfiguration {

    @Bean
    public OpenAPI mcpMarketingOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("MCP Marketing Suite API")
                .description("Open-source marketing copilot built on MCP and AI orchestration")
                .version("0.1.0-SNAPSHOT")
                .contact(new Contact()
                    .name("MCP Marketing Suite")
                    .url("https://github.com/yourusername/mcp-marketing-suite-java"))
                .license(new License()
                    .name("MIT License")
                    .url("https://opensource.org/licenses/MIT")));
    }
}

