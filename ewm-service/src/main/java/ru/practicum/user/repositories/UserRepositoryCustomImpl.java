package ru.practicum.user.repositories;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.user.User;

import java.util.List;

import static ru.practicum.user.QUser.user;

@Repository
@RequiredArgsConstructor
public class UserRepositoryCustomImpl implements UserRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<User> findUsers(Integer from, Integer size) {
        var query = jpaQueryFactory
                .selectFrom(user)
                .orderBy(user.id.asc());

        List<User> users = query
                .offset(from)
                .limit(size)
                .fetch();

        if (users.isEmpty()) {
            return List.of();
        }

        return users;
    }

    @Override
    public List<User> findUsers(List<Long> ids, Integer from, Integer size) {
        var query = jpaQueryFactory
                .selectFrom(user)
                .orderBy(user.id.asc());

        if (ids != null && !ids.isEmpty()) {
            query = query.where(user.id.in(ids));
        }

        List<User> users = query
                .offset(from)
                .limit(size)
                .fetch();

        if (users.isEmpty()) {
            return List.of();
        }

        return users;
    }
}