package ru.practicum.service;

import ru.practicum.dto.CompilationDto;
import ru.practicum.dto.NewCompilationDto;
import ru.practicum.dto.UpdateCompilationRequest;
import ru.practicum.model.Compilation;

import java.util.List;

public interface CompilationService {

    CompilationDto createCompilation(NewCompilationDto newCompilation);

    CompilationDto updateCompilationById(Long compId, UpdateCompilationRequest updatedCompilation);

    List<CompilationDto> getComplicationsPublic(Boolean pinned, Integer from, Integer size);

    CompilationDto getCompilationByIdPublic(Long compId);

    Compilation getCompilationById(Long compId);

    void deleteComplication(Long compId);
}