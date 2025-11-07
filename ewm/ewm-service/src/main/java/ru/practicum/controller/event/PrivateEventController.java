package ru.practicum.controller.event;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.dto.event.UpdateEventUserRequest;
import ru.practicum.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.service.EventService;

import java.util.List;

/**
 * Private API для работы с событиями текущего пользователя
 */
@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
@Slf4j
@Validated
public class PrivateEventController {

    private final EventService eventService;

    /**
     * Получение событий, добавленных текущим пользователем
     *
     * @param userId id текущего пользователя
     * @param from количество элементов, которые нужно пропустить для формирования текущего набора
     * @param size количество элементов в наборе
     * @return список событий
     */
    @GetMapping
    public List<EventShortDto> getUserEvents(
            @PathVariable @Min(1) Long userId,
            @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "10") @Positive Integer size) {

        log.info("GET /users/{}/events: from={}, size={}", userId, from, size);

        return eventService.getUserEvents(userId, from, size);
    }

    /**
     * Добавление нового события
     *
     * @param userId id текущего пользователя
     * @param newEventDto данные добавляемого события
     * @return добавленное событие
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto addEvent(
            @PathVariable @Min(1) Long userId,
            @Valid @RequestBody NewEventDto newEventDto) {

        log.info("POST /users/{}/events: newEventDto={}", userId, newEventDto);

        return eventService.addEvent(userId, newEventDto);
    }

    /**
     * Получение полной информации о событии, добавленном текущим пользователем
     *
     * @param userId id текущего пользователя
     * @param eventId id события
     * @return полная информация о событии
     */
    @GetMapping("/{eventId}")
    public EventFullDto getUserEventById(
            @PathVariable @Min(1) Long userId,
            @PathVariable @Min(1) Long eventId) {

        log.info("GET /users/{}/events/{}", userId, eventId);

        return eventService.getUserEventById(userId, eventId);
    }

    /**
     * Изменение события, добавленного текущим пользователем
     *
     * @param userId id текущего пользователя
     * @param eventId id события
     * @param updateRequest данные для изменения события
     * @return обновленное событие
     */
    @PatchMapping("/{eventId}")
    public EventFullDto updateUserEvent(
            @PathVariable @Min(1) Long userId,
            @PathVariable @Min(1) Long eventId,
            @Valid @RequestBody UpdateEventUserRequest updateRequest) {

        log.info("PATCH /users/{}/events/{}: updateRequest={}", userId, eventId, updateRequest);

        return eventService.updateUserEvent(userId, eventId, updateRequest);
    }

    @PatchMapping("/{eventId}/requests")
    public ResponseEntity<EventRequestStatusUpdateResult> updateRequestsStatus(
            @PathVariable @Min(1) Long userId,
            @PathVariable @Min(1) Long eventId,
            @Valid @RequestBody EventRequestStatusUpdateRequest request) {

        EventRequestStatusUpdateResult result = eventService.updateRequestsStatus(userId, eventId, request);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{eventId}/requests")
    public ResponseEntity<List<ParticipationRequestDto>> getEventParticipants(
            @PathVariable @NotNull Long userId,
            @PathVariable @NotNull Long eventId) {

        List<ParticipationRequestDto> dtos = eventService.getEventParticipantRequests(userId, eventId);
        return ResponseEntity.ok(dtos);
    }
}