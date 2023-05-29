package ru.practicum.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.dto.CompilationDto;
import ru.practicum.dto.NewCompilationDto;
import ru.practicum.model.Compilation;
import ru.practicum.model.Event;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CompilationMapper {
    private final EventMapper eventMapper;

    public Compilation toCompilation(NewCompilationDto compilationDto, List<Event> events) {
        return Compilation.builder()
                .pinned(Optional.ofNullable(compilationDto.getPinned()).orElse(false))
                .title(compilationDto.getTitle())
                .events(events)
                .build();
    }

    public CompilationDto toCompilationDto(Compilation compilation) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .title(compilation.getTitle())
                .events(eventMapper.toEventShortDto(compilation.getEvents()))
                .pinned(compilation.getPinned())
                .build();
    }

    public Compilation toCompilation(Compilation compilationOld, List<Event> events) {
        return Compilation.builder()
                .id(compilationOld.getId())
                .pinned(Optional.ofNullable(compilationOld.getPinned()).orElse(false))
                .title(compilationOld.getTitle())
                .events(events)
                .build();
    }

    public List<CompilationDto> toCompilationDto(List<Compilation> compilations) {
        return compilations.stream()
                .map(this::toCompilationDto)
                .collect(Collectors.toList());
    }
}
