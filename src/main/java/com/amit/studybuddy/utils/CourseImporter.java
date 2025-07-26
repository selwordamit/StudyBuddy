package com.amit.studybuddy.init;

import com.amit.studybuddy.domain.dtos.CourseImportDto;
import com.amit.studybuddy.domain.entities.Course;
import com.amit.studybuddy.domain.entities.DegreeCourse;
import com.amit.studybuddy.domain.enums.DegreeType;
import com.amit.studybuddy.repositories.CourseRepository;
import com.amit.studybuddy.repositories.DegreeCourseRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j // Enables logging via 'log' variable
public class CourseImporter {

    private final CourseRepository courseRepository;
    private final DegreeCourseRepository degreeCourseRepository;
    private final ObjectMapper objectMapper;

    @PostConstruct
    public void loadCourses() throws Exception {
        log.info("[COURSE_IMPORT] - Starting course import process...");

        InputStream inputStream = getClass().getResourceAsStream("/courses.json");
        CourseImportDto[] dtos = objectMapper.readValue(inputStream, CourseImportDto[].class);

        for (CourseImportDto dto : dtos) {
            log.debug("[COURSE_IMPORT] - Processing course: {}, degree: {}, year: {}",
                    dto.getName(), dto.getDegree(), dto.getYear());

            // Try to find the course by name, or create it if it doesn't exist
            Course course = courseRepository.findByName(dto.getName())
                    .orElseGet(() -> {
                        Course newCourse = Course.builder()
                                .id(UUID.randomUUID())
                                .name(dto.getName())
                                .build();
                        log.info("[COURSE_IMPORT] - Inserting new course: {}", newCourse.getName());
                        return courseRepository.save(newCourse);
                    });

            DegreeType degreeType = DegreeType.valueOf(dto.getDegree());

            // Check if the course is already linked to the given degree and year
            boolean alreadyExists = degreeCourseRepository
                    .findAllByDegreeAndStudyYear(degreeType, dto.getYear())
                    .stream()
                    .anyMatch(dc -> dc.getCourse().getName().equals(dto.getName()));

            if (!alreadyExists) {
                degreeCourseRepository.save(
                        DegreeCourse.builder()
                                .degree(degreeType)
                                .studyYear(dto.getYear())
                                .course(course)
                                .build()
                );
                log.info("[COURSE_IMPORT] - Linked course '{}' to degree '{}' (year {})",
                        course.getName(), degreeType, dto.getYear());
            } else {
                log.debug("[COURSE_IMPORT] - Link already exists: {} → {} year {}",
                        course.getName(), degreeType, dto.getYear());
            }
        }

        log.info("[COURSE_IMPORT] - Course import completed. Total courses processed: {}", dtos.length);
    }
}
