package ru.practicum.ewmmain.events.mapper;

import ru.practicum.ewmmain.events.dto.EventFullDto;
import ru.practicum.ewmmain.events.model.Event;

public class EventMapper {
    public static EventFullDto toEventFullDto(Event event, long confirmedRequests, long views) {
        return new EventFullDto(
                event.getAnnotation(),
                event.getCategory(),
                confirmedRequests,
                event.getCreatedOn(),
                event.getDescription(),
                event.getEventDate(),
                event.getId(),
                event.getInitiator(),
                event.getLocation(),
                event.getPaid(),
                event.getParticipantLimit(),
                event.getPublishedOn(),
                event.getRequestModeration(),
                event.getState(),
                event.getTitle(),
                views
        );
    }
}
