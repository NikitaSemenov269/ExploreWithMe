package ru.practicum.controller.compilation;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.NewCompilationDto;
import ru.practicum.dto.compilation.UpdateCompilationRequest;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.service.CompilationService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/compilations")
public class CompliationControllerAdmin {
    private final CompilationService compServ;

    @PostMapping
    public ResponseEntity<CompilationDto> saveCompilation(
            @Valid @RequestBody NewCompilationDto newCompilationDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(compServ.saveCompilation(newCompilationDto));
    }

    @DeleteMapping("/{compId}")
    public ResponseEntity<Void> deleteCompilation(@PathVariable @Min(1) Long compId) {
        compServ.deleteCompilation(compId);
        return ResponseEntity.status(204).build();
    }

    @PatchMapping("/{compId}")
    public ResponseEntity<CompilationDto> updateCompilation(
            @PathVariable @Min(1) Long compId,
            @RequestBody UpdateCompilationRequest updRequestCompilationDto
    ) {
        try {
            CompilationDto result = compServ.updateCompilation(compId, updRequestCompilationDto);
            return ResponseEntity.ok().body(result);

        } catch (DataIntegrityViolationException e) {
            String errorMsg = e.getMessage();

            // Проверка на превышение длины поля
            if (errorMsg != null && errorMsg.contains("value too long")) {
                throw new BadRequestException(
                        "Длина поля 'title' превышает допустимый лимит (50 символов). " +
                                "Переданное значение: '" + updRequestCompilationDto.getTitle() + "'"
                );
            }

            // Все остальные случаи передаём глобальному обработчику
            throw e;
        }
        // Обработка NotFoundException
        catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

}

