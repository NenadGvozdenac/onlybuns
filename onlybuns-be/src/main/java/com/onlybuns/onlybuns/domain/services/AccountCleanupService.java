package com.onlybuns.onlybuns.domain.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.onlybuns.onlybuns.infrastructure.interfaces.UserRepository;

@Service
public class AccountCleanupService {
    
    @Autowired
    private UserRepository userRepository;

    // This cron expression will run the task on the last day of every month at midnight
    @Scheduled(cron = "0 0 0 L * ?")
    @Transactional
    public void deleteInactiveAccounts() {
        try {
            userRepository.deleteByVerifiedFalse();
            System.out.println("Inactive accounts deleted successfully.");
        } catch (Exception e) {
            System.err.println("Error during account cleanup: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
