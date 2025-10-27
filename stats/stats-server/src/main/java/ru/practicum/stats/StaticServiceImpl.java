package ru.practicum.stats;

import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.DTO.RequestStatisticDto;
import ru.practicum.DTO.ResponseStatisticDto;
import ru.practicum.Interfaces.StaticMapper;
import ru.practicum.Interfaces.StaticRepository;
import ru.practicum.Interfaces.StaticService;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.DataIntegrityException;
import ru.practicum.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class StaticServiceImpl implements StaticService {
    private final StaticRepository staticRepository;
    private final StaticMapper mapper;

    @Override
    public void addHit(RequestStatisticDto requestStatisticDto) {
        try {
            if (requestStatisticDto == null) {
                throw new DataIntegrityException("DTO запроса статистики не может быть null");
            }
            if (requestStatisticDto.getApp() == null || requestStatisticDto.getApp().isBlank()) {
                throw new DataIntegrityException("Название приложения не может быть пустым");
            }
            if (requestStatisticDto.getUri() == null || requestStatisticDto.getUri().isBlank()) {
                throw new DataIntegrityException("URI не может быть пустым");
            }
            if (requestStatisticDto.getIp() == null || requestStatisticDto.getIp().isBlank()) {
                throw new DataIntegrityException("IP-адрес не может быть пустым");
            }
            Hit hit = mapper.toEntity(requestStatisticDto);
            staticRepository.addHit(hit);
            log.info("Хит успешно добавлен для URI: {}", requestStatisticDto.getUri());
        } catch (ValidationException e) {
            log.error("Ошибка валидации при добавлении хита: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Неожиданная ошибка при добавлении хита: {}", e.getMessage(), e);
            throw new ConflictException("Не удалось добавить хит из-за внутренней ошибки");
        }
    }

    @Override
    public List<ResponseStatisticDto> findStaticEvent(List<String> uris,
                                                      LocalDateTime start,
                                                      LocalDateTime end,
                                                      Boolean unique) {
        if (unique == null) {
            throw new DataIntegrityException("Параметр unique не может быть null");
        }

        if (start == null || end == null) {
            log.error("Параметры времени начала и окончания не могут быть пустыми");
            throw new DataIntegrityException("Параметры времени начала и окончания обязательны");
        }

        if (end.isBefore(start)) {
            throw new DataIntegrityException("Время окончания не может быть раньше времени начала");
        }

        if (end.equals(start)) {
            throw new DataIntegrityException("Время начала и окончания не могут совпадать");
        }

        if (start.isAfter(LocalDateTime.now()) || end.isAfter(LocalDateTime.now())) {
            throw new DataIntegrityException("Время начала и окончания не могут быть в будущем");
        }
        try {
            List<ResponseStatisticDto> result = staticRepository.findHits(uris, start, end, unique);
            log.info("Успешно найдено {} записей статистики для URI: {}", result.size(), uris);
            return result;
        } catch (
                NotFoundException e) {
            log.warn("Статистика не найдена для указанных критериев");
            throw e;
        } catch (
                Exception e) {
            log.error("Ошибка базы данных при получении статистики: {}", e.getMessage(), e);
            throw new ValidationException("Не удалось получить статистику из-за ошибки базы данных");
        }
    }
}
