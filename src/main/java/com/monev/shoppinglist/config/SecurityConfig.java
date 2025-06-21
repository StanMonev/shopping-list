package com.monev.shoppinglist.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)                 // REST: no CSRF token
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/index.html").permitAll() // allow a simple UI, if any
                        .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults());        // turn on HTTP Basic
        return http.build();
    }
}
