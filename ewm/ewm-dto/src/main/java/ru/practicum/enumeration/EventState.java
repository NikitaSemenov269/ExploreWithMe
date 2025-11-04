package ru.practicum.enumeration;

/**
 * Состояние жизненного цикла события
 */
public enum EventState {
    /**
     * Ожидание публикации (на модерации)
     */
    PENDING,

    /**
     * Опубликовано
     */
    PUBLISHED,

    /**
     * Отменено
     */
    CANCELED
}