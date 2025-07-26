package com.amit.studybuddy.domain.mappers;

import com.amit.studybuddy.domain.dtos.ChatMessageResponse;
import com.amit.studybuddy.domain.entities.ChatMessage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ChatMapper {

    @Mapping(target = "senderId", source = "sender.id")
    @Mapping(target = "senderName", source = "sender.firstName")
    ChatMessageResponse toResponse(ChatMessage message);

    List<ChatMessageResponse> toResponseList(List<ChatMessage> messages);
}
