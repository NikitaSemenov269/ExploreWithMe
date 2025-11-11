package ru.practicum.model;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.enumeration.EventState;

import java.time.LocalDateTime;

/**
 * Событие
 */
@Entity
@Table(name = "events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Краткое описание события
     */
    @Column(name = "annotation", nullable = false, length = 2000)
    private String annotation;

    /**
     * Категория события
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    /**
     * Дата и время создания события
     */
    @Column(name = "created_on", nullable = false)
    private LocalDateTime createdOn;

    /**
     * Полное описание события
     */
    @Column(name = "description", nullable = false, length = 7000)
    private String description;

    /**
     * Дата и время проведения события
     */
    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;

    /**
     * Инициатор события (пользователь)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "initiator_id", nullable = false)
    private User initiator;

    /**
     * Локация события (координаты)
     */
    @Embedded
    private Location location;

    /**
     * Нужно ли оплачивать участие
     */
    @Column(name = "paid", nullable = false)
    private Boolean paid;

    /**
     * Ограничение на количество участников (0 = без ограничений)
     */
    @Column(name = "participant_limit", nullable = false)
    private Integer participantLimit;

    /**
     * Дата и время публикации события
     */
    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    /**
     * Нужна ли пре-модерация заявок на участие
     * true - заявки должны одобряться инициатором
     * false - заявки принимаются автоматически
     */
    @Column(name = "request_moderation", nullable = false)
    private Boolean requestModeration;

    /**
     * Состояние жизненного цикла события
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false, length = 20)
    private EventState state;

    /**
     * Заголовок события
     */
    @Column(name = "title", nullable = false, length = 120)
    private String title;

    /**
     * Количество подтвержденных заявок на участие
     * Денормализовано для производительности
     */
    @Column(name = "confirmed_requests", nullable = false)
    @Builder.Default
    private Integer confirmedRequests = 0;
}