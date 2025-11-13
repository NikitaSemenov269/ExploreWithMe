package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.model.Category;

/**
 * Репозиторий для работы с категориями
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * Проверить существует ли категория с данным названием
     *
     * @param name название категории
     * @return true если категория с таким именем существует
     */
    boolean existsByName(String name);
}