package ru.practicum.enumeration;

/**
 * Статус заявки на участие в событии
 */
public enum ParticipationStatus {
    /**
     * Ожидает рассмотрения
     */
    PENDING,

    /**
     * Подтверждена
     */
    CONFIRMED,

    /**
     * Отклонена
     */
    REJECTED,

    /**
     * Отменена инициатором заявки
     */
    CANCELED
}