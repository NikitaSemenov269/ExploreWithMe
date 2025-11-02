package ru.practicum.user.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.DTO.user.NewUserRequest;
import ru.practicum.DTO.user.UserDto;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.User;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.repositories.UserRepository;

import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    private final UserRepository userRepository;

    private User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь c id %d не найден", userId)));
    }

    @Override
    @Transactional
    public UserDto save(NewUserRequest request) {
        User user = UserMapper.INSTANCE.toEntity(request);
        try {
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException(String.format("Email %s уже зарезервирован", user.getEmail()), e);
        }
        log.info("Сохраняем данные о пользователе {}", request.getName());
        return UserMapper.INSTANCE.toDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<UserDto> getUsers(List<Long> ids, Integer from, Integer size) {
        List<User> users;

        if (ids == null) {
            users = userRepository.findUsers(from, size);
        } else {
            users = userRepository.findUsers(ids, from, size);
        }

        log.info("Получаем данные о {} пользователях", users.size());
        return UserMapper.INSTANCE.toDtoList(users);
    }

    @Override
    @Transactional
    public void delete(Long userId) {
        User user = findById(userId);
        userRepository.deleteById(userId);
        log.info("Пользователь {} удален", user.getName());
    }
}
