package com.onlybuns.onlybuns.infrastructure.interfaces;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.onlybuns.onlybuns.domain.models.User;

import jakarta.persistence.LockModeType;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    List<User> findByVerifiedTrue();
    List<User> findBySentMailFalse();
      @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT u FROM User u WHERE u.username = :username")
    Optional<User> findByUsernameForFollowUnfollow(String username);
    
    // Pessimistic lock methods for user registration
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT u FROM User u WHERE u.username = :username")
    Optional<User> findByUsernameWithLock(@Param("username") String username);
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> findByEmailWithLock(@Param("email") String email);
    
    @Transactional
    void deleteByVerifiedFalse();

    @Transactional
    @Query(value = "SELECT * FROM users WHERE username = :username FOR UPDATE", nativeQuery = true)
    void lockUserTableByUsername(@Param("username") String username);
    
    // Additional pessimistic lock method for email
    @Transactional
    @Query(value = "SELECT * FROM users WHERE email = :email FOR UPDATE", nativeQuery = true)
    void lockUserTableByEmail(@Param("email") String email);
}
