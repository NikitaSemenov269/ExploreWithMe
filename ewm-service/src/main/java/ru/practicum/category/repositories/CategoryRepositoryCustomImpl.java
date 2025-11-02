package ru.practicum.category.repositories;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.category.Category;

import java.util.List;

import static ru.practicum.category.QCategory.category;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryCustomImpl implements CategoryRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public List<Category> findCategories(Integer from, Integer size) {
        List<Category> categories = jpaQueryFactory
                .selectFrom(category)
                .orderBy(category.id.asc())
                .offset(from)
                .limit(size)
                .fetch();

        if (categories.isEmpty()) {
            return List.of();
        }

        return categories;
    }
}
