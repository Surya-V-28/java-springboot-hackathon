package com.codeathonJava.codeathonJava.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
@Configuration
@EnableWebSecurity
public class Securityconfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        DefaultSecurityFilterChain build = httpSecurity
                // Disable CSRF protection for testing purposes (use with caution in production)
                .csrf().disable()
                // Form login configuration
                .formLogin(httpForm -> {
                    httpForm.loginPage("/req/login").permitAll();  // Custom login page, accessible to all
                    httpForm.defaultSuccessUrl("/index", true);    // Redirect to /index on successful login
                })
                // Authorization rules
                .authorizeHttpRequests(registry -> {
                    registry.requestMatchers("/req/signup","/req/verify", "/css/**", "/js/**").permitAll();  // Allow public access
                    registry.anyRequest().authenticated();  // All other endpoints require authentication
                })
                .build();
        return build;
    }
}
