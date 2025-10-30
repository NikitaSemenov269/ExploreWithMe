package ru.practicum.compilation;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "compilations")
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Описание подборки не может быть пустой строкой, или null.")
    @Column(name = "title")
    private String title;

    @Column(name = "pinned")
    private Boolean pinned;

    @ElementCollection(fetch = FetchType.LAZY)
    private Set<Long> eventsId;
}
