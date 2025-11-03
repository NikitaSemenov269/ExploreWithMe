package ru.practicum.user.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import ru.practicum.DTO.user.NewUserRequest;
import ru.practicum.DTO.user.UserDto;
import ru.practicum.DTO.user.UserShortDto;
import ru.practicum.user.User;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User toEntity(NewUserRequest newUserRequest);

    UserDto toDtoFromNewRequest(NewUserRequest newUserRequest);

    UserDto toDto(User user);

    UserDto toDtoFromShort(UserShortDto userShortDto);

    UserShortDto toShortDto(User user);

    User toEntityFromDto(UserDto userDto);

    List<UserDto> toDtoList(List<User> users);

    List<UserShortDto> toShortDtoList(List<User> users);

    List<UserShortDto> toShortDtoListFromFull(List<UserDto> userDtos);

    List<User> toEntityListFromDtos(List<UserDto> userDtos);
}