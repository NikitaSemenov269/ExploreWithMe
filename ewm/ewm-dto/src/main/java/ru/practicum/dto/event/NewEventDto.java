package ru.practicum.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.dto.location.LocationDto;

import java.time.LocalDateTime;

/**
 * Новое событие
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewEventDto {

    /**
     * Краткое описание
     */
    @NotBlank(message = "Аннотация не может быть пустой")
    @Size(min = 20, max = 2000, message = "Аннотация должна быть от 20 до 2000 символов")
    private String annotation;

    /**
     * Идентификатор категории
     */
    @NotNull(message = "Категория не может быть пустой")
    private Long category;

    /**
     * Полное описание события
     */
    @NotBlank(message = "Описание не может быть пустым")
    @Size(min = 20, max = 7000, message = "Описание должно быть от 20 до 7000 символов")
    private String description;

    /**
     * Дата и время на которые намечено событие
     */
    @NotNull(message = "Дата события не может быть пустой")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    /**
     * Локация события
     */
    @NotNull(message = "Локация не может быть пустой")
    private LocationDto location;

    /**
     * Нужно ли оплачивать участие (по умолчанию false)
     */
    @Builder.Default
    private Boolean paid = false;

    /**
     * Ограничение на количество участников. 0 = без ограничения (по умолчанию 0)
     */
    @Min(value = 0, message = "Лимит участников не может быть отрицательным")
    @Builder.Default
    private Integer participantLimit = 0;

    /**
     * Нужна ли пре-модерация заявок на участие (по умолчанию true)
     */
    @Builder.Default
    private Boolean requestModeration = true;

    /**
     * Заголовок события
     */
    @NotBlank(message = "Заголовок не может быть пустым")
    @Size(min = 3, max = 120, message = "Заголовок должен быть от 3 до 120 символов")
    private String title;
}