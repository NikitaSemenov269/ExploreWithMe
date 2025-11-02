package ru.practicum.DTO.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class NewUserRequest {
    @NotBlank(message = "name не может быть пустым")
    @Size(min = 2, max = 250, message = "name должен содержать от 2 до 250 символов")
    private String name;

    @NotBlank(message = "email не может быть пустым")
    @Size(min = 6, max = 254, message = "email должен содержать от 6 до 254 символов")
    @Email(message = "Некорректный формат email")
    private String email;
}
