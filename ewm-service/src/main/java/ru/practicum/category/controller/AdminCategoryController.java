package ru.practicum.category.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.DTO.category.CategoryDto;
import ru.practicum.DTO.category.NewCategoryDto;
import ru.practicum.category.services.AdminCategoryService;
import ru.practicum.exception.DuplicatedDataException;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/categories")
public class AdminCategoryController {
    private static final String VALUE_CAT_ID = "/{cat-id}";
    private static final String PATH_VARIABLE_CAT_ID = "cat-id";

    private final AdminCategoryService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto save(@RequestBody @Valid NewCategoryDto request) {
        log.info("Получен запрос POST /admin/categories c новой категорией: \"{}\"", request.getName());
        return service.save(request);
    }

    @PatchMapping(VALUE_CAT_ID)
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto update(@PathVariable(PATH_VARIABLE_CAT_ID) Long catId,
                              @RequestBody @Valid NewCategoryDto request) {
        log.info("Получен запрос PATCH /admin/categories/{}", catId);

        CategoryDto result;
        try {
            result = service.update(request, catId);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicatedDataException(e.getMessage(), e);
        }
        return result;
    }

    @DeleteMapping(VALUE_CAT_ID)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(PATH_VARIABLE_CAT_ID) Long catId) {
        log.info("Получен запрос DELETE /admin/categories/{}", catId);
        service.delete(catId);
    }
}
