package com.amit.studybuddy.controllers;

import com.amit.studybuddy.domain.dtos.MeetingRequest;
import com.amit.studybuddy.domain.dtos.MeetingResponse;
import com.amit.studybuddy.security.UserDetailsImpl;
import com.amit.studybuddy.services.MeetingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/meetings")
@RequiredArgsConstructor
@Slf4j
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Meetings")
public class MeetingController {

    private final MeetingService meetingService;

    @PostMapping("/match/{matchId}")
    @Operation(summary = "Create a new meeting for a match")
    public ResponseEntity<MeetingResponse> createMeeting(
            @PathVariable UUID matchId,
            @Valid @RequestBody MeetingRequest meetingRequest,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        log.info("[MEETING_CREATE] - [userId={}] [matchId={}] - STARTED", userDetails.getId(), matchId);
        MeetingResponse response = meetingService.createMeeting(matchId, meetingRequest, userDetails.getUser());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    @Operation(summary = "Get all meetings (admin or debug use)")
    public ResponseEntity<List<MeetingResponse>> getAllMeetings(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        log.info("[MEETING_FETCH_ALL] - [userId={}] - STARTED", userDetails.getId());
        List<MeetingResponse> responses = meetingService.getAllMeetings();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/match/{matchId}")
    @Operation(summary = "Get meetings for a specific match")
    public ResponseEntity<List<MeetingResponse>> getMeetingsByMatch(
            @PathVariable UUID matchId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        log.info("[MEETING_FETCH_BY_MATCH] - [userId={}] [matchId={}] - STARTED", userDetails.getId(), matchId);
        List<MeetingResponse> responses = meetingService.getMeetingsByMatchId(matchId);
        log.info("[MEETING_FETCH_BY_MATCH] - [matchId={}] - FOUND {} MEETINGS", matchId, responses.size());
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/{meetingId}")
    @Operation(summary = "Delete a specific meeting")
    public ResponseEntity<Void> deleteMeeting(
            @PathVariable UUID meetingId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        log.info("[MEETING_DELETE] - [userId={}] [meetingId={}] - STARTED", userDetails.getId(), meetingId);
        meetingService.deleteMeeting(meetingId, userDetails.getUser());
        return ResponseEntity.noContent().build();
    }
}
