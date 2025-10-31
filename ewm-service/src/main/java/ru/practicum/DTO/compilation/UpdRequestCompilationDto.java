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
public class UpdRequestCompilationDto {

    private Boolean pinned;

    private String title;

    private Set<Long> eventsId;

}