package ru.practicum.DTO.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class NewCategoryDto {
    @NotBlank(message = "Поле не может быть пустым")
    @Size(min = 1, max = 50, message = "Длина должна быть от 1 до 50 символов")
    private String name;
}
