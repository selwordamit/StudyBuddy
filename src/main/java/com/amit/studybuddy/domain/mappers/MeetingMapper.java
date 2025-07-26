package com.amit.studybuddy.domain.mappers;

import com.amit.studybuddy.domain.dtos.MeetingResponse;
import com.amit.studybuddy.domain.dtos.MeetingRequest;
import com.amit.studybuddy.domain.entities.Meeting;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper (componentModel = "spring")
public interface MeetingMapper {

    MeetingResponse toResponse(Meeting meeting);
    @Mapping(target = "match", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ratings", ignore = true)

    Meeting toEntity(MeetingRequest request);
    List<MeetingResponse> toResponseList(List<Meeting> meetings);

}
