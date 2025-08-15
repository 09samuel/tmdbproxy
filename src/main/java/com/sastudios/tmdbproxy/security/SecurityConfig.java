package com.sastudios.tmdbproxy.security;

import com.sastudios.tmdbproxy.util.RateLimitFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final RateLimitFilter rateLimitFilter;

    public SecurityConfig(RateLimitFilter rateLimitFilter) {
        this.rateLimitFilter = rateLimitFilter;
    }

    @Value("${supabase.jwt.secret}")
    private String secret;

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests(authz -> authz
//                        .anyRequest().authenticated()
//                )
//                .addFilterAfter(rateLimitFilter, BasicAuthenticationFilter.class)
//                .oauth2ResourceServer(oauth -> oauth.jwt(jwt -> jwt
//                        .decoder(jwtDecoder())
//                        .jwtAuthenticationConverter(jwtAuthenticationConverter())
//                ));
//        return http.build();
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

    @Bean
    JwtDecoder jwtDecoder() {
        if (secret == null || secret.isEmpty()) {
            throw new IllegalStateException("JWT secret is not set!");
        }

        SecretKey key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
        return NimbusJwtDecoder.withSecretKey(key)
                .macAlgorithm(MacAlgorithm.HS256) // optional but explicit
                .build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();

        // Set the principal claim to 'sub' (Supabase user id)
        converter.setPrincipalClaimName("sub");
        return converter;
    }
}
