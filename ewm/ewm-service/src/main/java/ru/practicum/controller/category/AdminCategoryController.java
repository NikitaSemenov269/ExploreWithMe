package ru.practicum.controller.category;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.NewCategoryDto;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.service.CategoryService;

/**
 * Контроллер для управления категориями администратором
 */
@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
@Slf4j
public class AdminCategoryController {

    private final CategoryService categoryService;

    /**
     * Добавление новой категории
     * POST /admin/categories
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto addCategory(@Valid @RequestBody NewCategoryDto newCategoryDto) {
        log.info("POST /admin/categories - добавление новой категории: {}", newCategoryDto.getName());
        return categoryService.addCategory(newCategoryDto);
    }

    /**
     * Удаление категории
     * DELETE /admin/categories/{catId}
     */
    @DeleteMapping("/{catId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long catId) {
        log.info("DELETE /admin/categories/{} - удаление категории", catId);

        try {
            categoryService.deleteCategory(catId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        } catch (DataIntegrityViolationException e) {
            String errorMsg = e.getMessage();

            // Проверка на нарушение внешнего ключа (связанные записи)
            if (errorMsg != null && (
                    errorMsg.contains("foreign key") ||
                            errorMsg.contains("внешний ключ") ||
                            errorMsg.contains("FK")
            )) {
                throw new ConflictException(
                        "Не удалось удалить категорию: имеются связанные записи. " +
                                "Убедитесь, что на категорию не ссылаются другие объекты."
                );
            }

            // Проверка на другие типичные нарушения целостности
            if (errorMsg != null && errorMsg.contains("duplicate")) {
                throw new ConflictException(
                        "Обнаружено дублирование данных при удалении категории. " +
                                "Проверьте корректность входных данных."
                );
            }

            // Все остальные случаи передаём глобальному обработчику
            throw e;

        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();

        } catch (IllegalArgumentException e) {
            throw new BadRequestException(
                    "Некорректный идентификатор категории: " + catId + ". " +
                            e.getMessage()
            );
        }
    }


    /**
     * Изменение категории
     * PATCH /admin/categories/{catId}
     */
    @PatchMapping("/{catId}")
    public ResponseEntity<CategoryDto> updateCategory(
            @PathVariable Long catId,
            @Valid @RequestBody CategoryDto categoryDto) {

        log.info("PATCH /admin/categories/{} - обновление категории", catId);

        try {
            CategoryDto updatedCategory = categoryService.updateCategory(catId, categoryDto);
            return ResponseEntity.ok(updatedCategory);

        } catch (DataIntegrityViolationException e) {
            String errorMsg = e.getMessage();

            // Проверка на нарушение уникального ограничения (дублирование)
            if (errorMsg != null && (
                    errorMsg.contains("duplicate") ||
                            errorMsg.contains("unique constraint") ||
                            errorMsg.contains("уникальное ограничение")
            )) {
                throw new ConflictException(
                        "Не удалось обновить категорию: значение поля нарушает уникальное ограничение. " +
                                "Возможно, указанная категория уже существует."
                );
            }

            // Проверка на нарушение внешнего ключа (если есть связанные сущности)
            if (errorMsg != null && (
                    errorMsg.contains("foreign key") ||
                            errorMsg.contains("внешний ключ") ||
                            errorMsg.contains("FK")
            )) {
                throw new ConflictException(
                        "Не удалось обновить категорию: имеются связанные записи. " +
                                "Убедитесь, что изменения не нарушают связи с другими объектами."
                );
            }

            // Проверка на превышение длины поля
            if (errorMsg != null && errorMsg.contains("value too long")) {
                throw new BadRequestException(
                        "Длина одного из полей превышает допустимый лимит. " +
                                "Проверьте длину вводимых данных."
                );
            }

            // Все остальные случаи передаём глобальному обработчику
            throw e;

        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();

        } catch (IllegalArgumentException e) {
            throw new BadRequestException(
                    "Некорректные данные для обновления категории: " + e.getMessage()
            );
        }
    }

}