package ru.practicum.Interfaces;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.DTO.RequestStatisticDto;
import ru.practicum.DTO.ResponseStatisticDto;
import ru.practicum.StatisticData;

@Mapper(componentModel = "spring")
public interface StaticMapper {

    StatisticData toStaticData(RequestStatisticDto requestStatisticDto);

    // В сервисном слое, или на выходе из репозитория, нужно будет руками добавить количество просмотров - hits.
    @Mapping(target = "hits", ignore = true)
    ResponseStatisticDto toStatisticDto(StatisticData statisticData);
}
