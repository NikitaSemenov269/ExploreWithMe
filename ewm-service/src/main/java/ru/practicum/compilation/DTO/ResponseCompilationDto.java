package ru.practicum.compilation.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.plugs.Event;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ResponseCompilationDto {

    @NotNull(message = "Id подборки не может быть null.")
    private Long id;

    private String title;

    private Boolean pinned;
    // !!! используется затычка Event.
    private List<Event> events;

}


