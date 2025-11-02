package ru.practicum.user.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.DTO.user.NewUserRequest;
import ru.practicum.DTO.user.UserDto;
import ru.practicum.user.services.AdminUserService;

import java.util.Collection;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/users")
public class AdminUserController {

    private static final String VALUE_CAT_ID = "/{user-id}";
    private static final String PATH_VARIABLE_CAT_ID = "user-id";

    private final AdminUserService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto save(@RequestBody @Valid NewUserRequest request) {
        log.info("Получен запрос POST /admin/users");
        return service.save(request);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<UserDto> getUsers(@RequestParam(required = false) List<Long> ids,
                                        @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                        @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("Получен запрос GET /admin/users со списком ID пользователей {} с параметрами from = {}, size = {}", ids, from, size);
        return service.getUsers(ids, from, size);
    }

    @DeleteMapping(VALUE_CAT_ID)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(PATH_VARIABLE_CAT_ID) Long userId) {
        log.info("Получен запрос DELETE /admin/users/{}", userId);
        service.delete(userId);
    }
}
