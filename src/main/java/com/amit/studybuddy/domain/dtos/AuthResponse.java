package com.amit.studybuddy.domain.dtos;

import com.amit.studybuddy.domain.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class AuthResponse {

    private String token;
    private String firstName;
    private String lastName;
    private Role role;
}
