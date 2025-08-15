package com.sastudios.tmdbproxy.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.jwt.*;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

//    @Value("${supabase.jwt.secret}")
//    private String secret;
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests(authz -> authz
//                        .anyRequest().authenticated()
//                )
//                .oauth2ResourceServer(oauth2 -> oauth2
//                        .jwt(jwt -> jwt.decoder(jwtDecoder()))
//                );
//        return http.build();
//    }
//
//    @Bean
//    JwtDecoder jwtDecoder() {
//        if (secret == null || secret.isEmpty()) {
//            throw new IllegalStateException("JWT secret is not set!");
//        }
//
//        SecretKey key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
//        return NimbusJwtDecoder.withSecretKey(key)
//                .macAlgorithm(MacAlgorithm.HS256) // optional but explicit
//                .build();
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)            // Disable CSRF
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()            // Allow all requests
                );
        return http.build();
    }

}
