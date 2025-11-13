package ru.practicum.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewCommentDto {

    @NotBlank(message = "Комментарий не может быть пустой строкой, или null.")
    private String comment;

    @NotNull(message = "Дата создания не может быть null.")
    private LocalDateTime createdOn;

    @NotNull(message = "ID события не может быть null.")
    private Long eventId;

}
