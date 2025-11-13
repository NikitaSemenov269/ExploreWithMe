package ru.practicum.controller.compilation;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.NewCompilationDto;
import ru.practicum.dto.compilation.UpdateCompilationRequest;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.service.CompilationService;

@Slf4j
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
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/{compId}")
    public ResponseEntity<CompilationDto> updateCompilation(
            @PathVariable @Min(1) Long compId,
            @RequestBody UpdateCompilationRequest updRequestCompilationDto) {

        log.info("PATCH /admin/compilations/{} - обновление подборки", compId);

        try {
            CompilationDto result = compServ.updateCompilation(compId, updRequestCompilationDto);
            return ResponseEntity.ok().body(result);

        } catch (DataIntegrityViolationException e) {
            String errorMsg = e.getMessage();
            log.warn("Нарушение целостности данных при обновлении подборки {}: {}", compId, errorMsg, e);

            // Проверка на превышение длины поля
            if (errorMsg != null && errorMsg.contains("value too long")) {
                throw new BadRequestException(
                        "Длина поля 'title' превышает допустимый лимит (50 символов). " +
                                "Переданное значение: '" + updRequestCompilationDto.getTitle() + "'"
                );
            }

            // Проверка на дублирование уникального поля
            if (errorMsg != null && (
                    errorMsg.contains("duplicate") ||
                            errorMsg.contains("unique constraint") ||
                            errorMsg.contains("уникальное ограничение"))) {
                throw new ConflictException(
                        "Не удалось обновить подборку: указанное название уже существует. " +
                                "Пожалуйста, выберите другое значение для поля 'title'."
                );
            }

            // Проверка на нарушение внешнего ключа
            if (errorMsg != null && (
                    errorMsg.contains("foreign key") ||
                            errorMsg.contains("внешний ключ") ||
                            errorMsg.contains("FK"))) {
                throw new ConflictException(
                        "Не удалось обновить подборку: имеются связанные записи. " +
                                "Убедитесь, что изменения не нарушают связи с другими объектами."
                );
            }

            // Все остальные случаи передаём глобальному обработчику
            throw e;

        } catch (NotFoundException e) {
            log.debug("Подборка с ID {} не найдена", compId);
            return ResponseEntity.notFound().build();

        } catch (IllegalArgumentException e) {
            log.warn("Некорректные аргументы при обновлении подборки {}: {}", compId, e.getMessage());
            throw new BadRequestException(
                    "Некорректные данные для обновления подборки: " + e.getMessage()
            );

        } catch (Exception e) {
            // Общий перехватчик для непредвиденных ошибок
            log.error("Неожиданная ошибка при обновлении подборки {}", compId, e);
            throw e;
        }
    }


}

