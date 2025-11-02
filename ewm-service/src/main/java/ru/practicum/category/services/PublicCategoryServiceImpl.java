package ru.practicum.category.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.DTO.category.CategoryDto;
import ru.practicum.category.Category;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.repositories.CategoryRepository;
import ru.practicum.exception.NotFoundException;

import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PublicCategoryServiceImpl implements PublicCategoryService {

    private final CategoryRepository categoryRepository;

    public Category findById(Long catId) {
        return categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException(String.format("Категория c id %d не найдена", catId)));
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<CategoryDto> getAll(Integer from, Integer size) {
        List<Category> categories = categoryRepository.findCategories(from, size);

        return CategoryMapper.INSTANCE.toDtoList(categories);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDto get(Long catId) {
        return CategoryMapper.INSTANCE.toDto(findById(catId));
    }
}
