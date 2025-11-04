package ru.practicum.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.user.UserShortDto;

import java.time.LocalDateTime;

/**
 * Краткая информация о событии
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventShortDto {

    /**
     * Идентификатор
     */
    private Long id;

    /**
     * Краткое описание
     */
    @NotBlank
    private String annotation;

    /**
     * Категория
     */
    @NotNull
    private CategoryDto category;

    /**
     * Количество одобренных заявок на участие
     */
    private Long confirmedRequests;

    /**
     * Дата и время проведения события
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull
    private LocalDateTime eventDate;

    /**
     * Инициатор события
     */
    @NotNull
    private UserShortDto initiator;

    /**
     * Нужно ли оплачивать участие
     */
    @NotNull
    private Boolean paid;

    /**
     * Заголовок
     */
    @NotBlank
    private String title;

    /**
     * Количество просмотров события
     */
    private Long views;
}