package ru.practicum.controller.category;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.NewCategoryDto;
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
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long catId) {
        log.info("DELETE /admin/categories/{} - удаление категории", catId);
        categoryService.deleteCategory(catId);
    }

    /**
     * Изменение категории
     * PATCH /admin/categories/{catId}
     */
    @PatchMapping("/{catId}")
    public CategoryDto updateCategory(@PathVariable Long catId,
                                      @Valid @RequestBody CategoryDto categoryDto) {
        log.info("PATCH /admin/categories/{} - обновление категории", catId);
        return categoryService.updateCategory(catId, categoryDto);
    }
}