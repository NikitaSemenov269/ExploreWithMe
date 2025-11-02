package ru.practicum.category.repositories;

import ru.practicum.category.Category;

import java.util.List;

public interface CategoryRepositoryCustom {

    List<Category> findCategories(Integer from, Integer size);

}
