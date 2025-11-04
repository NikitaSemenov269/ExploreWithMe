package ru.practicum.stats;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "hits_data")
public class Hit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // тип int согласно swagger, класс-обертка Integer для работы с null.

    @NotBlank(message = "Название сервиса обязательное поле.")
    @Column(name = "app", nullable = false)
    private String app;

    @NotBlank(message = "URI не может быть пустым")
    @Column(name = "uri")
    private String uri;

    @NotBlank(message = "Ip обязательное поле.")
    @Column(name = "ip", nullable = false)
    private String ip;

    @NotNull(message = "Дата и время запроса обязательные поля.")
    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

}
