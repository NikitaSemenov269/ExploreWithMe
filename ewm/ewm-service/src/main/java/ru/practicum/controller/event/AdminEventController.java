package ru.practicum.controller.event;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.UpdateEventAdminRequest;
import ru.practicum.enumeration.EventState;
import ru.practicum.service.EventService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Admin API для работы с событиями
 */
@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
@Slf4j
@Validated
public class AdminEventController {

     private final EventService eventService;

    /**
     * Поиск событий администратором
     *
     * @param users список id пользователей, чьи события нужно найти
     * @param states список состояний в которых находятся искомые события
     * @param categories список id категорий в которых будет вестись поиск
     * @param rangeStart дата и время не раньше которых должно произойти событие
     * @param rangeEnd дата и время не позже которых должно произойти событие
     * @param from количество элементов, которые нужно пропустить для формирования текущего набора
     * @param size количество элементов в наборе
     * @return список событий
     */
    @GetMapping
    public List<EventFullDto> getEvents(
            @RequestParam(required = false) List<Long> users,
            @RequestParam(required = false) List<EventState> states,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "10") @Positive Integer size) {

        log.info("GET /admin/events: users={}, states={}, categories={}, rangeStart={}, rangeEnd={}, from={}, size={}",
                users, states, categories, rangeStart, rangeEnd, from, size);

        return eventService.getAdminEvents(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    /**
     * Редактирование данных события и его статуса (отклонение/публикация)
     *
     * @param eventId id события
     * @param updateRequest данные для изменения события
     * @return обновленное событие
     */
    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(
            @PathVariable @Min(1) Long eventId,
            @Valid @RequestBody UpdateEventAdminRequest updateRequest) {

        log.info("PATCH /admin/events/{}: updateRequest={}", eventId, updateRequest);

        return eventService.updateEventByAdmin(eventId, updateRequest);
    }
}