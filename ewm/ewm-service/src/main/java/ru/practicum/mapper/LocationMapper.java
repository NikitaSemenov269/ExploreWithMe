package ru.practicum.mapper;

import org.mapstruct.Mapper;
import ru.practicum.dto.location.LocationDto;
import ru.practicum.model.Location;

/**
 * Маппер для преобразования Location entity в DTO и обратно
 */
@Mapper(componentModel = "spring")
public interface LocationMapper {

    /**
     * Преобразовать LocationDto в Location entity
     *
     * @param locationDto LocationDto
     * @return Location entity
     */
    Location toEntity(LocationDto locationDto);

    /**
     * Преобразовать Location entity в LocationDto
     *
     * @param location Location entity
     * @return LocationDto
     */
    LocationDto toDto(Location location);
}