package ru.practicum.dto.compilation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Данные для создания новой подборки событий
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewCompilationDto {

    /**
     * Список идентификаторов событий входящих в подборку
     */
    private List<Long> events;

    /**
     * Закреплена ли подборка на главной странице сайта (по умолчанию false)
     */
    @Builder.Default
    private Boolean pinned = false;

    /**
     * Заголовок подборки
     */
    @NotBlank(message = "Заголовок подборки не может быть пустым")
    @Size(min = 1, max = 50, message = "Заголовок подборки должен быть от 1 до 50 символов")
    private String title;
}