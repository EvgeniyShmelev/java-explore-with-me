package ru.practicum.ewmmain.subscriptions.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmmain.events.dto.EventShortDto;
import ru.practicum.ewmmain.subscriptions.service.SubscriptionService;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

@RestController
@RequestMapping(path = "/users/{id}/friends")
@RequiredArgsConstructor
@Slf4j
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    @GetMapping("/{friendId}/events")
    public Collection<EventShortDto> getFriendEvents(@PathVariable Long userId,
                                                     @PathVariable Long friendId,
                                                     @RequestParam(required = false, defaultValue = "0")
                                                     @PositiveOrZero int from,
                                                     @RequestParam(required = false, defaultValue = "10")
                                                     @Positive int size
    ) {
        log.debug("Получен запрос GET по пути /users/{}/friend/{}/events", userId, friendId);
        return subscriptionService.getFriendEvents(userId, friendId, from, size);
    }

    @PutMapping("/{friendId}")
    public void addFriend(@PathVariable @Positive @NotNull Long userId,
                          @PathVariable @Positive @NotNull Long friendId) {
        log.debug("Получен запрос PUT по пути /users/{}/friends/{}", userId, friendId);
        subscriptionService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{friendId}")
    public void deleteFriend(@PathVariable @Positive @NotNull Long userId,
                             @PathVariable @Positive @NotNull Long friendId) {
        log.debug("Получен запрос DELETE по пути /users/{}/friends/{}", userId, friendId);
        subscriptionService.deleteFriend(userId, friendId);
    }
}
