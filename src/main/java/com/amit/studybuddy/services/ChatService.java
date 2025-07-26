package com.amit.studybuddy.services;

import com.amit.studybuddy.domain.dtos.ChatMessageResponse;
import com.amit.studybuddy.domain.entities.ChatMessage;
import com.amit.studybuddy.domain.entities.User;

import java.util.List;
import java.util.UUID;

public interface ChatService {
    public ChatMessageResponse sendMessage(UUID matchId, User sender, String content);
    public List<ChatMessageResponse> getMessagesByMatch(UUID matchId);

}
