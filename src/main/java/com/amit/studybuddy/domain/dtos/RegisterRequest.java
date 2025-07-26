package com.amit.studybuddy.domain.dtos;

import com.amit.studybuddy.domain.enums.DegreeType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class RegisterRequest {
    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @NotNull
    private String institute; // TODO : add institutes to JSON/ enum

    @NotNull(message = "Degree must be provided")
    private DegreeType degree;

    @Min(value = 1, message = "Study year must be at least 1")
    @Max(value = 4, message = "Study year must be at most 4")
    private int studyYear;
}