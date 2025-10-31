package ru.practicum.DTO.compilation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RequestCompilationDto {

    private Boolean pinned = false;

    @Size(min = 1, max = 50, message = "")
    private String title;

    private Set<Long> eventsId;

}
