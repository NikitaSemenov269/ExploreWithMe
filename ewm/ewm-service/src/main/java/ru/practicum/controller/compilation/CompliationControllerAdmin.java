package ru.practicum.controller.compilation;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.compilation.NewCompilationDto;
import ru.practicum.dto.compilation.UpdateCompilationRequest;
import ru.practicum.service.CompilationService;


@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/compilations")
public class CompliationControllerAdmin {
    private final CompilationService compServ;

    @PostMapping
    public ResponseEntity<Void> saveCompilation(
            @Valid @RequestBody NewCompilationDto newCompilationDto) {
        compServ.saveCompilation(newCompilationDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{compId}")
    public ResponseEntity<Void> deleteCompilation(@PathVariable @Min(1) Long compId) {
        compServ.deleteCompilation(compId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/{compId}")
    public ResponseEntity<NewCompilationDto> updateCompilation(
            @PathVariable @Min(1) Long compId,
            @RequestBody UpdateCompilationRequest updRequestCompilationDto) {
        return ResponseEntity.ok().body(compServ.updateCompilation(compId, updRequestCompilationDto));
    }
}

