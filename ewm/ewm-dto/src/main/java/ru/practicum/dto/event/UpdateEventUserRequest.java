package ru.practicum.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.dto.location.LocationDto;
import ru.practicum.enumeration.StateAction;

import java.time.LocalDateTime;

/**
 * Данные для изменения информации о событии пользователем.
 * Если поле в запросе не указано (равно null) - значит изменение этих данных не требуется.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEventUserRequest {

    /**
     * Новая аннотация
     */
    @Size(min = 20, max = 2000, message = "Аннотация должна быть от 20 до 2000 символов")
    private String annotation;

    /**
     * Новая категория
     */
    private Long category;

    /**
     * Новое описание
     */
    @Size(min = 20, max = 7000, message = "Описание должно быть от 20 до 7000 символов")
    private String description;

    /**
     * Новые дата и время на которые намечено событие
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    /**
     * Новая локация события
     */
    private LocationDto location;

    /**
     * Новое значение флага о платности мероприятия
     */
    private Boolean paid;

    /**
     * Новый лимит пользователей
     */
    @Min(value = 0, message = "Лимит участников не может быть отрицательным")
    private Integer participantLimit;

    /**
     * Нужна ли пре-модерация заявок на участие
     */
    private Boolean requestModeration;

    /**
     * Изменение состояния события (SEND_TO_REVIEW, CANCEL_REVIEW)
     */
    private StateAction stateAction;

    /**
     * Новый заголовок
     */
    @Size(min = 3, max = 120, message = "Заголовок должен быть от 3 до 120 символов")
    private String title;
}