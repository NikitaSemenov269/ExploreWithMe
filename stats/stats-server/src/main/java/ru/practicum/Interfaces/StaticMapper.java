package ru.practicum.Interfaces;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.DTO.RequestStatisticDto;
import ru.practicum.DTO.ResponseStatisticDto;
import ru.practicum.stats.Hit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Mapper(componentModel = "spring")
public interface StaticMapper {

    @Mapping(target = "timestamp", source = "timestamp")
    Hit toEntity(RequestStatisticDto requestStatisticDto);
    // Метод для маппинга даты из String в LocalDataTime.
    default LocalDateTime stringToLocalDateTime(String dateString) {
        // Важно! Аналогичный формат должен быть использован в сервисном слое.
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        try {
            return LocalDateTime.parse(dateString, formatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Неверный формат даты", e);
        }
    }

    // Так же необходимо для маппинга коллекции.
    ResponseStatisticDto toResponseDto(Hit hit);

    // Для маппинга в коллекцию. Автоматически определяет правила маппинга из toResponseDto.
    List<ResponseStatisticDto> toCollectionResponseDto(List<Hit> hits);
}
