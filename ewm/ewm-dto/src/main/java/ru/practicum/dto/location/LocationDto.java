package ru.practicum.dto.location;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Широта и долгота места проведения события
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationDto {

    /**
     * Широта
     */
    private Float lat;

    /**
     * Долгота
     */
    private Float lon;
}