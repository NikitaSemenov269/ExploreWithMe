package ru.practicum.stats;

import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.DTO.RequestStatisticDto;
import ru.practicum.DTO.ResponseStatisticDto;
import ru.practicum.Interfaces.StaticMapper;
import ru.practicum.Interfaces.StaticJPARepository;
import ru.practicum.Interfaces.StaticService;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.DataIntegrityException;
import ru.practicum.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class StaticServiceImpl implements StaticService {
    private final StaticJPARepository staticJPARepository;
    private final StaticMapper mapper;

    @Override
    @Transactional
    public void addHit(RequestStatisticDto requestStatisticDto) {
        try {
            hitValidation(requestStatisticDto);
            Hit hit = mapper.toEntity(requestStatisticDto);
            staticJPARepository.save(hit);
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
        staticEventValidation(start, end, unique);
        try {
            if (uris == null || uris.isEmpty()) {
                return staticJPARepository.findAllHits(start, end, unique);
            } else if (uris.contains("/events/")) {
                return staticJPARepository.findEventHits(start, end, unique);
            } else {
                return staticJPARepository.findHitsByUris(start, end, unique, uris);
            }
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

    private static void hitValidation(RequestStatisticDto requestStatisticDto) {
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
    }

    private static void staticEventValidation(LocalDateTime start, LocalDateTime end, Boolean unique) {
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

        if (end.equals(start.plusMinutes(15))) {
            throw new DataIntegrityException("Разница между началом и окончанием" +
                    " события не может быть менее 15 минут.");
        }
    }
}
