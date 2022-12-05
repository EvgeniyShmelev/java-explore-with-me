package ru.practicum.ewmmain.events.mapper;

import ru.practicum.ewmmain.events.dto.EventShortDto;
import ru.practicum.ewmmain.events.model.Event;

public class EventMapper {
    public static EventShortDto toEventShortDto(Event event, long confirmedRequests, long views) {
        return new EventShortDto(
                event.getAnnotation(),
                event.getCategory(),
                confirmedRequests,
                event.getEventDate(),
                event.getId(),
                event.getInitiator(),
                event.getPaid(),
                event.getTitle(),
                views
        );
    }
}
