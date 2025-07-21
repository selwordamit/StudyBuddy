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
    @Operation(summary = "Try match", security = @SecurityRequirement(name = "bearerAuth"))
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
            return ResponseEntity.noContent().build();
        }
    }


}
