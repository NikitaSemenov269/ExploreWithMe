package ru.practicum.controller.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.service.CategoryService;

import java.util.List;

/**
 * Контроллер для публичного доступа к категориям
 */
@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Slf4j
public class PublicCategoryController {

    private final CategoryService categoryService;

    /**
     * Получение категорий
     * GET /categories
     */
    @GetMapping
    public List<CategoryDto> getCategories(@RequestParam(defaultValue = "0") Integer from,
                                           @RequestParam(defaultValue = "10") Integer size) {
        log.info("GET /categories?from={}&size={} - получение категорий", from, size);
        return categoryService.getCategories(from, size);
    }

    /**
     * Получение информации о категории по её идентификатору
     * GET /categories/{catId}
     */
    @GetMapping("/{catId}")
    public CategoryDto getCategoryById(@PathVariable Long catId) {
        log.info("GET /categories/{} - получение категории по ID", catId);
        return categoryService.getCategoryById(catId);
    }
}