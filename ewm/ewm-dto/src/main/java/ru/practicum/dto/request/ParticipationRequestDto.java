package ru.practicum.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.enumeration.ParticipationStatus;

import java.time.LocalDateTime;

/**
 * Заявка на участие в событии
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParticipationRequestDto {

    /**
     * Идентификатор заявки
     */
    private Long id;

    /**
     * Дата и время создания заявки
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime created;

    /**
     * Идентификатор события
     */
    private Long event;

    /**
     * Идентификатор пользователя, отправившего заявку
     */
    private Long requester;

    /**
     * Статус заявки
     */
    private ParticipationStatus status;
}