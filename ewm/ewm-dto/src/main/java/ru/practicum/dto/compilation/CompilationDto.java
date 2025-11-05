package ru.practicum.dto.compilation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.dto.event.EventShortDto;

import java.util.List;

/**
 * Подборка событий
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompilationDto {

    /**
     * Идентификатор подборки
     */
    @NotNull
    private Long id;

    /**
     * Список событий входящих в подборку
     */
    private List<EventShortDto> events;

    /**
     * Закреплена ли подборка на главной странице сайта
     */
    @NotNull
    private Boolean pinned;

    /**
     * Заголовок подборки
     */
    @NotBlank
    private String title;
}