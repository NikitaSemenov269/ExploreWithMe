package ru.practicum.stats;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.practicum.DTO.ResponseStatisticDto;
import ru.practicum.Interfaces.StaticMapper;
import ru.practicum.Interfaces.StaticRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Slf4j
@Repository
@AllArgsConstructor
public class JdbcStaticRepositoryImpl implements StaticRepository {

    private final NamedParameterJdbcOperations jdbc;
    private final StaticMapper mapper;

   /* ResponseStatisticDto responseStatisticDto = jdbc.queryForObject(sql,
            Collections.singletonMap("id", id),
            new BeanPropertyRowMapper<>(ResponseStatisticDto.class));*/


    @Override
    public Collection<ResponseStatisticDto> findStaticEvent(List<String> uris,
                                                            LocalDateTime start,
                                                            LocalDateTime end,
                                                            Boolean unique) {



        String sql = "SELECT * FROM statics_data";
        if (uris != null && !uris.isEmpty()) {
            sql += " WHERE uris IN (:uris)";
        }


        ResponseStatisticDto responseStatisticDto = jdbc.queryForObject(sql,
    }
}

