package com.amit.studybuddy.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class CourseResponse {
    private UUID id;
    private String name;
}
