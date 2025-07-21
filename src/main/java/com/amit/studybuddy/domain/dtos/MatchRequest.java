package com.amit.studybuddy.domain.dtos;

import lombok.Data;

import java.util.UUID;

@Data
public class MatchRequest {
    private UUID courseId;
}
