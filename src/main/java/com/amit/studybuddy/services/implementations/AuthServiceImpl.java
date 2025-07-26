package com.amit.studybuddy.services.implementations;

import com.amit.studybuddy.domain.dtos.AuthResponse;
import com.amit.studybuddy.domain.dtos.LoginRequest;
import com.amit.studybuddy.domain.dtos.RegisterRequest;
import com.amit.studybuddy.domain.entities.Profile;
import com.amit.studybuddy.domain.entities.User;

import com.amit.studybuddy.domain.entities.VerificationToken;
import com.amit.studybuddy.domain.mappers.AuthMapper;
import com.amit.studybuddy.repositories.ProfileRepository;
import com.amit.studybuddy.repositories.UserRepository;
import com.amit.studybuddy.repositories.VerificationTokenRepository;
import com.amit.studybuddy.security.UserDetailsImpl;
import com.amit.studybuddy.services.AuthService;
import com.amit.studybuddy.security.JwtService;
import com.amit.studybuddy.services.EmailService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Service responsible for user authentication, registration, and email verification logic.
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final AuthMapper authMapper;
    private final ProfileRepository profileRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final EmailService emailService;

    /**
     * Registers a new user and sends a verification email.
     *
     * @param request RegisterRequest containing user registration details
     * @return AuthResponse containing JWT token and user details
     */
    @Override
    public AuthResponse register(RegisterRequest request) {
        log.info("[REGISTER] - [{}] - STARTED", request.getEmail());

        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("[REGISTER] - [{}] - FAILED - Email already in use", request.getEmail());
            throw new EntityExistsException("Email is already in use");
        }

        if (!request.getEmail().endsWith(".ac.il") && !request.getEmail().contains("campus.ac.il")) {
            log.warn("[REGISTER] - [{}] - FAILED - Invalid academic email", request.getEmail());
            throw new IllegalArgumentException("Only academic email addresses are allowed");
        }

        // Step 1: Create and save user entity
        User user = authMapper.toUser(request, passwordEncoder);
        userRepository.save(user);

        // Step 2: Create empty profile for user
        Profile profile = Profile.builder()
                .user(user)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .bio("")
                .profilePictureUrl(null)
                .location("Unknown")
                .institution(request.getInstitute())
                .degree(request.getDegree())
                .studyYear(request.getStudyYear())
                .verified(false)
                .emailNotificationsEnabled(true)
                .pushNotificationsEnabled(true)
                .build();
        profileRepository.save(profile);

        // Step 3: Generate and save verification token
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = VerificationToken.builder()
                .token(token)
                .user(user)
                .expiryDate(LocalDateTime.now().plusHours(24))
                .build();
        verificationTokenRepository.save(verificationToken);

        // Step 4: Send verification email
        emailService.sendVerificationEmail(user, token);

        // Step 5: Generate JWT and return response
        String jwt = jwtService.generateToken(new UserDetailsImpl(user));
        log.info("[REGISTER] - [{}] - SUCCESS", request.getEmail());
        return authMapper.toAuthResponse(user, jwt);
    }

    /**
     * Authenticates a user using email and password and returns a JWT token.
     *
     * @param request LoginRequest containing login credentials
     * @return AuthResponse containing JWT token and user details
     */
    @Override
    public AuthResponse authenticateUser(LoginRequest request) {
        log.info("[LOGIN] - [{}] - STARTED", request.getEmail());

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    log.error("[LOGIN] - [{}] - FAILED - User not found", request.getEmail());
                    return new UsernameNotFoundException("User not found with email: " + request.getEmail());
                });

        Profile profile = profileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Profile not found"));

        if (!profile.getVerified()) {
            log.warn("[LOGIN] - [{}] - FAILED - Email not verified", request.getEmail());
            throw new AuthenticationCredentialsNotFoundException("Email not verified. Please check your inbox.");
        }

        // Perform authentication
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        // Generate JWT
        String token = jwtService.generateToken(new UserDetailsImpl(user));
        log.info("[LOGIN] - [{}] - SUCCESS", request.getEmail());
        return authMapper.toAuthResponse(user, token);
    }

    /**
     * Verifies a user's email using a token and activates their profile.
     *
     * @param token verification token sent via email
     */
    @Override
    public void verifyEmail(String token) {
        log.info("[VERIFY_EMAIL] - [token={}] - STARTED", token);

        VerificationToken vt = verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> {
                    log.error("[VERIFY_EMAIL] - [token={}] - FAILED - Invalid token", token);
                    return new IllegalArgumentException("Invalid token");
                });

        if (vt.getExpiryDate().isBefore(LocalDateTime.now())) {
            log.warn("[VERIFY_EMAIL] - [token={}] - FAILED - Token expired", token);
            throw new IllegalArgumentException("Token has expired");
        }

        Profile profile = profileRepository.findByUserId(vt.getUser().getId())
                .orElseThrow(() -> {
                    log.error("[VERIFY_EMAIL] - [userId={}] - FAILED - Profile not found", vt.getUser().getId());
                    return new EntityNotFoundException("Profile not found");
                });

        profile.setVerified(true);
        profileRepository.save(profile);
        verificationTokenRepository.delete(vt);

        log.info("[VERIFY_EMAIL] - [userId={}] - SUCCESS - Email verified", vt.getUser().getId());
    }

    /**
     * Resends a new verification email to the user if not already verified.
     *
     * @param email user's email address
     */
    @Override
    public void resendVerificationEmail(String email) {
        log.info("[RESEND_VERIFICATION] - [{}] - STARTED", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("[RESEND_VERIFICATION] - [{}] - FAILED - User not found", email);
                    return new UsernameNotFoundException("User not found");
                });

        if (user.getVerified()) {
            log.warn("[RESEND_VERIFICATION] - [{}] - SKIPPED - Already verified", email);
            throw new IllegalStateException("Email already verified");
        }

        // Remove old token (if any)
        verificationTokenRepository.deleteByUserId(user.getId());

        // Generate and save new token
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = VerificationToken.builder()
                .token(token)
                .user(user)
                .expiryDate(LocalDateTime.now().plusHours(24))
                .build();

        verificationTokenRepository.save(verificationToken);

        // Send new email
        emailService.sendVerificationEmail(user, token);

        log.info("[RESEND_VERIFICATION] - [{}] - SUCCESS - New token sent", email);
    }
}
