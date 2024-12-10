package com.onlybuns.onlybuns.infrastructure.interfaces;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.onlybuns.onlybuns.domain.models.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    List<User> findByVerifiedTrue();
    
    @Transactional
    void deleteByVerifiedFalse();

    @Transactional
    @Query(value = "SELECT * FROM users WHERE username = :username FOR UPDATE", nativeQuery = true)
    void lockUserTableByUsername(@Param("username") String username);
}
