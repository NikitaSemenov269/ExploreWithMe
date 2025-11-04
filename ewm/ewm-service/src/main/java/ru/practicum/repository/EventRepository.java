package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import ru.practicum.model.Event;

/**
 * Репозиторий для работы с событиями
 */
@Repository
public interface EventRepository extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event> {
    Long getMaxRequestsLimit(Long eventId);

    boolean isPublished(Long eventId);

    boolean isInitiator(Long eventId, Long userId);
}