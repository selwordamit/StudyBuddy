package com.amit.studybuddy.domain.mappers;

import com.amit.studybuddy.domain.dtos.ProfileResponse;
import com.amit.studybuddy.domain.dtos.UpdateProfileRequest;
import com.amit.studybuddy.domain.entities.Profile;
import org.mapstruct.*;

/**
 * Mapper interface to convert between Profile entity and related DTOs.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProfileMapper {

    /**
     * Converts a Profile entity to a ProfileResponse DTO.
     * Used when sending profile details to the client.
     */
    ProfileResponse toResponse(Profile profile);

    // Converts a Profile entity into an UpdateProfileRequest DTO.
    UpdateProfileRequest toRequest(Profile profile);

    /**
     * Updates a Profile entity with non-null values from an UpdateProfileRequest DTO.
     * This method only overwrites properties that are not null.
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateProfileFromDto(UpdateProfileRequest request, @MappingTarget Profile profile);

    // Removed: AfterMapping updateUserFullName, as fullName is now split in Profile entity directly
}
