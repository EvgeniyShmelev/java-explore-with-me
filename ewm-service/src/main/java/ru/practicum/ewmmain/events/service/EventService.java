package ru.practicum.ewmmain.events.service;

import ru.practicum.ewmmain.events.dto.*;
import ru.practicum.ewmmain.request.dto.ParticipantRequestDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Collection;

public interface EventService {

    Collection<EventFullDto> getAllByAdmin(Collection<Long> users, Collection<String> states,
                                           Collection<Long> categories, LocalDateTime rangeStart,
                                           LocalDateTime rangeEnd, int from, int size);

    EventFullDto updateByAdmin(Long eventId, AdminUpdateEventRequest adminUpdateEventRequest);

    EventFullDto publishByAdmin(Long eventId);

    EventFullDto rejectDyAdmin(Long eventId);

    Collection<EventShortDto> getAllEventsByUser(Long userId, int from, int size);

    EventFullDto createEvent(Long userId, NewEventDto newEventDto);

    EventFullDto updateUserEvent(Long userId, UpdateEventRequest updateEventRequest);

    EventFullDto getUserEvent(Long userId, Long eventId);

    EventFullDto cancelUserEvent(Long userId, Long eventId);

    Collection<ParticipantRequestDto> getEventRequests(Long userId, Long eventId);

    ParticipantRequestDto confirmRequest(Long userId, Long eventId, Long requestId);

    ParticipantRequestDto rejectRequest(Long userId, Long eventId, Long requestId);

    EventFullDto getShortDtoById(Long eventId, HttpServletRequest requestId);

    Collection<EventShortDto> getAll(String text, Collection<Long> categories, boolean paid,
                                     LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                     boolean onlyAvailable, String sort,
                                     Integer from, Integer size, HttpServletRequest request);

}
