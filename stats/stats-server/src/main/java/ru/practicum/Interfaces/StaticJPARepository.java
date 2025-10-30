package ru.practicum.Interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.DTO.ResponseStatisticDto;
import ru.practicum.stats.Hit;

import java.time.LocalDateTime;
import java.util.List;

public interface StaticJPARepository extends JpaRepository<Hit, Integer> {

    // Все URI (uris = null || uris.isEmpty())
    @Query("""
            SELECT new ru.practicum.DTO.ResponseStatisticDto(
                h.app, h.uri,
                CASE WHEN :unique = true THEN COUNT(DISTINCT h.ip) ELSE COUNT(h) END
            )
            FROM Hit h
            WHERE h.timestamp BETWEEN :start AND :end
            GROUP BY h.app, h.uri
            ORDER BY COUNT(h) DESC
            """)
    List<ResponseStatisticDto> findAllHits(@Param("start") LocalDateTime start,
                                           @Param("end") LocalDateTime end,
                                           @Param("unique") Boolean unique);

    // Конкретные URI
    @Query("""
            SELECT new ru.practicum.DTO.ResponseStatisticDto(
                h.app, h.uri,
                CASE WHEN :unique = true THEN COUNT(DISTINCT h.ip) ELSE COUNT(h) END
            )
            FROM Hit h
            WHERE h.timestamp BETWEEN :start AND :end
            AND h.uri IN :uris
            GROUP BY h.app, h.uri
            ORDER BY COUNT(h) DESC
            """)
    List<ResponseStatisticDto> findHitsByUris(@Param("start") LocalDateTime start,
                                              @Param("end") LocalDateTime end,
                                              @Param("unique") Boolean unique,
                                              @Param("uris") List<String> uris);

    // Все события (/events/)
    @Query("""
            SELECT new ru.practicum.DTO.ResponseStatisticDto(
                h.app, h.uri,
                CASE WHEN :unique = true THEN COUNT(DISTINCT h.ip) ELSE COUNT(h) END
            )
            FROM Hit h
            WHERE h.timestamp BETWEEN :start AND :end
            AND h.uri LIKE '/events/%'
            GROUP BY h.app, h.uri
            ORDER BY COUNT(h) DESC
            """)
    List<ResponseStatisticDto> findEventHits(@Param("start") LocalDateTime start,
                                             @Param("end") LocalDateTime end,
                                             @Param("unique") Boolean unique);
}
