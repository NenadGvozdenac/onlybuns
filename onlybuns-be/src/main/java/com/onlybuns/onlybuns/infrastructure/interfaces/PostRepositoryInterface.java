package com.onlybuns.onlybuns.infrastructure.interfaces;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import com.onlybuns.onlybuns.domain.models.Post;

import io.lettuce.core.dynamic.annotation.Param;
import jakarta.persistence.LockModeType;

public interface PostRepositoryInterface extends JpaRepository<Post, Long>{
    
    Optional<Post> findById(long id);
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Post p WHERE p.id = :id")
    Optional<Post> findByIdWithLock(@Param("id") Long id);
    
    @Query("SELECT p FROM Post p WHERE p.user.username = :username ORDER BY p.dateOfCreation DESC")
    List<Post> findByUsername(@Param("username") String username);
}
