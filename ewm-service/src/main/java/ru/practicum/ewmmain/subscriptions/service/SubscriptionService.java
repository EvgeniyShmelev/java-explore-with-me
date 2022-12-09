package ru.practicum.ewmmain.subscriptions.service;

import ru.practicum.ewmmain.events.dto.EventShortDto;

import java.util.Collection;

public interface SubscriptionService {

    Collection<EventShortDto> getFriendEvents(Long userId, Long friendId, int from, int size);

    void addFriend(Long userId, Long friendId);

    void deleteFriend(Long userId, Long friendId);

}
