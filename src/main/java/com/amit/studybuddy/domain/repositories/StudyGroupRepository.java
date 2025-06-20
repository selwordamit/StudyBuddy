package com.amit.studybuddy.domain.repositories;

import com.amit.studybuddy.domain.entities.StudyGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudyGroupRepository extends JpaRepository<StudyGroup, UUID> {

    Optional<StudyGroup> findByName(String name);
    boolean existsByName(String name);
    void deleteById(UUID id);
    Optional<StudyGroup> findByIdAndUserId(UUID id, UUID userId);
    boolean existsByIdAndUserId(UUID id, UUID userId);
    void deleteByIdAndUserId(UUID id, UUID userId);
}
