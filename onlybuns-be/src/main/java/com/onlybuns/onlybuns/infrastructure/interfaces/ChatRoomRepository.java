package com.onlybuns.onlybuns.infrastructure.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;

import com.onlybuns.onlybuns.domain.models.ChatRoom;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
}
