package com.amit.studybuddy.controllers;


import com.amit.studybuddy.domain.dtos.ProfileResponse;
import com.amit.studybuddy.domain.dtos.UpdateProfileRequest;
import com.amit.studybuddy.security.UserDetailsImpl;
import com.amit.studybuddy.services.ProfileService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
@Slf4j
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping
    public ResponseEntity<ProfileResponse> getProfile() {
        UUID userId = getCurrentUserId();
        log.info("[PROFILE_GET] - [userId={}] - Controller received request", userId);
        ProfileResponse response = profileService.getProfile(userId);
        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<ProfileResponse> updateProfile(@Valid @RequestBody UpdateProfileRequest request) {
        UUID userId = getCurrentUserId();
        log.info("[PROFILE_UPDATE] - [userId={}] - Controller received update request", userId);
        ProfileResponse updated = profileService.updateProfile(userId, request);
        return ResponseEntity.ok(updated);
    }

    // helper method to get userId from JWT
    private UUID getCurrentUserId() {
        UserDetailsImpl user = (UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        return user.getId();
    }
}
