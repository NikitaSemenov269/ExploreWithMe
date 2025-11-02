package ru.practicum.category.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import ru.practicum.DTO.category.CategoryDto;
import ru.practicum.DTO.category.NewCategoryDto;
import ru.practicum.category.Category;

import java.util.List;

@Mapper(componentModel = org.mapstruct.MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CategoryMapper {

    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    @Mapping(source = "name", target = "name")
    Category toEntity(NewCategoryDto newCategoryDto);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    CategoryDto toDto(Category category);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    Category toEntity(CategoryDto categoryDto);

    @Mapping(target = "name", source = "name")
    Category updateFromNewCategoryDto(NewCategoryDto newCategoryDto, @MappingTarget Category category);

    List<CategoryDto> toDtoList(List<Category> categories);

    List<Category> toEntityList(List<CategoryDto> categoryDtos);
}
