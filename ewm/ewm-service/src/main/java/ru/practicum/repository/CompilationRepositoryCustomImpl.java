package ru.practicum.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.mapper.CompilationMapper;
import ru.practicum.mapper.EventMapper;
import ru.practicum.model.Compilation;
import ru.practicum.model.Event;


import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static ru.practicum.model.QCompilation.compilation;
import static ru.practicum.model.QEvent.event;


@Repository
@RequiredArgsConstructor
public class CompilationRepositoryCustomImpl implements CompilationRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    private final CompilationMapper compilationMapper;
    private final EventMapper eventMapper;

    @Override
    public List<CompilationDto> findCompilations(Boolean pinned, Pageable pageable) {
        List<Compilation> compilations;

        if (pinned != null && pinned) {
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
                .flatMap(comp -> comp.getEvents().stream()
                        .map(Event::getId))
                .collect(Collectors.toSet());

        Map<Long, EventShortDto> eventsDtoMap = jpaQueryFactory
                .select(Projections.constructor(EventShortDto.class,
                        event.id,
                        event.annotation,
                        event.title,
                        event.description,
                        event.eventDate,
                        event.paid,
                        event.confirmedRequests
                ))
                .from(event)
                .where(event.id.in(allEventIds))
                .fetch()
                .stream()
                .collect(Collectors.toMap(EventShortDto::getId, Function.identity()));

        return compilations.stream()
                .map(comp -> new CompilationDto(
                        comp.getId(),
                        comp.getTitle(),
                        comp.getPinned(),
                        comp.getEvents().stream()
                                .map(eventsDtoMap::get)
                                .filter(Objects::nonNull)
                                .sorted(Comparator.comparing(EventShortDto::getEventDate).reversed())
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CompilationDto> findCompilationById(Long compId) {
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

        CompilationDto dtoFull = compilationMapper.toDto(comp);
        dtoFull.setEvents(events);
        return Optional.of(dtoFull);
    }
}
