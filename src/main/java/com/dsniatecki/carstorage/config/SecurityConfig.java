package com.dsniatecki.carstorage.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

public class SecurityConfig {

    @Bean
    SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http.csrf()
                .disable()
                .authorizeExchange()
                .pathMatchers("/actuator/**", "/api/internal/**")
                .permitAll()
                .anyExchange()
                .authenticated()
                .and()
                .oauth2ResourceServer()
                .jwt()
                .and()
                .and()
                .build();
    }
}
