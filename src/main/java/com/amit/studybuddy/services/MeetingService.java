package com.amit.studybuddy.services;

import com.amit.studybuddy.domain.dtos.MeetingRequest;
import com.amit.studybuddy.domain.dtos.MeetingResponse;
import com.amit.studybuddy.domain.entities.Meeting;
import com.amit.studybuddy.domain.entities.User;

import java.util.List;
import java.util.UUID;

public interface MeetingService {
    public MeetingResponse createMeeting(UUID matchId, MeetingRequest meetingRequest, User creator);
    List<MeetingResponse> getAllMeetings();
    List<MeetingResponse> getMeetingsByMatchId(UUID matchId);
    public void deleteMeeting(UUID meetingId, User user);



}