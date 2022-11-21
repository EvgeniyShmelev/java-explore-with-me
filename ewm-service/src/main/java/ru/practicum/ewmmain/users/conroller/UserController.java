package ru.practicum.ewmmain.users.conroller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmmain.users.dto.NewUserRequest;
import ru.practicum.ewmmain.users.dto.UserDto;
import ru.practicum.ewmmain.users.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

@RestController
@RequestMapping(path = "/admin/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
    private final UserService userService;

    @GetMapping
    public Collection<UserDto> getAllUsers(@RequestParam Collection<Long> ids,
                                           @RequestParam(required = false, defaultValue = "0")
                                           @PositiveOrZero int from,
                                           @RequestParam(required = false, defaultValue = "10")
                                           @Positive int size) {
        log.info("Получен запрос списка пользователей");
        return userService.getAll(ids, from, size);
    }

    @PostMapping
    public UserDto add(@Valid @RequestBody NewUserRequest newUserRequest) {
        log.info("Создание нового пользователя: {}", newUserRequest);
        return userService.add(newUserRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long userId) {
        log.info("Удален пользователь {}", userId);
        userService.deleteUser(userId);
    }
}
