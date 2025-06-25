package com.amit.studybuddy.domain.mappers;

import com.amit.studybuddy.domain.dtos.ProfileResponse;
import com.amit.studybuddy.domain.dtos.UpdateProfileRequest;
import com.amit.studybuddy.domain.entities.Profile;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProfileMapper {

    // Converts a Profile entity to a ProfileResponse DTO
    ProfileResponse toResponse(Profile profile);

    // Converts a Profile entity to an UpdateProfileRequest DTO
    UpdateProfileRequest toRequest(Profile profile);

    // Update existing Profile with new values (ignore nulls)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateProfileFromDto(UpdateProfileRequest request, @MappingTarget Profile profile);

    // Set full name inside nested User
    @AfterMapping
    default void updateUserFullName(UpdateProfileRequest request, @MappingTarget Profile profile) {
        if (request.getFullName() != null) {
            profile.getUser().setFullName(request.getFullName());
        }
    }
}
