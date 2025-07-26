package com.amit.studybuddy.controllers;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import com.amit.studybuddy.domain.dtos.MatchRequest;
import com.amit.studybuddy.domain.dtos.MatchResponse;
import com.amit.studybuddy.domain.entities.Match;
import com.amit.studybuddy.domain.mappers.MatchMapper;
import com.amit.studybuddy.repositories.MatchRepository;
import com.amit.studybuddy.security.UserDetailsImpl;
import com.amit.studybuddy.services.MatchingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/match")
@RequiredArgsConstructor
@Slf4j
public class MatchController {

    private final MatchingService matchingService;
    private final MatchMapper matchMapper;
    private final MatchRepository matchRepository;

    /**
     * Attempts to match the current user to another student for a specific course.
     */
    @Operation(summary = "Try match")
    @PostMapping
    public ResponseEntity<?> tryMatch(
            @RequestBody @Valid MatchRequest request,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {

        UUID userId = userDetails.getId();
        UUID courseId = request.getCourseId();

        log.info("[MATCH_TRY] - [userId={}] [courseId={}] - REQUEST RECEIVED", userId, courseId);

        Optional<Match> matchOpt = matchingService.tryMatch(userId, courseId);

        if (matchOpt.isPresent()) {
            Match match = matchOpt.get();
            MatchResponse response = matchMapper.toMatchResponse(match);
            log.info("[MATCH_TRY] - [userId={}] [courseId={}] [matchId={}] - MATCH FOUND", userId, courseId, match.getId());
            return ResponseEntity.ok(response);
        } else {
            log.info("[MATCH_TRY] - [userId={}] [courseId={}] - NO MATCH FOUND", userId, courseId);
            return ResponseEntity.noContent().build(); // 204
        }
    }

    /**
     * Get the first active match of the current user.
     */
    @Operation(summary = "Get current match")
    @GetMapping
    public ResponseEntity<?> getCurrentMatch(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        UUID userId = userDetails.getId();
        log.info("[MATCH_GET] - [userId={}] - STARTED", userId);

        Optional<Match> matchOpt = matchRepository
                .findFirstByUserIdOrMatchedUserId(userId, userId);

        if (matchOpt.isPresent()) {
            MatchResponse response = matchMapper.toMatchResponse(matchOpt.get());
            log.info("[MATCH_GET] - [userId={}] [matchId={}] - FOUND", userId, response.getId());
            return ResponseEntity.ok(response);
        } else {
            log.info("[MATCH_GET] - [userId={}] - NO MATCH FOUND", userId);
            return ResponseEntity.noContent().build(); // 204
        }
    }

    /**
     * Get all matches that involve the current user (initiator or matched).
     */
    @Operation(summary = "Get all matches for current user")
    @GetMapping("/all")
    public ResponseEntity<?> getAllUserMatches(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        UUID userId = userDetails.getId();
        log.info("[MATCH_GET_ALL] - [userId={}] - STARTED", userId);

        var matches = matchingService.getAllUserMatches(userId);

        if (matches.isEmpty()) {
            log.info("[MATCH_GET_ALL] - [userId={}] - NO MATCHES FOUND", userId);
            return ResponseEntity.noContent().build(); // 204
        }

        var responseList = matches.stream()
                .map(matchMapper::toMatchResponse)
                .toList();

        log.info("[MATCH_GET_ALL] - [userId={}] - FOUND {} MATCHES", userId, responseList.size());
        return ResponseEntity.ok(responseList);
    }
}
