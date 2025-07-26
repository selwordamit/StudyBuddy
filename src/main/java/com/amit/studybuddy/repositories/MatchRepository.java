package com.amit.studybuddy.repositories;

import com.amit.studybuddy.domain.entities.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MatchRepository extends JpaRepository<Match, UUID> {

    // Retrieve all matches where the user is either the initiator or the matched user
    List<Match> findAllByUserIdOrMatchedUserId(UUID userId1, UUID userId2);

    // Retrieve the first match for the user (used for "current match" feature)
    Optional<Match> findFirstByUserIdOrMatchedUserId(UUID userId1, UUID userId2);

    // Check if a match already exists for this user and course (either side of the match)
    boolean existsByUserIdAndCourseIdOrMatchedUserIdAndCourseId(
            UUID userId1, UUID courseId1, UUID userId2, UUID courseId2
    );

    // Retrieve an existing match by course and user (regardless of initiator/matched position)
    Optional<Match> findByUserIdAndCourseIdOrMatchedUserIdAndCourseId(
            UUID userId1, UUID courseId1, UUID userId2, UUID courseId2
    );
}