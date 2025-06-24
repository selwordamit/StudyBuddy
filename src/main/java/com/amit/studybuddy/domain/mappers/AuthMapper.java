package com.amit.studybuddy.domain.mappers;

import com.amit.studybuddy.domain.entities.User;
import com.amit.studybuddy.domain.dtos.AuthResponse;
import com.amit.studybuddy.domain.dtos.RegisterRequest;
import org.mapstruct.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.mapstruct.ReportingPolicy;


import java.time.LocalDateTime;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AuthMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", expression = "java(passwordEncoder.encode(request.getPassword()))")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")

    @Mapping(target = "role", constant = "USER")
    @Mapping(target = "verified", constant = "false")
    User toUser(RegisterRequest request, @Context PasswordEncoder passwordEncoder);

    default AuthResponse toAuthResponse(User user, String token) {
        return AuthResponse.builder()
                .token(token)
                .fullName(user.getFullName())
                .role(user.getRole())
                .build();
    }
}

