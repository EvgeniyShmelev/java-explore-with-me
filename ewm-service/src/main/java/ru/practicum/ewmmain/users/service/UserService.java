package ru.practicum.ewmmain.users.service;

import ru.practicum.ewmmain.users.dto.NewUserRequest;
import ru.practicum.ewmmain.users.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getAll(List<Long> ids, int from, int size);

    UserDto add(NewUserRequest newUserRequest);

    void deleteUser(Long id);
}