package com.mcp.marketing.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Application configuration properties
 */
@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "app")
public class AppConfiguration {

    private Outputs outputs = new Outputs();

    @Setter
    @Getter
    public static class Outputs {
        private String directory = "./outputs";
        private boolean enabled = true;
    }
}
