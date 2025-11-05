package ru.practicum.mapper;

import org.mapstruct.Mapper;
import ru.practicum.dto.user.NewUserRequest;
import ru.practicum.dto.user.UserDto;
import ru.practicum.dto.user.UserShortDto;
import ru.practicum.model.User;

/**
 * Маппер для преобразования User entity в DTO и обратно
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * Преобразовать NewUserRequest в User entity
     *
     * @param request запрос на создание пользователя
     * @return User entity
     */
    User toEntity(NewUserRequest request);

    /**
     * Преобразовать User entity в UserDto
     *
     * @param user User entity
     * @return UserDto
     */
    UserDto toDto(User user);

    /**
     * Преобразовать User entity в UserShortDto
     *
     * @param user User entity
     * @return UserShortDto
     */
    UserShortDto toShortDto(User user);
}