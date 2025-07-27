package com.onlybuns.onlybuns.infrastructure.interfaces;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.onlybuns.onlybuns.domain.models.ChatUser;
import com.onlybuns.onlybuns.domain.models.User;

public interface ChatUserRepository extends JpaRepository<ChatUser, Long> {
    List<ChatUser> findByUser(User user);
}
