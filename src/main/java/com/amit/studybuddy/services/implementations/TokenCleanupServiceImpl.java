package com.amit.studybuddy.services.implementations;

import com.amit.studybuddy.repositories.VerificationTokenRepository;
import com.amit.studybuddy.services.TokenCleanupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenCleanupServiceImpl implements TokenCleanupService {

    private final VerificationTokenRepository verificationTokenRepository;

    // This method deletes all verification tokens that have expired
    @Override
    @Scheduled(fixedRate = 1000 * 60 * 60) // Every hour
    public void cleanExpiredTokens() {
        log.info(" [TOKEN_CLEANUP] - STARTED - Checking for expired tokens");
        verificationTokenRepository.deleteByExpiryDateBefore(LocalDateTime.now());
    }
}
