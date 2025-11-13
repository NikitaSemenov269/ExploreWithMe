package ru.practicum.util;

import java.util.List;

/**
 * Утилиты для работы с URI
 */
public class UriUtils {

    private UriUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Формирование URI для события
     *
     * @param eventId ID события
     * @return URI события
     */
    public static String makeEventUri(Long eventId) {
        return "/events/" + eventId;
    }

    /**
     * Формирование списка URI для событий
     *
     * @param eventIds список ID событий
     * @return список URI событий
     */
    public static List<String> makeEventUris(List<Long> eventIds) {
        return eventIds.stream()
                .map(UriUtils::makeEventUri)
                .toList();
    }
}