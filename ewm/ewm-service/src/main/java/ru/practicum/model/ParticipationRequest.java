package ru.practicum.model;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.enumeration.ParticipationStatus;

import java.time.LocalDateTime;

/**
 * Заявка на участие в событии
 */
@Entity
@Table(name = "participation_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParticipationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Дата и время создания заявки
     */
    @Column(name = "created", nullable = false)
    private LocalDateTime created;

    /**
     * Событие
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    /**
     * Пользователь, отправивший заявку
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester;

    /**
     * Статус заявки
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private ParticipationStatus status;
}