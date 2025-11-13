package ru.practicum.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RequestStatisticDto {
    @NotBlank(message = "Название сервиса обязательное поле.")
    private String app;

    private String uri;

    @NotBlank(message = "Ip обязательное поле.")
    private String ip;

    @NotBlank(message = "Дата и время запроса обязательные поля.")
    private String timestamp;
}
