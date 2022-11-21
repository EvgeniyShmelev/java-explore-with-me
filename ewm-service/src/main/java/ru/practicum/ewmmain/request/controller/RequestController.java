package ru.practicum.ewmmain.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmmain.request.dto.ParticipantRequestDto;

import java.util.Collection;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class RequestController {
    private final RequestService requestService;

    @GetMapping("/{userId}/requests")
    public Collection<ParticipantRequestDto> getAllRequestsByUser(@PathVariable(required = true) Long userId) {
        log.info("Получение заявок пользователя {} на участие в чужих событиях", userId);
        return requestService.getAll(userId);
    }

    @PostMapping("/{userId}/requests")
    public ParticipantRequestDto createRequest(@PathVariable Long userId,
                                               @RequestParam(required = false) Long eventId) {
        log.info("Добавлен запрос пользователя: {} на участие в событии: {}", userId, eventId);
        return requestService.add(userId, eventId);
    }

    @PatchMapping("/{userId}/requests/{reqId}/cancel")
    public ParticipantRequestDto update(@PathVariable(required = true) Long userId,
                                        @PathVariable(required = true) Long requestId) {
        log.info("Отмена заявки: {}, на участие в событии {}", requestId, userId);
        return requestService.update(userId, requestId);
    }
}
