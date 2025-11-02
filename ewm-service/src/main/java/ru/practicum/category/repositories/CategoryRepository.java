package ru.practicum.category.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.category.Category;

public interface CategoryRepository extends JpaRepository<Category, Long>,
        QuerydslPredicateExecutor<Category>,
        CategoryRepositoryCustom {

}
