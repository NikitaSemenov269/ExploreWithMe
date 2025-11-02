package ru.practicum.category.controller;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.DTO.category.CategoryDto;
import ru.practicum.category.services.PublicCategoryService;

import java.util.Collection;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/categories")
public class PublicCategoryController {

    private static final String VALUE_CAT_ID = "/{cat-id}";
    private static final String PATH_VARIABLE_CAT_ID = "cat-id";

    private final PublicCategoryService service;

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public Collection<CategoryDto> getAll(@RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                          @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("Получен запрос GET /categories с параметрами from = {}, size = {}", from, size);
        return service.getAll(from, size);
    }

    @GetMapping(value = VALUE_CAT_ID)
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto get(@PathVariable(value = PATH_VARIABLE_CAT_ID) Long catId) {
        log.info("Получен запрос GET /categories/{}", catId);
        return service.get(catId);
    }
}
