package ru.practicum.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    @NotBlank(message = "Имя пользователя должно быть заполнено")
    private String name;

    @Column(name = "email")
    @NotBlank(message = "Имя пользователя должно быть заполнено")
    private String email;
}
