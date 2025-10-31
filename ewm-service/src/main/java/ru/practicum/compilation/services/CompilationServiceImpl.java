package ru.practicum.compilation.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.DTO.compilation.CompilationDto;
import ru.practicum.DTO.compilation.RequestCompilationDto;
import ru.practicum.DTO.compilation.ResponseCompilationDto;
import ru.practicum.DTO.compilation.UpdRequestCompilationDto;
import ru.practicum.compilation.Compilation;
import ru.practicum.compilation.interfaces.CompilationMapper;
import ru.practicum.compilation.interfaces.CompilationRepository;
import ru.practicum.compilation.interfaces.CompilationService;
import ru.practicum.exception.NotFoundException;


import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
@Transactional
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compRepo;
    //   private final EventRepository eventRepo;
    private final CompilationMapper mapper;

    // ЛОГИ ЛОГИ ЛОГИ ЛОГИ

    @Override
    @Transactional(readOnly = true)
    public List<ResponseCompilationDto> findCompilations(Boolean pinned, Pageable pageable) {
        // валидация
        return compRepo.findCompilations(pinned, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseCompilationDto findCompilationById(Long compId) {
        // валидация
        return compRepo.findCompilationById(compId).orElseThrow(() -> new NotFoundException("GG"));
    }

    @Override
    public void saveCompilation(RequestCompilationDto compilationDto) {
      /*  if (!eventRepo.existsAllByIdIn(compilationDto.getEventsId())) {
            throw new NotFoundException("Событие не найдено (с каким id ?)");
        }*/
        try {
            compRepo.save(mapper.toEntity(compilationDto));
            // исключения из прошлого проекта
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteCompilation(Long compId) {
        // валидация
        try {
            compRepo.deleteById(compId);
        } catch (Exception e) {
            throw new RuntimeException("");
        }
    }

    @Override
    public CompilationDto updateCompilation(Long id, UpdRequestCompilationDto updReqCompDto) {
        // валидация
        Compilation compilation = compRepo.findById(id)
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
            compRepo.save(compilation);
            return mapper.toDto(compilation);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
