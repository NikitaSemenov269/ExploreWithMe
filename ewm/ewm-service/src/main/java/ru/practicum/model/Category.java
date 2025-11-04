package ru.practicum.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Категория событий
 */
@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Название категории (уникальное)
     */
    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name;
}