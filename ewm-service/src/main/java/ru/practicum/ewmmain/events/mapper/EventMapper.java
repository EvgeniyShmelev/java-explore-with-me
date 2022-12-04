package ru.practicum.ewmmain.events.mapper;

import lombok.Data;

@Data
public class EventMapper {
   /* private static ModelMapper modelMapper;

    public static EventFullDto toEventFullDto(Event event) {
        Location location = new Location();
        location.setLat(event.getLat());
        location.setLon(event.getLon());

        return EventFullDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .description(event.getDescription())
                .eventDate(event.getEventDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .location(location)
                .category(modelMapper.map(event.getCategory(), CategoryDto.class))
                .participantLimit(event.getParticipantLimit())
                .paid(event.getPaid())
                .requestModeration(event.getRequestModeration())
                .confirmedRequests(event.getConfirmedRequests())
                .createdOn(event.getCreatedOn())
                .publishedOn(event.getPublishedOn())
                .views(event.getViews())
                .initiator(event.getInitiator())
                .state(event.getState().toString())
                .build();
    }*/
}
