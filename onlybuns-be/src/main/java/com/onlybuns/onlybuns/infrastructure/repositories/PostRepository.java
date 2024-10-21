package com.onlybuns.onlybuns.infrastructure.repositories;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.onlybuns.onlybuns.domain.models.Post;
import com.onlybuns.onlybuns.infrastructure.interfaces.PostRepositoryCustom;
import com.onlybuns.onlybuns.infrastructure.interfaces.PostRepositoryInterface;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Repository
public class PostRepository implements PostRepositoryCustom{

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
    
}
