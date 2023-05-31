package ru.practicum.controller.adminController;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.NewCompilationDto;
import ru.practicum.dto.compilation.UpdateCompilationRequest;
import ru.practicum.service.CompilationService;

import javax.validation.Valid;

@Validated
@RestController
@RequestMapping(path = "/admin/compilations")
@RequiredArgsConstructor
public class AdminCompilationController {
    private final CompilationService compilationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto createCompilation(@RequestBody @Valid NewCompilationDto dto) {
        return compilationService.createCompilation(dto);
    }

    @DeleteMapping("{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable Long compId) {
        compilationService.deleteCompilation(compId);
    }

    @PatchMapping("{compId}")
    public CompilationDto updateCompilations(@PathVariable Long compId,
                                             @RequestBody @Valid UpdateCompilationRequest dto) {
        return compilationService.updateCompilations(compId, dto);
    }
}
