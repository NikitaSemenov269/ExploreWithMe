package ru.practicum.Interfaces;

import ru.practicum.DTO.ResponseStatisticDto;
import ru.practicum.stats.Hit;

import java.time.LocalDateTime;
import java.util.List;

public interface StaticRepository {

    void addHit(Hit hit);

    List<ResponseStatisticDto> findHits(List<String> uris,
                                                     LocalDateTime start,
                                                     LocalDateTime end,
                                                     Boolean unique);
}
