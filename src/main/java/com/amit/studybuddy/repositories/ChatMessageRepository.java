package com.amit.studybuddy.repositories;

import com.amit.studybuddy.domain.entities.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, UUID> {

    @Query("SELECT m FROM ChatMessage m JOIN FETCH m.sender WHERE m.match.id = :matchId ORDER BY m.timestamp ASC")
    List<ChatMessage> findByMatchIdWithSender(UUID matchId);

    List<ChatMessage> findByMatchIdOrderByTimestampAsc(UUID matchId);

    // Searching message by sender or group
    List<ChatMessage> findByMatchId(UUID matchId);

    List<ChatMessage> findBySenderId(UUID senderId);
}
