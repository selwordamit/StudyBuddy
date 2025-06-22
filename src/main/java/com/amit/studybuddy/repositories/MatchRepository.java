package com.amit.studybuddy.repositories;

import com.amit.studybuddy.domain.entities.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MatchRepository extends JpaRepository<Match, UUID> {
    Optional<Match> findByUserIdAndCourseId(UUID userId, UUID courseId);
    boolean existsByUserIdAndCourseId(UUID userId, UUID courseId);
    void deleteByUserIdAndCourseId(UUID userId, UUID courseId);
    Optional<Match> findByMatchedUserIdAndCourseId(UUID matchedUserId, UUID courseId);

}
