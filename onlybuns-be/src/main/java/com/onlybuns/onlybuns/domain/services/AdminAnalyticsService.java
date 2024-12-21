package com.onlybuns.onlybuns.domain.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onlybuns.onlybuns.core.misc.Result;
import com.onlybuns.onlybuns.domain.models.Post;
import com.onlybuns.onlybuns.domain.models.User;
import com.onlybuns.onlybuns.domain.models.UserRole;
import com.onlybuns.onlybuns.domain.serviceinterfaces.AdminAnalyticsInterface;
import com.onlybuns.onlybuns.infrastructure.interfaces.PostRepositoryInterface;
import com.onlybuns.onlybuns.infrastructure.interfaces.UserRepository;
import com.onlybuns.onlybuns.presentation.dtos.responses.AdminAnalyticsDto;

@Service
public class AdminAnalyticsService implements AdminAnalyticsInterface {
     
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepositoryInterface postRepository;

    public Result<AdminAnalyticsDto> getAdminAnalytics(String admingUsername) {

        var userOpt = userRepository.findByUsername(admingUsername);

        if (userOpt.isEmpty()) {
            return Result.failure("User not found", 403);
        }

        User admin = userOpt.get();

        if(admin.getRole() != UserRole.ADMIN) {
            return Result.failure("User is not an admin", 405);
        }

        List<User> users = userRepository.findAll();

        int postsPerWeek = 0;
        int postsPerMonth = 0;
        int postsPerYear = 0;
        int commentsPerWeek = 0;
        int commentsPerMonth = 0;
        int commentsPerYear = 0;
        int usersJustPosted = 0;
        int usersJustCommented = 0;
        int usersWithoutPostsOrComments = 0;
        //int usersWithBoth = 0;

        for (User user : users){
            List<Post> posts = user.getPosts();
            // List<Comment> comments = user.getComments();

            if(posts.size() > 0){ // && comments.size() == 0){
                usersJustPosted++;
            }

            // if(posts.size() == 0 && comments.size() > 0){
            //     usersJustCommented++;
            // }

            if(posts.size() == 0){// && comments.size() == 0){
                usersWithoutPostsOrComments++;
            }

            //if(posts.size() > 0 && comments.size() > 0){
            //    usersWithBoth++;
            //}
        }

        List<Post> posts = postRepository.findAll();

        for (Post post : posts){
            if(post.getDateOfCreation().isAfter(LocalDateTime.now().plusHours(1).minusWeeks(1))){
                postsPerWeek++;
            }

            if(post.getDateOfCreation().isAfter(LocalDateTime.now().plusHours(1).minusMonths(1))){
                postsPerMonth++;
            }

            if(post.getDateOfCreation().isAfter(LocalDateTime.now().plusHours(1).minusYears(1))){
                postsPerYear++;
            }

        }
        // List<Comment> comments = post.getComments();
    
        // for (Comment comment : comments){
        //     if(comment.getDateOfCreation().isAfter(LocalDateTime.now().plusHours(1).minusWeeks(1))){
        //         commentsPerWeek++;
        //     }
        
        //     if(comment.getDateOfCreation().isAfter(LocalDateTime.now().plusHours(1).minusMonths(1))){
        //         commentsPerMonth++;
        //     }

        //     if(comment.getDateOfCreation().isAfter(LocalDateTime.now().plusHours(1).minusYears(1))){
        //         commentsPerYear++;
        //     }
        // }

        AdminAnalyticsDto adminAnalyticsDto = new AdminAnalyticsDto(postsPerWeek, postsPerMonth, postsPerYear, commentsPerWeek, commentsPerMonth, commentsPerYear, usersJustPosted, usersJustCommented, usersWithoutPostsOrComments);

        return Result.success(adminAnalyticsDto);
    }
    
}
