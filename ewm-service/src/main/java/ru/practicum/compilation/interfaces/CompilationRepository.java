package ru.practicum.compilation.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import ru.practicum.compilation.Compilation;

@Repository
public interface CompilationRepository extends JpaRepository<Compilation, Long>,
        QuerydslPredicateExecutor<Compilation>,
        CompilationRepositoryCustom {

    /* Данный интерфейс не требует расширений методами, т.к.:
     1. JpaRepository<Compilation, Long> предоставляет:
        - CRUD: save(), findById(), findAll(), delete(), count(), existsById()
        - Пакетные: saveAll(), deleteAll(), deleteInBatch()
        - Сортировка/пагинация: findAll(Sort), findAll(Pageable)

     2. QuerydslPredicateExecutor<Compilation> предоставляет:
        - Динамические предикаты: findOne(Predicate), findAll(Predicate)
        - count(Predicate), exists(Predicate) с сортировкой и пагинацией

     3. CompilationRepositoryCustom предоставляет:
        - Кастомные методы для сложной бизнес-логики
        - Сложные QueryDSL запросы, JOIN операции

     Для методов, которые могут быть обработаны JPA Spring boot - добавляйте их в этот интерфейс.
     Для расширения более сложной функциональности добавляйте методы в CompilationRepositoryCustom.*/
}
