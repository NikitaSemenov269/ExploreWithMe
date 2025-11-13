package ru.practicum.dto.compilation;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Изменение информации о подборке событий.
 * Если поле в запросе не указано (равно null) - значит изменение этих данных не требуется.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCompilationRequest {

    /**
     * Список id событий подборки для полной замены текущего списка
     */
    private List<Long> events;

    /**
     * Закреплена ли подборка на главной странице сайта
     */
    private Boolean pinned;

    /**
     * Заголовок подборки
     */
    @Size(min = 1, max = 50, message = "Заголовок подборки должен быть от 1 до 50 символов")
    private String title;
}