package ru.practicum.user.services;

import ru.practicum.DTO.user.NewUserRequest;
import ru.practicum.DTO.user.UserDto;

import java.util.Collection;
import java.util.List;

public interface AdminUserService {
    UserDto save(NewUserRequest request);

    Collection<UserDto> getUsers(List<Long> ids, Integer from, Integer size);

    void delete(Long userId);
}
