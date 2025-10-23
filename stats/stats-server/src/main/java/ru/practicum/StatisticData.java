package ru.practicum;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "statics_data")
public class StatisticData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // тип int согласно swagger, класс-обертка Integer для работы с null.

    @NotBlank(message = "Название сервиса обязательное поле.")
    @Column(name = "app", nullable = false)
    private String app;

    @Column(name = "uri")
    private String uri;

    @NotBlank(message = "Ip обязательное поле.")
    @Column(name = "ip", nullable = false)
    private String ip;

    @NotBlank(message = "Дата и время запроса обязательные поля.")
    @Column(name = "timestamp", nullable = false)
    private String timestamp;

}
