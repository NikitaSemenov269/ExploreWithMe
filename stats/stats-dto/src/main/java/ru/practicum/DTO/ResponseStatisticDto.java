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
public class ResponseStatisticDto {
    @NotBlank(message = "Название сервиса обязательное поле.")
    private String app;

    private String uri;

    private Integer hits;
}


