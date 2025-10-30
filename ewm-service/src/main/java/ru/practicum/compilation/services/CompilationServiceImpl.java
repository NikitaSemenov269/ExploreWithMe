package ru.practicum.compilation.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.compilation.interfaces.CompilationRepository;

@Service
@Slf4j
@AllArgsConstructor
@Transactional
public class CompilationServiceImpl {
    private final CompilationRepository compRepo;

}
