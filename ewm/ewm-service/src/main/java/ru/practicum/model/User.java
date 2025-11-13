package ru.practicum.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Пользователь сервиса
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Имя пользователя
     */
    @Column(name = "name", nullable = false, length = 250)
    private String name;

    /**
     * Email (уникальный)
     */
    @Column(name = "email", nullable = false, unique = true, length = 254)
    private String email;
}