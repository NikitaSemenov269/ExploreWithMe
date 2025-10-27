package ru.practicum.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.DTO.RequestStatisticDto;
import ru.practicum.DTO.ResponseStatisticDto;
import ru.practicum.Interfaces.StaticService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@AllArgsConstructor
public class StatsController {
    private final StaticService staticService;

    @PostMapping("/hit")
    public ResponseEntity<Void> saveHit(@Valid @RequestBody RequestStatisticDto requestStatisticDto) {
        staticService.addHit(requestStatisticDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/stats")
    public ResponseEntity<List<ResponseStatisticDto>> getStats(
            @RequestParam(required = false) List<String> uris,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
            @RequestParam(defaultValue = "false") boolean unique
    ) {
        List<ResponseStatisticDto> stats = staticService.findStaticEvent(uris, start, end, unique);
        return ResponseEntity.ok(stats);
    }
}
