package ru.practicum.plugs;

import org.mapstruct.Mapper;
import ru.practicum.DTO.event.EventShortDto;

@Mapper
public interface EventMapper {

    EventShortDto toDto(Event event);
}
