package ru.practicum.compilation.interfaces;

import org.springframework.stereotype.Repository;
import ru.practicum.compilation.DTO.ResponseCompilationDto;

import java.util.List;

@Repository
public interface CompilationRepositoryCustom {

    public List<ResponseCompilationDto> getCompilations(Boolean pinned);
}