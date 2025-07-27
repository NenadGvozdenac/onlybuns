package com.onlybuns.onlybuns.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Allow all endpoints
                .allowedOrigins("http://localhost:5173",
                                 "http://localhost:3000",
                                 "http://localhost:3001",
                                 "http://localhost:3002",
                                 "http://localhost:3003",
                                 "http://localhost:3005",
                                 "http://localhost:9090",
                                 "http://prometheus:9090") // Allow your Vue.js frontend and Prometheus
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allowed HTTP methods
                .allowCredentials(true); // Allow credentials (like cookies)
    }
}