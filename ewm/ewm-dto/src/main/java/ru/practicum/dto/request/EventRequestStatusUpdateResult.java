package ru.practicum.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Результат подтверждения/отклонения заявок на участие в событии
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventRequestStatusUpdateResult {

    /**
     * Подтвержденные заявки
     */
    private List<ParticipationRequestDto> confirmedRequests;

    /**
     * Отклоненные заявки
     */
    private List<ParticipationRequestDto> rejectedRequests;
}