package ru.practicum.ewmmain.events.service;

import ru.practicum.ewmmain.events.dto.*;
import ru.practicum.ewmmain.events.model.EventStatus;
import ru.practicum.ewmmain.request.dto.ParticipantRequestDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface EventService {

    List<EventFullDto> getAllByAdmin(List<Long> users, List<EventStatus> states,
                                     List<Long> categories, LocalDateTime rangeStart,
                                     LocalDateTime rangeEnd, int from, int size);

    EventFullDto updateByAdmin(Long eventId, AdminUpdateEventRequest adminUpdateEventRequest);

    EventFullDto publishByAdmin(Long eventId);

    EventFullDto rejectDyAdmin(Long eventId);

    List<EventShortDto> getAllEventsByUser(Long userId, int from, int size);

    EventFullDto createEvent(Long userId, NewEventDto newEventDto);

    EventFullDto updateUserEvent(Long userId, UpdateEventRequest updateEventRequest);

    EventFullDto getUserEvent(Long userId, Long eventId);

    EventFullDto cancelUserEvent(Long userId, Long eventId);

    List<ParticipantRequestDto> getEventRequests(Long userId, Long eventId);

    ParticipantRequestDto confirmRequest(Long userId, Long eventId, Long requestId);

    ParticipantRequestDto rejectRequest(Long userId, Long eventId, Long requestId);

    EventFullDto getShortDtoById(Long eventId, HttpServletRequest requestId);

    List<EventShortDto> getAll(String text, List<Long> categories, Boolean paid,
                                     LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                     boolean onlyAvailable, String sort,
                                     Integer from, Integer size, HttpServletRequest request);

}
