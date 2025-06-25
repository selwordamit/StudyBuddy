package com.amit.studybuddy.domain.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateProfileRequest {

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
