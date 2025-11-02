package ru.practicum.category.services;

import ru.practicum.DTO.category.CategoryDto;

import java.util.Collection;

public interface PublicCategoryService {
    Collection<CategoryDto> getAll(Integer from, Integer size);

    CategoryDto get(Long catId);
}
