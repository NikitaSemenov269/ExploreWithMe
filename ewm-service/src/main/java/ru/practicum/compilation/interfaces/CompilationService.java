package ru.practicum.compilation.interfaces;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.DTO.compilation.CompilationDto;
import ru.practicum.DTO.compilation.RequestCompilationDto;
import ru.practicum.DTO.compilation.ResponseCompilationDto;
import ru.practicum.DTO.compilation.UpdRequestCompilationDto;

import java.util.List;

@Service
public interface CompilationService {
    List<ResponseCompilationDto> findCompilations(Boolean pinned, Pageable pageable);

    ResponseCompilationDto findCompilationById(Long compId);

    void saveCompilation(RequestCompilationDto compilationDto);

    void deleteCompilation(Long compId);

    CompilationDto updateCompilation(Long id, UpdRequestCompilationDto updReqCompDto);
}
