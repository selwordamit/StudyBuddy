package com.amit.studybuddy.services;

import com.amit.studybuddy.domain.dtos.MeetingRequest;
import com.amit.studybuddy.domain.dtos.MeetingResponse;
import com.amit.studybuddy.domain.entities.Meeting;

import java.util.List;
import java.util.UUID;

public interface MeetingService {
    MeetingResponse createMeeting(UUID matchId, MeetingRequest meetingRequest);
    List<MeetingResponse> getAllMeetings();
    List<MeetingResponse> getMeetingsByMatchId(UUID matchId);
    void deleteMeeting(UUID meetingId);



}