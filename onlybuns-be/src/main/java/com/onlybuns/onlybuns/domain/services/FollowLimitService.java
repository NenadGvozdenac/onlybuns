package com.onlybuns.onlybuns.domain.services;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class FollowLimitService {

    private final Map<Long, List<Long>> userFollowTimestamps = new HashMap<>();

    private static final int MAX_FOLLOWS_PER_MINUTE = 2;

    public boolean canFollow(Long userId) {
        // Get the current time in milliseconds
        long currentTime = System.currentTimeMillis();
        
        // Get the list of follow timestamps for the user
        List<Long> followTimestamps = userFollowTimestamps.getOrDefault(userId, new LinkedList<>());
        
        // Remove timestamps older than 1 minute (60000 ms)
        followTimestamps.removeIf(timestamp -> currentTime - timestamp > 60000);
        
        // If the number of follows in the last minute exceeds the limit, return false
        if (followTimestamps.size() >= MAX_FOLLOWS_PER_MINUTE) {
            return false;
        }
        
        // Otherwise, record the current follow action and allow it
        followTimestamps.add(currentTime);
        userFollowTimestamps.put(userId, followTimestamps);
        return true;
    }
}
