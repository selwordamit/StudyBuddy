package com.amit.studybuddy.domain.mappers;
import com.amit.studybuddy.domain.dtos.MatchResponse;
import com.amit.studybuddy.domain.entities.Match;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MatchMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "matchedUser.id", target = "matchedUserId")
    @Mapping(source = "course.id", target = "courseId")
    @Mapping(source = "status", target = "status")
    MatchResponse toMatchResponse(Match match);
    List<MatchResponse> toMatchResponseList(List<Match> matches);


}