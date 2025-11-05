package ru.practicum.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.NewCompilationDto;


import java.util.List;
import java.util.Optional;

@Repository
public interface CompilationRepositoryCustom {

    List<CompilationDto> findCompilations(Boolean pinned, Pageable pageable);

    Optional<CompilationDto> findCompilationById(Long compId);

}