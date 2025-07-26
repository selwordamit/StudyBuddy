package com.amit.studybuddy.domain.dtos;


import com.amit.studybuddy.domain.enums.DegreeType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateProfileRequest {

        @NotNull
        private String fullName;
        private String bio;
        private String profilePictureUrl;
        @NotNull
        private String location;
        @NotNull
        private String institution;
        @NotNull
        private DegreeType degree;
        @NotNull
        private int studyYear;
        private Boolean emailNotificationsEnabled;
        private Boolean pushNotificationsEnabled;
}
