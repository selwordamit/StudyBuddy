package com.amit.studybuddy.services.implementations;

import com.amit.studybuddy.domain.dtos.MeetingRequest;
import com.amit.studybuddy.domain.dtos.MeetingResponse;
import com.amit.studybuddy.domain.entities.Match;
import com.amit.studybuddy.domain.entities.Meeting;
import com.amit.studybuddy.domain.entities.User;
import com.amit.studybuddy.domain.enums.MeetingType;
import com.amit.studybuddy.domain.mappers.MeetingMapper;
import com.amit.studybuddy.repositories.MatchRepository;
import com.amit.studybuddy.repositories.MeetingRepository;
import com.amit.studybuddy.services.MeetingService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class MeetingServiceImpl implements MeetingService {

    private final MeetingRepository meetingRepository;
    private final MeetingMapper meetingMapper;
    private final MatchRepository matchRepository;

    /**
     * Creates a new meeting for a given match.
     * Validates meeting type-specific fields and ensures the authenticated user is part of the match.
     *
     * @param matchId ID of the match between two users
     * @param meetingRequest the meeting data (location, time, etc.)
     * @param creator the currently authenticated user
     * @return MeetingResponse DTO of the saved meeting
     */
    @Override
    public MeetingResponse createMeeting(UUID matchId, MeetingRequest meetingRequest, User creator) {
        log.debug("[MEETING_CREATE] - [userId={}] [matchId={}] - Validating match existence", creator.getId(), matchId);

        // Validate meeting details based on type (ONLINE vs IN_PERSON)
        if (meetingRequest.getType() == MeetingType.ONLINE) {
            if (meetingRequest.getZoomLink() == null || meetingRequest.getZoomLink().isBlank()) {
                throw new IllegalArgumentException("Zoom link is required for online meetings");
            }
            if (meetingRequest.getLocation() != null && !meetingRequest.getLocation().isBlank()) {
                throw new IllegalArgumentException("Location must be empty for online meetings");
            }
        }

        if (meetingRequest.getType() == MeetingType.IN_PERSON) {
            if (meetingRequest.getLocation() == null || meetingRequest.getLocation().isBlank()) {
                throw new IllegalArgumentException("Location is required for in-person meetings");
            }
            if (meetingRequest.getZoomLink() != null && !meetingRequest.getZoomLink().isBlank()) {
                throw new IllegalArgumentException("Zoom link must be empty for in-person meetings");
            }
        }

        // Load and validate the match
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> {
                    log.warn("[MEETING_CREATE] - [matchId={}] - Match not found", matchId);
                    return new EntityNotFoundException("Match not found with id: " + matchId);
                });

        // Ensure the user is part of the match (security check)
        if (!match.getUser().getId().equals(creator.getId()) &&
                !match.getMatchedUser().getId().equals(creator.getId())) {
            log.warn("[MEETING_CREATE] - [userId={}] - Not authorized for match {}", creator.getId(), matchId);
            throw new AccessDeniedException("You are not a participant in this match");
        }

        // Map DTO to entity and save
        Meeting meeting = meetingMapper.toEntity(meetingRequest);
        meeting.setMatch(match);

        meetingRepository.save(meeting);

        log.info("[MEETING_CREATE] - [matchId={}] [meetingId={}] - SUCCESS", matchId, meeting.getId());
        return meetingMapper.toResponse(meeting);
    }

    /**
     * Fetches all meetings in the system (admin/debug only).
     */
    @Override
    public List<MeetingResponse> getAllMeetings() {
        log.debug("[MEETING_FETCH_ALL] - Fetching all meetings");

        List<Meeting> meetings = meetingRepository.findAll();

        log.info("[MEETING_FETCH_ALL] - Found {} meetings", meetings.size());
        return meetingMapper.toResponseList(meetings);
    }

    /**
     * Fetches all meetings related to a specific match.
     */
    @Override
    public List<MeetingResponse> getMeetingsByMatchId(UUID matchId) {
        log.debug("[MEETING_FETCH_BY_MATCH] - [matchId={}] - Retrieving meetings", matchId);

        List<Meeting> meetings = meetingRepository.findByMatchId(matchId);

        log.info("[MEETING_FETCH_BY_MATCH] - [matchId={}] - Found {} meetings", matchId, meetings.size());
        return meetingMapper.toResponseList(meetings);
    }

    /**
     * Deletes a meeting by ID. Future versions may restrict to meeting creator or match participant.
     */
    @Override
    public void deleteMeeting(UUID meetingId, User user) {
        log.debug("[MEETING_DELETE] - [userId={}] [meetingId={}] - Request to delete", user.getId(), meetingId);

        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> {
                    log.warn("[MEETING_DELETE] - [meetingId={}] - Not found", meetingId);
                    return new EntityNotFoundException("Meeting not found with id: " + meetingId);
                });

        // Optional: Check if user is participant in the match before deletion

        meetingRepository.deleteById(meetingId);

        log.info("[MEETING_DELETE] - [meetingId={}] - SUCCESS", meetingId);
    }
}
