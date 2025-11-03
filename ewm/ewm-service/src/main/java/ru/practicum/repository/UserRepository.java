package ru.practicum.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.model.User;

import java.util.List;

/**
 * Репозиторий для работы с пользователями
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Найти пользователей по списку ID с пагинацией
     *
     * @param ids список ID пользователей
     * @param pageable параметры пагинации
     * @return список пользователей
     */
    List<User> findByIdIn(List<Long> ids, Pageable pageable);

    /**
     * Проверить существует ли пользователь с данным email
     *
     * @param email email для проверки
     * @return true если пользователь с таким email существует
     */
    boolean existsByEmail(String email);
}