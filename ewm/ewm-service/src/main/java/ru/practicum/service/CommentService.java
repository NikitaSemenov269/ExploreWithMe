package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.NewCommentDto;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.CommentMapper;
import ru.practicum.model.Comment;
import ru.practicum.model.Event;
import ru.practicum.model.User;
import ru.practicum.repository.CommentRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.ParticipationRequestRepository;
import ru.practicum.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.enumeration.EventState.PUBLISHED;
import static ru.practicum.enumeration.ParticipationStatus.CONFIRMED;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class CommentService {
    private final CommentRepository commRep;
    private final UserRepository userRep;
    private final EventRepository eventRep;
    private final ParticipationRequestRepository partReqRep;
    private final CommentMapper mapper;

    // Создание комментария для события
    public CommentDto addComment(Long userId, NewCommentDto newCommentDto) {
        Long eventId = newCommentDto.getEventId();
        log.info("Попытка создания нового комментария для события ID: {} от пользователя ID: {}", eventId, userId);

        Event event = eventRep.findById(eventId).orElseThrow(
                () -> new NotFoundException("Событие с ID: " + eventId + " не найдено.")
        );

        // Статус события должен быть "Опубликовано"
        if (!PUBLISHED.equals(event.getState())) {
            throw new ConflictException("Статус события: " + event.getState() + " не соответствует ожидаемому.");
        }

        /*
         * Нужна ли пре-модерация заявок на участие
         * true - заявки должны одобряться инициатором
         * false - заявки принимаются автоматически
         */
        if (event.getRequestModeration()) {
            // Поиск заявки на участие в событии
            if (!partReqRep.existsByRequesterIdAndEventIdAndStatus(userId, eventId, CONFIRMED)) {
                throw new ConflictException("Пользователь с ID: "
                        + userId + " не найден среди участников события с ID: "
                        + eventId);
            }
        }

        User user = userRep.findById(userId).orElseThrow(
                () -> new NotFoundException("Пользователь с ID: " + userId + " не найден."));

        try {
            Comment comment = mapper.toEntity(newCommentDto);
            comment.setCreatedOn(LocalDateTime.now());
            comment.setEvent(event);
            comment.setAuthor(user);

            Comment savedComment = commRep.save(comment);
            CommentDto commentDto = mapper.toDto(savedComment);

            log.info("Успешное сохранение нового комментария ID: {}," +
                    " для события ID: {}," +
                    " от пользователя ID: {}", savedComment.getId(), eventId, userId);

            return commentDto;
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("Непредвиденная ошибка при сохранении нового комментария.");
        }
    }

    // Поиск всех комментариев для определенного события
    @Transactional(readOnly = true)
    public List<CommentDto> findAllByIdEvent(Long eventId) {
        log.info("Попытка получения коллекции комментариев");
        if (eventId == null) {
            log.info("Возврат пустого списка комментариев.");
            return new ArrayList<>();
        }

        if (!eventRep.existsById(eventId)) {
            throw new NotFoundException("Событие с ID: " + eventId + " не найдено.");
        }

        List<CommentDto> comments = commRep.findByEventIdWithAuthor(eventId)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());

        log.info("Список комментариев для события ID: {} успешно сформирован.", eventId);
        return comments;
    }

    // Удаление комментария
    public void deleteComment(Long userId, Long commentId) {
        Comment comment = commRep.findById(commentId).orElseThrow(
                () -> new NotFoundException("Комментарий с ID: " + commentId + " не найден."));

        if (!userRep.existsById(userId)) throw new ConflictException("Пользователя с ID: "
                + userId + " не существует.");

        // Проверка прав пользователя
        // ID уже есть в прокси, поэтому обращения к БД не произойдет
        if (!comment.getAuthor().getId().equals(userId)) {
            throw new ConflictException("Вы не являетесь автором комментария с ID: " + commentId
                    + " и потому не можете удалить его.");
        }

        try {
            commRep.delete(comment);
            log.info("Успешное удаление комментария с ID: {}", commentId);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("Непредвиденная ошибка при удалении комментария с ID: " + commentId);
        }
    }
}
