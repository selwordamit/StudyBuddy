package com.amit.studybuddy.services.implementations;

import com.amit.studybuddy.domain.dtos.ProfileResponse;
import com.amit.studybuddy.domain.dtos.UpdateProfileRequest;
import com.amit.studybuddy.domain.entities.Profile;
import com.amit.studybuddy.domain.mappers.ProfileMapper;
import com.amit.studybuddy.repositories.ProfileRepository;
import com.amit.studybuddy.services.ProfileService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@Slf4j
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;
    @Override
    public ProfileResponse getProfile(UUID userId) {
        log.info("[PROFILE_GET] - [userId={}] - STARTED", userId);

        Profile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> {
                    log.error("[PROFILE_GET] - [userId={}] - FAILED - Profile not found", userId);
                    return new EntityNotFoundException("Profile not found for userId: " + userId);
                });

        log.info("[PROFILE_GET] - [userId={}] - SUCCESS", userId);
        return profileMapper.toResponse(profile);
    }


    @Override
    public ProfileResponse updateProfile(UUID userId, UpdateProfileRequest profileRequest) {
        log.info("[PROFILE_UPDATE] - [userId={}] - STARTED", userId);

        Profile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> {
                    log.error("[PROFILE_UPDATE] - [userId={}] - FAILED - Profile not found", userId);
                    return new EntityNotFoundException("Profile not found for userId: " + userId);
                });

        //  apply updates from DTO
        profileMapper.updateProfileFromDto(profileRequest, profile);

        //  persist updated profile
        profileRepository.save(profile);

        log.info("[PROFILE_UPDATE] - [userId={}] - SUCCESS", userId);
        return profileMapper.toResponse(profile);
    }


}
