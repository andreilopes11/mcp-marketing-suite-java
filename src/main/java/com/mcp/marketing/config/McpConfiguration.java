package com.mcp.marketing.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * MCP SDK configuration properties
 */
@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "mcp.sdk")
public class McpConfiguration {

    private boolean enabled = true;
    private Server server = new Server();
    private Resources resources = new Resources();
    private Tools tools = new Tools();

    @Setter
    @Getter
    public static class Server {
        private String name = "mcp-marketing-suite-server";
        private String version = "0.1.0";
        private String endpoint = "/mcp";
    }

    @Setter
    @Getter
    public static class Resources {
        private boolean enabled = true;
    }

    @Setter
    @Getter
    public static class Tools {
        private boolean enabled = true;
    }
}
