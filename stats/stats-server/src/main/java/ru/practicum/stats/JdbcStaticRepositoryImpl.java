package ru.practicum.stats;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.DTO.ResponseStatisticDto;
import ru.practicum.Interfaces.StaticRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
@AllArgsConstructor
@Transactional(readOnly = true)
public class JdbcStaticRepositoryImpl implements StaticRepository {

    private final NamedParameterJdbcOperations jdbc;

    @Override
    @Transactional
    public void addHit(Hit hit) {
        try {
            String sql = """
                    INSERT INTO hits_data (app, uri, ip, timestamp)
                    VALUES (:app, :uri, :ip, :timestamp)
                    """;

            Map<String, Object> params = new HashMap<>();
            params.put("app", hit.getApp());
            params.put("uri", hit.getUri());
            params.put("ip", hit.getIp());
            params.put("timestamp", hit.getTimestamp());

            jdbc.update(sql, params);
        } catch (DataAccessException e) {
            log.error("Ошибка при добавлении хита: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public Collection<ResponseStatisticDto> findHits(List<String> uris,
                                                     LocalDateTime start,
                                                     LocalDateTime end,
                                                     Boolean unique) {
        try {
            String sql = """
                    SELECT app, uri,
                    CASE WHEN :unique = TRUE THEN COUNT(DISTINCT ip)
                    ELSE COUNT (*) END as hits FROM hits_data WHERE
                    (:uris IS NULL OR uri IN (:uris)) AND timestamp >= :start
                    AND timestamp <= :end GROUP BY app, uri
                    ORDER BY hits DESC
                    """;

            Map<String, Object> params = new HashMap<>();
            params.put("uris", uris);
            params.put("start", start);
            params.put("end", end);
            params.put("unique", unique);

            return jdbc.query(sql, params, (rs, rowNum) -> {
                return new ResponseStatisticDto(
                        rs.getString("app"),
                        rs.getString("uri"),
                        rs.getInt("hits")
                );
            });
        } catch (DataAccessException e) {
            log.error("Ошибка при получении статистики: {}", e.getMessage());
            throw e;
        }
    }
}

