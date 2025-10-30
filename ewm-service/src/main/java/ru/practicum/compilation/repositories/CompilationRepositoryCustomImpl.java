package ru.practicum.compilation.repositories;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.compilation.Compilation;
import ru.practicum.compilation.DTO.ResponseCompilationDto;
import ru.practicum.compilation.interfaces.CompilationRepositoryCustom;
import ru.practicum.plugs.Event;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static ru.practicum.compilation.QCompilation.compilation;
import static ru.practicum.plugs.QEvent.event;

@Repository
@RequiredArgsConstructor
public class CompilationRepositoryCustomImpl implements CompilationRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    public List<ResponseCompilationDto> getCompilations(Boolean pinned) {

        List<Compilation> compilations;

        if (pinned) {
            compilations = jpaQueryFactory
                    .selectFrom(compilation)
                    .where(compilation.pinned.eq(true))
                    .fetch();
        } else {
            compilations = jpaQueryFactory
                    .selectFrom(compilation)
                    .fetch();
        }

        Set<Long> allEventIds = compilations.stream()
                .flatMap(comp -> comp.getEventsId().stream())
                .collect(Collectors.toSet());

        Map<Long, Event> eventsMap = jpaQueryFactory
                .selectFrom(event)
                .where(event.id.in(allEventIds))
                .fetch()
                .stream()
                .collect(Collectors.toMap(Event::getId, Function.identity()));

        return compilations.stream()
                .map(comp -> new ResponseCompilationDto(
                        comp.getId(),
                        comp.getTitle(),
                        comp.getPinned(),
                        comp.getEventsId().stream()
                                .map(eventsMap::get)
                                .filter(Objects::nonNull)
                                .sorted(Comparator.comparing(Event::getDate).reversed())
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }

}
