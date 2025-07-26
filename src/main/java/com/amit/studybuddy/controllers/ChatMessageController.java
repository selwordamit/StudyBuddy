package com.amit.studybuddy.controllers;


import com.amit.studybuddy.domain.dtos.ChatMessageRequest;
import com.amit.studybuddy.domain.dtos.ChatMessageResponse;
import com.amit.studybuddy.domain.entities.ChatMessage;
import com.amit.studybuddy.domain.entities.User;
import com.amit.studybuddy.repositories.ChatMessageRepository;
import com.amit.studybuddy.security.UserDetailsImpl;
import com.amit.studybuddy.services.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/chat/messages")
@RequiredArgsConstructor
@Slf4j
public class ChatMessageController {

    private final ChatService chatService;
    private final ChatMessageRepository chatMessageRepository;


    @PostMapping
    @Operation(summary = "Send message")
    public ResponseEntity<ChatMessageResponse> sendMessage(
            @RequestBody @Valid ChatMessageRequest request,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ChatMessageResponse response = chatService.sendMessage(
                request.getMatchId(),
                userDetails.getUser(),
                request.getContent()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{matchId}")
    public ResponseEntity<List<ChatMessageResponse>> getAllMatchMessages(
            @PathVariable UUID matchId,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        List<ChatMessageResponse> messages = chatService.getMessagesByMatch(matchId);
        return ResponseEntity.ok(messages);
    }
}

