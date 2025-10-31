package ru.practicum.DTO.event;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EventShortDto {
    private Long id;

    private String name;

    private String date;
}

