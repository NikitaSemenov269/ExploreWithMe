package ru.practicum.Interfaces;

import ru.practicum.DTO.RequestStatisticDto;
import ru.practicum.DTO.ResponseStatisticDto;

import java.util.Collection;
import java.util.List;

public interface StaticService {

    void addHit(RequestStatisticDto requestStatisticDto);

    Collection<ResponseStatisticDto> findStaticEvent(List<String> uris,
                                                     String start,
                                                     String end,
                                                     Boolean unique);
}
