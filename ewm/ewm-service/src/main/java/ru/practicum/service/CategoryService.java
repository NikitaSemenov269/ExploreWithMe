package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.NewCategoryDto;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.CategoryMapper;
import ru.practicum.model.Category;
import ru.practicum.repository.CategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис для работы с категориями
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    /**
     * Добавить новую категорию
     */
    @Transactional
    public CategoryDto addCategory(NewCategoryDto newCategoryDto) {
        Category category = categoryMapper.toEntity(newCategoryDto);

        try {
            category = categoryRepository.save(category);
            log.info("Создана новая категория с ID: {}", category.getId());
            return categoryMapper.toDto(category);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("Категория с именем '" + newCategoryDto.getName() + "' уже существует");
        }
    }

    /**
     * Удалить категорию
     */
    @Transactional
    public void deleteCategory(Long catId) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Категория не найдена с ID: " + catId));

        try {
            categoryRepository.delete(category);
            log.info("Удалена категория с ID: {}", catId);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("Нельзя удалить категорию, так как с ней связаны события");
        }
    }

    /**
     * Обновить категорию
     */
    @Transactional
    public CategoryDto updateCategory(Long catId, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Категория не найдена с ID: " + catId));

        category.setName(categoryDto.getName());

        try {
            category = categoryRepository.save(category);
            log.info("Обновлена категория с ID: {}", catId);
            return categoryMapper.toDto(category);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("Категория с именем '" + categoryDto.getName() + "' уже существует");
        }
    }

    /**
     * Получить все категории
     */
    public List<CategoryDto> getCategories(Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        List<Category> categories = categoryRepository.findAll(pageable).getContent();

        log.info("Получено {} категорий", categories.size());
        return categories.stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Получить категорию по ID
     */
    public CategoryDto getCategoryById(Long catId) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Категория не найдена с ID: " + catId));

        log.info("Получена категория с ID: {}", catId);
        return categoryMapper.toDto(category);
    }
}