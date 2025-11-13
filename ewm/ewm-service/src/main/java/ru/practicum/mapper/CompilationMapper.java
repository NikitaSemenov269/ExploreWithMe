package ru.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.NewCompilationDto;
import ru.practicum.model.Compilation;

@Mapper(componentModel = "spring")
public interface CompilationMapper {

    @Mapping(target = "events", ignore = true)
    @Mapping(target = "pinned", source = "pinned", defaultExpression = "java(false)")
    Compilation toEntity(NewCompilationDto newCompilationDto);

    @Mapping(target = "events", ignore = true)
    CompilationDto toDto(Compilation compilation);

}
