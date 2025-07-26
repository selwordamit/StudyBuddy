package com.amit.studybuddy.repositories;

import com.amit.studybuddy.domain.entities.DegreeCourse;
import com.amit.studybuddy.domain.enums.DegreeType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DegreeCourseRepository extends JpaRepository<DegreeCourse, UUID> {

    List<DegreeCourse> findAllByDegreeAndStudyYear(DegreeType degree, int studyYear);
}
