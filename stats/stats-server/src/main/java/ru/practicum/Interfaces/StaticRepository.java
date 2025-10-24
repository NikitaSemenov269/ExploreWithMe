package ru.practicum.Interfaces;

import ru.practicum.DTO.ResponseStatisticDto;
import ru.practicum.stats.Hit;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface StaticRepository {

    void addHit(Hit hit);

    Collection<ResponseStatisticDto> findHits(List<String> uris,
                                                     // Преобразование из String происходит на уровне сервисного слоя.
                                                     LocalDateTime start,
                                                     LocalDateTime end,
                                                     Boolean unique);
}
