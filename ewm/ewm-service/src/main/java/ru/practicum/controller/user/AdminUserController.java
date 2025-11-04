package ru.practicum.controller.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.user.NewUserRequest;
import ru.practicum.dto.user.UserDto;
import ru.practicum.service.UserService;

import java.util.List;

/**
 * Admin API для управления пользователями
 */
@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class AdminUserController {

    private final UserService userService;

    /**
     * Получение информации о пользователях
     *
     * @param ids список id пользователей
     * @param from количество элементов, которые нужно пропустить для формирования текущего набора
     * @param size количество элементов в наборе
     * @return список пользователей
     */
    @GetMapping
    public List<UserDto> getUsers(
            @RequestParam(required = false) List<Long> ids,
            @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "10") @Positive Integer size) {

        log.info("GET /admin/users: ids={}, from={}, size={}", ids, from, size);

        return userService.getUsers(ids, from, size);
    }

    /**
     * Добавление нового пользователя
     *
     * @param newUserRequest данные добавляемого пользователя
     * @return добавленный пользователь
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto registerUser(@Valid @RequestBody NewUserRequest newUserRequest) {

        log.info("POST /admin/users: newUserRequest={}", newUserRequest);

        return userService.registerUser(newUserRequest);
    }

    /**
     * Удаление пользователя
     *
     * @param userId id пользователя
     */
    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long userId) {

        log.info("DELETE /admin/users/{}", userId);

        userService.deleteUser(userId);
    }
}