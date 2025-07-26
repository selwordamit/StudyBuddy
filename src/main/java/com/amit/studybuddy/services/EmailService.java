package com.amit.studybuddy.services;

import com.amit.studybuddy.domain.entities.User;

public interface EmailService {
    void sendVerificationEmail(User firstName, String token);
}
