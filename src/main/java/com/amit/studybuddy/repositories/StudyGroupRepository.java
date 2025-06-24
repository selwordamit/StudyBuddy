package com.amit.studybuddy.repositories;

import com.amit.studybuddy.domain.entities.StudyGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudyGroupRepository extends JpaRepository<StudyGroup, UUID> {

    Optional<StudyGroup> findById(UUID id);

    List<StudyGroup> findByCourseId(UUID courseId);

    boolean existsByCourseId(UUID courseId);
}
