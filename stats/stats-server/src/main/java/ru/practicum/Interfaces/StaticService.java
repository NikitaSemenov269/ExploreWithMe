package ru.practicum.Interfaces;

import ru.practicum.DTO.RequestStatisticDto;
import ru.practicum.DTO.ResponseStatisticDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StaticService {

    void addHit(RequestStatisticDto requestStatisticDto);

    List<ResponseStatisticDto> findStaticEvent(List<String> uris,
                                                     LocalDateTime start,
                                                     LocalDateTime end,
                                                     Boolean unique);
}
