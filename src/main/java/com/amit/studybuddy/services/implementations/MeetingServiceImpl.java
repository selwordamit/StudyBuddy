package com.amit.studybuddy.services.implementations;

import com.amit.studybuddy.domain.dtos.MeetingRequest;
import com.amit.studybuddy.domain.dtos.MeetingResponse;
import com.amit.studybuddy.domain.entities.Match;
import com.amit.studybuddy.domain.entities.Meeting;
import com.amit.studybuddy.domain.enums.MeetingType;
import com.amit.studybuddy.domain.mappers.MeetingMapper;
import com.amit.studybuddy.repositories.MatchRepository;
import com.amit.studybuddy.repositories.MeetingRepository;
import com.amit.studybuddy.services.MeetingService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class MeetingServiceImpl implements MeetingService {

    private final MeetingRepository meetingRepository;
    private final MeetingMapper meetingMapper;
    private final MatchRepository matchRepository;

    /**
     * Creates a new meeting associated with a given match.
     * Validates that the match exists and maps the incoming request to a Meeting entity.
     *
     * @param matchId         the ID of the existing match between two users
     * @param meetingRequest  the request containing meeting details
     * @return MeetingResponse the created meeting's response DTO
     */
    @Override
    public MeetingResponse createMeeting(UUID matchId, MeetingRequest meetingRequest) {
        log.debug("[MEETING_CREATE] - [matchId={}] - Validating match existence", matchId);
        // Validate conditional fields based on meeting type
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
        // Check that the match exists
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> {
                    log.warn("[MEETING_CREATE] - [matchId={}] - Match not found", matchId);
                    return new EntityNotFoundException("Match not found with id: " + matchId);
                });

        // Map the request to a Meeting entity
        log.debug("[MEETING_CREATE] - [matchId={}] - Mapping request to entity", matchId);
        Meeting meeting = meetingMapper.toEntity(meetingRequest);
        meeting.setMatch(match);

        // Save the meeting
        meetingRepository.save(meeting);
        log.info("[MEETING_CREATE] - [matchId={}] - Meeting successfully created (id={})", matchId, meeting.getId());

        return meetingMapper.toResponse(meeting);
    }

    @Override
    public List<MeetingResponse> getAllMeetings() {
        log.debug("[MEETING_FETCH_ALL] - Fetching all meetings");

        List<Meeting> meetings = meetingRepository.findAll();
        log.info("[MEETING_FETCH_ALL] - Found {} meetings", meetings.size());

        return meetingMapper.toResponseList(meetings);
    }


    @Override
    public List<MeetingResponse> getMeetingsByMatchId(UUID matchId) {
        log.debug("[MEETING_FETCH_BY_MATCH] - [matchId={}] - Retrieving meetings", matchId);

        List<Meeting> meetings = meetingRepository.findByMatchId(matchId);
        log.info("[MEETING_FETCH_BY_MATCH] - [matchId={}] - Found {} meetings", matchId, meetings.size());

        return meetingMapper.toResponseList(meetings);
    }
    @Override
    public void deleteMeeting(UUID meetingId) {
        log.debug("[MEETING_DELETE] - [meetingId={}] - Validating existence", meetingId);

        if (!meetingRepository.existsById(meetingId)) {
            log.warn("[MEETING_DELETE] - [meetingId={}] - Not found", meetingId);
            throw new EntityNotFoundException("Meeting not found with id: " + meetingId);
        }

        meetingRepository.deleteById(meetingId);
        log.info("[MEETING_DELETE] - [meetingId={}] - SUCCESS", meetingId);
    }


}
