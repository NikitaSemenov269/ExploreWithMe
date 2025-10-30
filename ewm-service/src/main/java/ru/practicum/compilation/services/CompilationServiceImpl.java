package ru.practicum.compilation.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.compilation.DTO.ResponseCompilationDto;
import ru.practicum.compilation.interfaces.CompilationRepository;

import java.awt.print.Pageable;
import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
@Transactional
public class CompilationServiceImpl {
    private final CompilationRepository compRepo;

    public Collection<ResponseCompilationDto> findBy(Boolean pinned) {
       return null;
    }

}
