package ru.practicum.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

/**
 * Локация события (координаты)
 */
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Location {

    /**
     * Широта
     */
    @Column(name = "lat", nullable = false)
    private Float lat;

    /**
     * Долгота
     */
    @Column(name = "lon", nullable = false)
    private Float lon;
}