package com.amit.studybuddy.services;

import com.amit.studybuddy.domain.dtos.AuthResponse;
import com.amit.studybuddy.domain.dtos.LoginRequest;
import com.amit.studybuddy.domain.dtos.RegisterRequest;

public interface AuthService {

    AuthResponse register(RegisterRequest request);
    AuthResponse authenticateUser(LoginRequest request);
    public void verifyEmail(String token);

    public void resendVerificationEmail(String email);

}
