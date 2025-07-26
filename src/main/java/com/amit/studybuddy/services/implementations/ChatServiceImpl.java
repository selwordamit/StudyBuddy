package com.amit.studybuddy.services.implementations;


import com.amit.studybuddy.domain.dtos.ChatMessageResponse;
import com.amit.studybuddy.domain.entities.ChatMessage;
import com.amit.studybuddy.domain.entities.Match;
import com.amit.studybuddy.domain.entities.User;
import com.amit.studybuddy.domain.mappers.ChatMapper;
import com.amit.studybuddy.repositories.ChatMessageRepository;
import com.amit.studybuddy.repositories.MatchRepository;
import com.amit.studybuddy.services.ChatService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final MatchRepository matchRepository;
    private final ChatMapper chatMapper;

    /**
     * Handles sending a chat message within a match context.
     *
     * @param matchId the match/group the message is related to
     * @param sender the authenticated user sending the message
     * @param content the message content (text)
     * @return the saved message, mapped to response DTO
     */
    @Override
    public ChatMessageResponse sendMessage(UUID matchId, User sender, String content) {
        log.info("[CHAT_SEND] - [matchId={}] [senderId={}] - STARTED", matchId, sender.getId());

        // Validate that the match exists
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> {
                    log.warn("[CHAT_SEND] - [matchId={}] - MATCH NOT FOUND", matchId);
                    return new EntityNotFoundException("Match not found with id: " + matchId);
                });

        // Build and persist the chat message
        ChatMessage message = ChatMessage.builder()
                .match(match)
                .sender(sender)
                .content(content)
                .build();

        ChatMessage saved = chatMessageRepository.save(message);
        log.info("[CHAT_SEND] - [matchId={}] [messageId={}] - SAVED", matchId, saved.getId());

        return chatMapper.toResponse(saved);
    }

    /**
     * Retrieves all messages for a specific match, ordered by timestamp.
     *
     * @param matchId the match/group whose messages are requested
     * @return a list of chat message DTOs ordered chronologically
     */
    @Override
    public List<ChatMessageResponse> getMessagesByMatch(UUID matchId) {
        log.info("[CHAT_FETCH] - [matchId={}] - FETCHING MESSAGES", matchId);

        List<ChatMessage> messages = chatMessageRepository.findByMatchIdWithSender(matchId);

        log.info("[CHAT_FETCH] - [matchId={}] - FOUND {} MESSAGES", matchId, messages.size());

        return chatMapper.toResponseList(messages);
    }
}
