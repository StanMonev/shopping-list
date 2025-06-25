package com.monev.shoppinglist.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Simple security config with a BASIC AUTH function.
 */
@Configuration
public class SecurityConfig {

    /**
     * A function that uses basic auth to authenticate the web page for users visiting it. <br>
     *  - It will redirect a user to a login page if they haven't yet logged in. <br>
     *  - I could also skip the login page, but I wanted to add it for better frontend design. <br>
     *  - I won't use CSRF for now, because of the demo. <br>
     *
     * @param http {@link HttpSecurity} builder for filter order and auth rules.
     * @return the configured {@link SecurityFilterChain} that is registered as a bean.
     * @throws Exception if there is a misconfiguration
     */
    @Bean
    SecurityFilterChain web(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/javascript/**").permitAll()
                        .requestMatchers("/login", "/login/**", "/error").permitAll()
                        .anyRequest().authenticated())

                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true))
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout"))

                // allow HTTP Basic for Postman
                .httpBasic(Customizer.withDefaults())

                // no CSRF for simplicity
                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }
}

