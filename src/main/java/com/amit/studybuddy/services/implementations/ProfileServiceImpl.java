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


/**
 * Service for managing user profiles.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;

    /**
     * Retrieves the profile of a user by userId.
     *
     * @param userId UUID of the user
     * @return ProfileResponse containing profile details
     */
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

    /**
     * Updates the profile of a user with the given request data.
     *
     * @param userId         UUID of the user
     * @param profileRequest Update request DTO
     * @return ProfileResponse with updated profile
     */
    @Override
    public ProfileResponse updateProfile(UUID userId, UpdateProfileRequest profileRequest) {
        log.info("[PROFILE_UPDATE] - [userId={}] - STARTED", userId);

        Profile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> {
                    log.error("[PROFILE_UPDATE] - [userId={}] - FAILED - Profile not found", userId);
                    return new EntityNotFoundException("Profile not found for userId: " + userId);
                });

        // Apply updates from DTO to entity
        profileMapper.updateProfileFromDto(profileRequest, profile);

        // Persist changes
        profileRepository.save(profile);

        log.info("[PROFILE_UPDATE] - [userId={}] - SUCCESS", userId);
        return profileMapper.toResponse(profile);
    }
}
