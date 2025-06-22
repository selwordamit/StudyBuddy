package com.amit.studybuddy.repositories;


import com.amit.studybuddy.domain.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CourseRepository extends JpaRepository<Course, UUID> {

    Optional<Course> findByName(String name);
    boolean existsByName(String name);
    void deleteById(UUID id);
}
