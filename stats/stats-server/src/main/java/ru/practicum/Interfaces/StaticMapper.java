package ru.practicum.Interfaces;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.practicum.DTO.RequestStatisticDto;
import ru.practicum.DTO.ResponseStatisticDto;
import ru.practicum.stats.Hit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Mapper(componentModel = org.mapstruct.MappingConstants.ComponentModel.SPRING)
public interface StaticMapper {

    DateTimeFormatter DATE_TIME_PATTERN = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Mapping(target = "timestamp", source = "timestamp", qualifiedByName = "stringToLocalDateTime")
    Hit toEntity(RequestStatisticDto requestStatisticDto);

    // Метод для маппинга даты из String в LocalDataTime.
    @Named("stringToLocalDateTime")
    default LocalDateTime stringToLocalDateTime(String dateString) {
        try {
            return LocalDateTime.parse(dateString, DATE_TIME_PATTERN);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Неверный формат даты", e);
        }
    }

    // Так же необходимо для маппинга коллекции.
    ResponseStatisticDto toResponseDto(Hit hit);

    // Для маппинга в коллекцию. Автоматически определяет правила маппинга из toResponseDto.
    List<ResponseStatisticDto> toCollectionResponseDto(List<Hit> hits);
}
