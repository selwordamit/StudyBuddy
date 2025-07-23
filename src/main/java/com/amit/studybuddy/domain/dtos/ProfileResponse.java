package com.amit.studybuddy.domain.dtos;


import jakarta.persistence.Column;
import lombok.*;

@Getter
@AllArgsConstructor
@Builder
public class ProfileResponse {
    private String fullName;
    private String bio;
    private String profilePictureUrl;
    private String location;
    private String institution;
    private String degree;
    private int studyYear;
    private Boolean emailNotificationsEnabled;
    private Boolean pushNotificationsEnabled;
}

