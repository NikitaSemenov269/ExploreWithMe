package ru.practicum.DTO.compilation;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.DTO.event.EventShortDto;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ResponseCompilationDto {

    @NotNull(message = "Id подборки не может быть null.")
    private Long id;

    @Size(min = 1, max = 50)
    private String title;

    @NotNull
    private Boolean pinned;
    // !!! используется затычка Event.
    private List<EventShortDto> events;

}


