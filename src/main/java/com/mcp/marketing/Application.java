package com.mcp.marketing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import java.io.File;

@SpringBootApplication
@EnableAsync
public class Application {

    public static void main(String[] args) {
        // Ensure outputs directory exists
        new File("./outputs").mkdirs();

        SpringApplication.run(Application.class, args);
    }
}

