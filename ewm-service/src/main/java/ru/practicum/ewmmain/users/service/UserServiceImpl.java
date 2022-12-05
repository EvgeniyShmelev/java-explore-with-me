package ru.practicum.ewmmain.users.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.practicum.ewmmain.exception.BadRequestException;
import ru.practicum.ewmmain.exception.ConflictException;
import ru.practicum.ewmmain.exception.NotFoundException;
import ru.practicum.ewmmain.users.dto.NewUserRequest;
import ru.practicum.ewmmain.users.dto.UserDto;
import ru.practicum.ewmmain.users.model.User;
import ru.practicum.ewmmain.users.repository.UserRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<UserDto> getAll(List<Long> ids, int from, int size) {
        log.info("Получение списка всех пользователей");
        Pageable pageable = PageRequest.of(from, size);
        Collection<User> users = userRepository.findUsersByIdIn(ids, pageable);
        return users.stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserDto add(NewUserRequest newUserRequest) {
        log.info("Добавление пользователя: {}", newUserRequest);
        User user = modelMapper.map(newUserRequest, User.class);
        if (!StringUtils.hasText(user.getName())) {
            throw new BadRequestException("Пустое имя.");
        }
        Optional<User> userOptional = userRepository.getUserByName(user.getName());
        if (userOptional.isPresent()) {
            throw new ConflictException("Пользователь с именем " + user.getName() + "уже существует");
        }
        User newUser = userRepository.save(user);
        log.info("Добавлен пользователь: {}", newUser);
        return modelMapper.map(newUser, UserDto.class);
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("В БД нет пользователя с id " + userId));
        log.info("Удаление пользователя с id: {}", userId);
        userRepository.deleteById(userId);
    }
}
