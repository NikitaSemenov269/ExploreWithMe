package ru.practicum.DTO.compilation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CompilationDto {

    private Long id;

    private String title;

    private Boolean pinned;
    // !!! используется затычка Event.
    private Set<Long> eventsId;

}


