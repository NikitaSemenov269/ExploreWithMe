package ru.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.model.ParticipationRequest;

@Mapper(componentModel = "spring")
public interface ParticipationRequestMapper {
    ParticipationRequestMapper INSTANCE = Mappers.getMapper(ParticipationRequestMapper.class);

    @Mapping(target = "event", source = "event.id")
    @Mapping(target = "requester", source = "requester.id")
    @Mapping(target = "created", source = "created", dateFormat = "yyyy-MM-dd HH:mm:ss")
    ParticipationRequestDto toDto(ParticipationRequest entity);
}