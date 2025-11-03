package ru.practicum.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Подборка событий
 */
@Entity
@Table(name = "compilations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Compilation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Закреплена ли подборка на главной странице
     */
    @Column(name = "pinned", nullable = false)
    private Boolean pinned;

    /**
     * Заголовок подборки
     */
    @Column(name = "title", nullable = false, length = 50)
    private String title;

    /**
     * События, входящие в подборку
     */
    @ManyToMany
    @JoinTable(
            name = "compilation_events",
            joinColumns = @JoinColumn(name = "compilation_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id")
    )
    @Builder.Default
    private Set<Event> events = new HashSet<>();
}