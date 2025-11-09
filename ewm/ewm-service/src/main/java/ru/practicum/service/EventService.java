package ru.practicum.service;

import com.querydsl.core.BooleanBuilder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.DTO.RequestStatisticDto;
import ru.practicum.DTO.ResponseStatisticDto;
import ru.practicum.StatsClient;
import ru.practicum.dto.event.*;
import ru.practicum.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.enumeration.EventSort;
import ru.practicum.enumeration.EventState;
import ru.practicum.enumeration.ParticipationStatus;
import ru.practicum.enumeration.StateAction;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.EventMapper;
import ru.practicum.mapper.ParticipationRequestMapper;
import ru.practicum.model.*;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.ParticipationRequestRepository;
import ru.practicum.repository.UserRepository;
import ru.practicum.util.UriUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Сервис для работы с событиями
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final EventMapper eventMapper;
    private final StatsClient statsClient;
    private final ParticipationRequestRepository requestRepository;

    private static final int MIN_HOURS_BEFORE_EVENT = 2;
    private static final String APP_NAME = "ewm-service";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Получить публичные события с фильтрацией (только опубликованные)
     */
    public List<EventShortDto> getPublicEvents(
            String text,
            List<Long> categories,
            Boolean paid,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            Boolean onlyAvailable,
            EventSort sort,
            Integer from,
            Integer size,
            HttpServletRequest request) {

        // Валидация: rangeEnd должен быть позже rangeStart
        if (rangeStart != null && rangeEnd != null && rangeEnd.isBefore(rangeStart)) {
            throw new BadRequestException("Дата окончания не может быть раньше даты начала");
        }

        BooleanBuilder predicate = new BooleanBuilder();

        // Только опубликованные события
        predicate.and(QEvent.event.state.eq(EventState.PUBLISHED));

        // Текстовый поиск (без учета регистра)
        if (text != null && !text.isBlank()) {
            predicate.and(QEvent.event.annotation.containsIgnoreCase(text)
                    .or(QEvent.event.description.containsIgnoreCase(text)));
        }

        // Фильтр по категориям
        if (categories != null && !categories.isEmpty()) {
            predicate.and(QEvent.event.category.id.in(categories));
        }

        // Фильтр платные/бесплатные
        if (paid != null) {
            predicate.and(QEvent.event.paid.eq(paid));
        }

        // Фильтр по датам
        LocalDateTime start = rangeStart != null ? rangeStart : LocalDateTime.now();
        predicate.and(QEvent.event.eventDate.after(start));

        if (rangeEnd != null) {
            predicate.and(QEvent.event.eventDate.before(rangeEnd));
        }

        // Фильтр только доступные (не исчерпан лимит)
        if (onlyAvailable != null && onlyAvailable) {
            predicate.and(QEvent.event.participantLimit.eq(0)
                    .or(QEvent.event.confirmedRequests.lt(QEvent.event.participantLimit)));
        }

        // Сортировка
        Pageable pageable = PageRequest.of(from / size, size);
        List<Event> eventsList = StreamSupport.stream(
                        eventRepository.findAll(predicate, pageable).spliterator(), false)
                .toList();

        // Логирование запроса в статистику
        RequestStatisticDto hitDto = new RequestStatisticDto(
                APP_NAME,
                request.getRequestURI(),
                request.getRemoteAddr(),
                LocalDateTime.now().format(FORMATTER)
        );
        statsClient.saveHit(hitDto);

        // Получение просмотров из статистики
        Map<Long, Long> viewsMap = new HashMap<>();
        if (!eventsList.isEmpty()) {
            List<Long> eventIds = eventsList.stream()
                    .map(Event::getId)
                    .collect(Collectors.toList());

            List<String> uris = UriUtils.makeEventUris(eventIds);

            LocalDateTime statsStart = eventsList.stream()
                    .map(Event::getCreatedOn)
                    .min(LocalDateTime::compareTo)
                    .orElse(LocalDateTime.now().minusYears(1));

            List<ResponseStatisticDto> stats = statsClient.getStats(
                    statsStart.format(FORMATTER),
                    LocalDateTime.now().format(FORMATTER),
                    uris,
                    true
            );

            viewsMap = stats.stream()
                    .collect(Collectors.toMap(
                            stat -> Long.parseLong(stat.getUri().substring(stat.getUri().lastIndexOf("/") + 1)),
                            ResponseStatisticDto::getHits,
                            (existing, replacement) -> existing
                    ));
        }

        // Преобразование в DTO с проставлением просмотров
        Map<Long, Long> finalViewsMap = viewsMap;
        List<EventShortDto> result = eventsList.stream()
                .map(event -> {
                    EventShortDto dto = eventMapper.toShortDto(event);
                    dto.setViews(finalViewsMap.getOrDefault(event.getId(), 0L));
                    return dto;
                })
                .collect(Collectors.toList());

        // Сортировка по просмотрам, если указана
        if (sort == EventSort.VIEWS) {
            result.sort(Comparator.comparing(EventShortDto::getViews).reversed());
        }

        log.info("Найдено {} публичных событий", result.size());
        return result;
    }

    /**
     * Получить публичное событие по ID
     */
    public EventFullDto getPublicEventById(Long id, HttpServletRequest request) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Событие не найдено с ID: " + id));

        if (event.getState() != EventState.PUBLISHED) {
            throw new NotFoundException("Событие не опубликовано");
        }

        // Логирование просмотра в статистику
        RequestStatisticDto hitDto = new RequestStatisticDto(
                APP_NAME,
                request.getRequestURI(),
                request.getRemoteAddr(),
                LocalDateTime.now().format(FORMATTER)
        );
        statsClient.saveHit(hitDto);

        // Получение количества просмотров из статистики
        List<ResponseStatisticDto> stats = statsClient.getStats(
                event.getCreatedOn().format(FORMATTER),
                LocalDateTime.now().format(FORMATTER),
                List.of(UriUtils.makeEventUri(id)),
                true
        );

        EventFullDto result = eventMapper.toFullDto(event);
        result.setViews(stats.isEmpty() ? 0L : stats.getFirst().getHits());

        log.info("Получено публичное событие с ID: {}, просмотров: {}", id, result.getViews());
        return result;
    }

    /**
     * Получить события текущего пользователя
     */
    public List<EventShortDto> getUserEvents(Long userId, Integer from, Integer size) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь не найден с ID: " + userId);
        }

        BooleanBuilder predicate = new BooleanBuilder();
        predicate.and(QEvent.event.initiator.id.eq(userId));

        Pageable pageable = PageRequest.of(from / size, size);
        Iterable<Event> events = eventRepository.findAll(predicate, pageable);

        List<EventShortDto> result = StreamSupport.stream(events.spliterator(), false)
                .map(eventMapper::toShortDto)
                .collect(Collectors.toList());

        log.info("Получено {} событий пользователя {}", result.size(), userId);
        return result;
    }

    /**
     * Добавить новое событие
     */
    @Transactional
    public EventFullDto addEvent(Long userId, NewEventDto newEventDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден с ID: " + userId));

        Category category = categoryRepository.findById(newEventDto.getCategory())
                .orElseThrow(() -> new NotFoundException("Категория не найдена с ID: " + newEventDto.getCategory()));

        // Валидация: дата события должна быть не раньше чем через 2 часа
        if (newEventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(MIN_HOURS_BEFORE_EVENT))) {
            throw new BadRequestException("Дата события должна быть не раньше чем через " +
                    MIN_HOURS_BEFORE_EVENT + " часа от текущего момента");
        }

        Event event = eventMapper.toEntity(newEventDto);
        event.setInitiator(user);
        event.setCategory(category);
        event.setCreatedOn(LocalDateTime.now());
        event.setState(EventState.PENDING);
        event.setConfirmedRequests(0);

        event = eventRepository.save(event);
        log.info("Создано новое событие с ID: {}", event.getId());

        return eventMapper.toFullDto(event);
    }

    /**
     * Получить событие пользователя по ID
     */
    public EventFullDto getUserEventById(Long userId, Long eventId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь не найден с ID: " + userId);
        }

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие не найдено с ID: " + eventId));

        if (!event.getInitiator().getId().equals(userId)) {
            throw new NotFoundException("Событие не принадлежит пользователю");
        }

        return eventMapper.toFullDto(event);
    }

    /**
     * Обновить событие пользователя
     */
    @Transactional
    public EventFullDto updateUserEvent(Long userId, Long eventId, UpdateEventUserRequest updateRequest) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь не найден с ID: " + userId);
        }

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие не найдено с ID: " + eventId));

        if (!event.getInitiator().getId().equals(userId)) {
            throw new ConflictException("Событие не принадлежит пользователю");
        }

        // Можно изменить только отмененные события или события в состоянии ожидания модерации
        if (event.getState() == EventState.PUBLISHED) {
            throw new ConflictException("Нельзя изменить опубликованное событие");
        }

        // Валидация даты
        if (updateRequest.getEventDate() != null &&
                updateRequest.getEventDate().isBefore(LocalDateTime.now().plusHours(MIN_HOURS_BEFORE_EVENT))) {
            throw new BadRequestException("Дата события должна быть не раньше чем через " +
                    MIN_HOURS_BEFORE_EVENT + " часа от текущего момента");
        }

        // Обновление категории если указана
        if (updateRequest.getCategory() != null) {
            Category category = categoryRepository.findById(updateRequest.getCategory())
                    .orElseThrow(() -> new NotFoundException("Категория не найдена с ID: " + updateRequest.getCategory()));
            event.setCategory(category);
        }

        // Обновление состояния
        if (updateRequest.getStateAction() != null) {
            if (updateRequest.getStateAction() == StateAction.SEND_TO_REVIEW) {
                event.setState(EventState.PENDING);
            } else if (updateRequest.getStateAction() == StateAction.CANCEL_REVIEW) {
                event.setState(EventState.CANCELED);
            }
        }

        // Обновление остальных полей
        eventMapper.updateEventFromUserRequest(updateRequest, event);

        event = eventRepository.save(event);
        log.info("Обновлено событие с ID: {}", eventId);

        return eventMapper.toFullDto(event);
    }

    /**
     * Получить события администратором с фильтрацией
     */
    public List<EventFullDto> getAdminEvents(
            List<Long> users,
            List<EventState> states,
            List<Long> categories,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            Integer from,
            Integer size) {

        BooleanBuilder predicate = new BooleanBuilder();

        // Фильтр по пользователям
        if (users != null && !users.isEmpty()) {
            predicate.and(QEvent.event.initiator.id.in(users));
        }

        // Фильтр по состояниям
        if (states != null && !states.isEmpty()) {
            predicate.and(QEvent.event.state.in(states));
        }

        // Фильтр по категориям
        if (categories != null && !categories.isEmpty()) {
            predicate.and(QEvent.event.category.id.in(categories));
        }

        // Фильтр по датам
        if (rangeStart != null) {
            predicate.and(QEvent.event.eventDate.after(rangeStart));
        }

        if (rangeEnd != null) {
            predicate.and(QEvent.event.eventDate.before(rangeEnd));
        }

        Pageable pageable = PageRequest.of(from / size, size);
        Iterable<Event> events = eventRepository.findAll(predicate, pageable);

        List<EventFullDto> result = StreamSupport.stream(events.spliterator(), false)
                .map(eventMapper::toFullDto)
                .collect(Collectors.toList());

        log.info("Администратор получил {} событий", result.size());
        return result;
    }

    /**
     * Обновить событие администратором
     */
    @Transactional
    public EventFullDto updateEventByAdmin(Long eventId, UpdateEventAdminRequest updateRequest) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие не найдено с ID: " + eventId));

        // Валидация даты: должна быть не ранее чем за час от даты публикации
        if (updateRequest.getEventDate() != null) {
            if (updateRequest.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
                throw new BadRequestException("Дата начала события должна быть не ранее чем за час от даты публикации");
            }
        }

        // Обновление категории если указана
        if (updateRequest.getCategory() != null) {
            Category category = categoryRepository.findById(updateRequest.getCategory())
                    .orElseThrow(() -> new NotFoundException("Категория не найдена с ID: " + updateRequest.getCategory()));
            event.setCategory(category);
        }

        // Обновление состояния
        if (updateRequest.getStateAction() != null) {
            if (updateRequest.getStateAction() == StateAction.PUBLISH_EVENT) {
                if (event.getState() != EventState.PENDING) {
                    throw new ConflictException("Можно публиковать только события в состоянии ожидания публикации");
                }
                event.setState(EventState.PUBLISHED);
                event.setPublishedOn(LocalDateTime.now());
            } else if (updateRequest.getStateAction() == StateAction.REJECT_EVENT) {
                if (event.getState() == EventState.PUBLISHED) {
                    throw new ConflictException("Нельзя отклонить опубликованное событие");
                }
                event.setState(EventState.CANCELED);
            }
        }

        // Обновление остальных полей
        eventMapper.updateEventFromAdminRequest(updateRequest, event);

        event = eventRepository.save(event);
        log.info("Администратор обновил событие с ID: {}", eventId);

        return eventMapper.toFullDto(event);
    }


    @Transactional
    public EventRequestStatusUpdateResult updateRequestsStatus(
            Long userId, Long eventId, EventRequestStatusUpdateRequest request) {

        if (request.getStatus() == ParticipationStatus.PENDING) {
            throw new BadRequestException("Статус 'PENDING' не может быть установлен для заявок с ID: " + request.getRequestIds());
        }

        List<Long> requestIds = request.getRequestIds();

        // 1. Проверяем существование события
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId));


        // 2. Проверяем пре‑модерацию и лимит
        boolean preModeration = event.getRequestModeration();
        int maxLimit = event.getParticipantLimit();


        if (maxLimit == 0 || !preModeration) {
            throw new ConflictException(
                    "Pre-moderation is disabled or limit is 0, no status change needed");
        }

        // 3. Проверяем, что пользователь — инициатор события
        if (!userId.equals(event.getInitiator().getId())) {
            throw new ConflictException("User is not event initiator");
        }

        // 4. Получаем заявки для обновления
        List<ParticipationRequest> requests = requestRepository.findAllByEventIdAndIdIn(
                eventId, requestIds);

        if (requests.isEmpty()) {
            throw new NotFoundException("No requests found for the given IDs");
        }

        // 5. Проверяем, что все заявки в статусе PENDING
        for (ParticipationRequest req : requests) {
            if (req.getStatus() != ParticipationStatus.PENDING) {
                throw new ConflictException(
                        "Request " + req.getId() + " is not in PENDING status");
            }
        }

        // 6. Проверяем лимит подтверждённых заявок
        long confirmedCount = requestRepository.countByEventIdAndStatus(
                eventId, ParticipationStatus.CONFIRMED);

        if (confirmedCount >= maxLimit) {
            throw new ConflictException("Participant limit reached");
        }

        // 7. Обновляем статус выбранных заявок
        List<ParticipationRequest> rejectedDueToLimit = new ArrayList<>();
        long currentConfirmed = confirmedCount;

        if (request.getStatus() == ParticipationStatus.CONFIRMED) {
            currentConfirmed += requestIds.size();
        }

        // 8. Проверяем лимит и обновляем статусы
        if (currentConfirmed >= maxLimit) {
            long canConfirm = maxLimit - confirmedCount;

            List<Long> requestIdsPart = requestIds.stream()
                    .limit(canConfirm)
                    .collect(Collectors.toList());

            requestRepository.bulkUpdateStatus(eventId, requestIdsPart, request.getStatus());


            // Отклоняем оставшиеся PENDING заявки
            List<ParticipationRequest> allPendingRequests = requestRepository
                    .findAllByEventIdAndStatus(eventId, ParticipationStatus.PENDING);

            if (!allPendingRequests.isEmpty()) {
                requestRepository.rejectAllPendingRequests(eventId, ParticipationStatus.REJECTED);
                rejectedDueToLimit.addAll(allPendingRequests);
            }

            // УВЕЛИЧИВАЕМ confirmedRequests у события
            event.setConfirmedRequests(Math.min(maxLimit, (int) currentConfirmed));
            eventRepository.save(event);  // Сохраняем изменение


        } else {
            requestRepository.bulkUpdateStatus(eventId, requestIds, request.getStatus());

            // УВЕЛИЧИВАЕМ confirmedRequests на число подтверждённых заявок
            event.setConfirmedRequests((int) (confirmedCount + requestIds.size()));
            eventRepository.save(event);  // Сохраняем изменение
        }

        // 9. Формируем ответ
        List<ParticipationRequest> updatedRequests = requestRepository.findAllByEventIdAndIdIn(
                eventId, requestIds);
        Set<Long> alreadyRejectedIds = updatedRequests.stream()
                .filter(r -> r.getStatus() == ParticipationStatus.REJECTED)
                .map(ParticipationRequest::getId)
                .collect(Collectors.toSet());


        List<ParticipationRequestDto> confirmed = updatedRequests.stream()
                .filter(r -> r.getStatus() == ParticipationStatus.CONFIRMED)
                .map(ParticipationRequestMapper.INSTANCE::toDto)
                .toList();

        List<ParticipationRequestDto> rejected = new ArrayList<>(updatedRequests.stream()
                .filter(r -> r.getStatus() == ParticipationStatus.REJECTED)
                .map(ParticipationRequestMapper.INSTANCE::toDto)
                .toList());

        rejected.addAll(rejectedDueToLimit.stream()
                .filter(r -> !alreadyRejectedIds.contains(r.getId()))
                .map(ParticipationRequestMapper.INSTANCE::toDto)
                .toList());

        return EventRequestStatusUpdateResult.builder()
                .confirmedRequests(confirmed)
                .rejectedRequests(rejected)
                .build();
    }


    public List<ParticipationRequestDto> getEventParticipantRequests(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> {
                    log.warn("Event with ID {} not found", eventId);
                    return new NotFoundException("Event with id: " + eventId + "was not found");
                });
        User eventOwner = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("User with ID {} not found", userId);
                    return new NotFoundException("User with id: " + userId + "was not found");
                });
        if (!event.getInitiator().getId().equals(eventOwner.getId())) {
            throw new ConflictException("User with id = " + userId + " is not event initiator");
        }
        List<ParticipationRequest> requests = requestRepository.findAllByEventId(eventId);
        return requests.stream()
                .map(ParticipationRequestMapper.INSTANCE::toDto).toList();
    }
}