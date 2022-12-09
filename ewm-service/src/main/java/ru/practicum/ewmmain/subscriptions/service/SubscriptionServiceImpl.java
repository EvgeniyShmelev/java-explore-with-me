package ru.practicum.ewmmain.subscriptions.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmmain.events.dto.EventShortDto;
import ru.practicum.ewmmain.events.service.EventService;
import ru.practicum.ewmmain.exception.ForbiddenException;
import ru.practicum.ewmmain.users.model.User;
import ru.practicum.ewmmain.users.repository.UserRepository;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubscriptionServiceImpl implements SubscriptionService {

    private final UserRepository userRepository;
    private final EventService eventService;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public void addFriend(Long userId, Long friendId) {
        log.info("Добавление подписки пользователя с id {} на пользователя с id {}", userId, friendId);
        User user = modelMapper.map(userRepository.findById(userId), User.class);
        User friend = modelMapper.map(userRepository.findById(friendId), User.class);
        user.getFriends().add(friend);
        log.info("Пользователь с id {} подписался на события пользователя с id {}", userId, friendId);
    }

    @Override
    @Transactional
    public void deleteFriend(Long userId, Long friendId) {
        log.info("Удаление подписки пользователя с id {} на пользователя с id {}", userId, friendId);
        User user = modelMapper.map(userRepository.findById(userId), User.class);
        User friend = modelMapper.map(userRepository.findById(friendId), User.class);
        user.getFriends().remove(friend);
        log.info("Пользователь с id {} удалил подписку на события пользователя с id {}", userId, friendId);
    }

    @Override
    public Collection<EventShortDto> getFriendEvents(Long userId, Long friendId, int from, int size) {
        log.info("Получение пользователем с id {} событий с участием пользователя с id {}", userId, friendId);
        User user = modelMapper.map(userRepository.findById(userId), User.class);
        User friend = modelMapper.map(userRepository.findById(friendId), User.class);
        if (!user.getFriends().contains(friend)) {
            throw new ForbiddenException(String.format("Пользователь %s не дружит с пользователем %s.", friendId, userId));
        }
        return eventService.getEventsUserCreatedOrJoined(friendId, from, size);
    }
}
