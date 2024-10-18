package com.knowtheprocessbackend.knowtheprocessbackend;

import com.knowtheprocessbackend.knowtheprocessbackend.filter.JwtRequestFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@SpringBootApplication
public class KnowtheprocessBackendApplication {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtRequestFilter jwtRequestFilter) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF protection
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/auth/**").permitAll(); // Allow access to authentication endpoints
                    auth.requestMatchers("/admin/**").hasRole("ADMIN"); // Restrict access to admin routes
                    auth.anyRequest().authenticated(); // All other requests require authentication
                })
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Stateless session management
                );

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class); // Add JWT filter

        return http.build();
    }
    public static void main(String[] args) {
        SpringApplication.run(KnowtheprocessBackendApplication.class, args);
    }

}
