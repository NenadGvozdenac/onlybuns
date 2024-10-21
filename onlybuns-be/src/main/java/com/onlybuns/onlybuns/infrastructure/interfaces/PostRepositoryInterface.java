package com.onlybuns.onlybuns.infrastructure.interfaces;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.onlybuns.onlybuns.domain.models.Post;

public interface PostRepositoryInterface extends JpaRepository<Post, Long>{
    
    Optional<Post> findById(long id);
}
