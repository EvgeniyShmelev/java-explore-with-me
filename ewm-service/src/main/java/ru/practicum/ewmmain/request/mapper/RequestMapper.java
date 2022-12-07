package ru.practicum.ewmmain.request.mapper;

import ru.practicum.ewmmain.request.dto.ParticipantRequestDto;
import ru.practicum.ewmmain.request.model.Request;

public class RequestMapper {
    public static ParticipantRequestDto toRequestDto(Request request) {
        return new ParticipantRequestDto(
                request.getId(),
                request.getEvent().getId(),
                request.getRequester().getId(),
                request.getCreated(),
                request.getStatus()
        );
    }
}
