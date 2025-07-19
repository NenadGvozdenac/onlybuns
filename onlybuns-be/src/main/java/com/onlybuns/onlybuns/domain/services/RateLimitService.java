package com.onlybuns.onlybuns.domain.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.time.Duration;

@Service
public class RateLimitService {
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    private static final int MAX_COMMENTS_PER_HOUR = 60;
    private static final int WINDOW_SIZE_SECONDS = 3600; // 1 hour
    
    public boolean isCommentAllowed(String username) {
        String key = "comment_rate_limit:" + username;
        Object currentCountObj = redisTemplate.opsForValue().get(key);
        
        if (currentCountObj == null) {
            // First comment in this window
            redisTemplate.opsForValue().set(key, 1, Duration.ofSeconds(WINDOW_SIZE_SECONDS));
            return true;
        }
        
        int count = (Integer) currentCountObj;
        if (count >= MAX_COMMENTS_PER_HOUR) {
            return false; // Rate limit exceeded
        }
        
        // Increment counter
        redisTemplate.opsForValue().increment(key);
        return true;
    }
    
    public int getRemainingComments(String username) {
        String key = "comment_rate_limit:" + username;
        Object currentCountObj = redisTemplate.opsForValue().get(key);
        
        if (currentCountObj == null) {
            return MAX_COMMENTS_PER_HOUR;
        }
        
        return Math.max(0, MAX_COMMENTS_PER_HOUR - (Integer) currentCountObj);
    }
}
