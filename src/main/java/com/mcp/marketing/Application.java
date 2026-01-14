package com.mcp.marketing;

import com.mcp.marketing.service.OutputService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    /**
     * Initialize output directory structure on application startup
     */
    @Bean
    public CommandLineRunner initOutputDirectories(OutputService outputService) {
        return args -> outputService.initializeOutputDirectories();
    }
}

