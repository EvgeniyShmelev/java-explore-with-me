package ru.practicum.ewmmain.events.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmmain.events.dto.AdminUpdateEventRequest;
import ru.practicum.ewmmain.events.dto.EventFullDto;
import ru.practicum.ewmmain.events.model.EventStatus;
import ru.practicum.ewmmain.events.service.EventService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping(path = "/admin/events")
@RequiredArgsConstructor
@Slf4j
@Validated
public class AdminEventController {
    private final EventService eventService;

    @GetMapping
    public Collection<EventFullDto> getAllEventsByAdmin(@RequestParam(required = false) List<Long> users,
                                                        @RequestParam(required = false) List<EventStatus> states,
                                                        @RequestParam(required = false) List<Long> categories,
                                                        @RequestParam(required = false)
                                                        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                                        @RequestParam(required = false)
                                                        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                                        @RequestParam(defaultValue = "0") int from,
                                                        @RequestParam(defaultValue = "10") int size) {
        log.info("Получен запрос списка событий");
        return eventService.getAllByAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PutMapping("/{eventId}")
    public EventFullDto adminUpdateEventRequest(@PathVariable Long eventId,
                                                @RequestBody AdminUpdateEventRequest adminUpdateEventRequest) {
        log.info("Обновлено событие: id: {} event: {}", eventId, adminUpdateEventRequest);
        return eventService.updateByAdmin(eventId, adminUpdateEventRequest);
    }

    @PatchMapping("/{eventId}/publish")
    public EventFullDto publishEvent(@PathVariable Long eventId) {
        log.info("Команда на публикацию события: {}", eventId);
        return eventService.publishByAdmin(eventId);
    }

    @PatchMapping("/{eventId}/reject")
    public EventFullDto rejectEvent(@PathVariable Long eventId) {
        log.info("Событие: {} отклонено", eventId);
        return eventService.rejectDyAdmin(eventId);
    }
}
