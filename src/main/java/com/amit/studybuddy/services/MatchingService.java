package com.amit.studybuddy.services;

import com.amit.studybuddy.domain.entities.Match;

import java.util.Optional;
import java.util.UUID;

public interface MatchingService {
    Optional<Match> tryMatch(UUID userId, UUID courseId);
}
