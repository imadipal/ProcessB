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


    public static void main(String[] args) {
        SpringApplication.run(KnowtheprocessBackendApplication.class, args);
    }

}
