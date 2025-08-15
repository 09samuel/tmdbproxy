package com.sastudios.tmdbproxy.config;


import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class RateLimitConfig {

    // In-memory storage for buckets
    private final Map<String, Bucket> userBuckets = new ConcurrentHashMap<>();

    private final Bucket globalBucket = Bucket.builder()
            .addLimit(Bandwidth.builder()
                    .capacity(2000) // global limit
                    .refillIntervally(2000, Duration.ofHours(1))
                    .build())
            .build();

    @Bean
    public BucketConfiguration perUserConfig() {
        return BucketConfiguration.builder()
                .addLimit(Bandwidth.builder()
                        .capacity(100) // per-user limit
                        .refillIntervally(100, Duration.ofHours(1))
                        .build())
                .build();
    }

    @Bean
    public Bucket globalRateLimiter() {
        return globalBucket;
    }

    public Bucket resolveUserBucket(String userId) {
        return userBuckets.computeIfAbsent(userId, key ->
                Bucket.builder()
                        .addLimit(Bandwidth.builder()
                                .capacity(100)
                                .refillIntervally(100, Duration.ofHours(1))
                                .build())
                        .build());
    }
}