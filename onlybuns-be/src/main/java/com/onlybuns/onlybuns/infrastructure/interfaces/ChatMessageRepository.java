package com.onlybuns.onlybuns.infrastructure.interfaces;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.onlybuns.onlybuns.domain.models.ChatMessage;
import com.onlybuns.onlybuns.domain.models.ChatRoom;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findAllByChatRoomOrderByTimestampDesc(ChatRoom chatRoom);
    List<ChatMessage> findAllByChatRoomOrderByTimestampAsc(ChatRoom chatRoom);
}

