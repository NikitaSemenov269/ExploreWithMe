package ru.practicum.category.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.DTO.category.CategoryDto;
import ru.practicum.DTO.category.NewCategoryDto;
import ru.practicum.category.Category;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.repositories.CategoryRepository;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminCategoryServiceImpl implements AdminCategoryService {

    private final CategoryRepository categoryRepository;


    public Category findById(Long catId) {
        return categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException(String.format("Категория c id %d не найдена", catId)));
    }

    @Override
    public CategoryDto save(NewCategoryDto request) {
        Category category = CategoryMapper.INSTANCE.toEntity(request);
        try {
            category = categoryRepository.save(category);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException(String.format("Категория \"%s\" уже существует", category.getName()), e);
        }
        log.info("Сохраняем данные о категории {}", request.getName());
        return CategoryMapper.INSTANCE.toDto(category);
    }

    @Override
    public CategoryDto update(NewCategoryDto request, Long catId) {
        Category updatedCategory = CategoryMapper.INSTANCE.updateFromNewCategoryDto(request, findById(catId));

        updatedCategory = categoryRepository.save(updatedCategory);

        log.info("Обновляем категорию \"{}\"", updatedCategory.getName());
        return CategoryMapper.INSTANCE.toDto(updatedCategory);
    }

    @Override
    public void delete(Long catId) {
        Category category = findById(catId);
        categoryRepository.deleteById(catId);
        log.info("Категория \"{}\" удалена", category.getName());
    }
}
