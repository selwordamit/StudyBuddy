package com.amit.studybuddy.controllers;

import com.amit.studybuddy.domain.dtos.CourseResponse;
import com.amit.studybuddy.domain.entities.DegreeCourse;
import com.amit.studybuddy.domain.entities.Profile;
import com.amit.studybuddy.domain.enums.DegreeType;
import com.amit.studybuddy.repositories.DegreeCourseRepository;
import com.amit.studybuddy.repositories.ProfileRepository;
import com.amit.studybuddy.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/courses")
@RequiredArgsConstructor
public class CourseController {

    private final DegreeCourseRepository degreeCourseRepository;
    private final ProfileRepository profileRepository;


    @GetMapping("/by-degree")
    @Operation(summary = "Get courses by degree and year")
    public ResponseEntity<List<CourseResponse>> getCoursesByDegreeAndYear(
            @RequestParam DegreeType degree,
            @RequestParam int year) {

        List<DegreeCourse> degreeCourses = degreeCourseRepository.findAllByDegreeAndStudyYear(degree, year);

        List<CourseResponse> response = degreeCourses.stream()
                .map(dc -> new CourseResponse(dc.getCourse().getId(), dc.getCourse().getName()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/my-degree")
    @Operation(summary = "Get courses based on the user's profile (degree and study year)")
    public ResponseEntity<List<CourseResponse>> getCoursesByUserProfile(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        UUID userId = userDetails.getId();

        Profile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Profile not found for user ID: " + userId));

        DegreeType degree = profile.getDegree();
        int studyYear = profile.getStudyYear();

        List<DegreeCourse> degreeCourses = degreeCourseRepository.findAllByDegreeAndStudyYear(degree, studyYear);

        List<CourseResponse> response = degreeCourses.stream()
                .map(dc -> new CourseResponse(dc.getCourse().getId(), dc.getCourse().getName()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

}
