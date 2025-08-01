package com.amit.studybuddy.repositories;

import com.amit.studybuddy.domain.entities.UserCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserCourseRepository extends JpaRepository<UserCourse, UUID> {

    Optional<UserCourse> findByUserIdAndCourseId(UUID userId, UUID courseId);
    boolean existsByUserIdAndCourseId(UUID userId, UUID courseId);
    void deleteByUserIdAndCourseId(UUID userId, UUID courseId);
    Optional<UserCourse> findFirstByCourseIdAndUserIdNot(UUID courseId, UUID userId);
    List<UserCourse> findByCourseId(UUID courseId);

}
