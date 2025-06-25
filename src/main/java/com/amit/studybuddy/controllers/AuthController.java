package com.amit.studybuddy.controllers;


import com.amit.studybuddy.domain.dtos.AuthResponse;
import com.amit.studybuddy.domain.dtos.LoginRequest;
import com.amit.studybuddy.domain.dtos.RegisterRequest;
import com.amit.studybuddy.exceptions.ErrorResponse;
import com.amit.studybuddy.services.AuthService;
import jakarta.persistence.EntityExistsException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.text.html.parser.Entity;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

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
        AuthResponse authResponse = authService.login(loginRequest);
        log.info("Login request processed successfully");
        return ResponseEntity.ok(authResponse);
    }
}
