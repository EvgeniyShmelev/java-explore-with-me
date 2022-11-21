package ru.practicum.ewmmain.events.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmmain.events.dto.EventFullDto;
import ru.practicum.ewmmain.events.dto.EventShortDto;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.Collection;

@RestController
@RequestMapping(path = "/events")
@RequiredArgsConstructor
@Slf4j
public class PublicEventController {
    private final EventService eventService;

    @GetMapping("/{id}")
    public EventFullDto getEventByIdPublic(@PathVariable Long eventId, HttpServletRequest request) {
        log.info("Получение события по идентификатору: {}", eventId);
        return eventService.getShortDtoById(eventId, request);
    }

    @GetMapping
    public Collection<EventShortDto> getAllById(@RequestParam(required = false) String text,
                                                @RequestParam(required = false) Long[] categories,
                                                @RequestParam(required = false) Boolean paid,
                                                @RequestParam(required = false)
                                                @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                                @RequestParam(required = false)
                                                @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                                @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                                @RequestParam(required = false) String sort,
                                                @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                @RequestParam(defaultValue = "10") @Positive int size,
                                                HttpServletRequest request) {
        log.info("Получен запрос списка событий {}", text);
        return eventService.getAll(text, categories, paid, rangeStart, rangeEnd, onlyAvailable,
                sort, from, size, request);
    }
}
