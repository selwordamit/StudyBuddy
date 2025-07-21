package com.amit.studybuddy.domain.dtos;

import com.amit.studybuddy.domain.entities.User;
import com.amit.studybuddy.domain.enums.MatchStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class MatchResponse {
    private UUID id;
    private UUID userId;
    private UUID matchedUserId;
    private UUID courseId;
    private MatchStatus status;
    private LocalDateTime createdAt;
}
