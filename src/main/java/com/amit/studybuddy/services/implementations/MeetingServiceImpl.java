package com.amit.studybuddy.services.implementations;

import com.amit.studybuddy.domain.dtos.MeetingRequest;
import com.amit.studybuddy.domain.dtos.MeetingResponse;
import com.amit.studybuddy.domain.entities.Match;
import com.amit.studybuddy.domain.entities.Meeting;
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
        log.info("[MEETING_CREATE] - [matchId={}] - STARTED", matchId);

        // Check that the match exists
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> {
                    log.warn("[MEETING_CREATE] - Match not found with ID: {}", matchId);
                    return new EntityNotFoundException("Match not found with id: " + matchId);
                });

        // Map the request to a Meeting entity
        Meeting meeting = meetingMapper.toEntity(meetingRequest);
        meeting.setMatch(match);

        // Save the meeting
        meetingRepository.save(meeting);

        log.info("[MEETING_CREATE] - [matchId={}] - MEETING CREATED SUCCESSFULLY", matchId);

        return meetingMapper.toResponse(meeting);
    }

    @Override
    public List<MeetingResponse> getMeetingsByMatchId(UUID matchId) {
        log.info("[MEETING_FETCH_ALL] - [matchId={}] - STARTED", matchId);

        List<Meeting> meetings = meetingRepository.findByMatchId(matchId);

        log.info("[MEETING_FETCH_ALL] - [matchId={}] - FOUND {} MEETINGS", matchId, meetings.size());

        return meetingMapper.toResponseList(meetings);
    }

}
