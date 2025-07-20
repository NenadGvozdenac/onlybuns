package com.onlybuns.onlybuns.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
public class MetricsService {

    private final MeterRegistry meterRegistry;
    private final Set<String> activeUsers = ConcurrentHashMap.newKeySet();
    private final AtomicInteger activeUserCount = new AtomicInteger(0);

    public MetricsService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        
        // Register custom metrics
        Gauge.builder("active_users_total", activeUserCount, AtomicInteger::get)
                .description("Total number of currently active users")
                .register(meterRegistry);
    }

    /**
     * Record a user login event
     */
    public void recordUserLogin(String username) {
        if (activeUsers.add(username)) {
            activeUserCount.incrementAndGet();
            log.debug("User {} logged in. Active users count: {}", username, activeUserCount.get());
        }
        
        // Count login events
        Counter.builder("user_logins_total")
                .description("Total number of user login events")
                .register(meterRegistry)
                .increment();
    }

    /**
     * Record a user logout event
     */
    public void recordUserLogout(String username) {
        if (activeUsers.remove(username)) {
            activeUserCount.decrementAndGet();
            log.debug("User {} logged out. Active users count: {}", username, activeUserCount.get());
        }
        
        // Count logout events
        Counter.builder("user_logouts_total")
                .description("Total number of user logout events")
                .register(meterRegistry)
                .increment();
    }

    /**
     * Record a post creation event
     */
    public void recordPostCreation() {
        Counter.builder("posts_created_total")
                .description("Total number of posts created")
                .register(meterRegistry)
                .increment();
    }

    /**
     * Record a post advertisement event
     */
    public void recordPostAdvertisement() {
        Counter.builder("posts_advertised_total")
                .description("Total number of posts marked for advertisement")
                .register(meterRegistry)
                .increment();
    }

    /**
     * Time a post creation operation
     */
    public Timer.Sample startPostCreationTimer() {
        return Timer.start(meterRegistry);
    }

    /**
     * Stop the post creation timer
     */
    public void stopPostCreationTimer(Timer.Sample sample) {
        sample.stop(Timer.builder("post_creation_duration")
                .description("Time taken to create a post")
                .register(meterRegistry));
    }

    /**
     * Get current active user count
     */
    public int getActiveUserCount() {
        return activeUserCount.get();
    }

    /**
     * Check if user is currently active
     */
    public boolean isUserActive(String username) {
        return activeUsers.contains(username);
    }

    /**
     * Record HTTP request metrics manually if needed
     */
    public void recordHttpRequest(String method, String uri, int status, long durationMs) {
        Timer.builder("http_requests_manual")
                .description("Manual HTTP request timer")
                .tag("method", method)
                .tag("uri", uri)
                .tag("status", String.valueOf(status))
                .register(meterRegistry)
                .record(durationMs, java.util.concurrent.TimeUnit.MILLISECONDS);
    }
}
