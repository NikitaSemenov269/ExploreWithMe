package ru.practicum.user.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import ru.practicum.DTO.user.NewUserRequest;
import ru.practicum.DTO.user.UserDto;
import ru.practicum.DTO.user.UserShortDto;
import ru.practicum.user.User;

import java.util.List;

@Mapper(componentModel = org.mapstruct.MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "name", source = "name")
    @Mapping(target = "email", source = "email")
    User toEntity(NewUserRequest newUserRequest);

    UserDto toDtoFromNewRequest(NewUserRequest newUserRequest);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "email", source = "email")
    UserDto toDto(User user);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    UserDto toDtoFromShort(UserShortDto userShortDto);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    UserShortDto toShortDto(User user);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "email", source = "email")
    User toEntityFromDto(UserDto userDto);

    List<UserDto> toDtoList(List<User> users);

    List<UserShortDto> toShortDtoList(List<User> users);

    List<UserShortDto> toShortDtoListFromFull(List<UserDto> userDtos);

    List<User> toEntityListFromDtos(List<UserDto> userDtos);
}