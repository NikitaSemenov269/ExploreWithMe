package ru.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.NewCategoryDto;
import ru.practicum.model.Category;

/**
 * Маппер для преобразования Category entity в DTO и обратно
 */
@Mapper(componentModel = "spring")
public interface CategoryMapper {

    /**
     * Преобразовать Category entity в CategoryDto
     *
     * @param category Category entity
     * @return CategoryDto
     */
    CategoryDto toDto(Category category);

    /**
     * Преобразовать NewCategoryDto в Category entity
     *
     * @param newCategoryDto NewCategoryDto
     * @return Category entity
     */
    @Mapping(target = "id", ignore = true)
    Category toEntity(NewCategoryDto newCategoryDto);
}