package ru.practicum.category.services;

import ru.practicum.DTO.category.CategoryDto;
import ru.practicum.DTO.category.NewCategoryDto;
import ru.practicum.category.Category;

public interface AdminCategoryService {
    Category findById(Long catId);

    CategoryDto save(NewCategoryDto request);

    CategoryDto update(NewCategoryDto request, Long catId);

    void delete(Long catId);
}
