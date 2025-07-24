package com.amit.studybuddy.controllers;

import com.amit.studybuddy.domain.dtos.MeetingRequest;
import com.amit.studybuddy.domain.dtos.MeetingResponse;
import com.amit.studybuddy.domain.entities.Meeting;
import com.amit.studybuddy.services.MeetingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/meetings")
@RequiredArgsConstructor
@Slf4j
public class MeetingController {

    private final MeetingService meetingService;

    @PostMapping("/match/{matchId}")
    public ResponseEntity<MeetingResponse> createMeeting(
            @PathVariable UUID matchId,
            @Valid @RequestBody MeetingRequest meetingRequest) {
        log.info("[CREATE_MEETING] - [matchId={}] - STARTED", matchId);
        MeetingResponse meetingResponse = meetingService.createMeeting(matchId, meetingRequest);
        return ResponseEntity.ok(meetingResponse);

    }

    @GetMapping("/all")
    public ResponseEntity<List<MeetingResponse>> getAllMeetings() {
        log.info("FETCHING_ALL_MEETINGS");
        List<MeetingResponse> meetingResponses = meetingService.getAllMeetings();
        return ResponseEntity.ok(meetingResponses);
    }

    @GetMapping("/match/{matchId}")
    public ResponseEntity<List<MeetingResponse>> getMeetingsByMatch(@PathVariable UUID matchId) {
        log.info("[MEETING_FETCH_BY_MATCH] - [matchId={}] - STARTED", matchId);
        List<MeetingResponse> meetingResponses = meetingService.getMeetingsByMatchId(matchId);
        log.info("[MEETING_FETCH_BY_MATCH] - [matchId={}] - FOUND {} MEETINGS", matchId, meetingResponses.size());
        return ResponseEntity.ok(meetingResponses);
    }
    @DeleteMapping("/{meetingId}")

    public ResponseEntity<Void> deleteMeeting(@PathVariable UUID meetingId) {
        log.info("[MEETING_DELETE] - [meetingId={}] - REQUEST RECEIVED", meetingId);
        meetingService.deleteMeeting(meetingId);
        return ResponseEntity.noContent().build();
    }


}
