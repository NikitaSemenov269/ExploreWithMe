package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import ru.practicum.model.Compilation;

/**
 * Репозиторий для работы с подборками
 */

@Repository
public interface CompilationRepository extends JpaRepository<Compilation, Long>,
        QuerydslPredicateExecutor<Compilation> {
}
