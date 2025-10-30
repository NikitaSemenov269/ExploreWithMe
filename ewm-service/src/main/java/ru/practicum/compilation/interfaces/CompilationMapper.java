package ru.practicum.compilation.interfaces;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.compilation.Compilation;
import ru.practicum.compilation.DTO.RequestCompilationDto;
import ru.practicum.compilation.DTO.ResponseCompilationDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CompilationMapper {

    Compilation toEntity(RequestCompilationDto requestCompilationDto);

    @Mapping(target = "events", ignore = true)
    ResponseCompilationDto toDto(Compilation compilation);

    List<ResponseCompilationDto> toCollectionDto(List<Compilation> compilations);

}
