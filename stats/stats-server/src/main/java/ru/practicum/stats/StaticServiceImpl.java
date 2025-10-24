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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
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
            Hit hit = mapper.toEntity(requestStatisticDto);
            staticRepository.addHit(hit);
            log.info("");
            // установить более узкий перехват исключений
        } catch (Exception e) {
            //   throw new
        }
    }

    @Override
    public Collection<ResponseStatisticDto> findStaticEvent(List<String> uris,
                                                            String startStr,
                                                            String endStr,
                                                            Boolean unique) {
        if (unique == null) {
            // throw new ("");
        }

        LocalDateTime start = null;
        LocalDateTime end = null;

        if (startStr == null || startStr.isBlank() || endStr == null || endStr.isBlank()) {
            log.error("");
            // throw new ("");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        try {
            start = LocalDateTime.parse(startStr, formatter);
            end = LocalDateTime.parse(endStr, formatter);

            if (end.isBefore(start)) {
                // подставить нужное исключение
                //  throw new ("Время окончания аренды не может наступить раньше начала аренды.");
            }
            if (end.equals(start)) {
                // подставить нужное исключение
                //  throw new ("Время начала и окончания аренды не могут совпадать.");
            }
            // установить более узкий перехват исключений
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        try {
            Collection<ResponseStatisticDto> result = staticRepository.findHits(uris, start, end, unique);
            log.info("");
            return result;
            // установить более узкий перехват исключений
        } catch (
                Exception e) {
            throw new ValidationException();
        }
    }
}
