package com.onlybuns.onlybuns.infrastructure.config;

import com.onlybuns.onlybuns.infrastructure.metrics.MetricsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SessionConfig {

    @Bean
    public SessionEventListener sessionEventListener(MetricsService metricsService) {
        return new SessionEventListener(metricsService);
    }
}
