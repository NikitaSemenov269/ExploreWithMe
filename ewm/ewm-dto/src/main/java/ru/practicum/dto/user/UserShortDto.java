package ru.practicum.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Пользователь (краткая информация)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserShortDto {

    /**
     * Идентификатор
     */
    @NotNull
    private Long id;

    /**
     * Имя
     */
    @NotBlank
    private String name;
}