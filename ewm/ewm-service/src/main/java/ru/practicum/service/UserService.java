package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.user.NewUserRequest;
import ru.practicum.dto.user.UserDto;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.UserMapper;
import ru.practicum.model.User;
import ru.practicum.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис для работы с пользователями
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    /**
     * Получить список пользователей
     *
     * @param ids список ID пользователей (если null - вернуть всех)
     * @param from с какого элемента начать
     * @param size количество элементов
     * @return список пользователей
     */
    public List<UserDto> getUsers(List<Long> ids, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);

        List<User> users;
        if (ids == null || ids.isEmpty()) {
            users = userRepository.findAll(pageable).getContent();
        } else {
            users = userRepository.findByIdIn(ids, pageable);
        }

        log.info("Получено {} пользователей", users.size());
        return users.stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Зарегистрировать нового пользователя
     *
     * @param newUserRequest данные пользователя
     * @return созданный пользователь
     */
    @Transactional
    public UserDto registerUser(NewUserRequest newUserRequest) {
        // Проверка на уникальность email
        if (userRepository.existsByEmail(newUserRequest.getEmail())) {
            throw new ConflictException("Email уже используется: " + newUserRequest.getEmail());
        }

        User user = userMapper.toEntity(newUserRequest);

        try {
            user = userRepository.save(user);
            log.info("Создан новый пользователь с ID: {}", user.getId());
            return userMapper.toDto(user);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("Не удалось создать пользователя. Email уже существует: " +
                    newUserRequest.getEmail());
        }
    }

    /**
     * Удалить пользователя
     *
     * @param userId ID пользователя
     */
    @Transactional
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь не найден с ID: " + userId);
        }

        userRepository.deleteById(userId);
        log.info("Удален пользователь с ID: {}", userId);
    }
}