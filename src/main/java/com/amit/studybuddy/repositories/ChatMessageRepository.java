package com.amit.studybuddy.repositories;

import com.amit.studybuddy.domain.entities.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, UUID> {

    Optional<ChatMessage> findByContent(String content);

    // Optional: אם תרצה לחפש לפי group או sender
    List<ChatMessage> findByGroupId(UUID groupId);

    List<ChatMessage> findBySenderId(UUID senderId);
}
