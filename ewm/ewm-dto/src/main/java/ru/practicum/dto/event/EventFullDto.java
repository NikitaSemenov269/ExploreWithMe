package ru.practicum.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.dto.location.LocationDto;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.user.UserShortDto;
import ru.practicum.enumeration.EventState;

import java.time.LocalDateTime;

/**
 * Событие (полная информация)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventFullDto {

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
     * Количество одобренных заявок на участие в данном событии
     */
    private Long confirmedRequests;

    /**
     * Дата и время создания события
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;

    /**
     * Полное описание события
     */
    private String description;

    /**
     * Дата и время на которые намечено событие
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
     * Локация события
     */
    @NotNull
    private LocationDto location;

    /**
     * Нужно ли оплачивать участие
     */
    @NotNull
    private Boolean paid;

    /**
     * Ограничение на количество участников. 0 = без ограничения
     */
    private Integer participantLimit;

    /**
     * Дата и время публикации события
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn;

    /**
     * Нужна ли пре-модерация заявок на участие
     */
    private Boolean requestModeration;

    /**
     * Состояние жизненного цикла события
     */
    private EventState state;

    /**
     * Заголовок события
     */
    @NotBlank
    private String title;

    /**
     * Количество просмотров события
     */
    private Long views;
}