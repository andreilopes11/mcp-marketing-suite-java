package com.mcp.marketing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

import java.io.File;

@SpringBootApplication
@EnableAsync
public class McpMarketingApplication {

    public static void main(String[] args) {
        // Ensure outputs directory exists
        new File("./outputs").mkdirs();

        SpringApplication.run(McpMarketingApplication.class, args);
    }
}

