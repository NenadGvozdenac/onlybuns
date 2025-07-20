package com.onlybuns.onlybuns.infrastructure.config;

import com.onlybuns.onlybuns.infrastructure.metrics.MetricsService;
import org.springframework.stereotype.Component;

@Component
public class SessionEventListener {

    private final MetricsService metricsService;

    public SessionEventListener(MetricsService metricsService) {
        this.metricsService = metricsService;
    }

    public void userLoggedIn(String username) {
        metricsService.recordUserLogin(username);
    }

    public void userLoggedOut(String username) {
        metricsService.recordUserLogout(username);
    }
}
