package ru.practicum.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.NewCompilationDto;
import ru.practicum.dto.compilation.UpdateCompilationRequest;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.CompilationMapper;
import ru.practicum.model.Compilation;
import ru.practicum.repository.CompilationRepository;
import ru.practicum.repository.EventRepository;


import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
@Transactional
public class CompilationService {
    private final CompilationRepository compRep;
    private final EventRepository eventRepo;
    private final CompilationMapper mapper;

    // ЛОГИ ЛОГИ ЛОГИ ЛОГИ

    @Transactional(readOnly = true)
    public List<CompilationDto> findCompilations(Boolean pinned, Pageable pageable) {
        // валидация
        return compRep.findCompilations(pinned, pageable);
    }

    @Transactional(readOnly = true)
    public CompilationDto findCompilationById(Long compId) {
        // валидация
        return compRep.findCompilationById(compId).orElseThrow(() -> new NotFoundException("GG"));
    }

    public void saveCompilation(NewCompilationDto newCompilationDto) {

        try {
            compRep.save(mapper.toEntity(newCompilationDto));
            // исключения из прошлого проекта
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteCompilation(Long compId) {
        // валидация
        try {
            compRep.deleteById(compId);
        } catch (Exception e) {
            throw new RuntimeException("");
        }
    }

    public CompilationDto updateCompilation(Long id, UpdateCompilationRequest updReqCompDto) {
        // валидация
        Compilation compilation = compRep.findById(id)
                .orElseThrow(() -> new NotFoundException("GG"));

        if (updReqCompDto.getPinned() != null && !compilation.getPinned().equals(updReqCompDto.getPinned())) {
            compilation.setPinned(updReqCompDto.getPinned());
        }
        if (updReqCompDto.getTitle() != null && !compilation.getTitle().equals(updReqCompDto.getTitle())) {
            compilation.setTitle(updReqCompDto.getTitle());
        }
        if (updReqCompDto.getEventsId() != null && !compilation.getEventsId().equals(updReqCompDto.getEventsId())) {
            compilation.setEventsId(updReqCompDto.getEventsId());
        }

        try {
            compRep.save(compilation);
            return mapper.toDto(compilation);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
