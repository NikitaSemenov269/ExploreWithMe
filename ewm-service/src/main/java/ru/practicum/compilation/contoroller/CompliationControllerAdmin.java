package ru.practicum.compilation.contoroller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.DTO.compilation.CompilationDto;
import ru.practicum.DTO.compilation.RequestCompilationDto;
import ru.practicum.DTO.compilation.UpdRequestCompilationDto;
import ru.practicum.compilation.interfaces.CompilationService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/compilations")
public class CompliationControllerAdmin {
    private final CompilationService compServ;

    @PostMapping
    public ResponseEntity<Void> saveCompilation(
            @Valid @RequestBody RequestCompilationDto compilationDto) {
        compServ.saveCompilation(compilationDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{compId}")
    public ResponseEntity<Void> deleteCompilation(@PathVariable @Min(1) Long compId) {
        compServ.deleteCompilation(compId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/{compId}")
    public ResponseEntity<CompilationDto> updateCompilation(
            @PathVariable @Min(1) Long compId,
            @RequestBody UpdRequestCompilationDto updRequestCompilationDto) {
        return ResponseEntity.ok().body(compServ.updateCompilation(compId, updRequestCompilationDto));
    }
}

