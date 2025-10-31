package ru.practicum.compilation.contoroller;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.DTO.compilation.ResponseCompilationDto;

import ru.practicum.compilation.interfaces.CompilationService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/compilations")
public class CompilationController {
    private final CompilationService compServ;

    @GetMapping
    public ResponseEntity<List<ResponseCompilationDto>> findCompilations(
            @RequestParam(required = false) Boolean pinned,
            @RequestParam(defaultValue = "0") @Min(0) Integer from,
            @RequestParam(defaultValue = "10") @Min(1) Integer size) {

        Pageable pageable = PageRequest.of(from / size, size);
        return ResponseEntity.ok().body(compServ.findCompilations(pinned, pageable));
    }

    @GetMapping("/{compId}")
    public ResponseEntity<ResponseCompilationDto> findCompilationById(@PathVariable @Min(1) Long compId) {
        return ResponseEntity.ok().body(compServ.findCompilationById(compId));
    }
}

