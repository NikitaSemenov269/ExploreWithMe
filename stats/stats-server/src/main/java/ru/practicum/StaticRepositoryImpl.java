package ru.practicum;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.practicum.DTO.ResponseStatisticDto;
import ru.practicum.Interfaces.StaticMapper;
import ru.practicum.Interfaces.StaticRepository;

import java.util.Collections;

@Repository
@Slf4j
@AllArgsConstructor
public class StaticRepositoryImpl implements StaticRepository {

    private final NamedParameterJdbcOperations jdbc;
    private final StaticMapper mapper;


}


