package com.onlybuns.onlybuns.infrastructure.interfaces;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.onlybuns.onlybuns.domain.models.ChatMessage;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findTop10ByChatRoomIdOrderByTimestampDesc(Long chatRoomId);
}
