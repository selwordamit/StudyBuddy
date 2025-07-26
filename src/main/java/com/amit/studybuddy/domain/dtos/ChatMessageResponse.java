package com.amit.studybuddy.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class ChatMessageResponse {
    private UUID id;
    private String content;
    private LocalDateTime timestamp;
    private UUID senderId;
    private String senderName;
}
