package com.amit.studybuddy.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class CourseResponse {
    private UUID id;
    private String name;
}
