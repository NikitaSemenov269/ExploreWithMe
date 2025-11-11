package ru.practicum.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Пользователь
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    /**
     * Идентификатор
     */
    private Long id;

    /**
     * Имя
     */
    @NotBlank(message = "Имя не может быть пустым")
    @Size(min = 2, max = 250, message = "Имя должно быть от 2 до 250 символов")
    private String name;

    /**
     * Почтовый адрес
     */
    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Email должен быть корректным")
    @Size(min = 6, max = 254, message = "Email должен быть от 6 до 254 символов")
    private String email;
}