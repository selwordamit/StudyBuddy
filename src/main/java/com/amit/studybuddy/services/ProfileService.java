package com.amit.studybuddy.services;


import com.amit.studybuddy.domain.dtos.ProfileResponse;
import com.amit.studybuddy.domain.dtos.UpdateProfileRequest;

import java.util.UUID;


public interface ProfileService {

    ProfileResponse getProfile(UUID userId);
    ProfileResponse updateProfile(UUID userId, UpdateProfileRequest profileRequest);
}
