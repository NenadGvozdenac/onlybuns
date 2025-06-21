package com.onlybuns.onlybuns.domain.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import com.onlybuns.onlybuns.domain.models.User;
import com.onlybuns.onlybuns.infrastructure.interfaces.UserRepository;

public class SpamUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Scheduled(cron = "0 0 0 * * ?") public void sendSpamEmails()
    {

        List<User> users = userRepository.findBySentMailFalse();
        for (User user : users) {
            int newPosts = user.getFollowers().stream()
                .flatMap(follower -> follower.getPosts().stream())
                .filter(post -> post.getDateOfCreation().isAfter(user.getLastLoggedIn()))
                .toList().size();
            
            String subject = "New posts from your followers!";
            String message = "Hello " + user.getUsername() + ",\n\nThere have been " + newPosts +
                " new posts by your followers. Check them out!";

            emailService.sendSpamEmail(user.getEmail(), subject, message);

            user.setSentMail(true);
            userRepository.save(user);
        }
    }
    public void sendSpamEmail()
    {
        //nenadgvozdenacsrb@gmail.com
        List<User> users = userRepository.findBySentMailFalse();
        for (User user : users) {
            int newPosts = user.getFollowers().stream()
                .flatMap(follower -> follower.getPosts().stream())
                .filter(post -> post.getDateOfCreation().isAfter(user.getLastLoggedIn()))
                .toList().size();
            
            String subject = "New posts from your followers!";
            String message = "Hello " + user.getUsername() + ",\n\nThere have been " + newPosts +
                " new posts by your followers. Check them out!";

            emailService.sendSpamEmail("gegejik232@finfave.com", subject, message);

            user.setSentMail(true);
            userRepository.save(user);
        }
    }
}
