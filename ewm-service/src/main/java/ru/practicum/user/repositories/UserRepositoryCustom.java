package ru.practicum.user.repositories;

import ru.practicum.user.User;

import java.util.List;

public interface UserRepositoryCustom {
    List<User> findUsers(Integer from, Integer size);
    List<User> findUsers(List<Long> ids, Integer from, Integer size);
}
