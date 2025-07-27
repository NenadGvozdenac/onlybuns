package com.onlybuns.onlybuns.core.misc;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

@Component
public class CommentRateLimiter {

    private final Map<String, Deque<LocalDateTime>> userRequests = new ConcurrentHashMap<>();
    private final Duration timeWindow = Duration.ofMinutes(1);
    private final int maxRequests = 5;

    public synchronized boolean isAllowed(String username) {
        LocalDateTime now = LocalDateTime.now();
        Deque<LocalDateTime> requests = userRequests.computeIfAbsent(username, k -> new ArrayDeque<>());

        // Ukloni stare zahteve
        while (!requests.isEmpty() && Duration.between(requests.peek(), now).compareTo(timeWindow) > 0) {
            requests.poll();
        }

        if (requests.size() < maxRequests) {
            requests.add(now);
            return true;
        }

        return false;
    }
}
