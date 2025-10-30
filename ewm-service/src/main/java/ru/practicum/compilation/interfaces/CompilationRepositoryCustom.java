package ru.practicum.compilation.interfaces;

import ru.practicum.compilation.Compilation;

import java.util.List;

public interface CompilationRepositoryCustom {
    // Черновой метод для образца.
    List<Compilation> findByNameContains(String namePart);
}