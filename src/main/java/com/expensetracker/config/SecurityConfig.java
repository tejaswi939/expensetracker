package com.expensetracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())   // Disable CSRF for API requests
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/auth/**").permitAll()  // Allow login & register
                    .requestMatchers("/expenses/**").permitAll()
                    .anyRequest().authenticated()
            )
            .formLogin(form -> form.disable())   // Disable default login page
            .httpBasic(httpBasic -> httpBasic.disable());

        return http.build();
    }
}