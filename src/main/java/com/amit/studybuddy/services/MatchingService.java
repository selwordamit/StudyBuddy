package com.amit.studybuddy.services;

import com.amit.studybuddy.domain.dtos.MatchResponse;
import com.amit.studybuddy.domain.entities.Match;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MatchingService {
    Optional<Match> tryMatch(UUID userId, UUID courseId);
    List<Match> getAllUserMatches(UUID userId);

}
