package ru.practicum.compilation.repositories;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.compilation.Compilation;
import ru.practicum.compilation.QCompilation;
import ru.practicum.compilation.interfaces.CompilationRepositoryCustom;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CompilationRepositoryCustomImpl implements CompilationRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    // Черновая реализация метода для примера.
    @Override
    public List<Compilation> findByNameContains(String namePart) {
        QCompilation compilation = QCompilation.compilation;

        return jpaQueryFactory
                .selectFrom(compilation)
                .where(compilation.name.containsIgnoreCase(namePart))
                .fetch();
    }
}