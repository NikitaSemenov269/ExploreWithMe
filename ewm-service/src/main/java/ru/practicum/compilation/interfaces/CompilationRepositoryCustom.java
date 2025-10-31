package ru.practicum.compilation.interfaces;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import ru.practicum.DTO.compilation.CompilationDto;
import ru.practicum.DTO.compilation.ResponseCompilationDto;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompilationRepositoryCustom {

    List<ResponseCompilationDto> findCompilations(Boolean pinned, Pageable pageable);

    Optional<ResponseCompilationDto> findCompilationById(Long compId);

}