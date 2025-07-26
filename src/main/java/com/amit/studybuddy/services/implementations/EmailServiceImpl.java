package com.amit.studybuddy.services.implementations;

import com.amit.studybuddy.domain.entities.User;
import com.amit.studybuddy.services.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.beans.factory.annotation.Value;





@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Override
    @Async
    public void sendVerificationEmail(User user, String token) {
        String to = user.getEmail();
        String subject = "StudyBuddy - Verify your email";
        String link = "http://localhost:8080/api/v1/auth/verify?token=" + token;

        String body = String.format("""
                Hi %s,
                
                Welcome to StudyBuddy! Please verify your account by clicking the link below:
                %s
                
                If you didn't register, ignore this email.
                """, user.getFirstName(), link);

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);

            mailSender.send(message);
            log.info("Verification email sent to {}", to);
        } catch (Exception e) {
            log.error("Failed to send email to {}: {}", to, e.getMessage());
        }
    }
}
