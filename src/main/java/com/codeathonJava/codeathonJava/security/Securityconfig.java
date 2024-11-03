//package com.codeathonJava.codeathonJava.security;
//
//import com.codeathonJava.codeathonJava.service.CustomUserDetailsService;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//public class Securityconfig {
//
//    private final CustomUserDetailsService userDetailsService;
//
//    public Securityconfig(CustomUserDetailsService userDetailsService) {
//        this.userDetailsService = userDetailsService;
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity
//                .csrf().disable()
//                .formLogin(httpForm -> {
//                    httpForm.loginPage("/req/login").permitAll();
//                    httpForm.defaultSuccessUrl("/index", true);
//                })
//                .authorizeHttpRequests(registry -> {
//                    registry.requestMatchers("/req/signup","/req/verify", "/css/**", "/js/**").permitAll();
//                    registry.anyRequest().authenticated();
//                });
//        return httpSecurity.build();
//    }
//
//
//    @Bean
//    UserDetailsService userDetailsService() {
//        UserDetails user = User
//                .withUsername("dan")
//                .password("{noop}password")
//                .build();
//        return new InMemoryUserDetailsManager(user);
//    }
//}


package com.codeathonJava.codeathonJava.security;

import com.codeathonJava.codeathonJava.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class Securityconfig {

    private final CustomUserDetailsService userDetailsService;

    public Securityconfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().disable()
                .authorizeHttpRequests(registry -> {
                    registry.anyRequest().permitAll(); // Allows all requests without authentication
                });
        return httpSecurity.build();
    }
}

