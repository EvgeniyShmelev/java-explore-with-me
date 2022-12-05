package ru.practicum.ewmmain.events.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmmain.events.dto.EventFullDto;
import ru.practicum.ewmmain.events.dto.EventShortDto;
import ru.practicum.ewmmain.events.dto.NewEventDto;
import ru.practicum.ewmmain.events.dto.UpdateEventRequest;
import ru.practicum.ewmmain.events.service.EventService;
import ru.practicum.ewmmain.request.dto.ParticipantRequestDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

@RestController
@RequestMapping(path = "/users/{userId}/events")
@RequiredArgsConstructor
@Slf4j
public class PrivateEventController {
    private final EventService eventService;

    @GetMapping
    public Collection<EventShortDto> getAllEventsByUser(@PathVariable Long userId,
                                                        @RequestParam(required = false, defaultValue = "0")
                                                        @PositiveOrZero int from,
                                                        @RequestParam(required = false, defaultValue = "10")
                                                        @Positive int size) {
        log.info("��������� ������ �������, ����������� �������������: {}", userId);
        return eventService.getAllEventsByUser(userId, from, size);
    }

    @PostMapping
    public EventFullDto createEvent(@PathVariable Long userId,
                                    @RequestBody NewEventDto newEventDto) {
        log.info("�������� ������ �������: {}", newEventDto);
        return eventService.createEvent(userId, newEventDto);
    }

    @PatchMapping
    public EventFullDto updateEvent(@PathVariable Long userId,
                                    @RequestBody UpdateEventRequest updateEventRequest) {
        log.info("��������� �������: {}", updateEventRequest);
        return eventService.updateUserEvent(userId, updateEventRequest);
    }

    @GetMapping("{eventId}")
    public EventFullDto getEventById(@PathVariable Long userId,
                                     @PathVariable Long eventId) {
        log.info("��������� ������� {}, ������������ �������������: {}", eventId, userId);
        return eventService.getUserEvent(userId, eventId);
    }

    @PatchMapping("{eventId}")
    public EventFullDto cancelEvent(@PathVariable Long userId,
                                    @PathVariable Long eventId) {
        log.info("������ �������: {}", eventId);
        return eventService.cancelUserEvent(userId, eventId);
    }

    @GetMapping("{eventId}/requests")
    public Collection<ParticipantRequestDto> getAllRequestsByEvent(@PathVariable(required = true) Long userId,
                                                                   @PathVariable(required = true) Long eventId) {
        log.info("��������� �������� �� ������� � ������� {}, ����������� �������������: {}", eventId, userId);
        return eventService.getEventRequests(userId, eventId);
    }

    @PatchMapping("{eventId}/requests/{reqId}/confirm")
    public ParticipantRequestDto confirmRequest(@PathVariable(required = true) Long userId,
                                                @PathVariable(required = true) Long eventId,
                                                @PathVariable(required = true) Long reqId) {
        log.info("������������� ����� ������: {}, �� ������� � ������� {}", reqId, eventId);
        return eventService.confirmRequest(userId, eventId, reqId);
    }

    @PatchMapping("{eventId}/requests/{reqId}/reject")
    public ParticipantRequestDto rejectRequest(@PathVariable(required = true) Long userId,
                                               @PathVariable(required = true) Long eventId,
                                               @PathVariable(required = true) Long reqId) {
        log.info("���������� ����� ������: {}, �� ������� � ������� {}", reqId, eventId);
        return eventService.rejectRequest(userId, eventId, reqId);
    }
}
