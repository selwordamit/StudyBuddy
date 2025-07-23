package com.amit.studybuddy.domain.dtos;

import com.amit.studybuddy.domain.enums.MeetingType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.SuperBuilder;


import java.time.LocalDateTime;
import java.util.UUID;

@SuperBuilder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MeetingResponse {
    private UUID id;
    private MeetingType type;
    private String location;
    private String zoomLink;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime scheduledTime;

}
