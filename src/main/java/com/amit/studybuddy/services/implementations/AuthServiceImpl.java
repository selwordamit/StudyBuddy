package com.amit.studybuddy.service.impl;

import com.amit.studybuddy.domain.dtos.AuthResponse;
import com.amit.studybuddy.domain.dtos.LoginRequest;
import com.amit.studybuddy.domain.dtos.RegisterRequest;
import com.amit.studybuddy.domain.entities.User;

import com.amit.studybuddy.domain.mappers.AuthMapper;
import com.amit.studybuddy.repositories.UserRepository;
import com.amit.studybuddy.security.UserDetailsImpl;
import com.amit.studybuddy.services.AuthService;
import com.amit.studybuddy.security.JwtService;
import jakarta.persistence.EntityExistsException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final AuthMapper authMapper;

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EntityExistsException("Email is already in use");
        }

        User user = authMapper.toUser(request, passwordEncoder);
        userRepository.save(user);

        String token = jwtService.generateToken(new UserDetailsImpl(user));
        return authMapper.toAuthResponse(user, token);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + request.getEmail()));

        String token = jwtService.generateToken(new UserDetailsImpl(user));
        return authMapper.toAuthResponse(user, token);
    }
}
