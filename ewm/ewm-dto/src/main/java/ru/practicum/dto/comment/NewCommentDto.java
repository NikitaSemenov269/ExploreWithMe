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

    @NotBlank
    private String comment;

    @NotNull
    private LocalDateTime createdOn;

    @NotNull
    private Long eventId;

}
