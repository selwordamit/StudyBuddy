package com.amit.studybuddy.domain.dtos;


import com.amit.studybuddy.domain.enums.DegreeType;
import jakarta.persistence.Column;
import lombok.*;

@Getter
@AllArgsConstructor
@Builder
public class ProfileResponse {
    private String firstName;
    private String lastName;
    private String bio;
    private String profilePictureUrl;
    private String location;
    private String institution;
    private DegreeType degree;
    private int studyYear;
    private Boolean emailNotificationsEnabled;
    private Boolean pushNotificationsEnabled;
}

