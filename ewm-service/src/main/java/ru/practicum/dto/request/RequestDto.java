package ru.practicum.dto.request;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RequestDto {
    private Long id;
    private Long userId;
    private Long eventId;
}
