package ru.practicum.compilation.DTO;

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

    private Boolean pinned;

    private String title;

    private Set<Long> eventsId;

}
