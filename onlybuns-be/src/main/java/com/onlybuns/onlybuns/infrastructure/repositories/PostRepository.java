package com.onlybuns.onlybuns.infrastructure.repositories;

import java.util.Optional;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.onlybuns.onlybuns.domain.models.Post;
import com.onlybuns.onlybuns.domain.models.User;
import com.onlybuns.onlybuns.domain.models.Comment;
import com.onlybuns.onlybuns.infrastructure.interfaces.PostRepositoryCustom;
import com.onlybuns.onlybuns.infrastructure.interfaces.PostRepositoryInterface;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.stream.Collectors;

@Repository
public class PostRepository implements PostRepositoryCustom {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private PostRepositoryInterface postRepository;

    @Override
    @Transactional
    public void likePost(Long postId) {
        // Fetch the post by ID
        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            // Increment the number of likes
            post.setNumberOfLikes(post.getNumberOfLikes() + 1);
            // Persist the updated post
            entityManager.merge(post);
        }
    }

    // 5 max popular posts in the last 7 days
    public List<Post> findMostPopularPostsLastSevenDays() {
        List<Post> posts = postRepository.findAll().stream().filter(p -> p.getDateOfCreation().isAfter(LocalDateTime.now().minusDays(7))).sorted((p1, p2) -> p2.getNumberOfLikes().compareTo(p1.getNumberOfLikes())).limit(5).collect(Collectors.toList());
        return posts;
    }

    // 10 most popular posts
    public List<Post> findMostPopularPosts() {
        List<Post> posts = postRepository.findAll().stream().sorted((p1, p2) -> p2.getNumberOfLikes().compareTo(p1.getNumberOfLikes())).limit(10).collect(Collectors.toList());
        return posts;
    }

    // Count the number of posts in the last month
    public Long countPostsInTheLastMonth() {
        return Integer.toUnsignedLong(postRepository.findAll().stream().filter(p -> p.getDateOfCreation().isAfter(LocalDateTime.now().minusMonths(1))).collect(Collectors.toList()).size());
    }

    public List<User> findUsersThatMostLiked() {
        List<Post> allPosts = postRepository.findAll();

        List<List<User>> users = allPosts.stream().map(p -> p.getUsersThatLiked()).collect(Collectors.toList());

        HashMap<User, Integer> userLikes = new HashMap<User, Integer>();

        for (List<User> userList : users) {
            for (User user : userList) {
                if (userLikes.containsKey(user)) {
                    userLikes.put(user, userLikes.get(user) + 1);
                } else {
                    userLikes.put(user, 1);
                }
            }
        }

        List<User> mostLikedUsers = userLikes.entrySet().stream().sorted((u1, u2) -> u2.getValue().compareTo(u1.getValue())).limit(5).map(u -> u.getKey()).collect(Collectors.toList());

        return mostLikedUsers;
    }

    // Get all comments from all posts in the database
    public List<Comment> findAllComments() {
        List<Post> allPosts = postRepository.findAll();
        
        return allPosts.stream()
            .flatMap(post -> post.getComments().stream())
            .collect(Collectors.toList());
    }
}
