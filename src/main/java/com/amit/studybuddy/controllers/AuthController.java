package com.amit.studybuddy.controllers;


import com.amit.studybuddy.domain.dtos.AuthResponse;
import com.amit.studybuddy.domain.dtos.LoginRequest;
import com.amit.studybuddy.domain.dtos.RegisterRequest;
import com.amit.studybuddy.domain.entities.User;
import com.amit.studybuddy.exceptions.ErrorResponse;
import com.amit.studybuddy.repositories.ProfileRepository;
import com.amit.studybuddy.repositories.UserRepository;
import com.amit.studybuddy.repositories.VerificationTokenRepository;
import com.amit.studybuddy.services.AuthService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.parser.Entity;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository; // Test purpose only, not used in production code
    private final VerificationTokenRepository verificationTokenRepository; // Test purpose only, not used in production code
    private final ProfileRepository profileRepository; // Test purpose only, not used in production code

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        log.info("Received register request");
        AuthResponse authResponse = authService.register(registerRequest);
        log.info("Register request processed successfully");
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        log.info("Received login request");
        AuthResponse authResponse = authService.authenticateUser(loginRequest);
        log.info("Login request processed successfully");
        return ResponseEntity.ok(authResponse);
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyEmail(@RequestParam("token") String token) {
        log.info("Verifying email with token");
        authService.verifyEmail(token);
        return ResponseEntity.ok("Email verified successfully!");
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<String> resendVerification(@RequestParam("email") String email) {
        log.info("Resending verification email to {}", email);
        authService.resendVerificationEmail(email);
        return ResponseEntity.ok("Verification email resent");
    }

    // for testing purposes
    @DeleteMapping("/dev/delete-user")
    @Transactional
    public ResponseEntity<Void> deleteUser(@RequestParam String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        verificationTokenRepository.deleteByUserId(user.getId());
        profileRepository.deleteByUserId(user.getId());
        userRepository.delete(user);
        return ResponseEntity.noContent().build();
    }

}
