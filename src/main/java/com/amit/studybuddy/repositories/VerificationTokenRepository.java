package com.amit.studybuddy.repositories;

import com.amit.studybuddy.domain.entities.VerificationToken;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, UUID> {

    Optional<VerificationToken> findByToken(String token);

    @Modifying // This annotation is used to indicate that the method modifies the database
    @Transactional
    @Query("DELETE FROM VerificationToken v WHERE v.user.id = :userId") // Custom query to delete tokens by user ID
    void deleteByUserId(UUID userId); // Test purposes

    @Modifying
    @Transactional
    void deleteByExpiryDateBefore(LocalDateTime dateTime);


}
