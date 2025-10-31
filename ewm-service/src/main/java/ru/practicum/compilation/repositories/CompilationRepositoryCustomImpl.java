package ru.practicum.compilation.repositories;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import ru.practicum.DTO.compilation.ResponseCompilationDto;
import ru.practicum.DTO.event.EventShortDto;
import ru.practicum.compilation.Compilation;
import ru.practicum.compilation.interfaces.CompilationMapper;
import ru.practicum.compilation.interfaces.CompilationRepositoryCustom;
import ru.practicum.plugs.EventMapper;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static ru.practicum.compilation.QCompilation.compilation;
import static ru.practicum.plugs.QEvent.event;

@Repository
@RequiredArgsConstructor
public class CompilationRepositoryCustomImpl implements CompilationRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    private final CompilationMapper compilationMapper;
    private final EventMapper eventMapper;   // затычка

    @Override
    public List<ResponseCompilationDto> findCompilations(Boolean pinned, Pageable pageable) {
        // Пин приходит, либо не призодит и тогда null
        List<Compilation> compilations;

        if (pinned) {
            compilations = jpaQueryFactory
                    .selectFrom(compilation)
                    .where(compilation.pinned.eq(true))
                    .orderBy(compilation.id.asc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();
        } else {
            compilations = jpaQueryFactory
                    .selectFrom(compilation)
                    .orderBy(compilation.id.asc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();
        }

        if (compilations.isEmpty()) {
            return Collections.emptyList();
        }

        Set<Long> allEventIds = compilations.stream()
                .flatMap(comp -> comp.getEventsId().stream())
                .collect(Collectors.toSet());

        Map<Long, EventShortDto> eventsDtoMap = jpaQueryFactory
                .select(Projections.constructor(EventShortDto.class,
                        event.id
                      /*  event.annotation,
                        event.title,
                        event.description,
                        event.eventDate,
                        event.paid,
                        event.confirmedRequests,
                        event.views*/
                ))
                .from(event)
                .where(event.id.in(allEventIds))
                .fetch()
                .stream()
                .collect(Collectors.toMap(EventShortDto::getId, Function.identity()));

        return compilations.stream()
                .map(comp -> new ResponseCompilationDto(
                        comp.getId(),
                        comp.getTitle(),
                        comp.getPinned(),
                        comp.getEventsId().stream()
                                .map(eventsDtoMap::get)
                                .filter(Objects::nonNull)
                                .sorted(Comparator.comparing(EventShortDto::getDate).reversed())
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ResponseCompilationDto> findCompilationById(Long compId) {
        Compilation comp = jpaQueryFactory.selectFrom(compilation)
                .where(compilation.id.eq(compId))
                .fetchOne();

        if (comp == null) return Optional.empty();

        List<EventShortDto> events = jpaQueryFactory
                .select(Projections.constructor(EventShortDto.class,
                        event.id
                ))
                .from(event)
                .where(event.id.in(comp.getEventsId()))
                .fetch();

        ResponseCompilationDto dtoFull = compilationMapper.toResponseDto(comp);
        dtoFull.setEvents(events);
        return Optional.of(dtoFull);
    }
}
