package com.mcp.marketing.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Async configuration for parallel task execution
 * Optimizes CompletableFuture performance in MarketingService
 */
@Slf4j
@Configuration
public class AsyncConfiguration {

    @Bean(name = "marketingTaskExecutor")
    public Executor marketingTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // Core pool size: number of threads to keep alive
        executor.setCorePoolSize(3);

        // Max pool size: maximum number of threads
        executor.setMaxPoolSize(10);

        // Queue capacity: tasks waiting for thread availability
        executor.setQueueCapacity(25);

        // Thread name prefix for debugging
        executor.setThreadNamePrefix("marketing-async-");

        // Rejection policy: caller runs if pool is full
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        // Wait for tasks to complete on shutdown
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);

        executor.initialize();

        log.info("Initialized marketing task executor with core pool size: {}, max pool size: {}",
                executor.getCorePoolSize(), executor.getMaxPoolSize());

        return executor;
    }
}

