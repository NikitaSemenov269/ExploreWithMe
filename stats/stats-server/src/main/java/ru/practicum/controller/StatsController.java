package ru.practicum.controller;

import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.DTO.RequestStatisticDto;
import ru.practicum.DTO.ResponseStatisticDto;
import ru.practicum.client.StatsClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
public class StatsController {
    private final StatsClient statsClient;

    public StatsController(StatsClient statsClient) {
        this.statsClient = statsClient;
    }

    @PostMapping("/hit")
    public ResponseEntity<Void> saveHit(@Valid @RequestBody RequestStatisticDto requestStatisticDto) {
        statsClient.saveHit(requestStatisticDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/stats")
    public ResponseEntity<ResponseStatisticDto> getStats(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
            @RequestParam(required = false) List<String> uris,
            @RequestParam(defaultValue = "false") boolean unique
    ) {
        return ResponseEntity.ok(statsClient.getStats(
                start.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                end.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                uris,
                unique
        ));
    }

}
