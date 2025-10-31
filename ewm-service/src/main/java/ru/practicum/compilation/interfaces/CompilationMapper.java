package ru.practicum.compilation.interfaces;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.DTO.compilation.CompilationDto;
import ru.practicum.DTO.compilation.ResponseCompilationDto;
import ru.practicum.compilation.Compilation;
import ru.practicum.DTO.compilation.RequestCompilationDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CompilationMapper {

    Compilation toEntity(RequestCompilationDto requestCompilationDto);

    CompilationDto toDto(Compilation compilation);

    @Mapping(target = "events", ignore = true)
    ResponseCompilationDto toResponseDto(Compilation compilation);

    List<ResponseCompilationDto> toCollectionDto(List<Compilation> compilations);

}
