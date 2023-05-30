package ru.practicum.service;

import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.NewCompilationDto;
import ru.practicum.dto.compilation.UpdateCompilationRequest;

import java.util.List;

public interface CompilationService {

    CompilationDto createCompilation(NewCompilationDto dto);

    void deleteCompilation(Long compId);

    CompilationDto updateCompilations(Long compId, UpdateCompilationRequest dto);

    CompilationDto getCompilation(Long compId);

    List<CompilationDto> getCompilationsByFilters(Boolean pinned, Integer from, Integer size);

}
