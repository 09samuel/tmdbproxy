//package com.sastudios.tmdbproxy.config;
//
//import java.time.Duration;
//import java.util.HashMap;
//import java.util.Map;
//
//import com.sastudios.tmdbproxy.util.GzipRedisSerializer;
//import io.lettuce.core.RedisClient;
//import io.lettuce.core.RedisURI;
//import io.lettuce.core.api.StatefulConnection;
//import io.lettuce.core.api.StatefulRedisConnection;
//import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.cache.annotation.EnableCaching;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.cache.*;
//import org.springframework.data.redis.connection.*;
//import org.springframework.data.redis.connection.lettuce.*;
//import org.springframework.data.redis.serializer.*;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;
//import com.fasterxml.jackson.databind.DeserializationFeature;
//
//@Configuration
//@EnableCaching
//public class RedisConfig {
//
//    private static final Logger log = LoggerFactory.getLogger(RedisConfig.class);
//
//    @Value("${spring.data.redis.url}")
//    private String redisUrl; // e.g. redis://:password@hostname:6379
//
//    // Default TTLs (you can tune per-cache below)
//    private static final Duration DEFAULT_TTL = Duration.ofMinutes(30);
//
//    @Bean
//    public LettuceConnectionFactory redisConnectionFactory() {
//        log.info("Initializing Redis connection from URL: {}", redisUrl);
//
//        java.net.URI uri = java.net.URI.create(redisUrl);
//        boolean useSsl = uri.getScheme().equalsIgnoreCase("rediss");
//        RedisStandaloneConfiguration redisConfig = createStandaloneConfigFromUrl(redisUrl);
//
//        log.info("Parsed Redis Host: {}", redisConfig.getHostName());
//        log.info("Parsed Redis Port: {}", redisConfig.getPort());
//        log.info("Using SSL: {}", useSsl);
//
//        GenericObjectPoolConfig<StatefulConnection<?, ?>> poolConfig = new GenericObjectPoolConfig<>();
//        poolConfig.setMaxTotal(20);
//        poolConfig.setMaxIdle(10);
//        poolConfig.setMinIdle(1);
//        poolConfig.setMaxWait(Duration.ofMillis(1000));
//
//        LettucePoolingClientConfiguration.LettucePoolingClientConfigurationBuilder builder =
//                LettucePoolingClientConfiguration.builder()
//                        .commandTimeout(Duration.ofSeconds(5))
//                        .shutdownTimeout(Duration.ofMillis(100))
//                        .poolConfig(poolConfig);
//
//        if (useSsl) {
//            builder.useSsl();
//        }
//
//        LettuceClientConfiguration clientConfig = builder.build();
//        LettuceConnectionFactory factory = new LettuceConnectionFactory(redisConfig, clientConfig);
//
//        try {
//            factory.afterPropertiesSet();
//            log.info("RedisConnectionFactory created successfully with SSL: {}", useSsl);
//
//            try (RedisConnection connection = factory.getConnection()) {
//                String pong = connection.ping();
//                log.info("Redis PING response: {}", pong);
//            }
//        } catch (Exception e) {
//            log.error("Error creating or testing Redis connection: {}", e.getMessage(), e);
//        }
//
//        return factory;
//    }
//
//    private RedisStandaloneConfiguration createStandaloneConfigFromUrl(String url) {
//        // spring-data-redis accepts spring.redis.url but to build RedisStandaloneConfiguration we parse manually
//        // Supported URL forms: redis://[:password@]host[:port]
//
//        java.net.URI uri = java.net.URI.create(url);
//        String host = uri.getHost();
//        int port = uri.getPort() == -1 ? 6379 : uri.getPort();
//        RedisStandaloneConfiguration cfg = new RedisStandaloneConfiguration(host, port);
//
//        if (uri.getUserInfo() != null) {
//            String[] parts = uri.getUserInfo().split(":", 2);
//            String password = parts.length == 2 ? parts[1] : parts[0];
//            if (password != null && !password.isEmpty()) {
//                cfg.setPassword(RedisPassword.of(password));
//            }
//        }
//        return cfg;
//    }
//
//    @Bean
//    public RedisCacheManager cacheManager(LettuceConnectionFactory connectionFactory, ObjectMapper objectMapper) {
//        // 1. Create base Jackson serializer
//        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
//        Jackson2JsonRedisSerializer<Object> jacksonSerializer = new Jackson2JsonRedisSerializer<>(objectMapper, Object.class);
//
//
//        // 2. Wrap it with GZIP compression
//        RedisSerializer<Object> gzipSerializer = new GzipRedisSerializer<>(jacksonSerializer);
//
//        RedisSerializationContext.SerializationPair<Object> pair =
//                RedisSerializationContext.SerializationPair.fromSerializer(gzipSerializer);
//
//        // Default config
//        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
//                .entryTtl(DEFAULT_TTL)
//                .serializeValuesWith(pair)
//                .disableCachingNullValues();
//
//        // Per-cache configs (same as before)
//        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
//        cacheConfigurations.put("topRated", defaultConfig.entryTtl(Duration.ofMinutes(15)));
//        cacheConfigurations.put("search", defaultConfig.entryTtl(Duration.ofMinutes(5)));
//        cacheConfigurations.put("details", defaultConfig.entryTtl(Duration.ofHours(6)));
//        cacheConfigurations.put("recommendations", defaultConfig.entryTtl(Duration.ofHours(1)));
//        cacheConfigurations.put("episodes", defaultConfig.entryTtl(Duration.ofHours(6)));
//        cacheConfigurations.put("watchProviders", defaultConfig.entryTtl(Duration.ofHours(6)));
//
//        return RedisCacheManager.builder(connectionFactory)
//                .cacheDefaults(defaultConfig)
//                .withInitialCacheConfigurations(cacheConfigurations)
//                .transactionAware()
//                .build();
//    }
//
//
//    // Optional ObjectMapper bean (if you don't already have one configured)
//    @Bean
//    public ObjectMapper objectMapper() {
//        return new ObjectMapper();
//    }
//}
package com.sastudios.tmdbproxy.config;

import java.net.URI;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sastudios.tmdbproxy.util.GzipRedisSerializer;
import io.lettuce.core.api.StatefulConnection;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.*;
        import org.springframework.data.redis.connection.*;
        import org.springframework.data.redis.connection.lettuce.*;
        import org.springframework.data.redis.serializer.*;

@Configuration
@EnableCaching
public class RedisConfig {

    private static final Logger log = LoggerFactory.getLogger(RedisConfig.class);

    @Value("${spring.data.redis.url}")
    private String redisUrl;

    private static final Duration DEFAULT_TTL = Duration.ofMinutes(30);

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        log.info("Initializing Redis connection from URL: {}", redisUrl);

        URI uri = URI.create(redisUrl);
        boolean useSsl = uri.getScheme().equalsIgnoreCase("rediss");
        String host = uri.getHost();
        int port = uri.getPort() == -1 ? 6379 : uri.getPort();

        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration(host, port);

        if (uri.getUserInfo() != null) {
            String[] userInfo = uri.getUserInfo().split(":", 2);
            if (userInfo.length == 2 && userInfo[1] != null && !userInfo[1].isEmpty()) {
                redisConfig.setPassword(RedisPassword.of(userInfo[1]));
            }
        }

        log.info("Parsed Redis Host: {}", host);
        log.info("Parsed Redis Port: {}", port);
        log.info("Using SSL: {}", useSsl);

        GenericObjectPoolConfig<StatefulConnection<?, ?>> poolConfig = new GenericObjectPoolConfig<>();
        poolConfig.setMaxTotal(20);
        poolConfig.setMaxIdle(10);
        poolConfig.setMinIdle(1);
        poolConfig.setMaxWait(Duration.ofMillis(1000));

        LettucePoolingClientConfiguration.LettucePoolingClientConfigurationBuilder clientConfigBuilder =
                LettucePoolingClientConfiguration.builder()
                        .commandTimeout(Duration.ofSeconds(5))
                        .shutdownTimeout(Duration.ofMillis(100))
                        .poolConfig(poolConfig);

        if (useSsl) {
            clientConfigBuilder.useSsl();
        }

        return new LettuceConnectionFactory(redisConfig, clientConfigBuilder.build());
    }

    @Bean
    public RedisCacheManager cacheManager(LettuceConnectionFactory connectionFactory, ObjectMapper objectMapper) {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        Jackson2JsonRedisSerializer<Object> jacksonSerializer =
                new Jackson2JsonRedisSerializer<>(objectMapper, Object.class);

        RedisSerializer<Object> gzipSerializer = new GzipRedisSerializer<>(jacksonSerializer);

        RedisSerializationContext.SerializationPair<Object> pair =
                RedisSerializationContext.SerializationPair.fromSerializer(gzipSerializer);

        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(DEFAULT_TTL)
                .serializeValuesWith(pair)
                .disableCachingNullValues();

        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
        cacheConfigurations.put("topRated", defaultConfig.entryTtl(Duration.ofMinutes(15)));
        cacheConfigurations.put("search", defaultConfig.entryTtl(Duration.ofMinutes(5)));
        cacheConfigurations.put("details", defaultConfig.entryTtl(Duration.ofHours(6)));
        cacheConfigurations.put("recommendations", defaultConfig.entryTtl(Duration.ofHours(1)));
        cacheConfigurations.put("episodes", defaultConfig.entryTtl(Duration.ofHours(6)));
        cacheConfigurations.put("watchProviders", defaultConfig.entryTtl(Duration.ofHours(6)));

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaultConfig)
                .withInitialCacheConfigurations(cacheConfigurations)
                .transactionAware()
                .build();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
